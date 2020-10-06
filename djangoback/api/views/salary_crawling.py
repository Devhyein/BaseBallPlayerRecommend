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
from django.db import connections

@api_view(["GET"])
def getSalaryData(request):

    queryset = Player.objects.all()
    query, params = queryset.query.sql_with_params()
    df_player = pd.read_sql_query(query, connections['default'], params = params)

    for i in range(0, df_player.size):
        if df_player.iloc[i]['player_retire'] == 1: 
            continue

        pid = df_player.iloc[i]['player_id']
        pname = df_player.iloc[i]['player_name'] # 특정 셀의 데이터 가져오기: iloc
        pbirth = df_player.iloc[i]['player_birth']

        url = "http://www.statiz.co.kr/player.php?opt=11&name=" + pname + "&birth=" + pbirth
        driver = webdriver.Chrome('chromedriver.exe') # 이걸 for문 밖으로 빼는게 나을 것 같지만, 로딩속도 문제때문에 제대로 못 긁더라
        driver.implicitly_wait(20)
        driver.get(url)
        page = driver.execute_script('return document.body.innerHTML')
        soup = BeautifulSoup(''.join(page), 'html.parser', from_encoding='utf-8')

        table = soup.select('table.table-striped > tbody > tr')

        tds = [0, 0, 0]
        for row in table:
            texts = row.select('td')
            count = 0
            for t in texts:
                tds[count] = t.getText()
                count += 1
        
        print(tds)
        salary_num = "0"
        if tds[1] == 0 or len(tds[1]) < 2 or tds[1] == "900,000":
            pass
        else:
            salary_num = tds[1].replace(",", "")
        
        Salary(player_id=pid, salary=int(salary_num)).save()
        