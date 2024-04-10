# Date Project

## ✏️ 프로젝트 소개
- **빅데이터와 딥러닝을 활용한 웹 서비스 개발**
- 인원: 4
- 개발기간: 2022.10. - 2022.12.(약 3달)

## 🔍 프로젝트 개요
- 데이트 코스 추천을 위한 웹 서비스 개발
- 사용자가 원하는 태그들을 기반으로 추천되는, 또는 좋은 평가를 받은 데이트 장소들을 추천하는
  웹 서비스를 개발하였습니다.

## 📝 프로젝트 주요한 기술
- Spring Framework를 활용한 웹 서비스 제작
  - MVC 패턴을 활용하여 프로젝트 구성
  - React와 Thymeleaf 활용하여 연동
  - 데이터 베이스 관리
    - Mysql을 활용한 데이터 베이스 관리 
    - Mybatis, JDBC, JPA, Hibernate를 사용하여 구현
    - 게시판, 가게 데이터 관리, 회원 정보 관리 등 다양한 곳에 적용
  - 로그인 구현
    - Spring에서 제공하는 Security를 활용하여 로그인 구현
       
- Deep Learning을 활용한 추천 서비스 제작
  - 수집한 데이터를 사전에 점수로 수치화 하여 저장
  - 저장된 수치를 기반으로 사용자에게 추천
  - **자세한 과정은 아래에 서술**
---
## 🔥 Deep Learning을 활용한 수치화 과정
1. 개요
    - 사용자의 선택과 가장 적합한 곳을 추천하는 것을 목표로 함
      - 추천을 위해서는 객관적인 지표가 필요 
      - 객관적 지표를 **점수**로 표현
    - 장소의 수치화?
      - 일반적으로 좋은 장소를 직접 찾아볼 때의 과정에서 착안
      - 장소의 리뷰가 많고, 좋은 댓글, 그리고 별점이 높은 곳을 선택
    - **리뷰의 갯수, 리뷰의 긍정, 별점 + 사용자의 선택에 적합** 한 곳을 추천    
    - 데이트 코스의 추천이므로 음식점만 추천해주거나, 카페만 추천하는것 방지
      - 수집된 데이터들은 '음식점', '카페', '숙박'의 3개의 큰 카테고리로 분류
    
2. 진행 방법
    1. 데이터 수집
       - '공공데이터 포털'에서 제공하는 가게의 정보 수집
         - 범위는 서울로 한정함
       - 딥러닝 모델 학습에 필요한 말뭉치들 수집
         - 댓글들의 긍정, 부정을 판별하기 위한 모델 구축이므로 온라인 구어체 위주로 수집
         - AIhub의 온라인 말뭉치, 네이버나 게임 커뮤니티에서 사용된 댓글들 수집
           
    2. 데이터 정제 및 정규화
       - 공공데이터에서 제공되는 가게들에는 불필요하거나 비실용적인 정보들이 다수 존재
         - ex) 영업 코드, 종사자수, 소유주, 영업 면적 등
         - '폐업'이나 '영업 정지'상태의 가게 정보들도 같이 제공됨
         - 필요한 정보들만 정제(위치 정보, 업태명 등 또는 정상 영업 중인 곳)
       - 수집된 언어 데이터도 정제
         - 불필요하게 많은 특수문자 혹은 반복되는 문자 등
           
    3. 크롤링
       - 수치화에 필요한 정보들은 국내에서 가장 많이 사용되는 포털인 '네이버'에서 수집함
       - 리뷰, 별점, 키워드 리뷰 수집
       - 약 20만개의 장소들에서 수집
         
    4. 리뷰 분석
        1. 딥러닝 모델
           - 감성 분석 모델
             - AIhub의 온라인 말뭉치, 네이버나 게임 커뮤니티에서 사용된 댓글 
             - 데이터 정제, 형태소 분석, 토큰화 후 BILSTM과 Attention 기법으로 감성 분석 모델 구축
             - 최대한 많은 의견을 수용하기 위해 기존에 사용되던 긍부정 판별 기준을 0.5에서 0.3으로 조정
             - **주어진 문장의 긍정 및 부정을 판별 가능**
           - 키워드 분석 모델
             - 페이스북에서 제작한 'FastText'를 활용한 코사인 유사도 분석
             - 기존에 학습된 'FastText' 모델의 가중치에 온라인 구어체 말뭉치들을 추가로 학습
             - **코사인 유사도를 기반으로 주어진 단어와 유사한 단어 제공**
               
        2. 키워드 선정
           - 사용자가 선택할 키워드 20개를 선정


           
             ![image](https://github.com/20152842/BIT-Project/assets/66324867/bd113afa-99d0-4ad7-b512-99e019935358)
      

       
           - 위 20개의 키워드들을 '키워드 분석 모델'을 통해 키워드와 관련 있는 단어들을 정함
           - ex) 가성비 : 가성, 퀄리티, 싼, 저렴, 메리트, 스테디셀러...
             
        2. 감성 분석하여 나온 리뷰들 키워드화
           - **Graph Ranking Algorithm** 을 기반으로 한 'WordRank'
           - 이를 한국어에 적용한 'KR-WordRank'
           - 'KR-WordRank'를 사용하여 감성 분석에 통과한 리뷰들을 키워드화
             
        3. 리뷰 수치화
           - 리뷰 예시 : '맛있어요, 여기 메뉴가 좋고 저렴해요 메리트 있어요....'
           - 키워드화 -> 맛있, 메뉴 좋 저렴, 메리트
           - **'맛있'** 이라는 키워드를 '가성비' 카테고리의 **'가성, 퀄리티, 싼, 저렴, 메리트, 스테디셀러...'** 와 전부 코사인 유사도 분석
           - **각 리뷰들은 '가성비'라는 카테고리와의 유사도를 점수로 수치화**
           - 위의 방법으로 각 리뷰들은 다른 카테고리 '애견동반', '활동적', '감성적'등 모든 카테고리와 유사도의 점수를 수치화
           - 모든 리뷰들의 수치화된 점수를 평균내어 가게의 리뷰들은 모든 카테고리와 얼마나 유사한지 점수를 수치화하게 됨


          
  3. 요약 및 결과
     >     1. 장소들을 점수로 수치화하는 것을 목표
     >     2. 수집된 장소들의 정보들로 다시 크롤링하여 별점, 댓글, 키워드 리뷰 등을 수집
     >     3. 별점 같이 수치화된 정보들은 평균내어 사용
     >     4. 글자로 이루어져 있는 정보들은 키워드화 및 감정분석 모델을 사용하여 수치화
     >     5. 각 장소들은 일정한 기준에 따른 점수로 평가되어짐

     
     
     ![sentiment](https://github.com/20152842/BIT-Project/assets/66324867/f5450391-ee99-43f8-a258-2ada28e31439)

     
---

## 💻 웹 페이지 구성

|Main|Introduction|How to use|
|:-:|:-:|:-:|
|![스크린샷 2024-03-24 171954](https://github.com/20152842/BIT-Project/assets/66324867/51065dd1-b7e7-4f03-8ce8-bae3b5d4b3a6)|![스크린샷 2024-03-24 165641](https://github.com/20152842/BIT-Project/assets/66324867/7c1356d5-a10b-4267-8c85-4997c10c5e86)|![스크린샷 2024-03-24 165708](https://github.com/20152842/BIT-Project/assets/66324867/4423d761-8f77-4269-afca-c064ac41ff83)|

|Board|Board Ex|
|:-:|:-:|
|![스크린샷 2024-03-24 165746](https://github.com/20152842/BIT-Project/assets/66324867/e8545046-a4a2-47bb-a1fe-0318c944c15e)|![스크린샷 2024-03-24 165817](https://github.com/20152842/BIT-Project/assets/66324867/bb7c6e10-d692-45b7-8845-34403b1b99e3)|

|Sign up|Sign in|
|:-:|:-:|
|![스크린샷 2024-03-24 165851](https://github.com/20152842/BIT-Project/assets/66324867/861416a3-dd33-4ca6-8a97-a55511ffb631)|![스크린샷 2024-03-24 165835](https://github.com/20152842/BIT-Project/assets/66324867/4e8c099c-7ae2-4307-a9d2-1aed9347f523)|




### 주요 기능 - 코스 추천
|||
|:-:|:-:|
|![스크린샷 2024-03-24 165912](https://github.com/20152842/BIT-Project/assets/66324867/75b2d20c-8640-4042-9e30-ab309cce3790)|![스크린샷 2024-03-24 165925](https://github.com/20152842/BIT-Project/assets/66324867/ce6566c5-35f2-4414-91b8-b9692a2ab6e8)|
|![스크린샷 2024-03-24 165946](https://github.com/20152842/BIT-Project/assets/66324867/22fbf8f4-ae9f-4147-a6e1-7e2bb56c59d4)||


  
