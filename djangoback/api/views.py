# models 와 sereializers 모듈 가져옴
from api import models, serializers
from rest_framework import viewsets
from rest_framework.pagination import PageNumberPagination
from bs4 import BeautifulSoup
from urllib.request import urlopen
from selenium import webdriver
import pandas as pd
import re
from .models import *
from django.conf import settings
from sqlalchemy import create_engine

def getPitchersRecords(request):

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
    url = 'http://www.statiz.co.kr/stat.php?re=1&lr=0&sn=30&pa=0&ys=2020&ye=2020'
    driver.implicitly_wait(20) # 로딩될때까지 대기를 줬는데도?
    driver.get(url)
    page = driver.execute_script('return document.body.innerHTML')
    soup = BeautifulSoup(''.join(page), 'html.parser', from_encoding='utf-8')
    table = soup.select('#mytable tr') # 표

    #print(table)

    # '타자' (HT로 표시) 를 빼먹었었다.
    cols = ['player_id', 'player_name', 'pitcher_year', 'team', 'WAR*',
            'pitcher_G', 'pitcher_CG', 'pitcher_SHO', 'pitcher_GS', 'pitcher_W', 'pitcher_L', 'pitcher_SV', 
            'pitcher_HLD', 'pitcher_IP', 'pitcher_R', 'pitcher_ER', 'pitcher_HT', 'pitcher_H', 'pitcher_2B', 'pitcher_3B', 
            'pitcher_Homerun', 'pitcher_BB',
            'pitcher_IBB', 'pitcher_HBP', 'pitcher_SO', 'pitcher_BK', 'pitcher_WP', 
            'pitcher_ERA', 'pitcher_FIP', 'pitcher_WHIP', 'pitcher_ERA_plus', 'pitcher_FIP_plus', 'pitcher_WAR', 'pitcher_WPA']

    datas = []
    regex = re.compile('\d\d\D+')

    for row in table: # 각 줄
        cells = row.select('td')
        temp_list = []

        for data in cells: # 각 칸
            text = data.get_text()
            
            if regex.match(text):
                year = text[:2]
                team = text[2:]
                temp_list.append(year)
                temp_list.append(team)
            
            else:
                temp_list.append(text)

        #print(len(temp_list))
        if len(temp_list) == 34:
            #print(temp_list)
            datas.append(temp_list)

    df = pd.DataFrame(datas, columns=cols)
    print(df)

    players = df[["player_name", "team"]]
    print(players)
    records = df.drop(columns=['player_name', 'team', 'WAR*', 'pitcher_HT']) # DB 저장용

    teams = {'K': 1, '삼': 3, '두': 4, 'S': 6, 'L': 11, '롯': 13, '한': 14, 'N': 16, '키': 19}

    engine = create_engine(database_url, echo=False)

    # ValueError: Cannot assign "1": "Player.player_position" must be a "Position" instance 에러: 외래키 제약조건 관련
    for i, p in players.iterrows():
        pos = Position.objects.get(pk=1)
        Player(player_name=p.player_name, team_id=teams[p.team], player_position=pos).save()
        player_obj = Player.objects.filter(player_name=p.player_name) # 리스트로 온다. select ~ where 문에 해당하므로
        pid = player_obj[0].player_id
        records['player_id'][i] = pid

    print(records)
    records = records.set_index('player_id')
    records.to_sql('record_pitcher', con=engine, if_exists='append')
    # players_from_db = Player.objects.all()
    # for p in players_from_db:
    #     print(p.player_name)


# class PlayerViewSet(viewsets.ModelViewSet):
def getHittersRecords(request):
    driver = webdriver.Chrome('chromedriver.exe')
    # re: 0은 타격, 1은 투수, 2는 수비스탯
    # pa, sn: pa번째 선수부터 sn명의 목록을 한 페이지에 띄운다.
    # ys, ye: year start ~ end
    # 이상하게 한번에 몇명을 긁는지에 따라 다 안 긁힐때가 있다. 왜?
    url = 'http://www.statiz.co.kr/stat.php?re=0&lr=0&sn=30&pa=0&ys=2020&ye=2020'
    driver.implicitly_wait(20) # 로딩될때까지 대기를 줬는데도?
    driver.get(url)
    #webpage = urlopen(url)
    page = driver.execute_script('return document.body.innerHTML')
    soup = BeautifulSoup(''.join(page), 'html.parser', from_encoding='utf-8')
    #print(source)
    table = soup.select('#mytable tr') # 표

    cols = ['id', 'player_name', 'season', 'team', 'position', 'WAR', 'G', 'PA', 'AB', 'R', 'H', '2B', '3B',
                    'HR', 'TB', 'RBI', 'SB', 'CS', 'BB', 'HBP', 'IBB', 'SO', 'GDP', 'SH', 'SF',
                    'BA', 'OBP', 'SLG', 'OPS', 'wOBA', 'wRC_plus', 'WAR*', 'WPA']

    datas = []

    index = 0
    regex = re.compile('\d\d\D\w+')

    for row in table: # 각 줄
        cells = row.select('td')
        temp_list = []

        for data in cells: # 각 칸
            text = data.get_text()
            
            if regex.match(text):
                year = text[:2]
                team = text[2]
                pos = text[3:]
                temp_list.append(year)
                temp_list.append(team)
                temp_list.append(pos)
            
            else:
                temp_list.append(text)

        print(temp_list) 
        #print(len(temp_list))
        if len(temp_list) == 33:
            datas.append(temp_list)
        index = index+1

    # 첨엔 데이터프레임 먼저 정의하고 Series로 append하는 식으로 해보려고 했는데 잘 안됐다...
    df = pd.DataFrame(datas, columns=cols)
    print(df)

    players = df[["player_name", "team", "position"]]
    print(players)

    positions = {'P': '1', 'C': '2', '1B': '3', '2B': '4', '3B': '5', 'SS': '6', 'LF': '7', 'CF': '8', 'RF': '9', 'DH': '10'}

    # 선수이름 컬럼명을 name으로 했더니 p.name이라는 컬럼이 기본으로 있어서 그거랑 겹쳐서 이상하게 된다. player_name으로 변경
    for i, p in players.iterrows():
        print (p)
        Player(player_name=p.player_name, team_id=1, player_position=positions[p.position]).save()


# 페이지네이션을 위한 클래스 생성
# PageNumberPagination 을 상속받음
class SmallPagination(PageNumberPagination):
    page_size = 10
    page_size_query_param = "page_size"
    max_page_size = 50

# Store 모델에 대한 ViewSet 지정
class StoreViewSet(viewsets.ModelViewSet):
    # 1. 정의해둔 serializer 클래스 지정
    serializer_class = serializers.StoreSerializer
    
    # 2. 정의한 pagination 클래스 지정(Optional)
    pagination_class = SmallPagination

    # 3. 상황에 맞는 queryset 지정
    # 항상 같은 queryset 사용 할 땐
    # queryset = models.Store.objects.all() 해도 된다
    def get_queryset(self):
        # 쿼리 파라미터에서 name 이라는 항목의 값을 가져옴
        name = self.request.query_params.get("name", "")
        # 이름에 해당 문자열을 포함하는 음식점 목록만 가져오는데 id 순으로 정렬함
        queryset = (
            models.Store.objects.all().filter(store_name__contains=name).order_by("id")
        )
        # 위에서 뽑은 결과를 queryset 으로 지정하기위해 반환
        return queryset
