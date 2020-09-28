# models 와 sereializers 모듈 가져옴
from api import serializers
from api.views import models
from rest_framework import viewsets
from rest_framework.pagination import PageNumberPagination
from bs4 import BeautifulSoup
from urllib.request import urlopen
from selenium import webdriver
import pandas as pd
import re
from .models import *
from django.conf import settings
from sqlalchemy import create_engine # to_sql 사용하기 위해 필요했던 라이브러리
import hashlib
from rest_framework.decorators import api_view, parser_classes

pitcher_cols = ['player_id', 'birth', 'player_name', 'pitcher_year', 'team', 'WAR*',
            'pitcher_G', 'pitcher_CG', 'pitcher_SHO', 'pitcher_GS', 'pitcher_W', 'pitcher_L', 'pitcher_SV', 
            'pitcher_HLD', 'pitcher_IP', 'pitcher_R', 'pitcher_ER', 'pitcher_HT', 'pitcher_H', 'pitcher_2B', 'pitcher_3B', 
            'pitcher_Homerun', 'pitcher_BB',
            'pitcher_IBB', 'pitcher_HBP', 'pitcher_SO', 'pitcher_BK', 'pitcher_WP', 
            'pitcher_ERA', 'pitcher_FIP', 'pitcher_WHIP', 'pitcher_ERA_plus', 'pitcher_FIP_plus', 'pitcher_WAR', 'pitcher_WPA']

hitter_cols = ['player_id', 'birth', 'player_name', 'hitter_year', 'team', 'position', 'WAR*', 
                    'hitter_gamecnt', 'hitter_PA', 'hitter_AB', 'hitter_score', 'hitter_hit', 
                    'hitter_double', 'hitter_triple',
                    'hitter_homerun', 'hitter_TB', 'hitter_RBI', 'hitter_SB', 'hitter_CS', 
                    'hitter_BB', 'hitter_HBP', 'hitter_IBB', 
                    'hitter_SO', 'hitter_GDP', 'hitter_SH', 'hitter_SF',
                    'hitter_BA', 'hitter_OBP', 'hitter_SLG', 'hitter_OPS', 'hitter_wOBA', 
                    'hitter_wRC', 'hitter_WAR', 'hitter_WPA']

fielder_cols = ['player_id', 'birth', 'player_name', 'fielder_year', 'team', 'position', 'WAAwithADJ*', 
                'fielder_g', 'fielder_gs', 'fielder_inn', 'fielder_ch', 
                'fielder_po', 'fielder_a', 'fielder_e', 'fielder_fld', 'fielder_rf9',
                'fielder_rng', 'fielder_arm', 'fielder_cs', 'fielder_blk', 'fielder_e_plus', 
                'fielder_raa', 'fielder_133', 'fielder_posadj', 'fielder_raawithadj', 'fielder_waawoadj', 'fielder_waawithadj']

# KIA랑 KT가 둘 다 K인데 어떡하냐? (kt: 21)
# 어떤 데이터는 KT를 k로 표기하고 있다....
# 삼미랑 삼성도 삼이다 둘 다... (삼미: 10)
teams = {'K': 1, '해': 2, '삼': 3, '두': 4, 'O': 5, 'S': 6, '현': 7, '태': 8, '청': 9, 
        'L': 11, 'M': 12, '롯': 13, '한': 14, '빙': 15, 'N': 16, '히': 17, '넥': 18, '키': 19, '쌍': 20, 'k': 21}

positions = {'P': 1, 'C': 2, '1B': 3, '2B': 4, '3B': 5, 'SS': 6, 'LF': 7, 'CF': 8, 'RF': 9, 'DH': 10}

# <= 2013년은
# 타자기록: WPA가 없음
# 투수기록: 2루타, 3루타 맞은 수랑 WPA가 없음
# 수비기록: 몇몇 빼곤 아예 없음

@api_view(["GET"])
def getPitchersRecords(request):
    """
    2015~2020년의 투수 데이터 크롤링하여 DB에 삽입 (player 테이블 & record_pitcher 테이블)
    """
    for year in range(2015, 2021, 1): # 2019년부터 1982년까지 거슬러올라가면서 기록을 넣는다
        for start in range(0, 500, 20):
            inserted = getRecords_crawling(1, year, start)
            if (inserted == 0): # 페이지에 선수정보가 더이상 안뜨면 다음년도로 넘어감 
                break

@api_view(["GET"])
def getHittersRecords(request):
    """
    2015~2020년의 타자 데이터 크롤링하여 DB에 삽입 (player 테이블 & record_hitter 테이블)
    """
    for year in range(2015, 2021, 1):
        for start in range(0, 500, 20):
            inserted = getRecords_crawling(0, year, start)
            if (inserted == 0): 
                break

@api_view(["GET"])
def getFieldersRecords(request):
    """
    2015~2020년의 수비 데이터 크롤링하여 DB에 삽입 (player 테이블 & record_fielder 테이블)
    """
    for year in range(2015, 2021, 1):
        for start in range(0, 500, 20):
            inserted = getRecords_crawling(2, year, start)
            if (inserted == 0): 
                break

def getRecords_crawling(type, year, start):

    user = settings.DATABASES['default']['USER']
    password = settings.DATABASES['default']['PASSWORD']
    database_name = settings.DATABASES['default']['NAME']
    database_host = settings.DATABASES['default']['HOST']
    port = settings.DATABASES['default']['PORT']

    database_url = 'mysql://{user}:{password}@{database_host}:{port}/{database_name}'.format(
        user=user,
        password=password,
        database_name=database_name,
        database_host=database_host,
        port=port
    )

    driver = webdriver.Chrome('chromedriver.exe')
    url = 'http://www.statiz.co.kr/stat.php?re=' + str(type) + '&lr=0&sn=20&pa=' + str(start) + '&ys=' + str(year) + '&ye=' + str(year)
    driver.implicitly_wait(20) # 로딩될때까지 대기를 줬는데도?
    driver.get(url)
    page = driver.execute_script('return document.body.innerHTML')
    soup = BeautifulSoup(''.join(page), 'html.parser', from_encoding='utf-8')
    table = soup.select('#mytable tr') # 표

    #print(table)

    # '타자' (HT로 표시) 를 빼먹었었다.
    # birth: 생일. 동명이인 구분을 위해서 스탯티즈에서는 이름-생일을 pk로 사용하고 있는 것 같다
    cols = []
    if type == 0:
        cols = hitter_cols
    elif type == 1:
        cols = pitcher_cols
    elif type == 2:
        cols = fielder_cols

    datas = []
    regex = re.compile('\d\d\D+')

    for row in table: # 각 줄
        cells = row.select('td')
        temp_list = []
        for data in cells: # 각 칸
            # data의 타입이 string이 아니라는 점에 주의... str 캐스팅을 한번 해줘야 문자열 메서드를 쓸 수 있었다
            data_str = str(data)
            if len(temp_list) == 1 and data_str.find("a href") > 0: # 선수이름 링크 보고 생일 뽑아내기
                birthday = data_str.split("birth=")[1][:10]
                temp_list.append(birthday)

            text = data.get_text()
            
            # 연도-팀-세부포지션 뽑아내기. 투수는 세부포지션이 없지만 타자는 있다.
            if regex.match(text):
                if type == 1:
                    year = text[:2]
                    team = text[-1]
                    temp_list.append(year)
                    temp_list.append(team)
                else:
                    if len(text) <= 3: # 포지션 없는 선수는 그냥 긁지 말자...
                        break
                    year = text[:2]

                    if (text[-1] == 'P'): # 투수가 타자로 나오는 경우 가끔 있는데... 타자기록에선 무시하고, 수비기록에는 포함
                        if type == 0: break
                        elif type == 2:
                            pos = text[-1]
                            team = text[-2]
                    elif (text[-1] == 'C'):
                        pos = text[-1]
                        team = text[-2]
                    else:
                        pos = text[-2:len(text)]
                        team = text[-3]
                    temp_list.append(year)
                    temp_list.append(team)
                    temp_list.append(pos)
            
            else:
                if text == '  ': # 비어있는 칸 처리: 일단은 0으로 하긴 했는데 null같은 걸로 보내는게 낫나?
                    text = '0'
                temp_list.append(text)

        print(len(temp_list))
        if len(temp_list) > len(cols) - 5:
            print(temp_list)
            datas.append(temp_list)

    df = pd.DataFrame(datas, columns=cols)
    print(df)

    if type==1:
        players = df[["player_name", "team", "birth"]]
    else:
        players = df[["player_name", "team", "birth", "position"]]
    print(players)

    if type==1:
        records = df.drop(columns=['player_name', 'birth', 'team', 'WAR*', 'pitcher_HT']) # DB 저장용
    elif type==0:
        records = df.drop(columns=['player_name', 'birth', 'team', 'position', 'WAR*'])
    elif type==2:
        records = df.drop(columns=['player_name', 'birth', 'team', 'position', 'WAAwithADJ*'])

    engine = create_engine(database_url, echo=False)

    # ValueError: Cannot assign "1": "Player.player_position" must be a "Position" instance 에러: 외래키 제약조건 관련
    for i, p in players.iterrows():
        if type==1:
            pos = Position.objects.get(pk=1)
        else:
            pos = Position.objects.get(pk=positions[p.position])
            
        # player_id: 이름 + 생일 hash로 생성
        hash_string = p.player_name + p.birth
        #pid = abs(hash(hash_string)) % (10 ** 8)
        # 위의 해시함수는 파이썬 3.3부터 non-deterministic하게 바뀌었다... 늘 똑같은 결과 원하면 무슨 환경변수 설정해야된다 함
        # 이 해시함수는 한글 들어있음 인코딩 해줘야 함
        pid = int(hashlib.sha1(hash_string.encode('utf-8')).hexdigest(), 16) % (10 ** 8)

        # insert
        Player(player_id=pid, player_name=p.player_name, team_id=teams[p.team], player_birth=p.birth, player_position=pos).save()
        #player_obj = Player.objects.filter(player_name=p.player_name) # 리스트로 온다. select ~ where 문에 해당하므로
        #pid = player_obj[0].player_id
        records['player_id'][i] = pid

    print(records)
    records = records.set_index('player_id')
    # records라는 pandas dataframe에 있는 모든 데이터를, record_pitcher라는 테이블에 insert하겠다.
    if type == 0:
        records.to_sql('record_hitter', con=engine, if_exists='append')
    elif type == 1:
        records.to_sql('record_pitcher', con=engine, if_exists='append')
    elif type == 2:
        records.to_sql('record_fielder', con=engine, if_exists='append')

    return len(table)
    # select 
    # players_from_db = Player.objects.all()
    # for p in players_from_db:
    #     print(p.player_name)

def getEntireRecords(request):
    count = 0
    for start in range(0, 1200, 20):
        print(count)
        driver = webdriver.Chrome('chromedriver.exe')
        url = 'http://www.statiz.co.kr/stat_at.php?re=2&lr=0&sn=20&pa=' + str(start) + '&ys=2015&ye=2020'
        driver.implicitly_wait(200) # 로딩될때까지 대기를 줬는데도?
        driver.get(url)
        page = driver.execute_script('return document.body.innerHTML')
        soup = BeautifulSoup(''.join(page), 'html.parser', from_encoding='utf-8')
        table = soup.select('#mytable tr') # 표

        for row in table: # 각 줄
            cells = row.select('td')
            temp_list = []
            for data in cells: # 각 칸
                # data의 타입이 string이 아니라는 점에 주의... str 캐스팅을 한번 해줘야 문자열 메서드를 쓸 수 있었다
                data_str = str(data)
                if len(temp_list) == 1 and data_str.find("a href") > 0: # 선수이름 링크 보고 생일 뽑아내기
                    birthday = data_str.split("birth=")[1][:10]
                    temp_list.append(birthday)

                text = data.get_text()
                #print(text)

                if text.find('+') >= 0:
                    hash_string = temp_list[2] + temp_list[1] # 이름+생일
                    pid = int(hashlib.sha1(hash_string.encode('utf-8')).hexdigest(), 16) % (10 ** 8)
                    try:
                        player = Player.objects.get(pk=pid)
                        player.player_retire = 0
                        player.save()
                        count += 1
                        break
                    except Player.DoesNotExist:
                        break

                else:
                    if text == '  ':
                        text = '0'
                    temp_list.append(text)



# class PlayerViewSet(viewsets.ModelViewSet):
# def getHittersRecords(request):
#     driver = webdriver.Chrome('chromedriver.exe')
#     # re: 0은 타격, 1은 투수, 2는 수비스탯
#     # pa, sn: pa번째 선수부터 sn명의 목록을 한 페이지에 띄운다.
#     # ys, ye: year start ~ end
#     # 이상하게 한번에 몇명을 긁는지에 따라 다 안 긁힐때가 있다. 왜?
#     url = 'http://www.statiz.co.kr/stat.php?re=0&lr=0&sn=30&pa=0&ys=2020&ye=2020'
#     driver.implicitly_wait(20) # 로딩될때까지 대기를 줬는데도?
#     driver.get(url)
#     #webpage = urlopen(url)
#     page = driver.execute_script('return document.body.innerHTML')
#     soup = BeautifulSoup(''.join(page), 'html.parser', from_encoding='utf-8')
#     #print(source)
#     table = soup.select('#mytable tr') # 표

#     datas = []

#     index = 0
#     regex = re.compile('\d\d\D\w+')

#     for row in table: # 각 줄
#         cells = row.select('td')
#         temp_list = []

#         for data in cells: # 각 칸
#             text = data.get_text()
            
#             if regex.match(text):
#                 year = text[:2]
#                 team = text[2]
#                 pos = text[3:]
#                 temp_list.append(year)
#                 temp_list.append(team)
#                 temp_list.append(pos)
            
#             else:
#                 temp_list.append(text)

#         print(temp_list) 
#         #print(len(temp_list))
#         if len(temp_list) == 33:
#             datas.append(temp_list)
#         index = index+1

#     # 첨엔 데이터프레임 먼저 정의하고 Series로 append하는 식으로 해보려고 했는데 잘 안됐다...
#     df = pd.DataFrame(datas, columns=cols)
#     print(df)

#     players = df[["player_name", "team", "position"]]
#     print(players)

#     positions = {'P': '1', 'C': '2', '1B': '3', '2B': '4', '3B': '5', 'SS': '6', 'LF': '7', 'CF': '8', 'RF': '9', 'DH': '10'}

#     # 선수이름 컬럼명을 name으로 했더니 p.name이라는 컬럼이 기본으로 있어서 그거랑 겹쳐서 이상하게 된다. player_name으로 변경
#     for i, p in players.iterrows():
#         print (p)
#         Player(player_name=p.player_name, team_id=1, player_position=positions[p.position]).save()


# # 페이지네이션을 위한 클래스 생성
# # PageNumberPagination 을 상속받음
# class SmallPagination(PageNumberPagination):
#     page_size = 10
#     page_size_query_param = "page_size"
#     max_page_size = 50

# # Store 모델에 대한 ViewSet 지정
# class StoreViewSet(viewsets.ModelViewSet):
#     # 1. 정의해둔 serializer 클래스 지정
#     serializer_class = serializers.StoreSerializer
    
#     # 2. 정의한 pagination 클래스 지정(Optional)
#     pagination_class = SmallPagination

#     # 3. 상황에 맞는 queryset 지정
#     # 항상 같은 queryset 사용 할 땐
#     # queryset = models.Store.objects.all() 해도 된다
#     def get_queryset(self):
#         # 쿼리 파라미터에서 name 이라는 항목의 값을 가져옴
#         name = self.request.query_params.get("name", "")
#         # 이름에 해당 문자열을 포함하는 음식점 목록만 가져오는데 id 순으로 정렬함
#         queryset = (
#             models.Store.objects.all().filter(store_name__contains=name).order_by("id")
#         )
#         # 위에서 뽑은 결과를 queryset 으로 지정하기위해 반환
#         return queryset