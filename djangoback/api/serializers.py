# 여기 models 앞에 . 은 왜 붙죠?
# from .models import Store
from api.views.models import *
from rest_framework import serializers

# Store 모델 직렬화 관련 설정
# class StoreSerializer(serializers.ModelSerializer):
#     class Meta:
#         model = Store
#         fields = [
#             "id",
#             "store_name",
#             "branch",
#             "area",
#             "tel",
#             "address",
#             "latitude",
#             "longitude",
#             "category_list",
#         ]

class PlayerSerializer(serializers.ModelSerializer):
    class Meta:
        model = Player
        fields = '__all__'

class RecordFielderSerializer(serializers.ModelSerializer):
    class Meta:
        model = RecordFielder
        fields = '__all__'