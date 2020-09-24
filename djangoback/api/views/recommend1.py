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
from django.db import connections
from django.http import JsonResponse # http 응답을 위함
from sqlalchemy import create_engine # to_sql 사용하기 위해 필요했던 라이브러리
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
    manual_parameters = [param1, param2, param3, param4,  param5, param6, param7,
                         param8, param9, param10, param11]
)

@api_view(["GET"])
def recommend_player_method1(request):
    """
    팀 스탯을 받아 가장 약한 부분을 찾아내어, 해당 부분을 채워줄 수 있는 선수를 추천해준다
    - parameters: team_id (int), 0~1 사이 값으로 표준화된 팀 스탯 10개 (float)
    - returns: recommended 리스트 (추천 선수 10명의 player_id)
    """

    team_stats = {}
    # django에서 GET request를 받는 방법
    teamid = request.GET.get('team_id',  '')
    team_stats['deterrent'] = request.GET.get('deterrent', '')
    team_stats['defense'] = request.GET.get('defense', '')
    team_stats['era'] = request.GET.get('era', '')
    team_stats['contact'] = request.GET.get('contact', '')
    team_stats['health'] = request.GET.get('health', '')
    team_stats['control'] = request.GET.get('control', '')
    team_stats['power'] = request.GET.get('power', '')
    team_stats['shoulder'] = request.GET.get('shoulder', '')
    team_stats['speed'] = request.GET.get('speed', '')
    team_stats['stability'] = request.GET.get('stability', '')

    print(teamid)
    print(team_stats)

    # player 테이블 pandas로 가져오기
    queryset = Player.objects.all()
    query, params = queryset.query.sql_with_params()
    df_player = pd.read_sql_query(query, connections['default'], params = params)

    # player 테이블 정리: 필요없는 컬럼 지우고, 우리팀 선수 지움
    print(df_player)
    df_player = df_player.drop(columns=['player_num', 'player_birth'])
    # request에서 가져오는 teamid는 문자열이라 그냥 바로 넣으면 인식 못하는 듯... int로 변환
    df_player = df_player[df_player['team_id'] != int(teamid)] 
    print(df_player)

    # 은퇴선수 거르기
    df_player = df_player[df_player['player_retire'] == 0]

    # 팀 스탯 중 약한게 뭔지 뽑아내자.
    res = sorted(team_stats.items(), key=(lambda x: x[1]), reverse = False)
    weak_stat = res[0]

    # 약한게 투수스탯이면 -> pitcher 테이블 보는 곳으로
    # 약한게 공격스탯이면 (power, speed, contact) -> hitter 테이블로 
    # 약한게 수비스탯이면 (shoulder, defense) -> fielder 테이블로
    recommended = []

    #recommended_player = [75162451, 44235944, 59621752, 52933398, 28698115]

    if weak_stat[0] in pitcher_stat:
        recommended = pitcher_recommend(df_player, weak_stat[0])
    elif weak_stat[0] in hitter_stat:
        recommended = hitter_recommend(df_player, weak_stat[0])
    elif weak_stat[0] in fielder_stat:
        recommended = fielder_recommend(df_player, weak_stat[0])

    return_object = {
        'recommended': recommended
    }
    return JsonResponse(return_object)

def pitcher_recommend(players, stat):
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
    print(df_pitcher)
    df_pitcher = pd.merge(df_pitcher, players, on='player_id', how='inner') # left join하면 우리팀 선수가 껴버리고, right join하면 쓸데없는 컬럼이 잔뜩 생김.
    print(df_pitcher)

    pitchers_sort = []

    if stat == 'era':
        pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_era_plus", "pitcher_year"].apply(cal_era).to_frame('ERA+')
        pitchers_sort = pitchers_grouped.sort_values(by=['ERA+'], ascending=False)
    elif stat == 'health':
        pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_g", "pitcher_ip", "pitcher_year"].apply(cal_health).to_frame('health')
        pitchers_sort = pitchers_grouped.sort_values(by=['health'], ascending=False)
    elif stat == 'control':
        pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_so", "pitcher_bb", "pitcher_hbp", "pitcher_year"].apply(cal_control).to_frame('control')
        pitchers_sort = pitchers_grouped.sort_values(by=['control'], ascending=False)
    elif stat == 'stability':
        pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_wp", "pitcher_bk", "pitcher_year"].apply(cal_stability).to_frame('stability')
        pitchers_sort = pitchers_grouped.sort_values(by=['stability'], ascending=True)
    elif stat == 'deterrent':
        pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_homerun", "pitcher_year"].apply(cal_deterrent).to_frame('deterrent')
        pitchers_sort = pitchers_grouped.sort_values(by=['deterrent'], ascending=True)

    # 173명
    print(pitchers_sort[:10])
    print(type(pitchers_sort))
    return_arr = []
    for player in pitchers_sort[:10].iterrows():
        return_arr.append(player[0])

    return return_arr

# def minimum_pa(year, pa):
#     if (year == 20):
#         return pa >= (100 * 3.1)
#     else:
#         return pa >= (144 * 3.1)

def hitter_recommend(players, stat):
    queryset = RecordHitter.objects.all()
    query, params = queryset.query.sql_with_params()
    df_hitter = pd.read_sql_query(query, connections['default'], params = params)

    print(df_hitter) # 923명

    # 규정타석 거르기
    #df_hitter = df_hitter[df_hitter.apply(lambda x: minimum_pa(df_hitter['hitter_year'], df_hitter['hitter_pa']))] 
    df_hitter = df_hitter.loc[((df_hitter.hitter_year==20) & (df_hitter.hitter_pa>=100*3.1)) | ((df_hitter.hitter_year!=20) & (df_hitter.hitter_pa>=144*3.1))]


    # 필요한 스탯: 장타율, 홈런, 도루, 도실, 3루타, 타율, BB, K. 그 외의 컬럼들은 싹 날렸다
    df_hitter = df_hitter[['player_id','hitter_year','hitter_pa','hitter_slg','hitter_homerun', 'hitter_sb', 'hitter_cs'
                        ,'hitter_triple', 'hitter_ba', 'hitter_bb', 'hitter_so']]
    # 325개의 기록
    print(df_hitter)

    df_hitter = pd.merge(df_hitter, players, on='player_id', how='inner')
    print(df_hitter)
    # hitter_grouped_ = df_hitter.groupby(["player_id"])
    # print(hitter_grouped_.size())

    hitters_sort = []

    if stat == 'power':
        hitters_grouped = df_hitter.groupby(["player_id"])["hitter_slg", "hitter_year"].apply(cal_power).to_frame('power')
        hitters_sort = hitters_grouped.sort_values(by=['power'], ascending=False)
    elif stat == 'contact':
        hitters_grouped = df_hitter.groupby(["player_id"])["hitter_bb", "hitter_so", "hitter_year"].apply(cal_contact).to_frame('contact')
        hitters_sort = hitters_grouped.sort_values(by=['contact'], ascending=False)
    elif stat == 'speed':
        hitters_grouped = df_hitter.groupby(["player_id"])["hitter_sb", "hitter_cs", "hitter_year"].apply(cal_speed).to_frame('speed')
        hitters_sort = hitters_grouped.sort_values(by=['speed'], ascending=False)
    # 173명
    print(hitters_sort[:10])
    print(type(hitters_sort))
    return_arr = []

    # pandas 데이터프레임을 행 순회할땐 그냥 for~in 뒤에 df를 붙여주기만 하는게 아니라 꼭 iterrows() 를 써야 하는 듯하다
    for player in hitters_sort[:10].iterrows():
        return_arr.append(player[0])

    return return_arr

def fielder_recommend(players, stat):
    queryset = RecordFielder.objects.all()
    query, params = queryset.query.sql_with_params()
    df_fielder = pd.read_sql_query(query, connections['default'], params = params)

    # 규정 수비이닝 (골글 기준): 경기수*5
    df_fielder = df_fielder.loc[((df_fielder.fielder_year==20) & (df_fielder.fielder_inn>=100*5)) | ((df_fielder.fielder_year!=20) & (df_fielder.fielder_inn>=144*5))]

    # 필요한 스탯: 수비율, RNG, 보살/ARM/CS
    df_fielder = df_fielder[['player_id','fielder_year','fielder_fld','fielder_rng','fielder_a', 'fielder_arm', 'fielder_cs']]
    print(df_fielder)

    df_fielder = pd.merge(df_fielder, players, on='player_id', how='inner')
    print(df_fielder)

    fielders_sort = []

    if stat == 'defense':
        fielders_grouped = df_fielder.groupby(["player_id"])["fielder_fld", "fielder_rng", "fielder_year"].apply(cal_defense).to_frame('defense')
        fielders_sort = fielders_grouped.sort_values(by=['defense'], ascending=False)
    elif stat == 'shoulder':
        fielders_grouped = df_fielder.groupby(["player_id"])["fielder_a", "fielder_arm", "fielder_cs", "player_position", "fielder_year"].apply(cal_shoulder).to_frame('shoulder')
        fielders_sort = fielders_grouped.sort_values(by=['shoulder'], ascending=False)

    # 173명
    print(fielders_sort[:10])
    print(type(fielders_sort))
    return_arr = []

    # pandas 데이터프레임을 행 순회할땐 그냥 for~in 뒤에 df를 붙여주기만 하는게 아니라 꼭 iterrows() 를 써야 하는 듯하다
    for player in fielders_sort[:10].iterrows():
        return_arr.append(player[0])

    return return_arr


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