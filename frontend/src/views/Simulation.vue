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
                  <th v-for="column in tableColumns" :key="column">{{ column }}</th>
                </tr>
              </thead>
              <draggable v-model="yourLineupPlayers" tag="tbody">
                <tr v-for="(row, rowIdx) in yourLineupPlayers" :key="rowIdx">
                    <td>{{atBat[rowIdx]}}</td>
                    <td>{{row.player_name}}</td>
                </tr>
              </draggable>
            </table>
          </div>
        </div>
      </div>
      
      <!-- 가운데 -->
      <div class="col-xl mr-1 ml-1 text-center align-self-center">

        <!-- 현재 경기 score -->
        <div v-if="lineupId>0" style="margin-top:40px;">
          <div class="score">
            {{yourLineupName}}  {{your_total_score}} vs {{total_score}}  {{lineupName}}
          </div>
          <div class="play_info">
            {{game.innings}}회 
            <span v-if="game.is_top">초</span>
            <span v-else>말</span>
            {{game.out_count}}아웃  
            <span v-if="game.is_attack" style="margin-left:10px;">{{lineupName}}</span>
            <span v-else>{{yourLineupName}}</span>
            공격
          </div>
        </div>

        <!-- 경기 score table -->
        <div style="overflow-x:auto;">
          <table class="score_table" id="score_table" style="width:100%;margin-top: 25px; margin-bottom:20px;">
            <thead>
              <tr>
                <th class="col-2">TEAM</th>
                <th v-for="key in 12" :key="key" class="inning_score col">{{key}}</th>
                <!-- <th class="col">결과</th> -->
              </tr>
            </thead>
            <tbody>
              <tr style="border:1px solid black">
                <td>{{lineupName}}</td>
                <td v-for="(score,idx) in score.my_score_array" :key="idx">{{score}}</td>
                <!-- <td class="col">0</td> -->
              </tr>            
              <tr>
                <td>{{yourLineupName}}</td>
                <td v-for="(score,idx) in score.your_score_array" :key="idx">{{score}}</td>
                <!-- <td class="col">0</td> -->
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="lineupId>0" style="height:250px; margin-bottom:20px;">
          <img v-if="game.base_info_array[0]==0 && game.base_info_array[1]==0 && game.base_info_array[2]==0" src="/img/field/no.png" alt="경기장" style="height:250px; width: 100%;">
          <img v-if="game.base_info_array[0]==1 && game.base_info_array[1]==0 && game.base_info_array[2]==0" src="/img/field/base1.png" alt="경기장" style="height:250px; width: 100%;">
          <img v-if="game.base_info_array[0]==0 && game.base_info_array[1]==1 && game.base_info_array[2]==0" src="/img/field/base2.png" alt="경기장" style="height:250px; width: 100%;">
          <img v-if="game.base_info_array[0]==0 && game.base_info_array[1]==0 && game.base_info_array[2]==1" src="/img/field/base3.png" alt="경기장" style="height:250px; width: 100%;">
          <img v-if="game.base_info_array[0]==1 && game.base_info_array[1]==1 && game.base_info_array[2]==0" src="/img/field/base12.png" alt="경기장" style="height:250px; width: 100%;">
          <img v-if="game.base_info_array[0]==1 && game.base_info_array[1]==0 && game.base_info_array[2]==1" src="/img/field/base13.png" alt="경기장" style="height:250px; width: 100%;">
          <img v-if="game.base_info_array[0]==0 && game.base_info_array[1]==1 && game.base_info_array[2]==1" src="/img/field/base23.png" alt="경기장" style="height:250px; width: 100%;">
          <img v-if="game.base_info_array[0]==1 && game.base_info_array[1]==1 && game.base_info_array[2]==1" src="/img/field/base123.png" alt="경기장" style="height:250px; width: 100%;">
      
          <!-- 경기내용 <br/>
          000 선수가 1루로 진입했습니다. <br/>

          게임을 계속하시려면 진행을 교체하시려면 선수 검색을 해주세요. <br/> -->
        </div>

        <!-- 타석 타자 정보 -->
        <div style="font-size: 15px; text-align: left;"> 다음 타석 타자 정보 </div>
        <div v-if="lineupId>0" style="overflow-x:auto;height:100px; border:1px solid black; margin-bottom:20px;">
          <table style="width:100%;margin-top: 20px;">
            <thead>
              <tr>
                <th v-for="(column,idx) in hitterInfoColumns" :key="idx">{{ column }}</th>
              </tr>
            </thead>
            <tbody style="margin-top:5px;">
              <tr v-if="game.is_attack">
                <td>{{game.my_hit_order+1}}</td>
                <td>{{lineupPlayers[game.my_hit_order].player_name}}</td>
                <td>{{hit_info.hit1_count}}</td>
                <td>{{hit_info.hit2_count}}</td>
                <td>{{hit_info.hit3_count}}</td>
                <td>{{hit_info.homerun_count}}</td>
                <td>{{hit_info.foul_count}}</td>
              </tr>
              <tr v-else>
                <td>{{game.your_hit_order+1}}</td>
                <td>{{yourLineupPlayers[game.your_hit_order].player_name}}</td>
                <td>{{hit_info.hit1_count}}</td>
                <td>{{hit_info.hit2_count}}</td>
                <td>{{hit_info.hit3_count}}</td>
                <td>{{hit_info.homerun_count}}</td>
                <td>{{hit_info.foul_count}}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 경기 시작 버튼 -->
        <div v-if="lineupId>0">
          <div v-if="!start" class="gameBtn">
            <base-button type="primary" @click="clickStartBtn">게임시작</base-button>
          </div>

          <div v-if="start" class="gameBtn"> 
            <base-button type="primary" v-if="game.game_status" @click="clickProgressBtn">게임진행</base-button>
            <base-button type="primary" v-if="game.game_status" @click="clickEndBtn">게임종료</base-button>
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
              <template v-if="lineupPlayers.length>1">
                <draggable v-model="lineupPlayers" tag="tbody">
                  <tr v-for="(row, rowIdx) in lineupPlayers" :key="rowIdx">
                      <td>{{atBat[rowIdx]}}</td>
                      <td>{{row.player_name}}</td>
                  </tr>
                </draggable>
              </template>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- loading modal -->
    <loading :active.sync="isLoading"
        loader="bars"
        color="#007bff"
        :height="128"
        :width="128"
        :can-cancel="false" 
        :is-full-page="true"></loading>

  </div>
</template>

<script>

// Alert
import swal from 'sweetalert';

// API
import PlayerAPI from "@/api/PlayerAPI";

// Drag and Drop
import draggable from 'vuedraggable'

// Loading
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/vue-loading.css';

export default {
  components: {
    draggable,
    Loading
  },
  data() {
    return {
      isLoading: false,
      // 라인업 리스트
      lineupList: [],
      yourLineupList: [],

      // 라인업 선수 목록
      lineupPlayers: [{
        player_name : ""
      }],
      yourLineupPlayers: [],

      // 드롭다운으로 라인업 선택하는 동작을 위한 변수
      lineupName: "나의 라인업 선택",
      lineupId: 0,
      yourLineupName: "상대 라인업 선택",
      yourLineupId: 0,

      // 라인업 선수 테이블 컬럼들
      tableColumns: [
        "타순"
        , "이름"
      ],     
      // 상대 라인업 선수 테이블 컬럼들
      yourTableColumns: [
        "타순"
        , "이름"
      ],

      // 테이블을 위한 데이터
      atBat: [
        '1번 타자', '2번 타자', '3번 타자', '4번 타자', '5번 타자',
        '6번 타자', '7번 타자', '8번 타자', '9번 타자', '투수'
      ],

      // 시뮬레이션 관련 
      start : false,
      simulation_id : 0,
      hitterInfoColumns: [
        "타순","이름","타석","1루타","2루타","3루타","홈런","파울"
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
        base_info_array : [0,0,0],
        my_score : [],
        your_score : [],
        my_hit_order : 0, 
        your_hit_order : 0, 
        game_status : false
      },
      score : {
        my_score_array : [0,0,0,0,0,0,0,0,0,0,0,0],
        your_score_array : [0,0,0,0,0,0,0,0,0,0,0,0]
      },
      hit_info : {
        at_bat_count : 0,
        homerun_count : 0,
        hit1_count : 0,
        hit2_count : 0,
        hit3_count : 0,
        foul_count : 0
      },
      // my_lineup_array : [],
      total_score : 0,
      your_total_score : 0,
      // teamStatData : [],
      // playerStatData : [],
      // searchedPlayerTableData : []
    }
  },

  created() {
    if(this.$store.state.userInfo.id == undefined) {
      swal("경고", "로그인이 필요한 서비스입니다.", "warning");
      this.$router.push({name: "Login"});
      return;
    }
    
    this.isLoading = true;
    // 라인업 리스트 가져오기
    PlayerAPI.getLineupList(
      "none=none",
      res => {
        this.lineupList = res.lineupList;
        this.yourLineupList = res.lineupList;

        this.isLoading = false;
      },
      err => {
        console.log(err);
        this.isLoading = false;
      }
    );

    // this.teamStatData = this.computeTeamStatData();
    // this.playerStatData = this.computePlayerStatData();
    // this.searchedPlayerTableData = this.computeSearchedPlayerTableData();
  },
  methods: {
    changeLineup(id, name) {
      this.lineupId = id;
      this.lineupName = name;
      this.isNewLineup = false;

      this.isLoading = true;
      PlayerAPI.getLineupPlayerWithTeamStat(
        "lineup=" + id,
        res => {
          this.lineupPlayers = res.playerList;
          this.teamStats = res.teamStat;

          // teamStats 에 team_id 가 포함되어있다 이거 빼야한다
          delete this.teamStats.team_id;

          // this.teamStatData = this.computeTeamStatData();

          console.log(res);
          this.isLoading = false;
        },
        err => {
          console.log(err);
          this.isLoading = false;
        }
      );
    },
    changeYourLineup(id, name) {
      this.yourLineupId = id;
      this.yourLineupName = name;

      this.isLoading = true;
      PlayerAPI.getLineupPlayerWithTeamStat(
        "lineup=" + id,
        res => {
          this.yourLineupPlayers = res.playerList;
          this.teamStats = res.teamStat;

          // teamStats 에 team_id 가 포함되어있다 이거 빼야한다
          delete this.teamStats.team_id;

          // this.teamStatData = this.computeTeamStatData();

          console.log(res);

          this.isLoading = false;
        },
        err => {
          console.log(err);
          this.isLoading = false;
        }
      );
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
      data.is_attack = true;

      this.isLoading = true;
      PlayerAPI.gameStart(
        data,
        res => {
          console.log(res);
          this.start = true;
          this.game = res.simulation.game;
          this.score = res.simulation.score;
          this.hit_info = res.simulation.hit_info;
          this.lineupId = this.game.my_lineup_id;
          this.yourLineupId = this.game.your_lineup_id;
          this.score.total_score = 0;
          this.score.your_total_score = 0;
          for (let i=0; i < this.score.my_score_array; i++) {
            this.total_score += this.score.my_score_array[i];
            this.your_total_score += this.score.your_score_array[i];
          }

          this.isLoading = false;
          swal("성공", "게임을 시작합니다.", "success");
        },
        err => {
          swal('실패', '게임 시작에 실패햐였습니다.', 'error');
          console.log(err);
          this.isLoading = false;
        },

      )
    },
    clickProgressBtn() {
      this.isLoading = true;
      PlayerAPI.gameProgress(
        {
          simulation_id : this.simulation_id
        },
        res => {
          console.log(res);
          this.game = res.simulation.game;
          this.score = res.simulation.score;
          this.hit_info = res.simulation.hit_info;
          this.lineupId = this.game.my_lineup_id;
          this.yourLineupId = this.game.your_lineup_id;

          this.score.total_score = 0;
          this.score.your_total_score = 0;
          for (let i=0; i < this.score.my_score_array; i++) {
            this.total_score += this.score.my_score_array[i];
            this.your_total_score += this.score.your_score_array[i];
          }
          // swal("성공", "게임을 중단합니다.", "success");
          this.isLoading = false;
        },
        err => {
          swal('실패', '게임 진행에 실패햐였습니다.', 'error');
          console.log(err);
          this.isLoading = false;
        }
      )
    },

    clickEndBtn() {
      this.isLoading = true;
      
      PlayerAPI.gameEnd(
        "simulation_id=" + this.simulation_id,
        res => {
          console.log(res);
          // 초기화
          this.start = false;
          this.game = [],
          this.score = [],
          this.hit_info = [],
          this.simulation_id = 0
          swal("성공", "게임을 종료하였습니다.", "success");
          this.isLoading = false;
        },
        err => {
          swal('실패', '게임 종료에 실패햐였습니다.', 'error');
          console.log(err);
          this.isLoading = false;
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


#sbo {
  text-align: left;
  margin-top: 2px;
}

#sbo div {
  margin-top: 3px;
}

.gameBtn {
  text-align: right;
}

.score {
  font-size: 25px;
}
.play_info {
  font-size: 17px;
  margin-top: 10px;
}
</style>
