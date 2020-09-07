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