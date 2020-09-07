### [dump.pkl 파일 링크]
* dump.pkl 파일은 첫 주에 만들었던 dump.pkl 파일 사용하면 됨
* 필요하면 아래 링크에서 다운로드
* [dump.pkl](https://drive.google.com/file/d/13MV9wbWwD-weIm8k9vqgtn_m1fzU4_UO/view?usp=sharing)

### [Django 프로젝트 설정 및 실행법]
1. 먼저 python 3.6.8 버전이 설치되어 있는지 확인한다
2. djangoback/data 위치에 위 dump.pkl 파일을 위치시킨다
3. djangoback 위치에서 아래 명령어를 사용해 가상환경을 만든다
```
# 명령어 수행하는 폴더 위치 중요!
python -m venv venv
```
4. 가상환경이 만들어지면 아래 명령어로 들어갔다 나갔다 할 수 있다
```
# 폴더 위치는 djangoback(중요)

# 윈도우의 경우
./venv/Scripts/activate

# 우분투(리눅스)의 경우
source ./venv/bin/activate

# 맥은 누군가 해보고 여기 적어줘여

# 가상환경 탈출은 공통
deactivate
```
5. 가상환경에 들어오면 터미널 프롬프트 왼쪽에 (venv) 이게 생긴다
```
(venv) PS C:\SSAFY\BigdataProj\djangoback>
```
6. 가상환경에 들어온 상태에서 아래 명령어로 패키지들을 설치한다
```
pip install djangorestframework
pip install pandas
```
7. 아래 명령어를 통해 패키지가 잘 설치되었는지 확인한다
```
pip list
```
8. 아래 명령어를 통해 DB 설정을 한다
```
python manage.py makemigrations
python manage.py migrate
python manage.py initialize
```
> 근데 우리는 DB를 MariaDB 같은거 쓸거같은데..
9. 아래 명령어를 사용하면 django 백엔드를 실행시켜볼 수 있다
```
python manage.py runserver
```
10. 크롬에서 아래 url 을 입력해보고 결과가 잘 나오나 확인해보자   
   
[http://127.0.0.1:8000/api/stores?name=베스킨라빈스](http://127.0.0.1:8000/api/stores?name=베스킨라빈스)
