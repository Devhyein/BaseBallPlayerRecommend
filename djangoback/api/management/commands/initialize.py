from pathlib import Path
import pandas as pd
from django.core.management.base import BaseCommand
from proj import settings
from api import models


# 이 소스가 management/commands 에 위치하면
# python manage.py initialize
# 위 명령어를 통해 호출하고 실행시킬 수 있다

# 스켈레톤에 있던 Store 데이터를 DB 에 넣는 코드
#
# 이 코드를 통해 Store 데이터를 불러와서 테스트해보기 위해
# 이 소스와 같은 위치에 dump.pkl 파일이 있어야함

class Command(BaseCommand):
    help = "initialize database"
    DATA_DIR = Path(settings.BASE_DIR) / "data"
    DATA_FILE = str(DATA_DIR / "dump.pkl")

    # dump.pkl 파일을 읽어서 데이터 가져오기
    def _load_dataframes(self):
        try:
            data = pd.read_pickle(Command.DATA_FILE)
        except:
            print(f"[-] Reading {Command.DATA_FILE} failed")
            exit(1)
        return data

    def _initialize(self):
        """
        Sub PJT 1에서 만든 Dataframe을 이용하여 DB를 초기화합니다.
        """
        print("[*] Loading data...")
        dataframes = self._load_dataframes()

        print("[*] Initializing stores...")
        models.Store.objects.all().delete() # 일단 지금 있는 모든 Store 데이터 지우기
        stores = dataframes["stores"]
        stores_bulk = [
            models.Store(
                id=store.id,
                store_name=store.store_name,
                branch=store.branch,
                area=store.area,
                tel=store.tel,
                address=store.address,
                latitude=store.latitude,
                longitude=store.longitude,
                category=store.category,
            )
            for store in stores.itertuples()
        ]
        models.Store.objects.bulk_create(stores_bulk)

        print("[+] Done")

    def handle(self, *args, **kwargs):
        self._initialize()
