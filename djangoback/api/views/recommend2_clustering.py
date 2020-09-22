from sklearn import datasets
import pandas as pd
# Perform the necessary imports
from scipy.cluster.hierarchy import linkage, dendrogram
import matplotlib.pyplot as plt

def clustering_test(request):
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







