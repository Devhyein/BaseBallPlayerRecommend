# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey and OneToOneField has `on_delete` set to the desired behavior
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from django.db import models


class Player(models.Model):
    player_id = models.AutoField(primary_key=True)
    team = models.ForeignKey('Team', models.DO_NOTHING)
    player_name = models.CharField(max_length=50, blank=True, null=True)
    player_num = models.IntegerField(blank=True, null=True)
    player_age = models.IntegerField(blank=True, null=True)
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
    hitter_homerum = models.IntegerField(blank=True, null=True)
    hitter_tb = models.IntegerField(blank=True, null=True)
    hitter_rbi = models.IntegerField(blank=True, null=True)
    hitter_sb = models.IntegerField(blank=True, null=True)
    hitter_cs = models.IntegerField(blank=True, null=True)
    hitter_bob = models.IntegerField(blank=True, null=True)
    hitter_bb = models.IntegerField(blank=True, null=True)
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
