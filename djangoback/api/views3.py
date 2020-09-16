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
from django.db import connections
from django.http import JsonResponse # http 응답을 위함
from sqlalchemy import create_engine # to_sql 사용하기 위해 필요했던 라이브러리

def recommend_player_method1(request):
    # DB -> pandas dataframe으로 데이터 불러오기
    queryset = RecordHitter.objects.all()
    query, params = queryset.query.sql_with_params()
    df_hitter = pd.read_sql_query(query, connections['default'], params = params)

    print(df_hitter)

    queryset = RecordPitcher.objects.all()
    query, params = queryset.query.sql_with_params()
    df_pitcher = pd.read_sql_query(query, connections['default'], params = params)

    print(df_pitcher)

    queryset = RecordFielder.objects.all()
    query, params = queryset.query.sql_with_params()
    df_fielder = pd.read_sql_query(query, connections['default'], params = params)

    print(df_fielder)

    recommended_player = [75162451, 44235944, 59621752, 52933398, 28698115]
    return_dummy = {
        'recommended': recommended_player
    }

    return JsonResponse(return_dummy)

