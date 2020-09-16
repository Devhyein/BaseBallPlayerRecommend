from django.db import models

# Create your models here.

# 1주차 명세서에 했던 Store 모델 정의
class Store(models.Model):
    id = models.IntegerField(primary_key=True)
    store_name = models.CharField(max_length=50)
    branch = models.CharField(max_length=20, null=True)
    area = models.CharField(max_length=50, null=True)
    tel = models.CharField(max_length=20, null=True)
    address = models.CharField(max_length=200, null=True)
    latitude = models.FloatField(max_length=10, null=True)
    longitude = models.FloatField(max_length=10, null=True)
    category = models.CharField(max_length=200, null=True)

    # 이건 category_list 에 대해 정의하는데
    # category_list 는 category 문자열을 '|' 기준으로
    # 나눈 문자열 리스트를 반환한다
    # 이때 카테고리가 없는경우 빈 리스트를 반환한다
    @property
    def category_list(self):
        return self.category.split("|") if self.category else []

# python manage.py inspectdb > models.py 로 뽑아낸다

class Player(models.Model):
    player_id = models.AutoField(primary_key=True)
    team = models.ForeignKey('Team', models.DO_NOTHING)
    player_name = models.CharField(max_length=50, blank=True, null=True)
    player_num = models.IntegerField(blank=True, null=True)
    player_birth = models.CharField(max_length=20, blank=True, null=True)
    player_position = models.ForeignKey('Position', models.DO_NOTHING, db_column='player_position')

    class Meta:
        managed = False
        db_table = 'player'


class Position(models.Model):
    player_position = models.IntegerField(primary_key=True)
    position_name = models.CharField(max_length=50, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'position'


class RecordFielder(models.Model):
    fielder_id = models.AutoField(primary_key=True)
    player = models.ForeignKey(Player, models.DO_NOTHING)
    fielder_year = models.IntegerField(blank=True, null=True)
    fielder_g = models.IntegerField(blank=True, null=True)
    fielder_gs = models.IntegerField(blank=True, null=True)
    fielder_inn = models.FloatField(blank=True, null=True)
    fielder_ch = models.IntegerField(blank=True, null=True)
    fielder_po = models.IntegerField(blank=True, null=True)
    fielder_a = models.IntegerField(blank=True, null=True)
    fielder_e = models.IntegerField(blank=True, null=True)
    fielder_fld = models.FloatField(blank=True, null=True)
    fielder_rf9 = models.FloatField(blank=True, null=True)
    fielder_rng = models.FloatField(blank=True, null=True)
    fielder_arm = models.FloatField(blank=True, null=True)
    fielder_cs = models.FloatField(blank=True, null=True)
    fielder_blk = models.FloatField(blank=True, null=True)
    fielder_e_plus = models.FloatField(blank=True, null=True)
    fielder_raa = models.FloatField(blank=True, null=True)
    fielder_133 = models.FloatField(blank=True, null=True)
    fielder_posadj = models.FloatField(blank=True, null=True)
    fielder_raawithadj = models.FloatField(blank=True, null=True)
    fielder_waawoadj = models.FloatField(blank=True, null=True)
    fielder_waawithadj = models.FloatField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'record_fielder'


class RecordHitter(models.Model):
    hitter_id = models.AutoField(primary_key=True)
    player = models.ForeignKey(Player, models.DO_NOTHING)
    hitter_year = models.IntegerField(blank=True, null=True)
    hitter_gamecnt = models.IntegerField(blank=True, null=True)
    hitter_pa = models.IntegerField(blank=True, null=True)
    hitter_ab = models.IntegerField(blank=True, null=True)
    hitter_score = models.IntegerField(blank=True, null=True)
    hitter_hit = models.IntegerField(blank=True, null=True)
    hitter_double = models.IntegerField(blank=True, null=True)
    hitter_triple = models.IntegerField(blank=True, null=True)
    hitter_homerun = models.IntegerField(blank=True, null=True)
    hitter_tb = models.IntegerField(blank=True, null=True)
    hitter_rbi = models.IntegerField(blank=True, null=True)
    hitter_sb = models.IntegerField(blank=True, null=True)
    hitter_cs = models.IntegerField(blank=True, null=True)
    hitter_bb = models.IntegerField(blank=True, null=True)
    hitter_hbp = models.IntegerField(blank=True, null=True)
    hitter_ibb = models.IntegerField(blank=True, null=True)
    hitter_so = models.IntegerField(blank=True, null=True)
    hitter_gdp = models.IntegerField(blank=True, null=True)
    hitter_sh = models.IntegerField(blank=True, null=True)
    hitter_sf = models.IntegerField(blank=True, null=True)
    hitter_ba = models.FloatField(blank=True, null=True)
    hitter_obp = models.FloatField(blank=True, null=True)
    hitter_slg = models.FloatField(blank=True, null=True)
    hitter_ops = models.FloatField(blank=True, null=True)
    hitter_woba = models.FloatField(blank=True, null=True)
    hitter_wrc = models.FloatField(blank=True, null=True)
    hitter_war = models.FloatField(blank=True, null=True)
    hitter_wpa = models.FloatField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'record_hitter'



class RecordPitcher(models.Model):
    pitcher_id = models.AutoField(primary_key=True)
    player = models.ForeignKey(Player, models.DO_NOTHING)
    pitcher_year = models.IntegerField(blank=True, null=True)
    pitcher_g = models.IntegerField(blank=True, null=True)
    pitcher_cg = models.IntegerField(blank=True, null=True)
    pitcher_sho = models.IntegerField(blank=True, null=True)
    pitcher_gs = models.IntegerField(blank=True, null=True)
    pitcher_w = models.IntegerField(blank=True, null=True)
    pitcher_l = models.IntegerField(blank=True, null=True)
    pitcher_sv = models.IntegerField(blank=True, null=True)
    pitcher_hld = models.IntegerField(blank=True, null=True)
    pitcher_ip = models.FloatField(blank=True, null=True)
    pitcher_r = models.IntegerField(blank=True, null=True)
    pitcher_er = models.IntegerField(blank=True, null=True)
    pitcher_h = models.IntegerField(blank=True, null=True)
    pitcher_2b = models.IntegerField(blank=True, null=True)
    pitcher_3b = models.IntegerField(blank=True, null=True)
    pitcher_homerun = models.IntegerField(blank=True, null=True)
    pitcher_bb = models.IntegerField(blank=True, null=True)
    pitcher_ibb = models.IntegerField(blank=True, null=True)
    pitcher_hbp = models.IntegerField(blank=True, null=True)
    pitcher_so = models.IntegerField(blank=True, null=True)
    pitcher_bk = models.IntegerField(blank=True, null=True)
    pitcher_wp = models.IntegerField(blank=True, null=True)
    pitcher_era = models.FloatField(blank=True, null=True)
    pitcher_fip = models.FloatField(blank=True, null=True)
    pitcher_whip = models.FloatField(blank=True, null=True)
    pitcher_era_plus = models.FloatField(blank=True, null=True)
    pitcher_fip_plus = models.FloatField(blank=True, null=True)
    pitcher_war = models.FloatField(blank=True, null=True)
    pitcher_wpa = models.FloatField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'record_pitcher'


class Stadium(models.Model):
    stadium_id = models.IntegerField(primary_key=True)
    stadium_name = models.CharField(max_length=100, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'stadium'


class Team(models.Model):
    team_id = models.AutoField(primary_key=True)
    stadium = models.ForeignKey(Stadium, models.DO_NOTHING)
    team_name = models.CharField(max_length=50, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'team'


class Lineup(models.Model):
    lineup_id = models.AutoField(primary_key=True)
    team = models.ForeignKey('Team', models.DO_NOTHING)
    hitter1 = models.IntegerField(blank=True, null=True)
    hitter2 = models.IntegerField(blank=True, null=True)
    hitter3 = models.IntegerField(blank=True, null=True)
    hitter4 = models.IntegerField(blank=True, null=True)
    hitter5 = models.IntegerField(blank=True, null=True)
    hitter6 = models.IntegerField(blank=True, null=True)
    hitter7 = models.IntegerField(blank=True, null=True)
    hitter8 = models.IntegerField(blank=True, null=True)
    hitter9 = models.IntegerField(blank=True, null=True)
    pitcher = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'lineup'