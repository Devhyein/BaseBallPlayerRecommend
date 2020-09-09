# models 와 sereializers 모듈 가져옴
from api import models, serializers
from rest_framework import viewsets
from rest_framework.pagination import PageNumberPagination
from bs4 import BeautifulSoup

def getPlayersRecords(request):
    url = 'http://www.statiz.co.kr/stat.php?mid=stat&re=0&ys=1982&ye=2020&se=0&te=&tm=&ty=0&qu=auto&po=0&as=&ae=&hi=&un=&pl=&da=1&o1=WAR_ALL_ADJ&o2=TPA&de=1&lr=0&tr=&cv=&ml=1&sn=30&pa=0&si=&cn='
    webpage = urlopen(url)
    source = BeautifulSoup(webpage, 'html.parser', from_encoding='utf-8')
    table = 


# 페이지네이션을 위한 클래스 생성
# PageNumberPagination 을 상속받음
class SmallPagination(PageNumberPagination):
    page_size = 10
    page_size_query_param = "page_size"
    max_page_size = 50

# Store 모델에 대한 ViewSet 지정
class StoreViewSet(viewsets.ModelViewSet):
    # 1. 정의해둔 serializer 클래스 지정
    serializer_class = serializers.StoreSerializer
    
    # 2. 정의한 pagination 클래스 지정(Optional)
    pagination_class = SmallPagination

    # 3. 상황에 맞는 queryset 지정
    # 항상 같은 queryset 사용 할 땐
    # queryset = models.Store.objects.all() 해도 된다
    def get_queryset(self):
        # 쿼리 파라미터에서 name 이라는 항목의 값을 가져옴
        name = self.request.query_params.get("name", "")
        # 이름에 해당 문자열을 포함하는 음식점 목록만 가져오는데 id 순으로 정렬함
        queryset = (
            models.Store.objects.all().filter(store_name__contains=name).order_by("id")
        )
        # 위에서 뽑은 결과를 queryset 으로 지정하기위해 반환
        return queryset
