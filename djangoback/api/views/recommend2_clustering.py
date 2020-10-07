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

from .five_tool_calculate import five_tool_hitter, five_tool_pitcher

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

    # 투수인지 타자인지 구분해서 보내기
    if pos.player_position == 1:
        result = pitcher_clustering(pid)
    else:
        result = hitter_clustering(pid)

    print(result)

    return_object = {
        'recommended': result
    }

    return JsonResponse(return_object)


def hitter_clustering(pid):
    # 선수 데이터 가져오기
    queryset = RecordHitter.objects.all()
    query, params = queryset.query.sql_with_params()
    df_hitter = pd.read_sql_query(query, connections['default'], params = params)

    queryset = RecordFielder.objects.all()
    query, params = queryset.query.sql_with_params()
    df_fielder = pd.read_sql_query(query, connections['default'], params = params)

    queryset = Player.objects.all()
    query, params = queryset.query.sql_with_params()
    df_player = pd.read_sql_query(query, connections['default'], params = params)

    # 선수 데이터 기반으로 5툴 계산.
    df_normalized = five_tool_hitter(df_player, df_hitter, df_fielder)
    #print(df_normalized)

    # 계층형 클러스터링
    mergings = linkage(df_normalized, method='complete')
    predict = pd.DataFrame(fcluster(mergings, 0.1, criterion='distance'))

    # K-Mean 클러스터링
    # model = KMeans(n_clusters=7,algorithm='auto')
    # model.fit(df_normalized)
    # predict = pd.DataFrame(model.predict(df_normalized))
    
    predict.columns=['predict']
    print(predict)

    # 그래프 시각화
    #evaluation(df_normalized)

    predict['player_id'] = df_normalized.reset_index()['player_id']
    df_q = predict.query("player_id == " + str(pid)) # 데이터 찾기
    target_c = df_q.iloc[0]['predict'] # 특정 셀의 데이터 가져오기: iloc

    result_list = []
    for player in predict.iterrows():
        if player[1].predict == target_c and int(player[1].player_id) != int(pid):
            result_list.append(int(player[1].player_id)) # int로 변환안해주면 JSON serializer가 numpy.int64를 못알아먹어서 에러뜸

    return result_list

def pitcher_clustering(pid):
    # 선수 데이터 가져오기
    queryset = Player.objects.all()
    query, params = queryset.query.sql_with_params()
    df_player = pd.read_sql_query(query, connections['default'], params = params)

    queryset = RecordPitcher.objects.all()
    query, params = queryset.query.sql_with_params()
    df_pitcher = pd.read_sql_query(query, connections['default'], params = params)
    
    # 5툴 계산
    df_normalized = five_tool_pitcher(df_player, df_pitcher)
    print(df_normalized)

    # 계층형 클러스터링
    mergings = linkage(df_normalized, method='complete')
    predict = pd.DataFrame(fcluster(mergings, 0.15, criterion='distance'))

    # # K-means 클러스터링
    # model = KMeans(n_clusters=27,algorithm='auto')
    # predict = pd.DataFrame(model.fit_predict(df_normalized))
    
    predict.columns=['predict']

    # 그래프 시각화
    #evaluation(df_normalized)

    predict['player_id'] = df_normalized.reset_index()['player_id']
    df_q = predict.query("player_id == " + str(pid)) # 데이터 찾기
    target_c = df_q.iloc[0]['predict'] # 특정 셀의 데이터 가져오기: iloc

    result_list = []
    for player in predict.iterrows():
        if player[1].predict == target_c and int(player[1].player_id) != int(pid):
            result_list.append(int(player[1].player_id)) # int로 변환안해주면 JSON serializer가 numpy.int64를 못알아먹어서 에러뜸

    return result_list


def evaluation(df_normalized):
    # eps = [0.075, 0.1, 0.125, 0.15]
    # min_samples = [3, 4, 5]
    for y in range(1, 6):
        y = 0.1 + (y/20)

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

        # r = pd.concat([predict, df_normalized],axis=1)
        # r.columns = ['predict', 'era', 'whip', 'ip/g']
        # #print(r)

        # plt.rcParams["figure.figsize"] = (6, 6)
        # fig = plt.figure()
        # ax = fig.add_subplot(111, projection="3d")
        # plt.title("계층형 클러스터링 y" + str(y))
        # ax.scatter(r['era'],r['whip'], r['ip/g'], c=r['predict'],alpha=0.5)
        # plt.show()