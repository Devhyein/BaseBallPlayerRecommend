from api import serializers
from api.views import models
from rest_framework import viewsets
from rest_framework.pagination import PageNumberPagination
from bs4 import BeautifulSoup
from urllib.request import urlopen
from selenium import webdriver
import pandas as pd
import numpy as np
import re
from .models import *
from django.conf import settings
from django.db import connections
from django.http import JsonResponse # http 응답을 위함
from sqlalchemy import create_engine # to_sql 사용하기 위해 필요했던 라이브러리
from sklearn import preprocessing
# swagger
from rest_framework.decorators import api_view, parser_classes
from drf_yasg import openapi
from drf_yasg.utils import swagger_auto_schema

pitcher_stat = ['era', 'health', 'control', 'stability', 'deterrent']
hitter_stat = ['power', 'speed', 'contact']
fielder_stat = ['defense', 'shoulder']

# openapi.IN_QUERY: query string으로 값들을 받는다는 뜻. path나 body로 하면 그에 걸맞는 정의가 있어야 한다
param1 = openapi.Parameter('team_id', openapi.IN_QUERY, type=openapi.TYPE_INTEGER, required=True)
param2 = openapi.Parameter('deterrent', openapi.IN_QUERY, type=openapi.TYPE_NUMBER, required=True)
param3 = openapi.Parameter('defense', openapi.IN_QUERY, type=openapi.TYPE_NUMBER, required=True)
param4 = openapi.Parameter('era', openapi.IN_QUERY, type=openapi.TYPE_NUMBER, required=True)
param5 = openapi.Parameter('contact', openapi.IN_QUERY, type=openapi.TYPE_NUMBER, required=True)
param6 = openapi.Parameter('health', openapi.IN_QUERY, type=openapi.TYPE_NUMBER, required=True)
param7 = openapi.Parameter('control', openapi.IN_QUERY, type=openapi.TYPE_NUMBER, required=True)
param8 = openapi.Parameter('power', openapi.IN_QUERY, type=openapi.TYPE_NUMBER, required=True)
param9 = openapi.Parameter('shoulder', openapi.IN_QUERY, type=openapi.TYPE_NUMBER, required=True)
param10 = openapi.Parameter('speed', openapi.IN_QUERY, type=openapi.TYPE_NUMBER, required=True)
param11 = openapi.Parameter('stability', openapi.IN_QUERY, type=openapi.TYPE_NUMBER, required=True)

@swagger_auto_schema(
    method='get',
    manual_parameters = [param1, param2, param3, param4, param5, param6, param7,
                         param8, param9, param10, param11]
)

@api_view(["GET"])
def recommend_player_method1(request):
    """
    각 스탯에 대한 가중치를 받아 약한 부분을 채워줄 수 있는 선수를 추천해준다
    - parameters: team_id (int), 0~1 사이 값으로 표준화된 팀 스탯 10개 (float)
    - returns: recommended 리스트 (추천 선수 10명의 player_id)
    """

    weights = {}
    # django에서 GET request를 받는 방법
    teamid = request.GET.get('team_id',  '')
    weights['deterrent'] = request.GET.get('deterrent', '')
    weights['defense'] = request.GET.get('defense', '')
    weights['era'] = request.GET.get('era', '')
    weights['contact'] = request.GET.get('contact', '')
    weights['health'] = request.GET.get('health', '')
    weights['control'] = request.GET.get('control', '')
    weights['power'] = request.GET.get('power', '')
    weights['shoulder'] = request.GET.get('shoulder', '')
    weights['speed'] = request.GET.get('speed', '')
    weights['stability'] = request.GET.get('stability', '')

    for key in weights.keys():
        weights[key] = float(weights[key])

    # 팀 스탯에 따른 가중치 계산 - 현재 옵션: 스탯이 낮을수록 반대로 가중치는 높게 주는 방식
    # ex) power=0.4라면 2*(1-0.4) = 1.2니까 w=2.2
    # 자바 백엔드 쪽으로 옮겨짐
    print(weights)

    # player 테이블 pandas로 가져오기
    queryset = Player.objects.all()
    query, params = queryset.query.sql_with_params()
    df_player = pd.read_sql_query(query, connections['default'], params = params)

    # player 테이블 정리: 필요없는 컬럼 지우고, 우리팀 선수 지움
    #print(df_player)
    df_player = df_player.drop(columns=['player_num', 'player_birth'])

    # request에서 가져오는 teamid는 문자열이라 그냥 바로 넣으면 인식 못하는 듯... int로 변환
    df_player = df_player[df_player['team_id'] != int(teamid)] 
    #print(df_player)

    # 은퇴선수 거르기
    df_player = df_player[df_player['player_retire'] == 0]

    # 투수, 타자를 각각 추천해주는 걸로 변경
    recommended_pitcher = []
    recommended_hitter = []

    recommended_pitcher = pitcher_recommend(df_player, weights)
    recommended_hitter = hitter_recommend(df_player, weights)

    return_object = {
        'recommended_pitcher': recommended_pitcher,
        'recommended_hitter': recommended_hitter
    }
    return JsonResponse(return_object)

def pitcher_recommend(players, weights):
    # DB -> pandas dataframe으로 데이터 불러오기
    queryset = RecordPitcher.objects.all()
    query, params = queryset.query.sql_with_params()
    df_pitcher = pd.read_sql_query(query, connections['default'], params = params)

    # 규정이닝 거르기
    df_pitcher = df_pitcher.loc[((df_pitcher.pitcher_year==20) & (df_pitcher.pitcher_ip>=100)) | ((df_pitcher.pitcher_year!=20) & (df_pitcher.pitcher_ip>=144))]

    # 필요한 스탯: ERA+, 이닝, 경기수, K, BB (+HBP), 폭투, 보크, 피홈런
    df_pitcher = df_pitcher[['player_id','pitcher_year','pitcher_era_plus','pitcher_ip','pitcher_g', 'pitcher_so', 'pitcher_bb',
                        'pitcher_hbp','pitcher_bk', 'pitcher_wp', 'pitcher_homerun']]
    # 325개의 기록
    #print(df_pitcher)
    df_pitcher = pd.merge(df_pitcher, players, on='player_id', how='inner') # left join하면 우리팀 선수가 껴버리고, right join하면 쓸데없는 컬럼이 잔뜩 생김.
    #print(df_pitcher)

    pitchers_sort = []

    # 5툴 계산 
    pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_era_plus", "pitcher_year"].apply(cal_era).to_frame('ERA+')
    pitchers_grouped['health'] = df_pitcher.groupby(["player_id"])["pitcher_g", "pitcher_ip", "pitcher_year"].apply(cal_health).to_frame('health')
    pitchers_grouped['control'] = df_pitcher.groupby(["player_id"])["pitcher_so", "pitcher_bb", "pitcher_hbp", "pitcher_year"].apply(cal_control).to_frame('control')
    pitchers_grouped['stability'] = df_pitcher.groupby(["player_id"])["pitcher_wp", "pitcher_bk", "pitcher_year"].apply(cal_stability).to_frame('stability')
    pitchers_grouped['deterrent'] = df_pitcher.groupby(["player_id"])["pitcher_homerun", "pitcher_year"].apply(cal_deterrent).to_frame('deterrent')
    #print(pitchers_grouped)

    # 각 툴을 표준화한다
    x = pitchers_grouped.values #returns a numpy array
    min_max_scaler = preprocessing.MinMaxScaler()
    x_scaled = min_max_scaler.fit_transform(x)
    df_normalized = pd.DataFrame(x_scaled)
    df_normalized.index = pitchers_grouped.index
    df_normalized.columns = ['ERA+', 'health', 'control', 'stability', 'deterrent']
    #print(df_normalized)

    # 표준화한 값에다가 weights의 값을 각각 곱함
    df_normalized['total_score'] = df_normalized['ERA+'] * weights['era'] + df_normalized['health'] * weights['health'] + df_normalized['control'] * weights['control'] + df_normalized['stability'] * weights['stability'] + df_normalized['deterrent'] * weights['deterrent']
    #print(df_normalized)
    # 모든 툴을 더한 총스탯을 구하여 그 순으로 추천
    pitchers_sort = df_normalized.sort_values(by=['total_score'], ascending=False)

    # if stat == 'era':
    #     pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_era_plus", "pitcher_year"].apply(cal_era).to_frame('ERA+')
    #     pitchers_sort = pitchers_grouped.sort_values(by=['ERA+'], ascending=False)
    # elif stat == 'health':
    #     pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_g", "pitcher_ip", "pitcher_year"].apply(cal_health).to_frame('health')
    #     pitchers_sort = pitchers_grouped.sort_values(by=['health'], ascending=False)
    # elif stat == 'control':
    #     pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_so", "pitcher_bb", "pitcher_hbp", "pitcher_year"].apply(cal_control).to_frame('control')
    #     pitchers_sort = pitchers_grouped.sort_values(by=['control'], ascending=False)
    # elif stat == 'stability':
    #     pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_wp", "pitcher_bk", "pitcher_year"].apply(cal_stability).to_frame('stability')
    #     pitchers_sort = pitchers_grouped.sort_values(by=['stability'], ascending=True)
    # elif stat == 'deterrent':
    #     pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_homerun", "pitcher_year"].apply(cal_deterrent).to_frame('deterrent')
    #     pitchers_sort = pitchers_grouped.sort_values(by=['deterrent'], ascending=True)

    # 173명
    #print(pitchers_sort[:5])
    #print(type(pitchers_sort))
    return_arr = []
    for player in pitchers_sort[:5].iterrows():
        return_arr.append(player[0])

    return return_arr

# 원래 hitter, fielder 따로 봤었는데 이제 타자를 수비스탯까지 고려해서 추천해주는 걸로
def hitter_recommend(players, weights):
    queryset = RecordHitter.objects.all()
    query, params = queryset.query.sql_with_params()
    df_hitter = pd.read_sql_query(query, connections['default'], params = params)

    queryset = RecordFielder.objects.all()
    query, params = queryset.query.sql_with_params()
    df_fielder = pd.read_sql_query(query, connections['default'], params = params)

    #print(players)
    #print(df_hitter) # 923명

    # 규정타석 거르기
    #df_hitter = df_hitter[df_hitter.apply(lambda x: minimum_pa(df_hitter['hitter_year'], df_hitter['hitter_pa']))] 
    df_hitter = df_hitter.loc[((df_hitter.hitter_year==20) & (df_hitter.hitter_pa>=100*3.1)) | ((df_hitter.hitter_year!=20) & (df_hitter.hitter_pa>=144*3.1))]

    # 필요한 스탯: 장타율, 홈런, 도루, 도실, 3루타, 타율, BB, K. 그 외의 컬럼들은 싹 날렸다
    df_hitter = df_hitter[['player_id','hitter_year','hitter_pa','hitter_slg','hitter_homerun', 'hitter_sb', 'hitter_cs'
                        ,'hitter_triple', 'hitter_ba', 'hitter_bb', 'hitter_so']]

    df_fielder = df_fielder.loc[((df_fielder.fielder_year==20) & (df_fielder.fielder_inn>=100*5)) | ((df_fielder.fielder_year!=20) & (df_fielder.fielder_inn>=144*5))]

    # 필요한 스탯: 수비율, RNG, 보살/ARM/CS
    df_fielder = df_fielder[['player_id','fielder_year','fielder_fld','fielder_rng','fielder_a', 'fielder_arm', 'fielder_cs']]


    df_hitter = pd.merge(df_hitter, players, on='player_id', how='inner')   # player 테이블과 조인
    df_hitter = pd.merge(df_hitter, df_fielder, on='player_id', how='inner')    # fielder 테이블과 조인

    #print(df_hitter)
    # hitter_grouped_ = df_hitter.groupby(["player_id"])
    # print(hitter_grouped_.size())

    hitters_sort = []

    hitters_grouped = df_hitter.groupby(["player_id"])["hitter_slg", "hitter_year"].apply(cal_power).to_frame('power')
    hitters_grouped['contact'] = df_hitter.groupby(["player_id"])["hitter_bb", "hitter_so", "hitter_year"].apply(cal_contact).to_frame('contact')
    hitters_grouped['speed'] = df_hitter.groupby(["player_id"])["hitter_sb", "hitter_cs", "hitter_year"].apply(cal_speed).to_frame('speed')
    hitters_grouped['defense'] = df_hitter.groupby(["player_id"])["fielder_fld", "fielder_rng", "fielder_year"].apply(cal_defense).to_frame('defense')
    hitters_grouped['shoulder'] = df_hitter.groupby(["player_id"])["fielder_a", "fielder_arm", "fielder_cs", "player_position", "fielder_year"].apply(cal_shoulder).to_frame('shoulder')

    # 각 툴을 표준화한다
    x = hitters_grouped.values #returns a numpy array
    min_max_scaler = preprocessing.MinMaxScaler()
    x_scaled = min_max_scaler.fit_transform(x)
    df_normalized = pd.DataFrame(x_scaled)
    df_normalized.index = hitters_grouped.index
    df_normalized.columns = ['power', 'contact', 'speed', 'defense', 'shoulder']
    #print(df_normalized)

    # 표준화한 값에다가 weights의 값을 각각 곱함
    df_normalized['total_score'] = df_normalized['power'] * weights['power'] + df_normalized['contact'] * weights['contact'] + df_normalized['speed'] * weights['speed'] + df_normalized['defense'] * weights['defense'] + df_normalized['shoulder'] * weights['shoulder']
    #print(df_normalized)
    # 모든 툴을 더한 총스탯을 구하여 그 순으로 추천
    hitters_sort = df_normalized.sort_values(by=['total_score'], ascending=False)

    # 173명
    #print(hitters_sort[:5])
    #print(type(hitters_sort))
    return_arr = []

    # pandas 데이터프레임을 행 순회할땐 그냥 for~in 뒤에 df를 붙여주기만 하는게 아니라 꼭 iterrows() 를 써야 하는 듯하다
    for player in hitters_sort[:5].iterrows():
        return_arr.append(player[0])

    return return_arr

# def fielder_recommend(players, stat):
#     queryset = RecordFielder.objects.all()
#     query, params = queryset.query.sql_with_params()
#     df_fielder = pd.read_sql_query(query, connections['default'], params = params)

#     # 규정 수비이닝 (골글 기준): 경기수*5
#     df_fielder = df_fielder.loc[((df_fielder.fielder_year==20) & (df_fielder.fielder_inn>=100*5)) | ((df_fielder.fielder_year!=20) & (df_fielder.fielder_inn>=144*5))]

#     # 필요한 스탯: 수비율, RNG, 보살/ARM/CS
#     df_fielder = df_fielder[['player_id','fielder_year','fielder_fld','fielder_rng','fielder_a', 'fielder_arm', 'fielder_cs']]
#     print(df_fielder)

#     df_fielder = pd.merge(df_fielder, players, on='player_id', how='inner')
#     print(df_fielder)

#     fielders_sort = []

#     if stat == 'defense':
#         fielders_grouped = df_fielder.groupby(["player_id"])["fielder_fld", "fielder_rng", "fielder_year"].apply(cal_defense).to_frame('defense')
#         fielders_sort = fielders_grouped.sort_values(by=['defense'], ascending=False)
#     elif stat == 'shoulder':
#         fielders_grouped = df_fielder.groupby(["player_id"])["fielder_a", "fielder_arm", "fielder_cs", "player_position", "fielder_year"].apply(cal_shoulder).to_frame('shoulder')
#         fielders_sort = fielders_grouped.sort_values(by=['shoulder'], ascending=False)

#     # 173명
#     print(fielders_sort[:10])
#     print(type(fielders_sort))
#     return_arr = []

#     # pandas 데이터프레임을 행 순회할땐 그냥 for~in 뒤에 df를 붙여주기만 하는게 아니라 꼭 iterrows() 를 써야 하는 듯하다
#     for player in fielders_sort[:10].iterrows():
#         return_arr.append(player[0])

#     return return_arr


def cal_power(arr):
    sum = 0
    #print(arr)
    for data in arr.iterrows():
        weight = 1.0 + ((data[1].hitter_year - 15) * 0.1)
        sum += (data[1]['hitter_slg'] * weight)
    return sum / len(arr)

def cal_contact(arr):
    sum = 0
    for data in arr.iterrows():
        weight = 1.0 + ((data[1].hitter_year - 15) * 0.1)
        sum += ((data[1]['hitter_bb'] / data[1]['hitter_so']) * weight)
    return sum / len(arr)

def cal_speed(arr):
    sum = 0
    for data in arr.iterrows():
        weight = 1.0 + ((data[1].hitter_year - 15) * 0.1)
        sum += ((data[1]['hitter_sb'] / (data[1]['hitter_sb'] + data[1]['hitter_cs'])) * weight)
    return sum / len(arr)

def cal_era(arr):
    sum = 0
    for data in arr.iterrows():
        weight = 1.0 + ((data[1].pitcher_year - 15) * 0.1)
        sum += (data[1]['pitcher_era_plus'] * weight)
    return sum / len(arr)

def cal_health(arr):
    sum = 0
    for data in arr.iterrows():
        weight = 1.0 + ((data[1].pitcher_year - 15) * 0.1)
        sum += ((data[1]['pitcher_ip'] / data[1]['pitcher_g']) * weight)
    return sum / len(arr)

def cal_control(arr):
    sum = 0
    for data in arr.iterrows():
        weight = 1.0 + ((data[1].pitcher_year - 15) * 0.1)
        sum += ((data[1]['pitcher_so'] / (data[1]['pitcher_bb'] + data[1]['pitcher_hbp'])) * weight)
    return sum / len(arr)

def cal_stability(arr):
    sum = 0
    for data in arr.iterrows():
        weight = 1.0 + ((data[1].pitcher_year - 15) * 0.1)
        sum += ((data[1]['pitcher_bk'] + data[1]['pitcher_wp']) * weight)
    return sum / len(arr)

def cal_deterrent(arr):
    sum = 0
    for data in arr.iterrows():
        weight = 1.0 + ((data[1].pitcher_year - 15) * 0.1)
        sum += (data[1]['pitcher_homerun'] * weight)
    return sum / len(arr)

def cal_defense(arr):
    sum = 0
    for data in arr.iterrows():
        weight = 1.0 + ((data[1].fielder_year - 15) * 0.1)
        sum += ((data[1]['fielder_fld'] * data[1]['fielder_rng']) * weight)
    return sum / len(arr)

def cal_shoulder(arr):
    sum = 0
    for data in arr.iterrows():
        stat = ""

        if data[1]['player_position'] >= 7 and data[1]['player_position'] <= 9: # 외야수일 경우 ARM을 본다
            stat = "fielder_arm"
        elif data[1]['player_position'] == 2: # 포수일 경우 CS를 본다
            stat = "fielder_cs"
        else: # 그 외 포지션의 경우 A (보살) 을 본다
            stat = "fielder_a"

        weight = 1.0 + ((data[1].fielder_year - 15) * 0.1)
        sum += (data[1][stat] * weight)
    return sum / len(arr)