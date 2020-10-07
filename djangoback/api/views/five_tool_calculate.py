import pandas as pd
import numpy as np
from sklearn import preprocessing

def five_tool_hitter(players, df_hitter, df_fielder):

    # 필요한 스탯: 장타율, 홈런, 도루, 도실, 3루타, 타율, BB, K. 그 외의 컬럼들은 싹 날렸다
    df_hitter = df_hitter[['player_id','hitter_year','hitter_pa','hitter_slg','hitter_homerun', 'hitter_sb', 'hitter_cs'
                        ,'hitter_triple', 'hitter_ba', 'hitter_bb', 'hitter_so']]
    # 필요한 스탯: 수비율, RNG, 보살/ARM/CS
    df_fielder = df_fielder[['player_id','fielder_year','fielder_fld','fielder_rng','fielder_a', 'fielder_arm', 'fielder_cs']]
    df_hitter = pd.merge(df_hitter, players, on='player_id', how='inner')   # player 테이블과 조인
    df_hitter = pd.merge(df_hitter, df_fielder, on='player_id', how='inner')    # fielder 테이블과 조인

    hitters_grouped = df_hitter.groupby(["player_id"])["hitter_slg", "hitter_year"].apply(cal_power).to_frame('power')
    hitters_grouped['contact'] = df_hitter.groupby(["player_id"])["hitter_bb", "hitter_so", "hitter_year"].apply(cal_contact).to_frame('contact')
    hitters_grouped['speed'] = df_hitter.groupby(["player_id"])["hitter_sb", "hitter_cs", "hitter_year"].apply(cal_speed).to_frame('speed')
    hitters_grouped['defense'] = df_hitter.groupby(["player_id"])["fielder_fld", "fielder_rng", "fielder_year"].apply(cal_defense).to_frame('defense')
    hitters_grouped['shoulder'] = df_hitter.groupby(["player_id"])["fielder_a", "fielder_arm", "fielder_cs", "player_position", "fielder_year"].apply(cal_shoulder).to_frame('shoulder')

    hitters_grouped = hitters_grouped.replace([np.inf, -np.inf, np.nan], 0)

    # 각 툴을 표준화한다
    x = hitters_grouped.values #returns a numpy array
    min_max_scaler = preprocessing.MinMaxScaler()
    x_scaled = min_max_scaler.fit_transform(x)
    df_normalized = pd.DataFrame(x_scaled)
    df_normalized.index = hitters_grouped.index
    df_normalized.columns = ['power', 'contact', 'speed', 'defense', 'shoulder']
    
    return df_normalized


def five_tool_pitcher(players, df_pitcher):
    
    # 필요한 스탯: ERA+, 이닝, 경기수, K, BB (+HBP), 폭투, 보크, 피홈런
    df_pitcher = df_pitcher[['player_id','pitcher_year','pitcher_era_plus','pitcher_ip','pitcher_g', 'pitcher_so', 'pitcher_bb',
                        'pitcher_hbp','pitcher_bk', 'pitcher_wp', 'pitcher_homerun']]
    df_pitcher = pd.merge(df_pitcher, players, on='player_id', how='inner') # left join하면 우리팀 선수가 껴버리고, right join하면 쓸데없는 컬럼이 잔뜩 생김.

    pitchers_grouped = df_pitcher.groupby(["player_id"])["pitcher_era_plus", "pitcher_year"].apply(cal_era).to_frame('ERA+')
    pitchers_grouped['health'] = df_pitcher.groupby(["player_id"])["pitcher_g", "pitcher_ip", "pitcher_year"].apply(cal_health).to_frame('health')
    pitchers_grouped['control'] = df_pitcher.groupby(["player_id"])["pitcher_so", "pitcher_bb", "pitcher_hbp", "pitcher_year"].apply(cal_control).to_frame('control')
    pitchers_grouped['stability'] = df_pitcher.groupby(["player_id"])["pitcher_wp", "pitcher_bk", "pitcher_year"].apply(cal_stability).to_frame('stability')
    pitchers_grouped['deterrent'] = df_pitcher.groupby(["player_id"])["pitcher_homerun", "pitcher_year"].apply(cal_deterrent).to_frame('deterrent')
    #print(pitchers_grouped)

    pitchers_grouped = pitchers_grouped.replace([np.inf, -np.inf, np.nan], 0)

    # 각 툴을 표준화한다
    x = pitchers_grouped.values #returns a numpy array
    min_max_scaler = preprocessing.MinMaxScaler()
    x_scaled = min_max_scaler.fit_transform(x)
    df_normalized = pd.DataFrame(x_scaled)
    df_normalized.index = pitchers_grouped.index
    df_normalized.columns = ['ERA+', 'health', 'control', 'stability', 'deterrent']
    
    return df_normalized


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