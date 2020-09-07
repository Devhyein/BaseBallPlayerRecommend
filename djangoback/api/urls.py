from django.conf.urls import url
from rest_framework.routers import DefaultRouter
from api import views

# 요청 라우팅을 위해 default 라우터 가져옴
# trailing_slash : url 끝에 / 를 붙일지 말지 결정
router = DefaultRouter(trailing_slash=False)

# 'stores' url 로 요청시 StoreViewSet 에 설정된 내용대로 값을 반환해줌
# http://~~~/api/stores => 중간에 api 는 proj 의 urls 의 내용 참고
router.register(r"stores", views.StoreViewSet, basename="stores")

# 라우터에 등록한 url 패턴들을 urlpatterns 변수에 저장해놓기
urlpatterns = router.urls
