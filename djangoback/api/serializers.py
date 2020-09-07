# 여기 models 앞에 . 은 왜 붙죠?
from .models import Store
from rest_framework import serializers

# Store 모델 직렬화 관련 설정
class StoreSerializer(serializers.ModelSerializer):
    class Meta:
        model = Store
        fields = [
            "id",
            "store_name",
            "branch",
            "area",
            "tel",
            "address",
            "latitude",
            "longitude",
            "category_list",
        ]
