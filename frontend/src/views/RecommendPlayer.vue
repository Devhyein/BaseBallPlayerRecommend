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
                <tbody :key="lineupTableRenderKey">
                  <tr
                    v-for="(player, rowIdx) in lineupPlayers"
                    :key="rowIdx" 
                    :class="{'bg-translucent-warning': rowIdx == lineupSel,
                             'text-white': rowIdx == lineupSel}">
                      
                      <td :class="{'text-yellow': lineupPlayers[rowIdx].isFavorite}">
                        <i  class="fa fa-star" aria-hidden="true" @click="clickLineupFavorite(rowIdx)"></i>
                      </td>
                      
                      <td @click="clickLineupPlayer(rowIdx)">{{atBats[rowIdx]}}</td>
                      <td @click="clickLineupPlayer(rowIdx)">{{player.position}}</td>
                      <td @click="clickLineupPlayer(rowIdx)">{{player.player_name}}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <!-- <custom-table
            class="custom-table"
            :tableTitle="lineupName"
            :tableData="lineupPlayerTableData"
            :cols="tableColumns"
            :selectedRow="lineupSel"
            @clickRow="clickLineupPlayer"
          /> -->
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
          <tabs fill class="" @activateTab="activateTab">
            <tab-pane title="searchPlayer">
              <span slot="title">
                <i class="ni ni-cloud-upload-96" />
                팀에 필요한 선수
              </span>
              <div class="card custom-table mt-2">
                <div class="card-header border-0">
                  <div class="row align-items-center">
                    <div class="col">
                      <h3 class="mb-0">필요한 선수</h3>
                    </div>
                  </div>
                </div>
                <div class="table-responsive">
                  <table class="table tablesorter table-hover">
                    <thead class="thead-light">
                      <tr>
                        <th v-for="column in tableColumnsForRecommendAndRemoved" :key="column">{{ column }}</th>
                      </tr>
                    </thead>
                    <tbody :key="recommendTableRenderKey">
                      <tr
                        v-for="(player, rowIdx) in recommendPlayers"
                        :key="rowIdx" 
                        :class="{'bg-translucent-warning': rowIdx == recommendSel,
                                'text-white': rowIdx == recommendSel}">
                          
                          <td :class="{'text-yellow': recommendPlayers[rowIdx].isFavorite}">
                            <i  class="fa fa-star" aria-hidden="true" @click="clickRecommendFavorite(rowIdx)"></i>
                          </td>
                          
                          <td @click="clickRecommendPlayer(rowIdx)">{{player.position}}</td>
                          <td @click="clickRecommendPlayer(rowIdx)">{{player.player_name}}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </tab-pane>

            <tab-pane title="favoritePlayer">
              <span slot="title">
                <i class="ni ni-bell-55 mr-2" />
                비슷한 선수
              </span>
              <div class="card custom-table mt-2">
                <div class="card-header border-0">
                  <div class="row align-items-center">
                    <div class="col">
                      <h3 class="mb-0">유사한 선수</h3>
                    </div>
                  </div>
                </div>
                <div class="table-responsive">
                  <table class="table tablesorter table-hover">
                    <thead class="thead-light">
                      <tr>
                        <th v-for="column in tableColumnsForRecommendAndRemoved" :key="column">{{ column }}</th>
                      </tr>
                    </thead>
                    <tbody :key="similarTableRenderKey">
                      <tr
                        v-for="(player, rowIdx) in similarPlayerListShowData"
                        :key="rowIdx" 
                        :class="{'bg-translucent-warning': rowIdx == similarSel,
                                'text-white': rowIdx == similarSel}">
                          
                          <td :class="{'text-yellow': similarPlayerListShowData[rowIdx].isFavorite}">
                            <i  class="fa fa-star" aria-hidden="true" @click="clickSimilarFavorite(rowIdx)"></i>
                          </td>
                          
                          <td @click="clickSimilarPlayer(rowIdx)">{{player.position}}</td>
                          <td @click="clickSimilarPlayer(rowIdx)">{{player.player_name}}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
              <div>
                <base-pagination
                  :page-count="pageCount"
                  v-model="pageVal"
                  align="center"
                />
              </div>
            </tab-pane>
          </tabs>
        </div>
        <!-- 2. 선수 목록에서 제외된 선수 목록 테이블 -->
        <div class="row mt-2" v-if="removedPlayers.length > 0">
          <div class="card custom-table mt-2">
            <div class="card-header border-0">
              <div class="row align-items-center">
                <div class="col">
                  <h3 class="mb-0">제외된 선수</h3>
                </div>
              </div>
            </div>
            <div class="table-responsive">
              <table class="table tablesorter table-hover">
                <thead class="thead-light">
                  <tr>
                    <th v-for="column in tableColumnsForRecommendAndRemoved" :key="column">{{ column }}</th>
                  </tr>
                </thead>
                <tbody :key="removedTableRenderKey">
                  <tr
                    v-for="(player, rowIdx) in removedPlayers"
                    :key="rowIdx" 
                    :class="{'bg-translucent-warning': rowIdx == removedSel,
                             'text-white': rowIdx == removedSel}">
                      
                      <td :class="{'text-yellow': removedPlayers[rowIdx].isFavorite}">
                        <i  class="fa fa-star" aria-hidden="true" @click="clickRemovedFavorite(rowIdx)"></i>
                      </td>
                      
                      <td @click="clickRemovedPlayer(rowIdx)">{{player.position}}</td>
                      <td @click="clickRemovedPlayer(rowIdx)">{{player.player_name}}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <!-- <custom-table
            class="custom-table"
            tableTitle="제외된 선수"
            :tableData="removedPlayerTableData"
            :cols="tableColumnsForRecommendAndRemoved"
            :selectedRow="removedSel"
            @clickRow="clickRemovedPlayer"
          /> -->
        </div>
      </div>
    </div>

  </div>
</template>

<script>
// Charts
import CustomRadarChart from "@/components/Player/CustomRadarChart";

// Tables
// import CustomTable from "@/views/Tables/CustomTable";

// API
import PlayerAPI from "@/api/PlayerAPI";

// Alert
import swal from 'sweetalert';

export default {
  components: {
    CustomRadarChart,
    // CustomTable
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

      // 유사한 선수 목록
      similarPlayers: [],

      // 선수 목록에서 제외된 선수 목록
      removedPlayers: [],

      // 유사한 선수 페이지네이션을 위한 것
      similarPlayerListShowData: [],
      pageCount: 1,
      pageVal: 1,
      from: 0,
      total: 0,

      // 라인업과 추천선수 목록에서
      // 선택된 행을 기억하는 변수
      lineupSel: -1,
      recommendSel: -1,
      similarSel: -1,
      removedSel: -1,

      // 드롭다운으로 라인업 선택하는 동작을 위한 변수
      lineupName: "라인업 선택",
      lineupId: 0,

      // 라인업 선수 테이블 컬럼들
      tableColumns: [
        ""
        , "At bat"
        , "Position"
        , "Name"
      ],

      // 1~9 번 타자 + 투수
      atBats: [
        "1번 타자",
        "2번 타자",
        "3번 타자",
        "4번 타자",
        "5번 타자",
        "6번 타자",
        "7번 타자",
        "8번 타자",
        "9번 타자",
        "투수",
      ],

      // 추천선수, 제외된 선수 테이블 컬럼들
      tableColumnsForRecommendAndRemoved: [
        ""
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
      similarPlayersTableData: [],
      removedPlayerTableData: [],

      // 차트를 위한 데이터
      teamStatData: {},
      playerStatData: {},

      lineupTableRenderKey: 0,
      recommendTableRenderKey: 0,
      similarTableRenderKey: 0,
      removedTableRenderKey: 0,

      similarTab: false,
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
      this.similarPlayerListShowData = this.similarPlayers.slice(this.from, to);
    }
  },
  created() {
    if(this.$store.state.userInfo.id == undefined) {
      swal("경고", "로그인이 필요한 서비스입니다.", "warning");
      this.$router.push({name: "Login"});
      return;
    }

    PlayerAPI.getLineupList(
      "none=none",
      res => {
        this.lineupList = res;
        console.log(res);
      },
      err => {
        console.log(err);
      }
    );

    this.teamStatData = this.computeTeamStatData();
    this.playerStatData = this.computePlayerStatData();
  },
  methods: {
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

          ///////////////////////////////////////////////////////////////////////////////// 지울곳
          for(let i in this.recommendPlayers) {
            this.recommendPlayers[i].isFavorite = false;
          }
          for(let i in this.lineupPlayers) {
            this.lineupPlayers[i].isFavorite = false;
          }
          ////////////////////////////////////////////////////////////////////////////////////////

          this.teamStatData = this.computeTeamStatData();
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
          this.playerStats = res;
          this.playerStatData = this.computePlayerStatData();
        },
        err => {
          console.log(err);
        }
      )

      // 유사한 선수도 찾아줘야 한다면
      if(this.similarTab) {
        PlayerAPI.getSimilarPlayers(
          'player_id=' + id,
          res => {
            this.similarPlayers = res.recommendList;
            
            ///////////////////////////////////////////////////////////////////////////////// 지울곳
            for(let i in this.similarPlayers) {
              this.similarPlayers[i].isFavorite = false;
            }
            ////////////////////////////////////////////////////////////////////////////////////////

            this.resetSimilarPlayerShowData();
          },
          err => {
            console.log(err);
          }
        )
      }
    },
    resetSimilarPlayerShowData() {
      this.total = this.similarPlayers.length;
      this.from = 0;
      let to = 5;

      let v = this.total - 1;
      if(v < 0) v = 0;
      this.pageCount = parseInt(v / 5 + 1);
      this.pageVal = 1;

      if(to > this.total) to = this.total;

      this.similarPlayerListShowData = this.similarPlayers.slice(this.from, to);
    },

    clickLineupPlayer(index) {
      if(this.lineupSel != index) {
        this.lineupSel = index;
        
        this.getPlayerStat(this.lineupPlayers[index].player_id);
        this.playerName = this.lineupPlayers[index].player_name;
      }
    },
    clickRecommendPlayer(index) {
      if(this.recommendSel != index) {
        this.recommendSel = index;
        this.removedSel = -1;

        this.getPlayerStat(this.recommendPlayers[index].player_id);
        this.playerName = this.recommendPlayers[index].player_name;
      }
    },
    clickSimilarPlayer(index) {
      if(this.similarSel != index) {
        this.similarSel = index;
        this.removedSel = -1;

        // 이땐 오직 플레이어의 스탯만 가져오기
        PlayerAPI.getPlayerStat(
          'num=' + this.similarPlayerListShowData[index].player_id,
          res => {
            this.playerStats = res;
            this.playerStatData = this.computePlayerStatData();
          },
          err => {
            console.log(err);
          }
        )
        this.playerName = this.similarPlayerListShowData[index].player_name;
      }
    },
    clickRemovedPlayer(index) {
      if(this.removedSel != index) {
        this.removedSel = index;
        this.recommendSel = -1;
        this.similarSel = -1;

        this.getPlayerStat(this.removedPlayers[index].player_id);
        this.playerName = this.removedPlayers[index].player_name;
      }
    },
    changePlayer() {
      // 양쪽이 모두 선택되지 않은경우 그냥 반환
      if(this.lineupSel == -1 || 
          (this.recommendSel == -1 && this.removedSel == -1 && this.similarSel == -1)) {
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
      // 2. Similar 와 교환
      else if(this.similarSel != -1) {
        // 선택된 인덱스를 similarPlayers 의 해당 인덱스로 변환(계산)
        let similarIdx = this.from + this.similarSel;
        // 양쪽의 포지션이 다르면 교환 안함
        if(temp.position != this.similarPlayers[similarIdx].position) {
          alert('포지션이 다르면 교환이 안됩니다!');
          return;
        }

        this.similarPlayers[similarIdx].player_position = temp.player_position;
        this.lineupPlayers[this.lineupSel] = this.similarPlayers[similarIdx];
        this.similarPlayers.splice(similarIdx, 1);
        this.similarSel = -1;

        // similarPlayerShowData 에서 해당 데이터 빼고 다시 만들어주기
        this.resetSimilarPlayerShowData2();
      }
      // 3. removed 와 교환
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
    },
    resetSimilarPlayerShowData2() {
      this.total = this.similarPlayers.length;

      let to = this.from + 5
      if(to > this.total) to = this.total;
      this.similarPlayerListShowData = this.similarPlayers.slice(this.from, to);
    },

    activateTab(no) {
      console.log('Activate Tab No: ' + no);

      this.similarTab = (no == 1);
    },

    clickLineupFavorite(idx) {
      this.lineupPlayers[idx].isFavorite = !this.lineupPlayers[idx].isFavorite;
      this.lineupTableRenderKey += 1;
    },
    clickRecommendFavorite(idx) {
      this.recommendPlayers[idx].isFavorite = !this.recommendPlayers[idx].isFavorite;
      this.recommendTableRenderKey += 1;
    },
    clickSimilarFavorite(idx) {
      this.similarPlayers[idx].isFavorite = !this.similarPlayers[idx].isFavorite;
      this.similarTableRenderKey += 1;
    },
    clickRemovedFavorite(idx) {
      this.removedPlayers[idx].isFavorite = !this.removedPlayers[idx].isFavorite;
      this.removedTableRenderKey += 1;
    },
  }
};
</script>
<style scoped>
.custom-table {
  margin: auto;
}
</style>
