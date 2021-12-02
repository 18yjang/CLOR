# ⭐C'LOR⭐
#### 이화여자대학교 컴퓨터공학전공 졸업프로젝트
#### 5팀 이화스터디 : 1870079 장윤주, 1876300 이유림

___의류 이미지를 분석하여 어울리는 색상 조합과 의류 상품을 추천하는 서비스___

## 👚 프로젝트 개요
길거리를 걷다보면 많은 사람들의 옷이 무채색인 것을 발견할 수 있습니다.  의상은 사람의 이미지를 만들어줄 수 있는 만큼 그 조화가 매우 중요한데, 색상을 조금만 잘못 매치해도 어색하고 이상해보이는 경향이 있습니다. 단일제품으로는 예쁘다고 생각한 옷이 다른 옷들과 어울리지 않아 착용하지 못한 경우도 비일비재 합니다. 한국상품학회의 색 선호도에 따른 패션상품 구매행동에 관한 연구에 따르면 “미래 소비사회에 주축이 되는 여대생들”의 선호 색은 “보라계열과 검정계열이 19.8%로 가장 많았으며”, 가장 많이 구매하는 색에 대한 결과로 각 카테고리에서 검정계열이 가장 많았고  그 다음으로는 흰색 계열이 뒤를 따랐다고 합니다.  이처럼 “선호하는 색상과 의복 구매 색상에 차이가 나는 이유로는 ‘코디하기가 어렵다’가 37.1%로 가장 많았다”는 결과를 통해 저희는 무채색 세상이 발현한 이유는 색의 조화때문인 것을 알 수 있었습니다. 최근 퍼스널 컬러와 같이 색상에 대한 소비자의 관심이 상승함에 따라 다양한 색감의 조화가 더욱 절실하다 느꼈습니다.

하지만 현재 색상을 중점으로 코디를 도와주는 어플은 없어 직접 다른 의상과 조합해보거나 인터넷에 조합을 검색해보는 등 소모되는 시간이 많았습니다. 또한 인터넷에 검색하는 경우 저희의 원초적인 의문점인 “내가 소유한 의상에 어울리는 색”이 아닌, 기본적인 색상에 대한 정보만 제공되기에 이 어플을 개발하고자 하였습니다. 특히 소비자 뿐만 아닌 의류산업 또한 대중들의 편향된 취향에 따라 재고의 불균형이 이루어졌고 저희 어플리케이션이 마찬가지로 해결할 수 있을것으로 생각됩니다.

## 👕 구조
저희 프로그램 구조는 다음과 같습니다. 
<p align="center"><img src="https://user-images.githubusercontent.com/68594937/144373112-ae8ded09-56bc-4e3c-981b-0eba9cb0b736.PNG" width="40%" height="40%">

	
사용자가 의류사진을 입력하면 이미지가 안드로이드에서 Flask 기반 웹서버로 넘어가게 됩니다. 케라스와 텐서플로, 파이썬으로 작성된 모델들을 거쳐 입력된 사진의 카테고리와 주요 색상, 어울리는 색상의 목록과 함께 앱으로 돌아오게 됩니다. 사용자는 보색과 유사색 중 한가지의 색 조합을 선택하게되며, 추천되는 3가지의 색조합 중 하나를 선택해 일러스트로 감상할 수 있습니다.

	
## 👖 개발 상세
<p align="center"><img src="https://user-images.githubusercontent.com/68594937/144373105-ae2a9d84-0dd2-4403-a726-ce281aade2c6.PNG" width="40%" height="40%">


저희 프로그램은 사용자가 사진을 업로드만 하면 해당 의상의 카테고리와 색상을 검출하여 제공합니다. (이미지에 올라온 GrabCut은 사용하지 않습니다) FCN모델을 사용하여 이미지 배경을 삭제하며 카테고리 분류를 원활하게 하였고, K-means Clustering을 통해 색상의 비율을 얻어내 비율이 가장 큰 색상을 사용자에게 제공하게 됩니다. 분류되는 카테고리는 이처럼 큰 범주로는 상하의로 나뉘며, 그 내부에서 총 9가지의 카테고리로 분류됩니다. 두번째로는 문제 삼았던 소모 시간을 해소하기 위해 한눈에 특징이 드러나도록 구성하였습니다. 3가지의 추천을 원색과 함께 제공하여 눈으로 매칭할 수 있으며 평균 색상이 아닌 특유의 색상을 입력해 얻은 결과이므로 사용자 맞춤 결과가 제공됩니다. 최종적으로 상하의 일러스트까지 제공해 착용시 어떤 느낌을 줄 수 있는지 파악 가능합니다. 또한 색감에 대한 개인의 취향을 고려해 보색과 유사색 조합을 선택할 수 있도록 하였고, 사용자가 선택했던 조합들을 데이터 베이스에 남겨 만족도와 전체적인 통계를 제공합니다. 	

					
## 👗 예상 시나리오 – flow chart
 <p align="center"><img src="https://user-images.githubusercontent.com/79851987/142370338-83038c9f-db43-4397-b71c-018088caadee.png" width="60%" height="60%">

## 🧣 기술블로그
- 장윤주 : https://jmarble-dev.tistory.com/3, https://jmarble-dev.tistory.com/5
- 이유림 : https://emmee.tistory.com/2, https://emmee.tistory.com/3

## 🔎 딥러닝 부분 (FCN)   -  C'LOR_Classification_Model.ipynb
- 의류 이미지를 입력받으면 모델이 의상을 분석하고 제공함.
- tensor + keras 적용
- 현재 데이터셋 폴더 입력받아 학습 및 검증 결과 그래프로 도출
- 데이터 전처리에는 labelme 모듈을 사용

## 🔎 의류 영역 추출 – GrabCut
-  OpenCV 라이브러리의 Grabcut을 이용해 의류 영역을 제외한 공간을 삭제

## 🔎 의류 주요 색상 추출 – Dominant Color
- K-Means Clustering 알고리즘을 적용
- OpenCV 라이브러리 이용

## 🔎 색상 조합 알고리즘 – Color Table
- CIE Lab Space 변환
- 기준 색상값과의 color difference 이용

## 🧤 어플리케이션 – ClorApp
- 안드로이드 스튜디오 이용해 작업
- 어플리케이션 실행 순서도

 1. 옷 추가 기능
 <p align="center"><img src="https://user-images.githubusercontent.com/68594937/144373124-b8b70353-42c0-4e32-838c-738b3f0768e6.PNG" width="100%" height="100%">
 
 2. 옷장 기능
 <p align="center"><img src="https://user-images.githubusercontent.com/79851987/144371991-f0b95c2b-496b-450d-a8d9-1727f8aa1e85.png" width="30%" height="30%">
 
 3. 통계 기능
 <p align="center"><img src="https://user-images.githubusercontent.com/79851987/144372031-f50f9ad6-23d6-4a86-a963-29c690f43dc8.png" width="30%" height="30%">



## 🧢 웹 – web.py
- 플라스크를 이용한 웹 서버 구축
- 주요 작업 웹에서 진행


## 🧦 기대 효과
이 어플리케이션이 소비자에게는 의류 활용의 폭을 넓힐 수 있도록 하며 의상 선택에 소모되는 시간을 감소시켜주고 선택의 폭을 넓힘으로서 기업의 재고 불균형에도 마찬가지로 도움이 될 것입니다. 또한 퍼스널컬러를 병합하면 어플리케이션의 미래가치도 보장됨과 더불어 의류 기업과의 제휴를 통해 제공받은 카탈로그를 결과화면에 추천하며 상호작용을 이뤄낼 수 있을것입니다. 


