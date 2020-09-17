from django.conf.urls import url
from rest_framework.routers import DefaultRouter
from api import views
from api import views2
from api import views3

# 요청 라우팅을 위해 default 라우터 가져옴
# trailing_slash : url 끝에 / 를 붙일지 말지 결정
router = DefaultRouter(trailing_slash=False)

# 'stores' url 로 요청시 StoreViewSet 에 설정된 내용대로 값을 반환해줌
# http://~~~/api/stores => 중간에 api 는 proj 의 urls 의 내용 참고

# 두번째 인자로는 ViewSet이라는게 들어가야 한다. 알아보기
# router.register(r"stores", views.StoreViewSet, basename="stores")
# router.register(r"crawling", views.PlayerViewSet, basename="crawling")

# 라우터에 등록한 url 패턴들을 urlpatterns 변수에 저장해놓기
#urlpatterns = router.urls

# localhost:8000/api/pitcherdata -> views.py의 getPiterchersRecords() 메서드 호출

urlpatterns = [
    url(r'hitterdata', views.getHittersRecords),
    url(r'pitcherdata', views.getPitchersRecords),
    url(r'fielderdata', views.getFieldersRecords),
    url(r'retiredata', views.getEntireRecords),
    url(r'defaultlineup', views2.getLineup),
    url(r'recommend1', views3.recommend_player_method1)
]
