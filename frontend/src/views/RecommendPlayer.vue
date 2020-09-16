<template>
  <div>
    <base-header type="translucent-default" class="pb-5 pt-5"></base-header>

    <div class="container-fluid mt-2 row">
      <div class="col mr-1 ml-1  text-center align-self-center">
        <!-- 라인업 선택 -->
        <base-dropdown>
          <base-button
            slot="title"
            type="secondary"
            class="dropdown-toggle">
            {{lineupName}}
          </base-button>
          <template v-for="lineup in lineupList">
            <span
              :key="lineup.id"
              class="dropdown-item"
              @click="changeLineup(lineup.id, lineup.name)">
              {{lineup.name}}
            </span>
          </template>
        </base-dropdown>
        <!-- 선수 교체 -->
        <base-button
          slot="title"
          type="secondary"
          @click="changePlayer">
          선수 교체
        </base-button>
      </div>
    </div>

    <div class="container-fluid mt-2 row">
      <!-- left -->
      <div class="col-xl mr-1 ml-1">
        <!-- 선택된 라인업 선수 목록 -->
        <div class="row">
          <custom-table
            class="custom-table"
            :tableTitle="lineupName"
            :tableData="lineupPlayerTableData"
            :cols="tableColumns"
            :selectedRow="lineupSel"
            @clickRow="clickLineupPlayer"
          />
        </div>
      </div>

      <!-- center -->
      <div class="col-xl">
        <!-- 1. 팀 스탯 그래프 -->
        <div class="row">
          <custom-radar-chart
            class="col"
            title="Team Stat"
            :subTitle="lineupName"
            :data="teamStatData"
            :type="chartType" />
        </div>
        <!-- 2. 팀 스탯에 대한 설명 -->
        <div class="row mt-2">
          <custom-radar-chart
            class="col"
            title="Player stat"
            :subTitle="playerName"
            :data="playerStatData"
            :type="chartType" />
        </div>
      </div>

      <!-- right -->
      <div class="col-xl mr-1 ml-1">
        <!-- 1. 추천 선수 목록 테이블 -->
        <div class="row">
          <custom-table
            class="custom-table"
            tableTitle="추천 선수"
            :tableData="recommendPlayerTableData"
            :cols="tableColumns"
            :selectedRow="recommendSel"
            @clickRow="clickRecommendPlayer"
          />
        </div>
        <!-- 2. 선수 목록에서 제외된 선수 목록 테이블 -->
        <div class="row mt-2" v-if="removedPlayers.length > 0">
          <custom-table
            class="custom-table"
            tableTitle="제외된 선수"
            :tableData="removedPlayerTableData"
            :cols="tableColumns"
            :selectedRow="removedSel"
            @clickRow="clickRemovedPlayer"
          />
        </div>
      </div>
    </div>

  </div>
</template>

<script>
// Charts
import CustomRadarChart from "@/components/Player/CustomRadarChart";

// Tables
import CustomTable from "@/views/Tables/CustomTable";

export default {
  components: {
    CustomRadarChart,
    CustomTable
  },
  data() {
    return {
      // 팀 스탯
      teamStats: {
        era: 0.4
        , health: 0.6
        , control: 0
        , stability: 1
        , deterrent: 1
        , power: 1
        , speed: 0.3
        , contact: 0.6
        , defense: 0.3
        , shoulder: 0.6
      },

      // 플레이어 스탯
      playerStats: {
        five_tool: {
          power: 0,
          speed: 0,
          contact: 0,
          defense: 0,
          shoulder: 0,
        },
        stats: [],
      },

      // 라인업 리스트
      lineupList: [
        {
          id: 1,
          name: "당근 라인업"
        },
        {
          id: 2,
          name: "빠따 라인업"
        },
        {
          id: 3,
          name: "노빠구 라인업"
        },
      ],

      // 라인업 선수 목록
      lineupPlayers: [
        { id: 1234
          , order: 1
          , position: '우익수'
          , name: '김타자'
          , player_num: 12
          , age: 30
        },
        {
          id: 456
          , order: 2
          , position: '좌익수'
          , name: '최홈런'
          , player_num: 12
          , age: 30
        },
        {
          id: 112
          , order: 4
          , position: '포수'
          , name: '우사인볼트'
          , player_num: 12
          , age: 30
        },
        {
          id: 142
          , order: 0
          , position: '투수'
          , name: '김잘던짐'
          , player_num: 12
          , age: 30
        }
      ],

      // 추천 선수 목록
      recommendPlayers: [
        { id: 3333
          , order: 1
          , position: '우익수'
          , name: '나를써요'
          , player_num: 21
          , age: 72
        },
        {
          id: 4444
          , order: 2
          , position: '좌익수'
          , name: '홍길동'
          , player_num: 1
          , age: 320
        },
        {
          id: 5555
          , order: 4
          , position: '포수'
          , name: '이번타자'
          , player_num: 3
          , age: 24
        },
        {
          id: 6666
          , order: 0
          , position: '투수'
          , name: '손미끄러짐'
          , player_num: 29
          , age: 19
        }
      ],

      // 선수 목록에서 제외된 선수 목록
      removedPlayers: [],

      // 라인업과 추천선수 목록에서
      // 선택된 행을 기억하는 변수
      lineupSel: -1,
      recommendSel: -1,
      removedSel: -1,

      // 드롭다운으로 라인업 선택하는 동작을 위한 변수
      lineupName: "라인업 선택",
      lineupId: 0,

      // 추천선수, 라인업 선수 테이블 컬럼들
      tableColumns: [
        "At bat"
        , "Position"
        , "Name"
      ],

      // 선택한 선수의 이름 저장(스탯 보여주기 용)
      playerName: "Select player",

      // 그래프 타입(배경 색)
      chartType: "secondary",

      // 테이블을 위한 데이터
      lineupPlayerTableData: [],
      recommendPlayerTableData: [],
      removedPlayerTableData: [],

      // 차트를 위한 데이터
      teamStatData: {},
      playerStatData: {},
    }
  },
  created() {
    this.lineupPlayerTableData = this.computeLineupPlayerTableData();
    this.recommendPlayerTableData = this.computeRecommendPlayerTableData();
    this.teamStatData = this.computeTeamStatData();
    this.playerStatData = this.computePlayerStatData();
  },
  methods: {
    computeLineupPlayerTableData() {
      let arr = [];
      for(let player of this.lineupPlayers) {
        let atBat = player.order + '번 타자';
        if(player.order == 0) {
          atBat = '투수';
        }

        arr.push([
          atBat, 
          player.position, 
          player.name
        ]);
      }
      return arr;
    },
    computeRecommendPlayerTableData() {
      let arr = [];
      for(let player of this.recommendPlayers) {
        let atBat = player.order + '번 타자';
        if(player.order == 0) {
          atBat = '투수';
        }

        arr.push([
          atBat, 
          player.position, 
          player.name
        ]);
      }
      return arr;
    },
    computeRemovedPlayerTableData() {
      let arr = [];
      for(let player of this.removedPlayers) {
        let atBat = player.order + '번 타자';
        if(player.order == 0) {
          atBat = '투수';
        }

        arr.push([
          atBat, 
          player.position, 
          player.name
        ]);
      }
      return arr;
    },
    computeTeamStatData() {
      let obj = {};
      let label = [];
      let data = [];

      label = Object.keys(this.teamStats);
      for(let key of label) {
        data.push(this.teamStats[key]);
      }

      obj.label = label;
      obj.data = data;
      return obj;
    },
    computePlayerStatData() {
      let obj = {};
      let label = [];
      let data = [];

      label = Object.keys(this.playerStats.five_tool);
      for(let key of label) {
        data.push(this.playerStats.five_tool[key]);
      }

      obj.label = label;
      obj.data = data;
      return obj;
    },
    changeLineup(id, name) {
      this.lineupId = id;
      this.lineupName = name;
    },
    clickLineupPlayer(index) {
      if(this.lineupSel != index) {
        this.playerName = this.lineupPlayers[index].name;
        this.lineupSel = index;
      }
    },
    clickRecommendPlayer(index) {
      if(this.recommendSel != index) {
        this.playerName = this.recommendPlayers[index].name;
        this.recommendSel = index;
        this.removedSel = -1;
      }
    },
    clickRemovedPlayer(index) {
      if(this.removedSel != index) {
        this.playerName = this.removedPlayers[index].name;
        this.removedSel = index;
        this.recommendSel = -1;
      }
    },
    changePlayer() {
      // 양쪽이 모두 선택되지 않은경우 그냥 반환
      if(this.lineupSel == -1 || 
          (this.recommendSel == -1 && this.removedSel == -1)) {
        return;
      }

      // 선수 목록에서 한명 빼서 temp 에 보관
      let temp = this.lineupPlayers[this.lineupSel];

      // 1. recommend 와 교환
      if(this.recommendSel != -1) {
        this.lineupPlayers[this.lineupSel] = this.recommendPlayers[this.recommendSel];
        this.recommendPlayers.splice(this.recommendSel, 1);
      }
      // 2. removed 와 교환
      else if(this.removedSel != -1) {
        this.lineupPlayers[this.lineupSel] = this.removedPlayers[this.removedSel];
        this.removedPlayers.splice(this.removedSel, 1);
      }
      
      // 선수목록에서 빠진건 무조건 removedPlayers 로 가기
      this.removedPlayers.push(temp);

      this.lineupPlayerTableData = this.computeLineupPlayerTableData();
      this.recommendPlayerTableData = this.computeRecommendPlayerTableData();
      this.removedPlayerTableData = this.computeRemovedPlayerTableData();
    }
  }
};
</script>
<style scoped>
.custom-table {
  margin: auto;
}
</style>
