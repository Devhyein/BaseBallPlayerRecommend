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

def get_lineup(request):

    urls = ["http://www.statiz.co.kr/boxscore.php?opt=5&date=2020-05-05&stadium=%EB%9D%BC%EC%9D%B4%EC%98%A8%EC%A6%88%ED%8C%8C%ED%81%AC&hour=14",
            "http://www.statiz.co.kr/boxscore.php?opt=5&date=2020-05-05&stadium=%EC%9D%B8%EC%B2%9C%EB%AC%B8%ED%95%99&hour=14",
            "http://www.statiz.co.kr/boxscore.php?opt=5&date=2020-05-05&stadium=%EC%9E%A0%EC%8B%A4&hour=14",
            "http://www.statiz.co.kr/boxscore.php?opt=5&date=2020-05-05&stadium=%EC%B1%94%ED%94%BC%EC%96%B8%EC%8A%A4%ED%95%84%EB%93%9C&hour=14",
            "http://www.statiz.co.kr/boxscore.php?opt=5&date=2020-05-05&stadium=%EC%BC%80%EC%9D%B4%ED%8B%B0%EC%9C%84%EC%A6%88%ED%8C%8C%ED%81%AC&hour=14"]

    driver = webdriver.Chrome('chromedriver.exe')
    driver.implicitly_wait(20) # 로딩될때까지 대기를 줬는데도?

    for url in urls:
        driver.get(url)
        page = driver.execute_script('return document.body.innerHTML')
        soup = BeautifulSoup(''.join(page), 'html.parser', from_encoding='utf-8')
        table = soup.select('table.table-striped > tbody')
        print(table)

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