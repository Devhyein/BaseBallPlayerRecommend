# 여기 models 앞에 . 은 왜 붙죠?
from api.views.models import *
from rest_framework import serializers

class PlayerSerializer(serializers.ModelSerializer):
    class Meta:
        model = Player
        fields = '__all__'

class RecordFielderSerializer(serializers.ModelSerializer):
    class Meta:
        model = RecordFielder
        fields = '__all__'