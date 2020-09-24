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
    <div class="container-fluid mt-2 row">
      <card class="col ml-2 mr-2">
        <form class="navbar-search form-inline">
          <base-button
            slot="title"
            type="secondary"
            class="mr-2 mb-1"
            @click="modals.position = true">
            포지션
          </base-button>
          <modal
            :show.sync="modals.position"
            modal-classes="modal-dialog-centered modal-sm">
            <template slot="header">
                <h2 class="modal-title" id="exampleModalLabel">포지션 선택</h2>
            </template>
            <div>
              <base-button class="mb-2" :type="positionFilter[1] ? 'secondary' : 'warning'" @click="changePositionFilter(1)">투수</base-button>
              <base-button class="mb-2" :type="positionFilter[2] ? 'secondary' : 'warning'" @click="changePositionFilter(2)">포수</base-button>
              <base-button class="mb-2" :type="positionFilter[3] ? 'secondary' : 'warning'" @click="changePositionFilter(3)">1루수</base-button>
              <base-button class="mb-2" :type="positionFilter[4] ? 'secondary' : 'warning'" @click="changePositionFilter(4)">2루수</base-button>
              <base-button class="mb-2" :type="positionFilter[5] ? 'secondary' : 'warning'" @click="changePositionFilter(5)">3루수</base-button>
              <base-button class="mb-2" :type="positionFilter[6] ? 'secondary' : 'warning'" @click="changePositionFilter(6)">유격수</base-button>
              <base-button class="mb-2" :type="positionFilter[7] ? 'secondary' : 'warning'" @click="changePositionFilter(7)">좌익수</base-button>
              <base-button class="mb-2" :type="positionFilter[8] ? 'secondary' : 'warning'" @click="changePositionFilter(8)">중견수</base-button>
              <base-button class="mb-2" :type="positionFilter[9] ? 'secondary' : 'warning'" @click="changePositionFilter(9)">우익수</base-button>
              <base-button class="mb-2" :type="positionFilter[10] ? 'secondary' : 'warning'" @click="changePositionFilter(10)">지명타자</base-button>
            </div>
            <template slot="footer">
                <base-button type="secondary" @click="modals.position = false">Close</base-button>
            </template>
          </modal>

          <base-button
            slot="title"
            type="secondary"
            class="mr-2 mb-1"
            @click="modals.team = true">
            팀
          </base-button>
          <modal
            :show.sync="modals.team"
            modal-classes="modal-dialog-centered modal-sm">
            <template slot="header">
                <h2 class="modal-title" id="exampleModalLabel">팀 선택</h2>
            </template>
            <div>
              <base-button class="mb-2" :type="teamFilter[1] ? 'secondary' : 'warning'" @click="changeTeamFilter(1)">KIA 타이거즈</base-button>
              <base-button class="mb-2" :type="teamFilter[2] ? 'secondary' : 'warning'" @click="changeTeamFilter(2)">해태 타이거즈</base-button>
              <base-button class="mb-2" :type="teamFilter[3] ? 'secondary' : 'warning'" @click="changeTeamFilter(3)">삼성 라이온즈</base-button>
              <base-button class="mb-2" :type="teamFilter[4] ? 'secondary' : 'warning'" @click="changeTeamFilter(4)">두산 베어스</base-button>
              <base-button class="mb-2" :type="teamFilter[5] ? 'secondary' : 'warning'" @click="changeTeamFilter(5)">OB 베어스</base-button>
              <base-button class="mb-2" :type="teamFilter[6] ? 'secondary' : 'warning'" @click="changeTeamFilter(6)">SK 와이번스</base-button>
              <base-button class="mb-2" :type="teamFilter[7] ? 'secondary' : 'warning'" @click="changeTeamFilter(7)">현대 유니콘스</base-button>
              <base-button class="mb-2" :type="teamFilter[8] ? 'secondary' : 'warning'" @click="changeTeamFilter(8)">태평양 돌핀스</base-button>
              <base-button class="mb-2" :type="teamFilter[9] ? 'secondary' : 'warning'" @click="changeTeamFilter(9)">청보 핀토스</base-button>
              <base-button class="mb-2" :type="teamFilter[10] ? 'secondary' : 'warning'" @click="changeTeamFilter(10)">삼미 슈퍼스타즈</base-button>
              <base-button class="mb-2" :type="teamFilter[11] ? 'secondary' : 'warning'" @click="changeTeamFilter(11)">LG 트윈스</base-button>
              <base-button class="mb-2" :type="teamFilter[12] ? 'secondary' : 'warning'" @click="changeTeamFilter(12)">MBC 청룡</base-button>
              <base-button class="mb-2" :type="teamFilter[13] ? 'secondary' : 'warning'" @click="changeTeamFilter(13)">롯데 자이언츠</base-button>
              <base-button class="mb-2" :type="teamFilter[14] ? 'secondary' : 'warning'" @click="changeTeamFilter(14)">한화 이글스</base-button>
              <base-button class="mb-2" :type="teamFilter[15] ? 'secondary' : 'warning'" @click="changeTeamFilter(15)">빙그레 이글스</base-button>
              <base-button class="mb-2" :type="teamFilter[16] ? 'secondary' : 'warning'" @click="changeTeamFilter(16)">NC 다이노스</base-button>
              <base-button class="mb-2" :type="teamFilter[17] ? 'secondary' : 'warning'" @click="changeTeamFilter(17)">히어로즈</base-button>
              <base-button class="mb-2" :type="teamFilter[18] ? 'secondary' : 'warning'" @click="changeTeamFilter(18)">넥센 히어로즈</base-button>
              <base-button class="mb-2" :type="teamFilter[19] ? 'secondary' : 'warning'" @click="changeTeamFilter(19)">키움 히어로즈</base-button>
              <base-button class="mb-2" :type="teamFilter[20] ? 'secondary' : 'warning'" @click="changeTeamFilter(20)">쌍방울 레이더스</base-button>
              <base-button class="mb-2" :type="teamFilter[21] ? 'secondary' : 'warning'" @click="changeTeamFilter(21)">KT 위즈</base-button>
            </div>
            <template slot="footer">
                <base-button type="secondary" @click="modals.team = false">Close</base-button>
            </template>
          </modal>

          <div class="form-group mb-0">
              <label class="mr-3">
                이름 입력: 
              </label>
              <base-input
                placeholder="Search"
                class="input-group-alternative"
                alternative
                addon-right-icon="fas fa-search"
                v-model="searchVal"
                @clickIcon="search"
              ></base-input>
          </div>
        </form>

      </card>
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
      lineupPlayers: [
        { player_id: -1, position: '포지션', player_name: '이름' },
        { player_id: -1, position: '포지션', player_name: '이름' },
        { player_id: -1, position: '포지션', player_name: '이름' },
        { player_id: -1, position: '포지션', player_name: '이름' },
        { player_id: -1, position: '포지션', player_name: '이름' },
        { player_id: -1, position: '포지션', player_name: '이름' },
        { player_id: -1, position: '포지션', player_name: '이름' },
        { player_id: -1, position: '포지션', player_name: '이름' },
        { player_id: -1, position: '포지션', player_name: '이름' },
        { player_id: -1, position: '포지션', player_name: '이름' },
      ],

      // 검색된 선수 목록
      searchedPlayers: [],

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

      // 검색창에 입력한 내용
      searchVal: '',

      // 필터링에 쓰일 modal 을 위한 내용
      modals: {
        position: false,
        team: false,
      },
      
      // 포지션 필터링용 리스트
      positionFilter: [false,
        false, false, false, false, false, false, false, false, false, false],
      // 팀 필터링용 리스트
      teamFilter: [false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false],
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

      PlayerAPI.getLineupPlayerWithTeamStat(
        "lineup=" + id,
        res => {
          this.lineupPlayers = res.playerList;
          this.teamStats = res.teamStat;

          // teamStats 에 team_id 가 포함되어있다 이거 빼야한다
          delete this.teamStats.team_id;

          this.lineupPlayerTableData = this.computeLineupPlayerTableData();
          this.teamStatData = this.computeTeamStatData();

          console.log(res);
        },
        err => {
          console.log(err);
        }
      );
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
      if(id == -1) return;
      
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
    resetLineup() {
      console.log("reset");
    },
    saveLineup() {
      // swal("저장 완료", "라인업 저장이 완료되었습니다", "success");
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
    search() {
      console.log(this.searchVal);
    },
    // 버튼 누르면 바로 반영되게 하기 위한 편법
    changePositionFilter(idx) {
      let arr = [];
      for(let i in this.positionFilter) {
        let val = this.positionFilter[i];

        if(i == idx) arr.push(!val);
        else         arr.push(val);
      }
      this.positionFilter = arr;
    },
    changeTeamFilter(idx) {
      let arr = [];
      for(let i in this.teamFilter) {
        let val = this.teamFilter[i];

        if(i == idx) arr.push(!val);
        else         arr.push(val);
      }
      this.teamFilter = arr;
    }
  }
};
</script>
<style scoped>
.custom-table {
  margin: auto;
}
</style>
