## 주제
야구 선수 스카우팅 시스템
* 야구 선수들의 각종 기록을 시각화, 비교하고 드래프트, 트레이드, 스카우트 대상 선수를 추천해주는 시스템

## 기술 스택
* Backend (API Server) - SpringBoot
* Backend (Data Analysis Server) - django
* Web Server - Nginx: SpringBoot, django의 기본 내장 서버를 사용하는 것보다는 Nginx와 연동해서 사용하는 것이 가용성 +
* DBMS - MariaDB or PostgreSQL
* Frontend - Vue.js
    * 추후 고려: SpringBatch / Redis?

## 도메인 지식
### 야구의 기록들
스탯티즈 기록실 기준 정렬   
참고: http://old.statiz.co.kr/info.php?opt=0&sopt=0   

타자 = 야수. 팀의 공격 차례때는 타격을 하는 타자고, 팀의 수비 차례때는 수비를 하는 수비수가 된다.   
단, 지명타자 (DH) 라는 포지션은 수비를 하지 않고 공격 때만 나온다. (투수 대신에 타격하는 것이라 생각하면 됨)   

#### 타자 기록
* G: 출전한 게임 수 - 한국 프로야구는 현재 1시즌 144게임 체제
* 타석 (PA): 자기 차례가 돌아와서 치러 들어온 총 횟수
    * 규정타석: 현재 규정 기준 팀이 치른 경기 수 * 3.1. 타자는 이 기준 이상으로 타석을 소화해야만 그 시즌 기록이 공식 기록으로 인정받는다. 예를 들어 시즌에 1게임만 출전해서 1타석 들어가서 안타를 치면 타율이 1.000이 되는데, 이런건 시즌 타율왕으로 인정 안한다
* 타수 (AB): 타석 수에서 볼넷, 몸에 맞는 공, 희생타, 희생플라이, 타격방해, 주루방해를 제외한 것.
* 득점 (R): 타자가 출루한 뒤 어떻게든 한 바퀴를 돌아서 홈으로 돌아와서 점수가 되면 득점을 했다고 한다
* 안타 (H): 타자가 공을 쳤고 출루했으며 아웃되지 않은 것. 단 수비수 실책 등으로 운좋게 나간 경우는 안타로 기록되지 않는다 (기록원이 판단). 1루타, 2루타, 3루타, 홈런 모두 안타로 가산됨
* 2루타, 3루타 (2B, 3B): 안타인데 한번에 2루, 3루까지 간 것
* 홈런 (HR): 안타인데 한번에 홈까지 온 것 (보통 담장을 넘어가야 홈런이라 생각하지만 담장을 안넘어가도 한번에 한바퀴를 돌면 인사이드 파크 홈런이라고 해서 홈런으로 친다)
* 루타 (TB): 1루타는 1점, 2루타는 2점, 3루타는 3점, 홈런은 4점으로 쳐서 전부 더한 것
* 타점 (RBI): 타자가 공을 쳐서, 나 말고 나가있던 다른 주자를 홈으로 불러들여 점수가 되면 타점이라 한다
* 도루 (SB): 타자가 공을 치는 것과 상관없이 베이스에 나가있던 주자가 다음 루로 뛰는 것. (주자의 기록)
* 도실 (CS): 도루를 하려다가 실패해서 아웃되는 것
* 볼넷 (BB): 타자 입장에서 상대 투수가 볼 (스트라이크 존을 벗어나는 공) 을 4개 던지면 공을 안치고도 그냥 1루로 나간다.
* 사구 (HBP): 상대 투수가 던진 공이 타자의 몸에 맞으면 그냥 1루로 나간다.
* 고4 (IBB): 고의볼넷. 상대 투수가 공 컨트롤을 못해서 볼을 4개 던진게 아니라, 타자랑 상대할 의사가 없어서 일부러 바깥쪽으로만 공 4개를 던지는 것.
* 삼진 (SO): 타자 입장에서 상대 투수가 스트라이크를 3개 던지는동안 공을 못쳐서 아웃당한 것
* 병살 (GDP): 타자가 공을 쳤는데 원래 나가있던 주자랑 자기까지 2명이 한번에 아웃당하는 것
* 희타 (SH): 희생타. 타자가 공을 쳤고 자기는 아웃됐는데 3루에 있던 주자가 그 틈에 홈으로 들어와서 점수를 냈을 때 기록
* 희비 (SF): 희생플라이. 타자가 공을 멀리 띄우고 공이 잡혀서 자기는 아웃됐는데, 3루에 있던 주자가 공이 잡힌 직후 뛰기 시작해서 홈으로 들어와서 점수를 냈을 때 기록
* 타율 (BA): 나와서 안타를 치는 비율. 안타 / 타수
* 출루율 (OBP): 안타든 아니든 아웃을 당하지 않고 살아서 나가는 비율. 안타+볼넷+몸에맞는공 / 타수+볼넷+몸에맞는공+희생플라이
* 장타율 (SLG): 루타 / 타수. 즉 한번 나와서 3루타를 하나 쳤으면 3.000이 된다 (비율스탯이라 최대 1이라고 생각하기 쉽지만 이론상 최대값은 4.000)
* OPS: 출루율 + 장타율. 공식이 심플하면서도 타격 생산성을 잘 나타내주는 스탯
* wOBA: 가중출루율 정도로 번역하는데 공식이 꽤 복잡함
* wRC+: 조정 득점 창출력. 리그평균을 100으로 잡으므로 100보다 높으면 평균 이상의 타자, 두자리수면 평균 이하의 타자라는 뜻이다. 구장에 따른 파크팩터까지 반영함.
* WAR: 대체선수대비 승리기여도.  야구 통계 스탯의 결정판. 대체선수란 1군~2군을 아슬아슬하게 왔다갔다하는 수준의 선수를 의미하며, 그 선수를 썼을 때 대체선수 대비 몇 승을 더 올릴 수 있을지를 예상한 것. 오만 스탯이 다 계산식에 들어간다
* WPA: 승리확률 기여도.

#### 투수 기록
* 출장 (G): 말 그대로 경기에 나온 횟수. 선발투수는 보통 20~30 정도고 교체로 나오는 불펜투수는 자주 나오는 대신 짧게 던진다.
* 완투 (CG): 선발투수 혼자 경기 내내 던짐. (=투수교체가 없었을 때 선발투수에게 붙는 기록)
* 완봉 (SHO): 완투 + 무실점 경기
* 선발 (GS): 선발투수로 나온 경기수.
* 승 (W): 투수가 자기가 나온 상황에서 팀이 리드하는 점수를 내고 그대로 경기가 끝났을 때. 단, 선발투수는 5회 이상을 던져야 승리 요건을 충족.
* 패 (L): 투수가 자기가 나온 상황에서 팀이 리드를 뺏기고 그대로 경기가 끝났을 때. 
* 세 (SV): 마무리투수의 기록으로 팀이 아슬아슬하게 리드하고 있는 상황에서 자기가 경기를 끝냈을 때.
* 홀드 (HLD): 중간투수의 기록으로 팀이 아슬아슬하게 리드하고 있는 상황에서 다음 투수에게 리드를 무사히 넘겨줬을 때.
* 이닝 (IP): 회 = 이닝. 즉 몇 회를 던졌는가. 소수점은 아웃카운트 수를 뜻함 (ex: 선발투수가 6회 1아웃까지 잡고 내려오면 5.1이닝을 던진 것)
* 실점 (R): 말 그대로 투수가 올라와있는 상태에서 상대에게 내준 점수
* 자책점 (ER): 실점 중에서, 수비수들의 실책으로 인해 내준 점수를 제외한 점수
* 안타 (H): 보통 투수한테는 피안타라고 함. 타자에게 맞은 안타 수
* 2타, 3타, 홈런: 마찬가지
* 볼넷 (BB): 타자에게 내준 볼넷 수
* 고4 (IBB)
* 사구 (HBP)
* 삼진 (SO): 보통 투수기록은 탈삼진이라 부른다.
* 보크 (BK): 투수는 던질 때마다 일정한 동작을 거쳐야 하는데 그걸 어기는 반칙. 현재 나가있는 주자들이 1루씩 더 진루한다.
* 폭투 (WP): 공을 던졌는데 포수가 잡을 수 없는 이상한데로 날아가거나 땅에 패대기쳐서 그 사이에 타자/주자가 진루하는 경우.
* ERA: 평균자책점. 그 투수가 한 경기 (9회) 를 던진다고 했을때 평균적으로 실점할 것으로 예상되는 점수. 자책점*9 / 이닝
* FIP: 수비무관 평균자책점. 운빨, 수비빨을 배제하고 투수를 평가하기 위해 나온 스탯.
* WHIP: 이닝당 출루허용율. ex) 1회당 1명씩 계속 타자를 내보낸다면 1.00
* ERA+: ERA에다가 평균을 100으로 하고 구장 보정 (파크팩터) 등을 추가로 고려한 스탯.
* FIP+: 마찬가지로 FIP에다가 평균을 100으로 하고... 추가로 고려한 스탯.
* WAR: 대체선수대비 승리기여도. 타자의 그것과 목적은 같지만 계산식은 당연히 다르다.
* WPA: 승리확률 기여도. 매 순간 선수의 플레이로 인해 팀의 기대 승률이 변하는데, 그 변화량을 누적한 것. ex) 공을 던지기 전에 우리팀의 승리확률이 0.6이었는데 홈런을 맞아서 승리확률이 0.4로 떨어졌으면 WPA가 -0.2 된다.

#### 수비 기록 (참고: 스탯티즈 수비기록은 별로 신뢰성이 높은 편은 아니라고 함) (수비스탯 나도 너무 생소하다...)
* 출장 (G)
* 선발 (GS)
* 이닝 (Inn)
* 기회 (Ch): 자기한테 공이 날아온 횟수. 자살 + 보살 + 실책
* 자살 (PO): 자기 스스로 죽는다는 소리가 아니라 '자기' 가 직접 '타자/주자' 를 잡았다 (태그 또는 베이스를 밟아서 아웃시켰다) 는 뜻.
* 보살 (A): 어시스트. 다른 선수한테 공을 연결해서 그 선수가 타자/주자를 잡아내면 자기한테는 보살 (어시스트) 가 주어진다.
* 실책 (E): 에러. 말 그대로 정상적인 플레이를 했으면 아웃시켰을 상황에서 개그수비로 아웃을 못 시키는 것.
* 수비율 (Fld%): 자살+보살 / 기회. 즉 실책 안하고 제대로 수비한 비율
* RF9: 평균적으로 한경기에 수비를 몇개 해내느냐. 9*(자살+보살) / 이닝
* sFR: statiz Fielding Runs라고 해서 스탯티즈에서 자체적으로 고안한 수비 스탯이라고 함.
    * RNG: 수비 범위.
    * ARM: 어깨가 얼마나 강한지? (외야수에게만 적용)
    * CS: 도루 저지 (포수에게만 적용)
    * BLK: 블로킹 (포수에게만 적용)
    * E+: 실책 관련 득점 기여
    * RAA: 평균대비 수비득점기여. 0이 평균
    * /133: RAA를 133경기 기준으로 조정한 것 (지금은 1시즌 144게임인데 왜?)
* POSADJ: 포지션 가중치?
* RAA with ADJ: 포지션 가중치를 반영한 평균대비 수비득점기여.
* WAA w/o ADJ: 평균대비 수비 승리기여도.
* WAA with ADJ: 포지션 가중치를 반영한 평균대비 수비 승리기여도.

## 인프라 세팅
### 서버 접속: Alias 이용
- Git Bash를 실행한다
- cd ~ 입력하여 홈 디렉토리로 이동
- vi .bashrc 입력하여 .bashrc 파일을 생성 & 편집 (홈 디렉토리에 .bashrc를 생성해야 알아듣는다. 사용자 설정 파일임.)
```
alias goaws='cd [pem파일 경로] && ssh -i J3A110T.pem ubuntu@j3a110.p.ssafy.io'
```
- Git Bash를 재실행한 뒤, 'goaws' 명령어 입력을 통해 서버로 접속한다

### 패키지 설치 관리자 (apt) 업데이트
```
sudo apt update
sudo apt upgrade
```

### Java
* OpenJDK 14 설치
```
curl -O https://download.java.net/java/GA/jdk14/076bab302c7b4508975440c56f6cc26a/36/GPL/openjdk-14_linux-x64_bin.tar.gz
tar xvf openjdk-14_linux-x64_bin.tar.gz
sudo mv jdk-14 /opt/

sudo tee /etc/profile.d/jdk14.sh

export JAVA_HOME=/opt/jdk-14
export PATH=\$PATH:\$JAVA_HOME/bin

source /etc/profile.d/jdk14.sh

$ echo $JAVA_HOME
$ java --version
```

* OpenJDK 8 설치: 젠킨스 때문에 8 버전도 필요함
```
sudo apt-get install openjdk-8-jdk
java -version
```

* update-alternatives 설정??
```
sudo update-alternatives --install /usr/bin/java java /opt/jdk-14/bin/java 1
sudo update-alternatives --config java
```

### Jenkins
* 설치
```
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo apt-key add -
sudo sh -c 'echo deb https://pkg.jenkins.io/debian-stable binary/ > \
    /etc/apt/sources.list.d/jenkins.list'
sudo apt-get update
sudo apt-get install jenkins
```

* 자바 8 연동
```
cd /etc/init.d
sudo vi jenkins

파일 내용에서 PATH=... 부분 찾아서 java 8 경로 집어넣기

젠킨스 실행

sudo usermod -a -G docker jenkins (젠킨스가 도커 sudo 없이 실행가능하게 해주기)

젠킨스 서버 (8080 포트) 접속 후
sudo cat /var/lib/...
에서 비밀번호 알아내서 입력.
```

* 젠킨스 설정 & 깃랩 연동
    * Freestyle Project 생성   
    * Manage Jenkins -> gitlab 플러그인 설치   
    * Manage Jenkins -> Configure Systems -> Gitlab 에 빈칸 채워넣어 설정 (gitlab host URL: lab.ssafy.com, API 토큰 필요)
    * Config -> 소스코드 매니지먼트 -> gitlab 주소 입력
    * Credentials 설정: username with password
    * Build when a change is pushed to GitLab 체크 -> push events 체크
    * 상세설정에서 Allowed branches -> master 브랜치로 설정
    * Secret Token 생성 -> gitlab의 Setting - Integration 탭에서 젠킨스 URL과 시크릿 토큰 넣어주고 Add Webhook

### Nginx
```
sudo apt-get install nginx
cd /etc/nginx
nginx.conf 파일을 확인해보면 세부 설정파일 경로가 conf.d로 설정되어 있는걸 확인할 수 있다
cd /etc/nginx/conf.d

<spring.conf>
server {
       listen 80;
       listen [::]:80;
       server_name example.com;
       location /spring {
           rewrite ^/spring^/ /$1 break; -- /spring/ 을 그대로 붙여서 넘겨주겠다.
           proxy_pass http://localhost:8902; -- 넘겨줄 URL
       }
}
<front.conf>
server {
       listen 80;
       listen [::]:80;
       server_name example.com;
       location / {
           proxy_pass http://localhost:8901/;
       }
}


sudo nginx -t (문법 검사)
sudo service nginx reload

```

### Docker
* 의존성 패키지 설치
```
sudo apt-get install curl apt-transport-https ca-certificates software-properties-common
```

* 리포지토리 접근을 위한 인증키 추가
```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

* 리포지토리 추가
```
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt update
sudo apt upgrade
```

* 도커 설치 및 체크
```
apt-cache policy docker-ce
sudo apt install docker-ce
sudo service docker status
sudo service docker start
```

* 유저가 도커를 sudo 없이 사용하는 권한 부여하고 테스트
```
sudo usermod -aG docker $USER
docker info
docker ps
```

### MariaDB
* 도커 위에서 돌리기 & 설정
```
docker run --name maria-db -p 3306:3306 -e MYSQL_ROOT_PASSWORD=legend! -d mariadb
docker exec -it maria-db mysql -u root -p

create database legend;
create user 'legend'@'%' identified by 'legend!';
grant all privileges on legend.* to 'legend'@'%';
```

### 기타 이슈
- ./mvnw 실행 권한 없음: build-deploy.sh에 권한부여 명령어 추가
- 서버에서, 로컬에서 적용되는 코드 각각 다르게 어떻게?

## Commit Convention
* git flow policy
    - develop 브랜치 하위에 front, spring, django 세 개의 브랜치   
    - 각자 로컬에서 작업할 땐 저 셋 중 하나 아래에서 하위 브랜치로 feature/(작업기능명) 따서 작업. (작업기능명) 의 이름이 길어지면 언더바 사용   
    - front, spring, django 까지는 각자 merge 하고, develop으로 merge 할 때는 merge request   

* commit message   
ex) [Feat] Add social login
    - 커밋 타입: Feat, Fix, Refactor, Docs, Release
    - 커밋 제목: 영어로 간략하게 작성
    - 내용 (optional): 그 아랫줄부터는 자유롭게 내용 작성

* merge request    
ex) Feature/social_login to Front
    - 제목: 어떤 브랜치에서 어떤 브랜치로 머지하는지
    - 내용 (optional): 커밋했던 것들 내용 대충 요약 (영어로...)