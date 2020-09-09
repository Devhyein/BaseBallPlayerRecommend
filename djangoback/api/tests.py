#from django.test import TestCase
from bs4 import BeautifulSoup
from urllib.request import urlopen
from selenium import webdriver
import pandas as pd
import re

# 그냥 BS4만 썼을 때는 페이지가 완전히 로딩되기 전의 상태를 불러와버려 데이터를 다 긁을 수 없었다
# 결국 셀레니움과 함께해야 했음

driver = webdriver.Chrome('../chromedriver.exe')
# re: 0은 타격, 1은 투수, 2는 수비스탯
# pa, sn: pa번째 선수부터 sn명의 목록을 한 페이지에 띄운다.
# ys, ye: year start ~ end
# 이상하게 한번에 몇명을 긁는지에 따라 다 안 긁힐때가 있다. 왜?
url = 'http://www.statiz.co.kr/stat.php?re=0&lr=0&sn=120&pa=0&ys=2010&ye=2020'
driver.implicitly_wait(20) # 로딩될때까지 대기를 줬는데도?
driver.get(url)
#webpage = urlopen(url)
page = driver.execute_script('return document.body.innerHTML')
soup = BeautifulSoup(''.join(page), 'html.parser', from_encoding='utf-8')
#print(source)
table = soup.select('#mytable tr') # 표

cols = ['id', 'name', 'season', 'team', 'position', 'WAR', 'G', 'PA', 'AB', 'R', 'H', '2B', '3B',
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