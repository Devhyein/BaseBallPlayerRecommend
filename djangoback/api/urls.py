from django.conf.urls import url
from rest_framework.routers import DefaultRouter
from api.views import player_crawling
from api.views import lineup_crawling
from api.views import recommend1
from api.views import recommend2_clustering
from api.views import salary_crawling

from rest_framework import routers, permissions
from drf_yasg.views import get_schema_view
from drf_yasg import openapi
from django.conf import settings
from django.urls import path, include, re_path


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
    url(r'hitterdata', player_crawling.getHittersRecords),
    url(r'pitcherdata', player_crawling.getPitchersRecords),
    url(r'fielderdata', player_crawling.getFieldersRecords),
    url(r'retiredata', player_crawling.getEntireRecords),
    url(r'defaultlineup', lineup_crawling.getLineup),
    url(r'recommend1', recommend1.recommend_player_method1),
    url(r'recommend2', recommend2_clustering.clustering_test),
    url(r'salarydata', salary_crawling.getSalaryData)
]

schema_view = get_schema_view(
    openapi.Info(
        title="당근빠따 django API",
        default_version='v1',
        description="Test description",
    ),
    public=True,
    permission_classes=(permissions.AllowAny,),
)

if settings.DEBUG:
    urlpatterns += [
        re_path(r'^swagger(?P<format>\.json|\.yaml)$', schema_view.without_ui(cache_timeout=0), name='schema-json'),
        re_path(r'^swagger/$', schema_view.with_ui('swagger', cache_timeout=0), name='schema-swagger-ui'),
        re_path(r'^redoc/$', schema_view.with_ui('redoc', cache_timeout=0), name='schema-redoc')
    ]