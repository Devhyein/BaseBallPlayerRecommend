from sklearn import datasets
import pandas as pd
# Perform the necessary imports
from scipy.cluster.hierarchy import linkage, dendrogram
from scipy.cluster.hierarchy import fcluster

from sklearn.cluster import *
from sklearn import metrics
import matplotlib.pyplot as plt

from sklearn import preprocessing

from .models import *
from django.db import connections
from django.http import JsonResponse # http 응답을 위함

from rest_framework.decorators import api_view, parser_classes
from drf_yasg import openapi
from drf_yasg.utils import swagger_auto_schema

# 1. Player ID를 받는다
# 2. Player 테이블에서 해당 선수를 찾아 포지션이 뭔지 알아낸다.
# 3. 투수면 투수기록 테이블, 타자면 타자기록 테이블을 가져온다
# 4. 같은 포지션의 선수만 남겨둔다
# 5. 기록을 최근 3년 평균, 표준화한 데이터로 정제
# 6. 클러스터링 알고리즘을 돌려 클러스터를 추출
# 7. 클러스터 내에서 거리 가까운 순으로? 혹은 같은 클러스터 중에서 성적 좋은 순으로? TOP n명의 선수 ID 리턴

param1 = openapi.Parameter('player_id', openapi.IN_QUERY, type=openapi.TYPE_INTEGER, required=True)
@swagger_auto_schema(
    method='get',
    manual_parameters = [param1]
)

@api_view(["GET"])
def clustering_test(request):
    """
    1명의 player id를 받아 그 선수와 유사한 (같은 클러스터의) 선수들을 추천해준다
    - parameters: player_id (int)
    - returns: recommended 리스트 (같은 클러스터로 분류된 선수들의 player_id)
    """
    pid = request.GET.get('player_id',  '')

    player = Player.objects.get(player_id=pid)
    pos = player.player_position

    result = []

    if pos.player_position == 1:
        result = pitcher_clustering(pid)
    else:
        result = hitter_clustering(pid)

    print(result)

    return_object = {
        'recommended': result
    }

    return JsonResponse(return_object)

    # player 테이블 정리: 필요없는 컬럼 지우고, 우리팀 선수 지움
    # df_label = df['player_id']

    # df_hitter = df_hitter[['hitter_ba', 'hitter_obp', 'hitter_slg']]
    # print(df_label)
    # print(df_hitter)

    # model = KMeans(n_clusters=5,algorithm='auto')
    # model.fit(df_hitter)
    # predict = pd.DataFrame(model.predict(df_hitter))
    # predict.columns=['predict']
    # print(predict)

    # r = pd.concat([predict, df_hitter],axis=1)
    # print(r)

    # plt.rcParams["figure.figsize"] = (6, 6)
    # fig = plt.figure()
    # ax = fig.add_subplot(111, projection="3d")

    # ax.scatter(r['hitter_ba'],r['hitter_obp'], r['hitter_slg'], c=r['predict'],alpha=0.5)
    # centers = pd.DataFrame(model.cluster_centers_,columns=['hitter_ba', 'hitter_obp', 'hitter_slg'])
    # center_x = centers['hitter_ba']
    # center_y = centers['hitter_obp']
    # center_z = centers['hitter_slg']
    # ax.scatter(center_x,center_y,center_z,s=50,marker='D',c='r')
    # plt.show()

    # mergings = linkage(df_pitcher, method='complete')
    # plt.figure(figsize=(40,20))
    # dendrogram(mergings,
    #         labels = df_label.values,
    #         leaf_rotation=90,
    #         leaf_font_size=20,
    # )
    # # plt.show()

    # predict = pd.DataFrame(fcluster(mergings, 3, criterion='distance'))
    # predict.columns=['cluster']
    # print(predict)

def hitter_clustering(pid):
    queryset = RecordHitter.objects.all()
    query, params = queryset.query.sql_with_params()
    df_hitter = pd.read_sql_query(query, connections['default'], params = params)

    # 기록 데이터 정제
    # 최근 3년 데이터만, 규정이닝 걸러서 가져오기
    # df_hitter = df_hitter.loc[df_hitter.hitter_year >= 18]
    # df_hitter = df_hitter.loc[((df_hitter.hitter_year==20) & (df_hitter.hitter_pa>=100*3.1)) | ((df_hitter.hitter_year!=20) & (df_hitter.hitter_pa>=144*3.1))]

    # 타, 출, 장 세개로 일단
    df_hitter = df_hitter[['player_id', 'hitter_ba', 'hitter_obp', 'hitter_slg']]    

    # 선수별로 그룹핑
    df_grouped = df_hitter.groupby("player_id")
    df_grouped_mean = df_grouped.mean()
    print(df_grouped_mean)

    # 표준화
    x = df_grouped_mean.values #returns a numpy array
    min_max_scaler = preprocessing.MinMaxScaler()
    x_scaled = min_max_scaler.fit_transform(x)
    df_normalized = pd.DataFrame(x_scaled)

    print(df_normalized)

    # 계층형 클러스터링
    mergings = linkage(df_normalized, method='complete')
    predict = pd.DataFrame(fcluster(mergings, 0.03, criterion='distance'))

    # K-Mean 클러스터링
    # model = KMeans(n_clusters=7,algorithm='auto')
    # model.fit(df_normalized)
    # predict = pd.DataFrame(model.predict(df_normalized))
    
    predict.columns=['predict']
    print(predict)

    #evaluation(df_normalized)

    predict['player_id'] = df_grouped_mean.reset_index()['player_id']
    print(predict)

    df_q = predict.query("player_id == " + str(pid)) # 데이터 찾기
    print(df_q)
    target_c = df_q.iloc[0]['predict'] # 특정 셀의 데이터 가져오기: iloc

    result_list = []
    for player in predict.iterrows():
        if player[1].predict == target_c and int(player[1].player_id) != int(pid):
            result_list.append(int(player[1].player_id)) # int로 변환안해주면 JSON serializer가 numpy.int64를 못알아먹어서 에러뜸

    return result_list

def pitcher_clustering(pid):
    queryset = RecordPitcher.objects.all()
    query, params = queryset.query.sql_with_params()
    df_pitcher = pd.read_sql_query(query, connections['default'], params = params)

    # 기록 데이터 정제
    # 최근 3년 데이터만, 규정이닝 걸러서 가져오기
    # df_pitcher = df_pitcher.loc[df_pitcher.pitcher_year >= 18]
    # df_pitcher = df_pitcher.loc[((df_pitcher.pitcher_year==20) & (df_pitcher.pitcher_ip>=100)) | ((df_pitcher.pitcher_year!=20) & (df_pitcher.pitcher_ip>=144))]

    # 이닝소화력, 평균자책점, WHIP 세개를 보자
    df_pitcher = df_pitcher[['player_id', 'pitcher_g', 'pitcher_ip', 'pitcher_era', 'pitcher_whip']]    
    # 이닝수는 게임당 이닝수로 변환
    df_pitcher['pitcher_ip/g'] = df_pitcher.apply(lambda row: row.pitcher_ip / row.pitcher_g, axis=1)

    # 선수별로 그룹핑
    df_grouped = df_pitcher.groupby("player_id")
    df_grouped_mean = df_grouped.mean()
    print(df_grouped_mean)

    # 표준화
    df_grouped_mean = df_grouped_mean.drop(columns=['pitcher_g', 'pitcher_ip'])
    x = df_grouped_mean.values #returns a numpy array
    min_max_scaler = preprocessing.MinMaxScaler()
    x_scaled = min_max_scaler.fit_transform(x)
    df_normalized = pd.DataFrame(x_scaled)

    print(df_normalized)

    # 계층형 클러스터링
    mergings = linkage(df_normalized, method='complete')
    predict = pd.DataFrame(fcluster(mergings, 0.03, criterion='distance'))

    # # K-means 클러스터링
    # model = KMeans(n_clusters=27,algorithm='auto')
    # predict = pd.DataFrame(model.fit_predict(df_normalized))
    
    predict.columns=['predict']

    #evaluation(df_normalized)

    predict['player_id'] = df_grouped_mean.reset_index()['player_id']
    print(predict)

    df_q = predict.query("player_id == " + str(pid)) # 데이터 찾기
    print(df_q)
    target_c = df_q.iloc[0]['predict'] # 특정 셀의 데이터 가져오기: iloc

    result_list = []
    for player in predict.iterrows():
        if player[1].predict == target_c and int(player[1].player_id) != int(pid):
            result_list.append(int(player[1].player_id)) # int로 변환안해주면 JSON serializer가 numpy.int64를 못알아먹어서 에러뜸

    return result_list

    # r = pd.concat([predict, df_normalized],axis=1)
    # r.columns = ['predict', 'era', 'whip', 'ip/g']
    # print(r)

    # plt.rcParams["figure.figsize"] = (6, 6)
    # fig = plt.figure()
    # ax = fig.add_subplot(111, projection="3d")

    # ax.scatter(r['era'],r['whip'], r['ip/g'], c=r['predict'],alpha=0.5)
    # centers = pd.DataFrame(model.cluster_centers_,columns=['era', 'whip', 'ip/g'])
    # center_x = centers['era']
    # center_y = centers['whip']
    # center_z = centers['ip/g']
    # ax.scatter(center_x,center_y,center_z,s=50,marker='D',c='r')
    # plt.show()

def evaluation(df_normalized):
    # eps = [0.075, 0.1, 0.125, 0.15]
    # min_samples = [3, 4, 5]
    for y in range(1, 10):
        y = y/100
        mergings = linkage(df_normalized, method='complete')
        predict = pd.DataFrame(fcluster(mergings, y, criterion='distance'))
        #model = KMeans(n_clusters=n,algorithm='auto')
        #    model = DBSCAN(eps=e, min_samples=mins)

        #model.fit(df_normalized)
        # fit_predict() = fit() + predict() 라는 듯
        #predict = pd.DataFrame(model.fit_predict(df_normalized))

    # mergings = linkage(df_normalized, method='complete')
    
    # plt.figure(figsize=(40,20))
    # dendrogram(mergings,
    #         #labels = labels.values,
    #         leaf_rotation=90,
    #         leaf_font_size=20,
    # )
    # plt.show()

    # # y scale이 max 1.6까지다.
    # for y in range(1, 10):
    #     y = y/10
    #     predict = pd.DataFrame(fcluster(mergings, y, criterion='distance'))

        predict.columns=['predict']
        #print(predict)

        # 클러스터링 성능 평가
        print("DB SCORE: ", metrics.davies_bouldin_score(df_normalized, predict))

        r = pd.concat([predict, df_normalized],axis=1)
        r.columns = ['predict', 'era', 'whip', 'ip/g']
        #print(r)

        plt.rcParams["figure.figsize"] = (6, 6)
        fig = plt.figure()
        ax = fig.add_subplot(111, projection="3d")
        plt.title("계층형 클러스터링 y" + str(y))
        ax.scatter(r['era'],r['whip'], r['ip/g'], c=r['predict'],alpha=0.5)
        plt.show()


# def clustering_test_2(request):
#     iris = datasets.load_iris()
#     labels = pd.DataFrame(iris.target)
#     labels.columns=['labels']
#     data = pd.DataFrame(iris.data)
#     data.columns=['Sepal length','Sepal width','Petal length','Petal width']
#     data = pd.concat([data,labels],axis=1)

#     # Calculate the linkage: mergings
#     mergings = linkage(data,method='complete')

#     # Plot the dendrogram, using varieties as labels
#     plt.figure(figsize=(40,20))
#     dendrogram(mergings,
#             labels = labels.values,
#             leaf_rotation=90,
#             leaf_font_size=20,
#     )
#     plt.show()







