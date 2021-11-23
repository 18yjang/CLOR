from flask import Flask, render_template,request, jsonify
from werkzeug.utils import secure_filename
import sys, os, io

app = Flask(__name__)

@app.route('/')
def home():
    return '''
        <h1>C'LOR WEB</h1>
        <p> working on clothes detection </p>

        '''
@app.route('/test')
def test():
    return render_template('img_test.html', image_file="image/image_static.PNG")


@app.route('/test2', methods = ['GET', 'POST'])
def test2():
        print("log: got at test", file=sys.stderr)
        return jsonify({'status':'success'})
    
        """
        if request.method == 'POST':
        file = request.files['pic']
        filename = file.filename
        file.save(os.path.join("C:\\Users\whaze\OneDrive\Documents\GitHub\CLOR\static\image", filename))
        return render_template('img_test.html', image_file=os.path.join("image/",filename))
        """


@app.route('/upload')
def upload_file():
   return render_template('upload.html')

@app.route('/uploader', methods=['GET','POST'])
def upload():
    if request.method == 'POST':
        if request.files['file'].filename == 'NULL':
            flash('파일이 없습니다. 파일을 제출하세요!')
            return redirect(url_for('/'))
        
        file = request.files['file']
        file.save(secure_filename(file.filename))
        return '파일 업로드 성공!!'



@app.route('/2', methods=['GET', 'POST'])
def t2():
    if request.method == 'POST':
        f = request.files['file']
        d = request.form
        print(d)
        print(f)
        f.save("./static/"+secure_filename(f.filename))
        return 'uploads'        
    else:
        return "No Post"

    



@app.route('/removal', methods = ['POST'])
def backRemoval():
    print(request.files , file=sys.stderr)
    file = request.files['image'].read() ## byte file
    npimg = np.fromstring(file, np.uint8)
    img = cv2.imdecode(npimg,cv2.IMREAD_COLOR)
	######### Do preprocessing here ################
	# img[img > 150] = 0
	## any random stuff do here
	################################################
    img = Image.fromarray(img.astype("uint8"))
    rawBytes = io.BytesIO()
    img.save(rawBytes, "JPEG")
    rawBytes.seek(0)
    img_base64 = base64.b64encode(rawBytes.read())
    return jsonify({'status':str(img_base64)})


@app.route('/cldetection')
def clothesDetection(removedImg):
    return 'detected'

@app.route('/detection')
def colorDetection(removedImg):
    return 'color'

@app.route('/combination')
def colorCombination(color):
    return 'combination'


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)

'''
running at http://127.0.0.1:5000/

대역폭이 유사할것 집안에서 만들면 안됨.........

외부로 열려있는 웹서버를 사용해야함.

오라클 = 클라우드 프리티어 평생 무료 서버 인스턴스
리눅스: 무료 - 설치 해보는걸로..
윈도우: 유료.....


Create VM instance
Ubuntu
기업 = CentOS / OracleLinux
Socket용인지 RESTFUl용인지
웹페이지 : 파일 업로드 METHOD Type 지정해서 하는 웹 방식..
REST API = json 파일로 넘김
변수 parameter를 첨부해서 같이 보내는 것.
json 형태를 binary를 포함시켜서 보냄.
encoding 된 이미지를 다시 타입을 바꿔서
Parameter를 어떤 방식으로 변형시켜서 전달해야하는지.
이미지 전송

책.. 인터넷은 하나씩 던져서 체계 없음.
C가 아니면 라이브러리 속도가 떨어짐. < 라이브러리 생성은 힘들고, 가져오는게 좋음.
주어진 값을 어떤식으로 처리해서 보여줄지에 대해 고민해보기.

앱 후반부:
겉으로 보기에 된것처럼 보이게 만드는것. 무조건적인 전송 데이터를 기반으로 작동하도록 해보기.
서버: 외부에서 접속할 수 있도록 하는게 중요..

앱도 가상 에뮬 말고 실제 기기에서 테스트 해보기.

실제 가동 테스트 중요.

색상 개수를 통일 시키기.
고정된 상황이 좋음.

버그 트래킹 : 프린트f

colab -> 파일 만들기.

'''
