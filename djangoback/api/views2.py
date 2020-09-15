from api import models, serializers
from rest_framework import viewsets
from rest_framework.pagination import PageNumberPagination
import pandas as pd
import re
from .models import *
from django.conf import settings
from sqlalchemy import create_engine # to_sql 사용하기 위해 필요했던 라이브러리

def get_lineup_date(request):
    players_from_db = Player.objects.all() # 이건 다 가져오는거지만 where절로 일부만 가져올 수 있게 어떻게? 알아보자
    for p in players_from_db:
        print(p.player_name)

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