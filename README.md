# 당근? 빠따!
<img src="/uploads/cd8b44f7a84c047677f945a4b515c85e/logo.png" width="500px" >

<br />

방대한 프로야구 기록을 분석하여 보다 직관적으로 야구선수의 가치를 판단할 수 있게 하고,    
팀 분석과 비교, 시뮬레이션을 통해 구단의 의사결정에 도움을 줄 수 있는 심플 스카우팅 시스템

## 기술 스택 & 아키텍처
* 기술 스택
 <img src="/uploads/58b9eaed67f4aacb8b7d975ef9fd6103/사용스택.png" width="500px" >     
<br />

    * backend (API Server): SpringBoot
    * backend (Recommendation Server): Django - Django REST Framework
    * frontend: vue.js
    * infra: AWS, Docker
    * Code, CI/CD: Gitlab, Jenkins

* 아키텍처
 <img src="/uploads/2dcdb9bd98378257af2b94ac92c5e483/아키텍처.png" width="500px" >     
<br />

    * 기본적으로 SpringBoot API Server로 요청을 보내며, 추천 시스템과 관련된 기능의 경우 django Server와 추가적으로 통신함

## 화면
 * 선수 검색 화면: 스탯티즈로부터 크롤링한 선수 데이터를 검색, 단순 기록 뿐 아니라 막대그래프 & 방사형 그래프를 이용한 스탯 시각화
 
 <img src="/uploads/5ced4f2df39d9d3d32ca69863f301a39/선수검색1.PNG" width="600px" >    
 
 <img src="/uploads/e2b63acdaf5a61ab9f15c7fffe65e82e/선수검색2.PNG" width="600px" >    
 
<br/>
 
 * 라인업 비교 및 추천: 내 팀의 라인업과 다른 팀의 라인업을 비교하여 볼 수 있고, 내 팀에 부족한 부분을 채워줄 수 있는 선수를 추천

 <img src="/uploads/c718cb19477477602c4a44c30cb44610/팀비교1.PNG" width="500px" >    
 
 <img src="/uploads/6281b8a2c7d148529116f62d9a76fe2d/팀비교2.PNG" width="500px" >    

### 일정
<img src="/uploads/db3de085287297e6d912c1d7ea052f30/일정.PNG" width="700px" >    
<br />

* 주 단위 스프린트 진행, 매 스프린트마다 프로토타입 구현하여 배포 목표

### 팀원 소개
<img src="/uploads/4e520a4bd8d84b32125d8bebd7748efa/팀원소개.png" width="400px" >   

### Django 프로젝트 설정 및 실행법
1. 먼저 python 3.6.8 버전이 설치되어 있는지 확인한다
2. djangoback 위치에서 아래 명령어를 사용해 가상환경을 만든다
```
# 명령어 수행하는 폴더 위치 중요!
python -m venv venv
```
3. 가상환경이 만들어지면 아래 명령어로 들어갔다 나갔다 할 수 있다
```
# 폴더 위치는 djangoback(중요)

# 윈도우의 경우
./venv/Scripts/activate

# 우분투(리눅스)의 경우
source ./venv/bin/activate

# 가상환경 탈출은 공통
deactivate
```
4. 가상환경에 들어오면 터미널 프롬프트 왼쪽에 (venv) 라고 생긴다
```
(venv) PS C:\SSAFY\BigdataProj\djangoback>
```
5. 가상환경에 들어온 상태에서 아래 명령어로 패키지들을 설치한다
```
pip install -r requirements.txt
```
6. 아래 명령어를 통해 패키지가 잘 설치되었는지 확인한다
```
pip list
```
7. 아래 명령어를 통해 서버와 DB 동기화할 수 있음
```
python manage.py inspectdb > models.py
```
8. 아래 명령어를 사용하여 django 백엔드 서버 실행
```
python manage.py runserver
```