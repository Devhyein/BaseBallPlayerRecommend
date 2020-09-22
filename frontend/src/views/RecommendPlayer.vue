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
        <!-- 2. 선택된 선수 스탯 그래프 -->
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
            :cols="tableColumnsForRecommendAndRemoved"
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
            :cols="tableColumnsForRecommendAndRemoved"
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

// API
import PlayerAPI from "@/api/PlayerAPI";

export default {
  components: {
    CustomRadarChart,
    CustomTable
  },
  data() {
    return {
      // 팀 스탯
      teamStats: {
        era: 0
        , health: 0
        , control: 0
        , stability: 0
        , deterrent: 0
        , power: 0
        , speed: 0
        , contact: 0
        , defense: 0
        , shoulder: 0
      },

      // 수정된 팀 스탯
      modifiedTeamStat: {},
      isModifiedTeamStat: false,

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
      lineupList: [],

      // 라인업 선수 목록
      lineupPlayers: [],

      // 추천 선수 목록
      recommendPlayers: [],

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

      // 라인업 선수 테이블 컬럼들
      tableColumns: [
        "At bat"
        , "Position"
        , "Name"
      ],

      // 추천선수, 제외된 선수 테이블 컬럼들
      tableColumnsForRecommendAndRemoved: [
        "Position"
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
    PlayerAPI.getLineupList(
      "none=none",
      res => {
        this.lineupList = res;
      },
      err => {
        console.log(err);
      }
    );

    this.teamStatData = this.computeTeamStatData();
    this.playerStatData = this.computePlayerStatData();
  },
  methods: {
    computeLineupPlayerTableData() {
      let arr = [];
      for(let player of this.lineupPlayers) {
        let atBat = player.player_position + '번 타자';
        if(player.player_position == 10) {
          atBat = '투수';
        }

        arr.push([
          atBat, 
          player.position, 
          player.player_name
        ]);
      }
      return arr;
    },
    computeRecommendPlayerTableData() {
      let arr = [];
      for(let player of this.recommendPlayers) {
        arr.push([
          player.position, 
          player.player_name
        ]);
      }
      return arr;
    },
    computeRemovedPlayerTableData() {
      let arr = [];
      for(let player of this.removedPlayers) {
        arr.push([
          player.position, 
          player.player_name
        ]);
      }
      return arr;
    },
    computeTeamStatData() {
      let obj = {};
      let label = [];
      let data = [];

      label = Object.keys(this.teamStats);
      obj.label = label;
      
      for(let key of label) {
        data.push(this.teamStats[key]);
      }
      obj.data = {
        label: '수정 전',
        backgroundColor: "rgba(255, 0, 0, 0.2)",
        borderColor: "rgb(255, 0, 0)",
        borderWidth: 1,
        pointBackgroundColor: "rgb(255, 0, 0)",
        data: data
      };

      if(this.isModifiedTeamStat) {
        let data2 = [];
        for(let key of label) {
          data2.push(this.modifiedTeamStat[key]);
        }
        obj.data2 = {
          label: '수정 후',
          backgroundColor: "rgba(100, 100, 255, 0.2)",
          borderColor: "rgb(100, 100, 255)",
          borderWidth: 1,
          pointBackgroundColor: "rgb(100, 100, 255)",
          data: data2
        };
      }

      return obj;
    },
    computePlayerStatData() {
      let obj = {};
      let label = [];
      let data = [];

      label = Object.keys(this.playerStats.five_tool);
      obj.label = label;
      
      for(let key of label) {
        data.push(this.playerStats.five_tool[key]);
      }
      obj.data = {
        label: 'Player stat',
        backgroundColor: "rgba(255, 0, 0, 0.2)",
        borderColor: "rgb(255, 0, 0)",
        borderWidth: 1,
        pointBackgroundColor: "rgb(255, 0, 0)",
        data: data
      };

      return obj;
    },
    changeLineup(id, name) {
      this.lineupId = id;
      this.lineupName = name;

      PlayerAPI.getTeamStatWithRecommend(
        "lineup=" + id,
        res => {
          this.lineupPlayers = res.playerList;
          this.recommendPlayers = res.recommendList;
          this.teamStats = res.teamStat;

          // teamStats 에 team_id 가 포함되어있다 이거 빼야한다
          delete this.teamStats.team_id;

          this.lineupPlayerTableData = this.computeLineupPlayerTableData();
          this.recommendPlayerTableData = this.computeRecommendPlayerTableData();
          this.teamStatData = this.computeTeamStatData();

          console.log(res);
        },
        err => {
          console.log(err);
        }
      );
    },
    getPlayerStat(id) {
      PlayerAPI.getPlayerStat(
        'num=' + id,
        res => {
          console.log(res);
          this.playerStats = res;
          this.playerStatData = this.computePlayerStatData();
        },
        err => {
          console.log(err);
        }
      )
    },
    clickLineupPlayer(index) {
      if(this.lineupSel != index) {
        this.playerName = this.lineupPlayers[index].name;
        this.lineupSel = index;
        
        this.getPlayerStat(this.lineupPlayers[index].player_id);
        this.playerName = this.lineupPlayers[index].player_name;
      }
    },
    clickRecommendPlayer(index) {
      if(this.recommendSel != index) {
        this.playerName = this.recommendPlayers[index].name;
        this.recommendSel = index;
        this.removedSel = -1;

        this.getPlayerStat(this.recommendPlayers[index].player_id);
        this.playerName = this.recommendPlayers[index].player_name;
      }
    },
    clickRemovedPlayer(index) {
      if(this.removedSel != index) {
        this.playerName = this.removedPlayers[index].name;
        this.removedSel = index;
        this.recommendSel = -1;

        this.getPlayerStat(this.removedPlayers[index].player_id);
        this.playerName = this.removedPlayers[index].player_name;
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
        // 양쪽의 포지션이 다르면 교환 안함
        if(temp.position != this.recommendPlayers[this.recommendSel].position) {
          alert('포지션이 다르면 교환이 안됩니다!');
          return;
        }

        this.recommendPlayers[this.recommendSel].player_position = temp.player_position;
        this.lineupPlayers[this.lineupSel] = this.recommendPlayers[this.recommendSel];
        this.recommendPlayers.splice(this.recommendSel, 1);
        this.recommendSel = -1;
      }
      // 2. removed 와 교환
      else if(this.removedSel != -1) {
        // 양쪽의 포지션이 다르면 교환 안함
        if(temp.position != this.removedPlayers[this.removedSel].position) {
          alert('포지션이 다르면 교환이 안됩니다!');
          return;
        }

        this.removedPlayers[this.removedSel].player_position = temp.player_position;
        this.lineupPlayers[this.lineupSel] = this.removedPlayers[this.removedSel];
        this.removedPlayers.splice(this.removedSel, 1);
        this.removedSel = -1;
      }
      
      // 선수목록에서 빠진건 무조건 removedPlayers 로 가기
      this.removedPlayers.push(temp);

      this.lineupPlayerTableData = this.computeLineupPlayerTableData();
      this.recommendPlayerTableData = this.computeRecommendPlayerTableData();
      this.removedPlayerTableData = this.computeRemovedPlayerTableData();

      // 선수 교체가 일어났으므로
      // 바뀐 팀 스탯도 보여주기
      // 데이터는 라인업 선수들의 id 리스트
      let idList = [];
      for(let player of this.lineupPlayers) {
        idList.push(player.player_id);
      }
      PlayerAPI.getTeamStat(
        {playerList: idList},
        res => {
          this.modifiedTeamStat = res;
          this.isModifiedTeamStat = true;
          this.teamStatData = this.computeTeamStatData();
        },
        err => {
          console.log(err);
        }
      )
    }
  }
};
</script>
<style scoped>
.custom-table {
  margin: auto;
}
</style>
