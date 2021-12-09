from flask import Flask, render_template,request, jsonify
from werkzeug.utils import secure_filename
from PIL import Image

from sklearn.cluster import KMeans

from colormath.color_objects import LabColor, sRGBColor
from colormath.color_conversions import convert_color
from colormath.color_diff import delta_e_cie2000
import csv

import numpy as np
import sys, os, io, base64, json, cv2

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = '/Uploads/'

#@app.route('/')
#def home():
#    return '''
#        <h1>C'LOR WEB</h1>
#        <p> working on clothes detection </p>
#
#        '''

@app.route('/receive', methods = ['POST'])
def rec():
    image = request.get_json()
    json_data = json.dumps(image)
    json_data = json.loads(json_data)
    image = json_data["image"]
    image = base64.b64decode(image)
    png_arr = np.frombuffer(image, dtype=np.uint8)
    img = cv2.imdecode(png_arr, cv2.IMREAD_COLOR)
    img = Image.fromarray(img, 'RGB')
    img.save('input.png')
    #img.show()

    # 추가 ##### colorExtraction
    img = cv2.imread('input.png')
    #img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    reshape = img.reshape((img.shape[0] * img.shape[1], 3))
    cluster = KMeans(n_clusters = 10).fit(reshape)

    labels = np.arange(0, len(np.unique(cluster.labels_)) + 1)
    (hist, _) = np.histogram(cluster.labels_, bins = labels)
    hist = hist.astype("float")
    hist /= hist.sum()

    # Create frequency rect and iterate through each cluster's color and percentage
    # rect = np.zeros((50, 300, 3), dtype=np.uint8)
    colors = sorted([(percent, color) for (percent, color) in zip(hist, cluster.cluster_centers_)])
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

    ######### color match ##############
    #contrasting pair
    c_pair = {0 : [6, 7, 10], 1 : [6, 7, 10], 2 : [5, 6, 7], 3 : [7, 8, 9], 4 : [5, 7, 12], \
          5 : [42, 8, 16], 6 : [0, 1, 2], 7 : [0, 1, 10], 8 : [42, 5, 10], 9 : [3, 11, 12], \
          10 : [0, 7, 8], 11 : [12, 18, 21], 12 : [13, 14, 15], 13 : [18, 19, 21], \
          14 : [20, 19, 21], 15 : [19, 20, 21], 16 : [18, 20, 21], 17 : [11, 13, 12], \
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
            10 : [11, 12, 35], 11 : [13, 14, 15], 12 : [20, 21, 47], 13 : [11, 14, 63], \
            14 : [15, 17, 63], 15 : [13, 16, 17], 16 : [13, 14, 17], 17 : [14, 15, 16], \
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

    input_rgb = sRGBColor(r, g, b, is_upscaled=True)
    input_lab = convert_color(input_rgb, LabColor, through_rgb_type = sRGBColor)


    # 작성한 color_table.csv 파일로 numpy data 생성, csv 배열로 저장
    ## data = []
    ## color_table = open('color_table2.csv', 'r', encoding='utf-8')
    ## reader = csv.reader(color_table)

    ##for line in reader:
    ##    data.append(line[1: ])

    ##color_table.close()
    ##xy = np.array(data[1:])
    xy =[['0','244','197','191', '5', '170', '205'],\
        ['1', '209', '66', '68', '239', '146', '129'],\
        ['2', '241', '89', '42', '9', '210', '133'],\
        ['3', '246', '239', '221', '29', '140', '220'],\
        ['4', '251', '213', '78', '31', '229', '155'],\
        ['5', '1', '121', '73', '104', '236', '57'],\
        ['6', '201', '223', '237', '136', '120', '206'],\
        ['7', '27', '72', '114', '139', '148', '66'],\
        ['8', '115', '70', '90', '222', '58', '87'],\
        ['9', '99', '75', '71', '6', '40', '80'],\
        ['10', '174', '181', '189', '141', '24', '171'],\
        ['11', '255', '255', '255', '160', '0', '240'],\
        ['12', '0', '0', '0', '147', '55', '0'],\
        ['13', '249', '246', '239', '28', '109', '230'],\
        ['14', '235', '223', '201', '26', '110', '205'],\
        ['15', '234', '215', '201', '17', '106', '205'],\
        ['16', '211', '189', '168', '20', '79', '178'],\
        ['17', '170', '142', '120', '18', '55', '136'],\
        ['18', '111', '114', '93', '46', '24', '97'],\
        ['19', '123', '118', '99', '32', '26', '104'],\
        ['20', '167', '172', '178', '142', '16', '162'],\
        ['21', '67', '75', '77', '128', '17', '68'],\
        ['22', '206', '170', '154', '12', '83', '169'],\
        ['23', '177', '144', '135', '9', '51', '147'],\
        ['24', '184', '147', '92', '24', '94', '130'],\
        ['25', '174', '125', '69', '21', '104', '114'],\
        ['26', '133', '88', '55', '17', '100', '88'],\
        ['27', '144', '80', '70', '5', '83', '101'],\
        ['28', '123', '51', '52', '239', '99', '82'],\
        ['29', '64', '38', '41', '235', '61', '48'],\
        ['30', '255', '224', '141', '29', '240', '186'],\
        ['31', '226', '178', '158', '12', '130', '181'],\
        ['32', '240', '216', '204', '13', '131', '209'],\
        ['33', '202', '154', '144', '7', '85', '163'],\
        ['34', '233', '231', '242', '167', '71', '223'],\
        ['35', '169', '169', '159', '40', '13', '154'],\
        ['36', '92', '90', '75', '35', '24', '79'],\
        ['37', '65', '63', '64', '220', '4', '60'],\
        ['38', '254', '209', '178', '16', '234', '203'],\
        ['39', '250', '176', '127', '16', '222', '177'],\
        ['40', '239', '128', '111', '5', '192', '165'],\
        ['41', '245', '142', '107', '10', '210', '166'],\
        ['42', '245', '128', '48', '16', '218', '138'],\
        ['43', '227', '126', '74', '14', '176', '142'],\
        ['44', '198', '118', '83', '12', '121', '132'],\
        ['45', '162', '96', '70', '11', '95', '109'],\
        ['46', '20', '17', '70', '162', '146', '41'],\
        ['47', '61', '61', '35', '40', '65', '45'],\
        ['48', '175', '171', '188', '169', '27', '169'],\
        ['49', '134', '130', '157', '166', '29', '135'],\
        ['50', '185', '198', '125', '47', '94', '152'],\
        ['51', '87', '104', '57', '54', '70', '76'],\
        ['52', '244', '194', '196', '238', '167', '206'],\
        ['53', '186', '215', '237', '137', '141', '199'],\
        ['54', '67', '76', '117', '153', '65', '87'],\
        ['55', '246', '201', '93', '28', '215', '160'],\
        ['56', '221', '56', '43', '3', '174', '124'],\
        ['57', '17', '15', '78', '161', '163', '44'],\
        ['58', '227', '81', '139', '224', '173', '145'],\
        ['59', '49', '47', '73', '163', '52', '56'],\
        ['60', '216', '192', '220', '194', '69', '194'],\
        ['61', '166', '196', '234', '142', '148', '188'],\
        ['62', '44', '103', '169', '141', '141', '100'],\
        ['63', '216', '205', '176', '29', '81', '184'],\
        ['64', '113', '32', '43', '235', '134', '68'],\
        ['65', '35', '51', '230', '157', '191', '125'],\
        ['66', '144', '176', '89', '55', '85', '125'],\
        ['67', '35', '85', '64', '103', '100', '56'],\
        ['68', '160', '69', '72', '239', '95', '108'],\
        ['69', '235', '242', '154', '48', '185', '186'],\
        ['70', '199', '209', '160', '48', '83', '174'],\
        ['71', '145', '181', '186', '125', '55', '156'],\
        ['72', '110', '134', '162', '142', '52', '128'],\
        ['73', '208', '209', '211', '147', '8', '197'],\
        ['74', '211', '205', '121', '37', '121', '156'],\
        ['75', '243', '242', '240', '27', '27', '227'],\
        ['76', '201', '220', '223', '125', '61', '200'],\
        ['77', '214', '219', '191', '47', '67', '193'],\
        ['78', '251', '241', '229', '22', '176', '226'],\
        ['79', '130', '120', '106', '23', '24', '111'],\
        ['80', '211', '199', '192', '15', '43', '190']]

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

        

    print('유사 기준 : ', similar)
    print('가장 유사한 색상 인덱스 : ', index)
    result_color = rgb2hex(r, g, b)
    print('색상 : ', result_color)

    print('#complement 조합 : ',best_pair_c) #complementary 배색
    result_cpair = []
    for i in best_pair_c :
        r = int(xy[i][1])
        g = int(xy[i][2])
        b = int(xy[i][3])
        result_cpair.append(rgb2hex(r, g, b))
        #print(i, '번 색상', '( ',result_cpair[i], ')')

    print('#tonal 조합 : ', best_pair_t) #tonal 배색
    result_tpair = []
    for i in best_pair_t :
        r = int(xy[i][1])
        g = int(xy[i][2])
        b = int(xy[i][3])
        result_tpair.append(rgb2hex(r,g,b))
        #print(i, '번 색상', '( ',result_tpair[i], ')')
    
    return jsonify(result_color, result_cpair[0], result_cpair[1], result_cpair[2],
                   result_tpair[0], result_tpair[1], result_tpair[2])


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)


