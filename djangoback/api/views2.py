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
from sqlalchemy import create_engine # to_sql 사용하기 위해 필요했던 라이브러리

teams = {'KIA': 1, '해태': 2, '삼성': 3, '두산': 4, 'OB': 5, 'SK': 6, '현대': 7, '태평양': 8, '청보': 9, 
        'LG': 11, 'MBC': 12, '롯데': 13, '한화': 14, '빙그레': 15, 'NC': 16, '히어로즈': 17, 
        '넥센': 18, '키움': 19, '쌍방울': 20, 'KT': 21}

lineup_cols = ['team_id', 'hitter1', 'hitter2',
'hitter3','hitter4','hitter5','hitter6','hitter7','hitter8','hitter9','pitcher']

def getLineup(request):

    urls = ["http://www.statiz.co.kr/boxscore.php?opt=5&date=2020-05-05&stadium=%EB%9D%BC%EC%9D%B4%EC%98%A8%EC%A6%88%ED%8C%8C%ED%81%AC&hour=14",
            "http://www.statiz.co.kr/boxscore.php?opt=5&date=2020-05-05&stadium=%EC%9D%B8%EC%B2%9C%EB%AC%B8%ED%95%99&hour=14",
            "http://www.statiz.co.kr/boxscore.php?opt=5&date=2020-05-05&stadium=%EC%9E%A0%EC%8B%A4&hour=14",
            "http://www.statiz.co.kr/boxscore.php?opt=5&date=2020-05-05&stadium=%EC%B1%94%ED%94%BC%EC%96%B8%EC%8A%A4%ED%95%84%EB%93%9C&hour=14",
            "http://www.statiz.co.kr/boxscore.php?opt=5&date=2020-05-05&stadium=%EC%BC%80%EC%9D%B4%ED%8B%B0%EC%9C%84%EC%A6%88%ED%8C%8C%ED%81%AC&hour=14"]

    for url in urls:
        driver = webdriver.Chrome('chromedriver.exe') # 이걸 for문 밖으로 빼는게 나을 것 같지만, 로딩속도 문제때문에 제대로 못 긁더라
        driver.implicitly_wait(20)
        driver.get(url)
        page = driver.execute_script('return document.body.innerHTML')
        soup = BeautifulSoup(''.join(page), 'html.parser', from_encoding='utf-8')

        table_title = soup.select('h3.box-title')
        #print(table_title)
        team1 = table_title[1].getText().split(',')[0]
        team2 = table_title[2].getText().split(',')[0]
        #print(team1)
        #print(team2)

        table = soup.select('table.table-striped > tbody')
        
        lineup1 = table[0].select('td > a')
        lineup2 = table[1].select('td > a')

        save_lineup(team1, lineup1)
        save_lineup(team2, lineup2)

def save_lineup(team, lineup):
    print(lineup)

    temp_arr = []
    temp_arr.append(team)
    for col in lineup:
        print(col)
        birthday = str(col).split("birth=")[1][:10]
        player_name = col.getText()
        print(birthday)
        print(player_name)
        hash_string = player_name + birthday
        pid = abs(hash(hash_string)) % (10 ** 8)
        temp_arr.append(pid)

    Lineup(team_id=teams[temp_arr[0]], hitter1=temp_arr[1], hitter2=temp_arr[2],
            hitter3=temp_arr[3], hitter4=temp_arr[4],
            hitter5=temp_arr[5], hitter6=temp_arr[6],
            hitter7=temp_arr[7], hitter8=temp_arr[8],
            hitter9=temp_arr[9], pitcher=temp_arr[10]).save()
    # print(temp_arr)
    # df_lineup = pd.Series(temp_arr, columns=lineup_cols)
    # print(df_lineup)
    # df_lineup.to_sql('lineup', con=engine, if_exists='append')

    # 플레이어 -> pandas dataframe
    # 특정 팀의 최신 경기에서 라인업 크롤링
    
    # 라인업 DB 저장

    # 라인업을 구성하는 선수들 (10명) 을 Player 테이블 (dataframe) 에서 찾는다

    # 그 선수 10명에 대한 record 데이터를 DB에서 가져온다 -> where절로 각 플레이어에 대한 record 찾아서 가져오기

    # 표준화 -> 자바??

    # 팀 종합스탯 계산 

    # 팀 스탯 중에 약한 부분을 찾아내서 그걸 채워줄 수 있는 선수를 추천해줘 <<<<

    
    # 방안 1. 표준화를 파이썬쪽에서도 별개로 만든다
    # 방안 2. 표준화해서 팀 스탯 계산하는 것까지 자바에서 하고 그걸 파이썬으로 보내줘ㅓㅓㅓㅓㅓㅓ