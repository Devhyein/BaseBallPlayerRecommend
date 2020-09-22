<template>
  <div>
    <!-- 헤더 -->
    <base-header type="translucent-default" class="pb-5 pt-5"></base-header>
    
    <!-- 1번째 행: 라인업과 그 라인업의 스탯 -->
    <div class="container-fluid mt-2 row">
      <!-- 1번째 열: 라인업 선택 드롭다운과 라인업의 선수 목록 -->
      <div class="col-lg mr-1 ml-1  text-center align-self-center">
        <!-- 라인업 선택 드롭다운 -->
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
        <!-- 초기화, 저장 버튼 -->
        <base-button
          slot="title"
          type="secondary"
          class="ml-2"
          @click="resetLineup">
          초기화
        </base-button>
        <base-button
          slot="title"
          type="secondary"
          class="ml-2"
          @click="saveLineup">
          저장
        </base-button>
        <!-- 라인업 선수 목록 -->
        <custom-table
          class="custom-table mt-2"
          :tableTitle="lineupName"
          :tableData="lineupPlayerTableData"
          :cols="tableColumns"
          :selectedRow="lineupSel"
          @clickRow="clickLineupPlayer"
        />
      </div>
      <!-- 2번째 열 라인업에 대한 스탯 정보 -->
      <div class="col-lg mt-2">
        <custom-radar-chart
          class="col"
          title="Team Stat"
          :subTitle="lineupName"
          :data="teamStatData"
          :type="chartType" />
        
        <custom-radar-chart
          class="col mt-2"
          title="Player stat"
          :subTitle="playerName"
          :data="playerStatData"
          :type="chartType" />
      </div>
    </div>

    <!-- 2번째 행: 검색 기준 필터 -->
    <div class="container-fluid mt-2 row">
      <h1>선수 목록</h1>
    </div>

    <!-- 3번째 행: 라인업에 추가할 선수 목록(검색) -->
    <div class="container-fluid mt-2 row">
      <custom-table
        class="custom-table col mr-2 ml-2"
        tableTitle="검색된 선수 목록"
        :tableData="searchedPlayerTableData"
        :cols="tableColumnsForSearch"
        :selectedRow="searchedSel"
        @clickRow="clickSearchedPlayer"
      />
    </div>

  </div>
</template>

<script>
// Charts
import CustomRadarChart from "@/components/Player/CustomRadarChart";

// Tables
import CustomTable from "@/views/Tables/CustomTable";

// Alert
import swal from 'sweetalert';

// API
// import PlayerAPI from "@/api/PlayerAPI";

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
      lineupList: [
        {
          id: 123,
          name: '당근 라인업'
        },
        {
          id: 456,
          name: '빠따 라인업'
        }
      ],

      // 라인업 선수 목록
      lineupPlayers: [
        {
          player_id: 1,
          player_name: '김선수',
          player_position: 1,
          position: '유격수'
        },
        {
          player_id: 2,
          player_name: '이선수',
          player_position: 2,
          position: '좌익수'
        },
        {
          player_id: 3,
          player_name: '최선수',
          player_position: 3,
          position: '우익수'
        },
        {
          player_id: 4,
          player_name: '한선수',
          player_position: 4,
          position: '1루수'
        },
        {
          player_id: 5,
          player_name: '강선수',
          player_position: 5,
          position: '2루수'
        },
        {
          player_id: 6,
          player_name: '전선수',
          player_position: 6,
          position: '3루수'
        },
        {
          player_id: 7,
          player_name: '유선수',
          player_position: 7,
          position: '포수'
        },
        {
          player_id: 8,
          player_name: '윤선수',
          player_position: 8,
          position: '중견수'
        },
        {
          player_id: 9,
          player_name: '안선수',
          player_position: 9,
          position: '또뭐있지'
        },
        {
          player_id: 10,
          player_name: '임선수',
          player_position: 10,
          position: '투수'
        },
      ],

      // 추천 선수 목록
      searchedPlayers: [
        {
          player_id: 1,
          player_name: '김선수',
          player_team: '우리팀',
          position: '유격수',
          player_num: 0,
          player_age: 30
        },
        {
          player_id: 2,
          player_name: '이선수',
          player_team: '우리팀',
          position: '유격수',
          player_num: 0,
          player_age: 30
        },
        {
          player_id: 3,
          player_name: '안선수',
          player_team: '우리팀',
          position: '유격수',
          player_num: 0,
          player_age: 30
        },
        {
          player_id: 4,
          player_name: '손선수',
          player_team: '우리팀',
          position: '유격수',
          player_num: 0,
          player_age: 30
        },
        {
          player_id: 5,
          player_name: '임선수',
          player_team: '우리팀',
          position: '유격수',
          player_num: 0,
          player_age: 30
        },
        {
          player_id: 6,
          player_name: '유선수',
          player_team: '우리팀',
          position: '유격수',
          player_num: 0,
          player_age: 30
        },
        {
          player_id: 7,
          player_name: '강선수',
          player_team: '우리팀',
          position: '유격수',
          player_num: 0,
          player_age: 30
        },
      ],

      // 라인업과 추천선수 목록에서
      // 선택된 행을 기억하는 변수
      lineupSel: -1,
      searchedSel: -1,

      // 드롭다운으로 라인업 선택하는 동작을 위한 변수
      lineupName: "라인업 선택",
      lineupId: 0,

      // 라인업 선수 테이블 컬럼들
      tableColumns: [
        "At bat"
        , "Position"
        , "Name"
      ],

      // 검색된 선수 테이블 컬럼들
      tableColumnsForSearch: [
        "Name"
        , "Team"
        , "Position"
        , "Number"
        , "Age"
      ],

      // 선택한 선수의 이름 저장(스탯 보여주기 용)
      playerName: "Select player",

      // 그래프 타입(배경 색)
      chartType: "secondary",

      // 테이블을 위한 데이터
      lineupPlayerTableData: [],
      searchedPlayerTableData: [],

      // 차트를 위한 데이터
      teamStatData: {},
      playerStatData: {},
    }
  },
  created() {
    // PlayerAPI.getLineupList(
    //   "none=none",
    //   res => {
    //     this.lineupList = res;
    //   },
    //   err => {
    //     console.log(err);
    //   }
    // );

    this.teamStatData = this.computeTeamStatData();
    this.playerStatData = this.computePlayerStatData();
    this.lineupPlayerTableData = this.computeLineupPlayerTableData();
    this.searchedPlayerTableData = this.computeSearchedPlayerTableData();
  },
  methods: {
    computeLineupPlayerTableData() {
      let arr = [];
      let num = 1;
      for(let player of this.lineupPlayers) {
        let atBat = num + '번 타자';
        if(num == 10) {
          atBat = '투수';
        }

        arr.push([
          atBat, 
          player.position, 
          player.player_name
        ]);

        num += 1;
      }
      return arr;
    },
    computeSearchedPlayerTableData() {
      let arr = [];
      for(let player of this.searchedPlayers) {
        arr.push([
          player.player_name, 
          player.player_team, 
          player.position,
          player.player_num,
          player.player_age
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

      // PlayerAPI.getTeamStatWithRecommend(
      //   "lineup=" + id,
      //   res => {
      //     this.lineupPlayers = res.playerList;
      //     this.recommendPlayers = res.recommendList;
      //     this.teamStats = res.teamStat;

      //     // teamStats 에 team_id 가 포함되어있다 이거 빼야한다
      //     delete this.teamStats.team_id;

      //     this.lineupPlayerTableData = this.computeLineupPlayerTableData();
      //     this.recommendPlayerTableData = this.computeRecommendPlayerTableData();
      //     this.teamStatData = this.computeTeamStatData();

      //     console.log(res);
      //   },
      //   err => {
      //     console.log(err);
      //   }
      // );
    },
    clickLineupPlayer(index) {
      if(this.lineupSel != index) {
        this.playerName = this.lineupPlayers[index].name;
        this.lineupSel = index;
        this.searchedSel = -1;
        
        this.getPlayerStat(this.lineupPlayers[index].player_id);
        this.playerName = this.lineupPlayers[index].player_name;
      }
    },
    clickSearchedPlayer(index) {
      if(this.recommendSel != index) {
        this.playerName = this.searchedPlayers[index].name;
        this.searchedSel = index;
        this.lineupSel = -1;

        this.getPlayerStat(this.searchedPlayers[index].player_id);
        this.playerName = this.searchedPlayers[index].player_name;
      }
    },
    getPlayerStat(id) {
      console.log(id);
      // PlayerAPI.getPlayerStat(
      //   'num=' + id,
      //   res => {
      //     console.log(res);
      //     this.playerStats = res;
      //     this.playerStatData = this.computePlayerStatData();
      //   },
      //   err => {
      //     console.log(err);
      //   }
      // )
    },
    resetLineup() {
      console.log("reset");
    },
    saveLineup() {
      // this.makeToast("저장 완료", "라인업 저장이 완료되었습니다", "success");
      this.saveAs();
    },
    saveAs() {
      swal({
        text: '라인업 이름 입력',
        content: "input",
        button: {
          text: "Search!",
          closeModal: false,
        },
      })
      .then(name => {
        if (!name) {
          swal({
            title: "이름을 입력해주세요!",
            icon: "error"
          });
        } else {
          swal({
            title: "Input Result: ",
            text: name
          });
        }
      });
    },
    makeToast(title, message, icon){
      swal(title, message, icon);
    }
  }
};
</script>
<style scoped>
.custom-table {
  margin: auto;
}
</style>
