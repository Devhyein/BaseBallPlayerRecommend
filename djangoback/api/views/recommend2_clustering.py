from sklearn import datasets
import pandas as pd
# Perform the necessary imports
from scipy.cluster.hierarchy import linkage, dendrogram
from scipy.cluster.hierarchy import fcluster

from sklearn.cluster import KMeans
import matplotlib.pyplot as plt

from .models import *
from django.db import connections

def clustering_test(request):
    queryset = RecordHitter.objects.all()
    query, params = queryset.query.sql_with_params()
    df_hitter = pd.read_sql_query(query, connections['default'], params = params)

    # player 테이블 정리: 필요없는 컬럼 지우고, 우리팀 선수 지움
    df_label = df_hitter['player_id']

    df_hitter = df_hitter[['hitter_ba', 'hitter_obp', 'hitter_slg']]
    print(df_label)
    print(df_hitter)

    model = KMeans(n_clusters=5,algorithm='auto')
    model.fit(df_hitter)
    predict = pd.DataFrame(model.predict(df_hitter))
    predict.columns=['predict']
    print(predict)

    r = pd.concat([predict, df_hitter],axis=1)
    print(r)

    plt.rcParams["figure.figsize"] = (6, 6)
    fig = plt.figure()
    ax = fig.add_subplot(111, projection="3d")

    ax.scatter(r['hitter_ba'],r['hitter_obp'], r['hitter_slg'], c=r['predict'],alpha=0.5)
    centers = pd.DataFrame(model.cluster_centers_,columns=['hitter_ba', 'hitter_obp', 'hitter_slg'])
    center_x = centers['hitter_ba']
    center_y = centers['hitter_obp']
    center_z = centers['hitter_slg']
    ax.scatter(center_x,center_y,center_z,s=50,marker='D',c='r')
    plt.show()

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

def clustering_test_2(request):
    iris = datasets.load_iris()
    labels = pd.DataFrame(iris.target)
    labels.columns=['labels']
    data = pd.DataFrame(iris.data)
    data.columns=['Sepal length','Sepal width','Petal length','Petal width']
    data = pd.concat([data,labels],axis=1)

    # Calculate the linkage: mergings
    mergings = linkage(data,method='complete')

    # Plot the dendrogram, using varieties as labels
    plt.figure(figsize=(40,20))
    dendrogram(mergings,
            labels = labels.values,
            leaf_rotation=90,
            leaf_font_size=20,
    )
    plt.show()







