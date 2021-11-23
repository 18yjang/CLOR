# pip install colormath

import cv2, numpy as np
import csv
import json
import sys
from colormath.color_objects import LabColor, sRGBColor
from colormath.color_conversions import convert_color
from colormath.color_diff import delta_e_cie2000

f = sys.argv[1]
g = sys.argv[2]
h = sys.argv[3]


#contrasting pair
c_pair = {0 : [6, 7, 10], 1 : [6, 7, 10], 2 : [5, 6, 7], 3 : [7, 8, 9], 4 : [5, 7, 12], \
          5 : [42, 8, 16], 6 : [0, 1, 2], 7 : [0, 1, 10], 8 : [42, 5, 10], 9 : [3, 11, 12], 
          10 : [0, 7, 8], 11 : [12, 18, 21], 12 : [13, 14, 15], \
          13 : [18, 19, 21], 14 : [20, 19, 21], 15 : [19, 20, 21], 16 : [18, 20, 21], 17 : [11, 13, 12], \
          18 : [13, 15, 17], 19 : [11, 13, 14], 20 : [13, 15, 18], 21 : [13, 15, 16], \
          22 : [28, 29, 21], 23 : [46, 21, 29], 24 : [46, 21, 12], 25 : [11, 22, 29], \
          26 : [23, 36, 28], 27 : [11, 47, 21], 28 : [11, 22, 47], 29 : [22, 23, 24], \
          30 : [36, 37, 46], 31 : [34, 35, 36], 32 : [34, 36, 47], 33 : [36, 47, 12], \
          34 : [35, 36, 37], 35 : [11, 31, 32], 36 : [32, 30, 16], 37 : [34, 32, 31], \
          38 : [35, 36, 12], 39 : [11, 36, 37], 40 : [35, 36, 46], 41 : [11, 36, 46], \
          42 : [11, 36, 46], 43 : [11, 36, 37], 44 : [35, 36, 46], 45 : [11, 36, 46], \
          46 : [14, 15, 30], 47 : [30, 32, 33], 48 : [11, 35, 36], 49 : [11, 35, 36], \
          50 : [11, 54, 73], 51 : [79, 80, 11], 52 : [73, 53, 72], 53 : [63, 75, 52], \
          54 : [55, 43, 63], 55 : [54, 53, 59], 56 : [11, 57, 54], 57 : [30, 73, 11], \
          58 : [59, 11, 54], 59 : [80, 52, 53], 60 : [73, 53, 54], 61 : [59, 73, 42], \
          62 : [43, 70, 14], 63 : [12, 72, 47], 64 : [73, 63, 12], 65 : [26, 11, 14], \
          66 : [68, 55, 14], 67 : [68, 55, 14], 68 : [66, 67, 55], 69 : [72, 47, 12], \
          70 : [71, 26, 76], 71 : [26, 70, 74], 72 : [77, 73, 74], 73 : [77, 74, 47], \
          74 : [72, 47, 72], 75 : [72, 26, 71], 76 : [70, 74, 69], 77 : [76, 72, 71], \
          78 : [72, 79, 59], 79 : [51, 77, 73], 80 : [61, 72, 35]}

#tonal pair
t_pair = {0 : [1, 33, 32], 1 : [0, 3, 27], 2 : [3, 9, 44], 3 : [30, 80, 63], 4 : [3, 74, 30], \
          5 : [4, 6, 50], 6 : [7, 8, 54], 7 : [6, 8, 54], 8 : [48, 49, 60], 9 : [17, 26, 45], \
          10 : [11, 12, 35], 11 : [13, 14, 15], 12 : [20, 21, 47], \
          13 : [11, 14, 63], 14 : [15, 17, 63], 15 : [13, 16, 17], 16 : [13, 14, 17], 17 : [14, 15, 16], \
          18 : [19, 36, 47], 19 : [18, 47, 36], 20 : [11, 21, 12], 21 : [11, 20, 12], \
          22 : [23, 33, 32], 23 : [22, 33, 32], 24 : [25, 26, 79], 25 : [24, 26, 79], \
          26 : [24, 25, 79], 27 : [22, 23, 29], 28 : [27, 29, 68], 29 : [27, 28, 64], \
          30 : [24, 25, 26], 31 : [32, 33, 23], 32 : [31, 33, 23], 33 : [31, 27, 29], \
          34 : [48, 49, 60], 35 : [36, 21, 47], 36 : [47, 37, 35], 37 : [47, 12, 35], \
          38 : [42, 44, 40], 39 : [40, 42, 45], 40 : [39, 43, 45], 41 : [39, 44, 45], \
          42 : [39, 44, 43], 43 : [42, 44, 45], 44 : [39, 40, 45], 45 : [39, 44, 43], \
          46 : [21, 20, 37], 47 : [35, 36, 37], 48 : [34, 49, 60], 49 : [34, 48, 60], \
          50 : [69, 77, 51], 51 : [70, 77, 18], 52 : [13, 78, 32], 53 : [54, 72, 76], \
          54 : [57, 72, 59], 55 : [11, 75, 30], 56 : [64, 68, 27], 57 : [61, 72, 71], \
          58 : [52, 32, 33], 59 : [54, 57, 72], 60 : [48, 49, 34], 61 : [54, 72, 62], \
          62 : [61, 54, 72], 63 : [11, 80, 75], 64 : [29, 68, 45], 65 : [53, 54, 72], \
          66 : [70, 51, 67], 67 : [51, 70, 66], 68 : [64, 28, 29], 69 : [11, 13, 75], \
          70 : [50, 66, 69], 71 : [72, 76, 53], 72 : [71, 76, 53], 73 : [75, 13, 35], \
          74 : [14, 13, 75], 75 : [14, 35, 63], 76 : [71, 72, 53], 77 : [70, 66, 50], \
          78 : [11, 75, 14], 79 : [25, 80, 63], 80 : [79, 63, 14]}


class colorMatch(object):
    def __init__(self, r, g, b):
        self.r = r
        self.g = g
        self.b = b
        

    def color_match(self, r, g, b):
        input_rgb = sRGBColor(np.int64(r), np.int64(g), np.int64(b), is_upscaled= True)
        input_lab = convert_color(input_rgb, LabColor, through_rgb_type = sRGBColor)


        # 작성한 color_table.csv 파일로 numpy data 생성, csv 배열로 저장
        data = []
        color_table = open('color_table2.csv', 'r', encoding='utf-8')
        reader = csv.reader(color_table)

        for line in reader:
            data.append(line[1: ])

        color_table.close()
        xy = np.array(data[1:])

        #어울리는 pair 입력하기 (생략)
        

        #similar color 찾아 어울리는 색상 pair 값 출력
        similar = 100000000
        index = 0

        #색상 테이블 수에 따라서 range안의 값 바꾸기 - 출력해서 수 확인+1
        for i in range(81) : 
            color_n = xy[i][0]

            #sRGB 클래스 인스턴스 생성. RGB 값을 넣을 때 is_upscaled=True로 해줘야 함
            rgb = sRGBColor(float(xy[i][1]),float(xy[i][2]), float(xy[i][3]), is_upscaled=True)

            #Lab으로 변환
            lab = convert_color(rgb, LabColor, through_rgb_type=sRGBColor)
            #print(lab)

            delta_e = delta_e_cie2000(input_lab, lab)
            #print(i, '번: ',delta_e)

            if(similar > delta_e) :
                similar = delta_e
                index = i

        best_pair_c = c_pair[index]
        best_pair_t = t_pair[index]

        def rgb2hex(r, g, b): 
            hex_color = "#" + base10Tobase16(r) + base10Tobase16(g) + base10Tobase16(b) 
            return hex_color

        def base10Tobase16(i): 
            base16 = "%02X" % int(i) 
            return base16

        

        if (similar > 15) :
            print('유사 주의')

        print('유사 기준 : ', similar)
        print('가장 유사한 색상 인덱스 : ', index) 
        print('색상 : ', rgb2hex(np.int64(r), np.int64(g), np.int64(b)))

        print('#complement 조합 : ',best_pair_c) #complementary 배색 
        for i in best_pair_c :
            r = int(xy[i][1])
            g = int(xy[i][2])
            b = int(xy[i][3])
            print(i, '번 색상', '( ',rgb2hex(r, g, b), ')')

        print('#tonal 조합 : ', best_pair_t) #tonal 배색
        for i in best_pair_t :
            r = int(xy[i][1])
            g = int(xy[i][2])
            b = int(xy[i][3])
            print(i, '번 색상', '( ',rgb2hex(r, g, b), ')')
        

    
    
        
        






api = colorMatch(f, g, h)
api.color_match(f, g, h)



