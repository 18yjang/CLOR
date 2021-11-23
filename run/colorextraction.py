# pip3 install opencv-python
# pip3 install -U scikit-learn scipy matplotlib

import cv2, numpy as np
import sys
from sklearn.cluster import KMeans

f = sys.argv[1]

class colorExtraction(object):
    def __init__(self, imageid, version=1.1):
        self.imageid = imageid
        self.version = version

    def visualize_colors(self, cluster, centroids):
        # Get the number of different clusters, create histogram, and normalize
        labels = np.arange(0, len(np.unique(cluster.labels_)) + 1)
        (hist, _) = np.histogram(cluster.labels_, bins = labels)
        hist = hist.astype("float")
        hist /= hist.sum()

        # Create frequency rect and iterate through each cluster's color and percentage
        # rect = np.zeros((50, 300, 3), dtype=np.uint8)
        colors = sorted([(percent, color) for (percent, color) in zip(hist, centroids)])
        start = 0

        dominant = 0

        for (percent, color) in colors:
            #print(color, "{:0.2f}%".format(percent * 100))
            color = color.astype(np.int64)
            if dominant < percent*100 :
              if np.array_equal(color, [0,0,0]) == False :
                dom_color = color
            end = start + (percent * 300)
            # cv2.rectangle(rect, (int(start), 0), (int(end), 50), \
                       #   color.astype("uint8").tolist(), -1)
            start = end

        r=dom_color[0]
        g=dom_color[1]
        b=dom_color[2]
        
        return r, g, b #dom_color #[r, g, b]


api = colorExtraction(f)
image = cv2.imread(f)
image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
reshape = image.reshape((image.shape[0] * image.shape[1], 3))

cluster = KMeans(n_clusters=10).fit(reshape)
# dominant = api.visualize_colors(cluster, cluster.cluster_centers_)
r, g, b = api.visualize_colors(cluster, cluster.cluster_centers_)

print(r, g, b)
print(type(r))

        
