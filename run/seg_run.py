import cv2
import numpy as np
from tensorflow.keras.models import load_model
import tensorflow as tf
import sys
from tensorflow.compat.v1 import ConfigProto
from tensorflow.compat.v1 import InteractiveSession
config = ConfigProto()
config.gpu_options.allow_growth = True
session = InteractiveSession(config=config)




f = sys.argv[1]



saved = load_model("clor.h5")


class fashion_tools(object):
    def __init__(self,imageid,model,version=1.1):
        self.imageid = imageid
        self.model   = model
        self.version = version
        
    def get_dress(self,stack=False):
        """limited to top wear and full body dresses (wild and studio working)"""
        """takes input rgb----> return PNG"""
        name =  self.imageid
        file = cv2.imread(name)
        file = tf.image.resize_with_pad(file,target_height=224,target_width=224)
        rgb  = file.numpy()
        file = np.expand_dims(file,axis=0)/ 255.
        seq = self.model.predict(file)
        seq = seq[0][:,:,0]
        seq = np.expand_dims(seq,axis=-1)
        c1x = rgb*(1-seq)
        #c2x = rgb*(1-seq)
        cfx = c1x
        dummy = np.ones((rgb.shape[0],rgb.shape[1],1))
        rgbx = np.concatenate((rgb,dummy*255),axis=-1)
        rgbs = np.concatenate((cfx,(1-seq)*255.),axis=-1)
        if stack:
            stacked = rgbs
            return stacked
        else:
            return rgbs
        
        
    def get_patch(self):
        return None




    
###running code


api    = fashion_tools(f,saved)
image_ = api.get_dress(stack=True)
cv2.imwrite("result.png",image_)
