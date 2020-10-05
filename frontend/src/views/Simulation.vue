<template>
  <div>
    <!-- 헤더 -->
    <base-header type="translucent-default" class="pb-5 pt-5"></base-header>
    
    <div class="container-fluid mt-2 row">
      <!-- 왼쪽 - 상대 팀 라인업 -->
      <!-- 1번째 열: 라인업 선택 드롭다운과 라인업의 선수 목록 -->
      <div class="col-xl-3 mr-1 ml-1 text-center align-self-center">
        <base-dropdown>
          <base-button slot="title" type="secondary" class="dropdown-toggle">{{
            yourLineupName
          }}</base-button>
          <template v-for="yourLineup in yourLineupList">
            <span
              :key="yourLineup.id"
              class="dropdown-item"
              @click="changeYourLineup(yourLineup.id, yourLineup.name)"
              >{{ yourLineup.name }}</span
            >
          </template>
        </base-dropdown>
        <!-- 라인업 선수 목록 -->
        <div class="card custom-table row mt-1">
          <div class="card-header border-0">
            <div class="row align-items-center">
              <div class="col">
                <h3 class="mb-0">{{yourLineupName}}</h3>
              </div>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table tablesorter table-hover">
              <thead class="thead-light">
                <tr>
                  <th>AT BAT</th>
                  <th>NAME</th>
                </tr>
              </thead>
              <draggable v-model="yourLineupPlayers" tag="tbody">
                <tr v-for="(row, rowIdx) in yourLineupPlayers" :key="rowIdx">
                    <td @click="clickLineupPlayer(rowIdx)">{{atBat[rowIdx]}}</td>
                    <td @click="clickLineupPlayer(rowIdx)">{{row.player_name}}</td>
                </tr>
              </draggable>
            </table>
          </div>
        </div>
      </div>
      
      <!-- 가운데 -->
      <div class="col-xl mr-1 ml-1 text-center align-self-center">

        <!-- 경기 score table -->
        <div style="overflow-x:auto;">
          <table class="score_table" style="width:100%;margin-top: 85px; margin-bottom:20px;">
            <thead>
              <tr>
                <th>TEAM</th>
                <th v-for="key in 12" :key="key" class="inning_score">{{key}}</th>
                <th>결과</th>
              </tr>
            </thead>
            <tbody>
              <tr style="border:1px solid black">
                <td>{{lineupName}}</td>
                <td v-for="key in 12" :key="key">{{0}}</td>
                <td>0</td>
              </tr>            
              <tr>
                <td>{{yourLineupName}}</td>
                <td v-for="key in 12" :key="key">{{0}}</td>
                <td>0</td>
              </tr>
            </tbody>
          </table>
        </div>


        <!-- 경기 내용 추후 추가 -->
        <div v-if="lineupId>0" style="height:250px; margin-bottom:20px;">
          <img src="/img/field.png" alt="경기장" style="height:250px; width: 100%;">
        
          <!-- 경기내용 <br/>
          000 선수가 1루로 진입했습니다. <br/>

          게임을 계속하시려면 진행을 교체하시려면 선수 검색을 해주세요. <br/> -->
        </div>

        <!-- 타석 타자 정보 -->
        <div v-if="lineupId>0" style="overflow-x:auto;height:100px; border:1px solid black; margin-bottom:20px;">
          <table style="width:100%;margin-top: 20px;">
            <thead>
              <tr>
                <th v-for="column in hitterInfoColumns" :key="column">{{ column }}</th>
              </tr>
            </thead>
            <draggable v-model="hit_info" tag="tbody" style="margin-top:5px;">
              <tr>
                <td>{{lineupPlayers[hit_order]}}</td>
                <td v-for="row in hit_info" :key="row">{{row}}</td>
              </tr>
            </draggable>
          </table>
        </div>

        <!-- 경기 base 정보, sbo -->
        <div v-if="lineupId>0" style="height:140px; margin-bottom:20px;" class="base_info row">
          <div class="col-5">
            <div id="diamond">
              <div id="base2"></div>
              <div id="base1"></div>
              <div id="base3"></div>
            </div>
          </div>
          <div class="col-7">
            <div id="sbo">
              <div style="display: inline-block;">O</div>
              <div v-for="s in 2" :key="s" style="display: inline-block;"><div class="circle_o"></div></div>
            </div>
            
            <div class="col">
              <div v-if="!start" class="gameBtn">
                <base-button type="primary" @click="clickStartBtn">게임시작</base-button>
              </div>

              <div v-if="start" class="gameBtn"> 
                <base-button type="primary">게임진행</base-button>
                <base-button type="primary" @click="clickEndBtn">게임종료</base-button>
              </div>
            </div>
          </div>

        </div>
      </div>

    <!-- 오른쪽 - 우리 팀 라인업  -->
      <div class="col-xl-3 mr-1 ml-1 text-center align-self-center">
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
          <div class="dropdown-divider"></div>
        </base-dropdown>

        <!-- 라인업 선수 목록 -->
        <div class="card custom-table mt-2">
          <div class="card-header border-0">
            <div class="row align-items-center">
              <div class="col">
                <h3 class="mb-0">{{lineupName}}</h3>
              </div>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table tablesorter table-hover">
              <thead class="thead-light">
                <tr>
                  <th v-for="column in tableColumns" :key="column">{{ column }}</th>
                </tr>
              </thead>
              <draggable v-model="lineupPlayers" tag="tbody">
                <tr v-for="(row, rowIdx) in lineupPlayers" :key="rowIdx">
                    <td @click="clickLineupPlayer(rowIdx)">{{atBat[rowIdx]}}</td>
                    <td @click="clickLineupPlayer(rowIdx)">{{row.player_name}}</td>
                </tr>
              </draggable>
            </table>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>

// Alert
import swal from 'sweetalert';

// API
import PlayerAPI from "@/api/PlayerAPI";

// Drag and Drop
import draggable from 'vuedraggable'

export default {
  components: {
    draggable
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
      yourLineupList: [],

      // 라인업 선수 목록
      lineupPlayers: [],
      yourLineupPlayers: [],

      // 검색된 선수 목록
      searchedPlayers: [],

      // 검색된 선수 페이지네이션을 위한 것
      searchedPlayerListShowData: [],
      pageCount: 1,
      pageVal: 1,
      from: 0,
      total: 0,

      // 라인업과 추천선수 목록에서
      // 선택된 행을 기억하는 변수
      lineupSel: -1,
      searchedSel: -1,

      // 드롭다운으로 라인업 선택하는 동작을 위한 변수
      lineupName: "나의 라인업 선택",
      lineupId: 0,
      yourLineupName: "상대 라인업 선택",
      yourLineupId: 0,

      // 라인업 선수 테이블 컬럼들
      tableColumns: [
        "At bat"
        // , "Position"
        , "Name"
      ],     
      // 상대 라인업 선수 테이블 컬럼들
      yourTableColumns: [
        "At bat"
        // , "Position"
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
      atBat: [
        '1번 타자', '2번 타자', '3번 타자', '4번 타자', '5번 타자',
        '6번 타자', '7번 타자', '8번 타자', '9번 타자', '투수'
      ],
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

      // 시뮬레이션 관련 
      start : false,
      simulation_id : 0,
      hitterInfoColumns: [
        "이름","타석","1루타","2루타","3루타","홈런","파울"
      ], 

      game : {
        simulation_id : 0,
        user_id : 0,
        my_lineup_id : 0,
        your_lineup_id : 0,
        is_attack : true,
        innings : 0,
        is_top : true,
        out_count : 0,
        base_info_array : [],
        my_score : [],
        your_score : [],
        hit_order : 0, 
        is_progress : true
      },
      score : {
        my_score_array : [],
        your_score_array : []
      },
      hit_info : {
        at_bat_count : 0,
        homerun_count : 0,
        hit1_count : 0,
        hit2_count : 0,
        hit3_count : 0,
        foul_count : 0
      },
      my_lineup_array : [],
    }
  },
  watch: {
    pageVal(newVal) {
      // 1: 0 to 5
      // 2: 5 to 10
      // 3: 10 to 15
      this.from = (newVal - 1) * 5
      let to = this.from + 5
      if(to > this.total) to = this.total;
      this.searchedPlayerListShowData = this.searchedPlayers.slice(this.from, to);

      this.searchedPlayerTableData = this.computeSearchedPlayerTableData();
    }
  },
  created() {
    if(this.$store.state.userInfo.id == undefined) {
      swal("경고", "로그인이 필요한 서비스입니다.", "warning");
      this.$router.push({name: "Login"});
      return;
    }

    // 라인업 리스트 가져오기
    PlayerAPI.getLineupList(
      "none=none",
      res => {
        this.lineupList = res;
        this.yourLineupList = res;
      },
      err => {
        console.log(err);
      }
    );

    this.teamStatData = this.computeTeamStatData();
    this.playerStatData = this.computePlayerStatData();
    this.searchedPlayerTableData = this.computeSearchedPlayerTableData();
  },
  methods: {
    changeLineup(id, name) {
      this.lineupId = id;
      this.lineupName = name;
      this.isNewLineup = false;

      PlayerAPI.getLineupPlayerWithTeamStat(
        "lineup=" + id,
        res => {
          this.lineupPlayers = res.playerList;
          this.teamStats = res.teamStat;

          // teamStats 에 team_id 가 포함되어있다 이거 빼야한다
          delete this.teamStats.team_id;

          this.teamStatData = this.computeTeamStatData();

          console.log(res);
        },
        err => {
          console.log(err);
        }
      );
    },
    changeYourLineup(id, name) {
      this.yourLineupId = id;
      this.yourLineupName = name;

      PlayerAPI.getLineupPlayerWithTeamStat(
        "lineup=" + id,
        res => {
          this.yourLineupPlayers = res.playerList;
          this.teamStats = res.teamStat;

          // teamStats 에 team_id 가 포함되어있다 이거 빼야한다
          delete this.teamStats.team_id;

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
        this.lineupSel = index;
        this.searchedSel = -1;
        
        this.getPlayerStat(this.lineupPlayers[index].player_id);
        this.playerName = this.lineupPlayers[index].player_name;
      }
    },
    clickSearchedPlayer(index) {
      if(this.searchedSel != index) {
        this.searchedSel = index;
        this.lineupSel = -1;

        this.getPlayerStat(this.searchedPlayerListShowData[index].player_id);
        this.playerName = this.searchedPlayerListShowData[index].player_name;
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
    computeSearchedPlayerTableData() {
      let arr = [];
      for(let player of this.searchedPlayerListShowData) {
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
    search() {
      // 검색할 선수 이름의 일부
      let searchText = this.searchVal;
      if(this.searchVal.length == 0) {
        searchText = null
      }
      console.log(searchText);

      // 포지션 필터링
      let cnt = 0;
      let positions = '';
      for(let i in this.positionFilter) {
        let v = this.positionFilter[i];
        if(!v) {
          positions += (i + ",");
          cnt += 1;
        }
      }
      if(cnt == 0) {
        swal("검색 오류!", "적어도 하나의 포지션은 선택되어야 합니다!", "warning");
        return;
      }
      positions = positions.slice(0, -1);
      console.log(positions);

      // 팀
      cnt = 0;
      let teams = '';
      for(let i in this.teamFilter) {
        let v = this.teamFilter[i];
        if(!v) {
          teams += (i + ",");
          cnt += 1;
        }
      }
      if(cnt == 0) {
        swal("검색 오류!", "적어도 하나의 팀은 선택되어야 합니다!", "warning");
        return;
      }
      teams = teams.slice(0, -1);
      console.log(teams);

      PlayerAPI.getPlayerList(
        {
          searchText: searchText,
          teams: teams,
          positions: positions
        },
        res => {
          this.searchedPlayers = res;
          this.resetSearchedPlayerTableData();
        },
        err => {
          console.log(err);
        }
      );
    },
    resetSearchedPlayerTableData() {
      this.total = this.searchedPlayers.length;
      this.from = 0;
      let to = 5;

      let v = this.total - 1;
      if(v < 0) v = 0;
      this.pageCount = parseInt(v / 5 + 1);
      this.pageVal = 1;

      if(to > this.total) to = this.total;

      this.searchedPlayerListShowData = this.searchedPlayers.slice(this.from, to);

      this.searchedPlayerTableData = this.computeSearchedPlayerTableData();
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
    },

    newLineUp() {
      this.lineupId = 100;
      this.lineupName = "새 라인업";
      this.isNewLineup = true;
      this.lineupPlayers = [];
    },
    addToLineup(idx) {
      // 라인업을 먼저 선택해야함
      if(this.lineupId == 0) {
        swal("경고", "라인업을 먼저 선택해주세요", "warning");
        return;
      }
      // 라인업이 꽉 찼으면 더 이상 추가 할 수 없음
      let lineupLen = this.lineupPlayers.length;
      if(lineupLen == 10) {
        swal("경고", "라인업이 꽉 찼습니다, 선수를 비워주세요", "warning");
        return;
      }
      
      // 선택된 선수 정보 가져오기
      let player = this.searchedPlayerListShowData[idx]
      let playerId = player.player_id;

      // 라인업에 이미 동일한 선수가 있다면 중복
      let check = true;
      for(let p of this.lineupPlayers) {
        if(p.player_id == playerId) {
          check = false;
          break;
        }
      }
      if(!check) {
        swal("경고", "이미 라인업에 있는 선수입니다, 다른 선수를 선택해주세요", "warning");
        return;
      }

      // 라인업에 추가
      this.lineupPlayers.push(this.searchedPlayerListShowData[idx]);
    },
    deleteFromLineup(idx) {
      this.lineupPlayers.splice(idx, 1);
    },

    // 시뮬레이션 
    clickStartBtn() {
      if(this.lineupPlayers.length==0){
        swal("경고", "라인업을 먼저 선택해주세요.", "warning");
        return;
      }
      if(this.yourLineupPlayers.length==0){
        swal("경고", "상대방 라인업을 먼저 선택해주세요.", "warning");
        return;
      }
      // 백에 게임 시작 api 전송
      let data = {};
      data.user_id = this.$store.state.userInfo.id;
      data.my_lineup_id = this.lineupId;
      data.your_lineup_id = this.yourLineupId;
      // data.is_attack = true;
      PlayerAPI.gameStart(
        data,
        res => {
          console.log(res);
          this.start = true;
          this.game = res.game;
          this.score = res.score;
          this.hit_info = res.hit_info;
          this.lineupId = this.game.my_lineup_id;
          this.yourLineupId = this.game.your_lineup_id;

          swal("성공", "게임을 시작합니다.", "success");
        },
        err => {
          swal('실패', '게임 시작에 실패햐였습니다.', 'error');
          console.log(err);
        }
      )
    },

    clickProgressBtn() {
      // 교체 선수 있으면 백에 전달
      PlayerAPI.gameProgress(
        {
          simulation_id : this.simulation_id,
          replaced_player : this.replaced_player,
        },
        res => {
          console.log(res);
          this.game = res.game;
          this.score = res.score;
          this.hit_info = res.hit_info;
          this.lineupId = this.game.my_lineup_id;
          this.yourLineupId = this.game.your_lineup_id;

          // swal("성공", "게임을 중단합니다.", "success");
        },
        err => {
          swal('실패', '게임 진행에 실패햐였습니다.', 'error');
          console.log(err);
        }
      )
    },

    clickEndBtn() {
      PlayerAPI.gameEnd(
        {
          simulation_id:this.simulationId
        },
        res => {
          console.log(res);
          // 초기화
          this.start = false;
          this.game = [],
          this.score = [],
          this.hit_info = [],
          this.simulation_id = 0,
          this.replaced_player = 0,
          swal("성공", "게임을 종료하였습니다.", "success");
        },
        err => {
          swal('실패', '게임 종료에 실패햐였습니다.', 'error');
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
.score_table th, .score_table td {
   border:1px solid black;
}
.inning_score {
  width: 20px;
}

.base_info {
  /* background-color: #071D6799; */
}

#diamond {
  width: 0;
  height: 0;
  border: 50px solid transparent;
  border-bottom-color: green;
  position: relative;
  top: -50px;
  /* margin: 18px; */
  left: 20px;
}
#diamond:after {
  content: '';
  position: absolute;
  left: -50px;
  top: 50px;
  width: 0;
  height: 0;
  border: 50px solid transparent;
  border-top-color:  green;
}
#base2 {
  width: 0;
  height: 0;
  border: 10px solid transparent;
  border-bottom-color: white;
  position: relative;
  top: -10px;
  right: 10px;
}
#base2:after {
  content: '';
  position: absolute;
  left: -10px;
  top: 10px;
  width: 0;
  height: 0;
  border: 10px solid transparent;
  border-top-color:  white;
}
#base1 {
  width: 0;
  height: 0;
  border: 10px solid transparent;
  border-bottom-color: white;
  position: relative;
  top: 10px;
  right: -30px;
  z-index: 1;
}
#base1:after {
  content: '';
  position: absolute;
  left: -10px;
  top: 10px;
  width: 0;
  height: 0;
  border: 10px solid transparent;
  border-top-color:  white;
}
#base3 {
  width: 0;
  height: 0;
  border: 10px solid transparent;
  border-bottom-color: white;
  position: relative;
  top: -10px;
  right: 50px;
  z-index: 1;
}
#base3:after {
  content: '';
  position: absolute;
  left: -10px;
  top: 10px;
  width: 0;
  height: 0;
  border: 10px solid transparent;
  border-top-color:  white;
}

#sbo {
  text-align: left;
  margin-top: 2px;
}

#sbo div {
  margin-top: 3px;
}

.circle_s {
  width: 15px;
  height: 15px;
  border-radius: 50%;
  background-color: yellow;
  margin-left: 10px;
}
.circle_b {
  width: 15px;
  height: 15px;
  border-radius: 50%;
  background-color: darkslateblue;
  margin-left: 10px;
}
.circle_o {
  width: 15px;
  height: 15px;
  border-radius: 50%;
  background-color: crimson;
  margin-left: 10px;
}

.gameBtn {
  position: absolute;
  right: 0;
  top: 30px;
}
</style>
