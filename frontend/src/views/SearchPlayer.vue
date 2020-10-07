<template>
  <div>
    <base-header type="translucent-default" class="pb-5 pt-5"></base-header>

    <!--Content-->
    <div class="container-fluid mt-4">
      <!--Search form-->
        <form
          class="navbar-search form-inline"
          onsubmit="return false;">
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
                <h5> [ 주황색: 선택됨 ] </h5>
            </template>
            <div :key="positionRenderKey">
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
                <base-button type="secondary" @click="allCanclePosition">전체취소</base-button>
                <base-button type="secondary" @click="allChoicePosition">전체선택</base-button>
                <base-button type="secondary" @click="modals.position = false">완료</base-button>
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
                <h5> [ 주황색: 선택됨 ] </h5>
            </template>
            <div :key="teamRenderKey">
              <base-button class="mb-2" :type="teamFilter[1] ? 'secondary' : 'warning'" @click="changeTeamFilter(1)">KIA 타이거즈</base-button>
              <!-- <base-button class="mb-2" :type="teamFilter[2] ? 'secondary' : 'warning'" @click="changeTeamFilter(2)">해태 타이거즈</base-button> -->
              <base-button class="mb-2" :type="teamFilter[3] ? 'secondary' : 'warning'" @click="changeTeamFilter(3)">삼성 라이온즈</base-button>
              <base-button class="mb-2" :type="teamFilter[4] ? 'secondary' : 'warning'" @click="changeTeamFilter(4)">두산 베어스</base-button>
              <!-- <base-button class="mb-2" :type="teamFilter[5] ? 'secondary' : 'warning'" @click="changeTeamFilter(5)">OB 베어스</base-button> -->
              <base-button class="mb-2" :type="teamFilter[6] ? 'secondary' : 'warning'" @click="changeTeamFilter(6)">SK 와이번스</base-button>
              <!-- <base-button class="mb-2" :type="teamFilter[7] ? 'secondary' : 'warning'" @click="changeTeamFilter(7)">현대 유니콘스</base-button> -->
              <!-- <base-button class="mb-2" :type="teamFilter[8] ? 'secondary' : 'warning'" @click="changeTeamFilter(8)">태평양 돌핀스</base-button> -->
              <!-- <base-button class="mb-2" :type="teamFilter[9] ? 'secondary' : 'warning'" @click="changeTeamFilter(9)">청보 핀토스</base-button> -->
              <!-- <base-button class="mb-2" :type="teamFilter[10] ? 'secondary' : 'warning'" @click="changeTeamFilter(10)">삼미 슈퍼스타즈</base-button> -->
              <base-button class="mb-2" :type="teamFilter[11] ? 'secondary' : 'warning'" @click="changeTeamFilter(11)">LG 트윈스</base-button>
              <!-- <base-button class="mb-2" :type="teamFilter[12] ? 'secondary' : 'warning'" @click="changeTeamFilter(12)">MBC 청룡</base-button> -->
              <base-button class="mb-2" :type="teamFilter[13] ? 'secondary' : 'warning'" @click="changeTeamFilter(13)">롯데 자이언츠</base-button>
              <base-button class="mb-2" :type="teamFilter[14] ? 'secondary' : 'warning'" @click="changeTeamFilter(14)">한화 이글스</base-button>
              <!-- <base-button class="mb-2" :type="teamFilter[15] ? 'secondary' : 'warning'" @click="changeTeamFilter(15)">빙그레 이글스</base-button> -->
              <base-button class="mb-2" :type="teamFilter[16] ? 'secondary' : 'warning'" @click="changeTeamFilter(16)">NC 다이노스</base-button>
              <!-- <base-button class="mb-2" :type="teamFilter[17] ? 'secondary' : 'warning'" @click="changeTeamFilter(17)">히어로즈</base-button> -->
              <!-- <base-button class="mb-2" :type="teamFilter[18] ? 'secondary' : 'warning'" @click="changeTeamFilter(18)">넥센 히어로즈</base-button> -->
              <base-button class="mb-2" :type="teamFilter[19] ? 'secondary' : 'warning'" @click="changeTeamFilter(19)">키움 히어로즈</base-button>
              <!-- <base-button class="mb-2" :type="teamFilter[20] ? 'secondary' : 'warning'" @click="changeTeamFilter(20)">쌍방울 레이더스</base-button> -->
              <base-button class="mb-2" :type="teamFilter[21] ? 'secondary' : 'warning'" @click="changeTeamFilter(21)">KT 위즈</base-button>
            </div>
            <template slot="footer">
                <base-button type="secondary" @click="allCancleTeam">전체취소</base-button>
                <base-button type="secondary" @click="allChoiceTeam">전체선택</base-button>
                <base-button type="secondary" @click="modals.team = false">완료</base-button>
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
      <!--End search form-->

      <!--Table-->
      <div class="row mt-5">
        <div class="col mb-5">
          <div class="card custom-table mt-2">
            <div class="card-header border-0">
              <div class="row align-items-center">
                <div class="col">
                  <h3 class="mb-0">Player List</h3>
                </div>
              </div>
            </div>
            <div class="table-responsive">
              <table class="table tablesorter table-hover">
                <thead class="thead-light">
                  <tr>
                    <th v-for="column in playerListTableCols" :key="column">{{ column }}</th>
                  </tr>
                </thead>
                <tbody :key="tableRenderKey">
                  <tr v-for="(player, rowIdx) in playerListShowData" :key="rowIdx">
                      <td :class="{'text-yellow': playerListShowData[rowIdx].isFavorite}">
                        <i  class="fa fa-star" aria-hidden="true" @click="clickFavorite(rowIdx)"></i>
                      </td>
                      
                      <td @click="selectPlayer(rowIdx)">{{player.player_name}}</td>
                      <td @click="selectPlayer(rowIdx)">{{player.player_team}}</td>
                      <td @click="selectPlayer(rowIdx)">{{player.position}}</td>
                      <td @click="selectPlayer(rowIdx)">{{player.player_age}}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <!-- <custom-table
            tableTitle="Player List"
            :tableData="playerListTableData"
            :cols="playerListTableCols"
            @clickRow="selectPlayer"
          /> -->
          <div>
            <base-pagination
              :page-count="pageCount"
              v-model="pageVal"
              align="center"
            />
          </div>
        </div>
      </div>
      <!--End table-->

      <!--Charts-->
      <div class="row">
        <!-- 5Tool Chart -->
        <div class="col-xl-4 mb-5 mb-xl-0">
          <custom-radar-chart
            title="Player stat"
            :subTitle="playerName"
            :data="statRadarData"
            :type="chartType" />
        </div>

        <!--Bar Chart-->
        <div class="col-xl-8">
          <player-stat-chart :cols="cols" :vals="vals" :originVals="originVals" :type="chartType"/>
        </div>
      </div>
      <!-- End charts-->

      <!--Table-->
      <div class="row mt-5">
        <div class="col-xl-12 mb-5">
          <player-stat-table :cols="tableCols" :tableData="statTableData" />
        </div>
      </div>
      <!--End table-->
    </div>

    <!-- loading modal -->
    <loading :active.sync="modals.loading"
        loader="bars"
        color="#007bff"
        :height="128"
        :width="128"
        :can-cancel="false" 
        :is-full-page="true"></loading>
  </div>
</template>

<script>
// Charts
import PlayerStatChart from "@/components/Player/PlayerStatChart";
import CustomRadarChart from "@/components/Player/CustomRadarChart";

// Tables
// import CustomTable from "@/views/Tables/CustomTable";
import PlayerStatTable from "./Tables/PlayerStatTable";

// API
import PlayerAPI from "@/api/PlayerAPI";

// Alert
import swal from 'sweetalert';

// Loading
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/vue-loading.css';

export default {
  components: {
    PlayerStatChart,
    CustomRadarChart,

    // CustomTable,
    PlayerStatTable,

    Loading,
  },
  data() {
    return {
      ////////////////////////////////////////////////////////////////////////////
      playerList: [],
      playerListTableCols: [
        ""
        , "Name"
        , "Team"
        , "Position"
        , "Age"
      ],

      playerListShowData: [],
      pageCount: 1,
      pageVal: 1,
      from: 0,
      total: 0,

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
      ////////////////////////////////////////////////////////////////////////////

      searchType: "Name",
      searchVal: "",

      playerName: "Select player",

      chartType: "secondary",

      // 필터링에 쓰일 modal 을 위한 내용
      modals: {
        position: false,
        team: false,
        loading: false,
      },
      // 포지션 필터링용 리스트
      positionFilter: [false,
        false, false, false, false, false, false, false, false, false, false],
      // 팀 필터링용 리스트
      teamFilter: [false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false],
      
      positionRenderKey: 0,
      teamRenderKey: 0,

      tableRenderKey: 0,
    }
  },
  created() {
    if(this.$store.state.userInfo.id == undefined) {
      swal("경고", "로그인이 필요한 서비스입니다.", "warning");
      this.$router.push({name: "Login"});
      return;
    }
  },
  computed: {
    cols() {
      let arr = [];

      for (let stat of this.playerStats.stats) {
        arr.push(stat.stat_name);
      }
      return arr;
    },
    tableCols() {
      let arr = ['항목'];
      for(let c of this.cols) {
        arr.push(c);
      }
      return arr;
    },
    vals() {
      let arr = [];

      for (let stat of this.playerStats.stats) {
        arr.push(stat.stat_std);
      }
      return arr;
    },
    originVals() {
      let arr = [];

      for (let stat of this.playerStats.stats) {
        arr.push(stat.stat_value);
      }
      return arr;
    },
    playerListTableData() {
      let arr = [];
      for(let player of this.playerListShowData) {
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
    statTableData() {
      let arr = [];

      let arr2 = ['값'];
      for(let v of this.originVals) {
        arr2.push(v);
      }
      arr.push(arr2);

      let arr3 = ['상대 값'];
      for(let v of this.vals) {
        arr3.push(v);
      }
      arr.push(arr3);

      return arr;
    },
    statRadarData() {
      let obj = {};
      let labels = [];
      let data = [];

      labels = Object.keys(this.playerStats.five_tool);
      obj.label = labels;
      
      for(let key of labels) {
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
      this.playerListShowData = this.playerList.slice(this.from, to);
    }
  },
  methods: {
    changeSearchType(t) {
      this.searchType = t;
    },
    search() {
      // 검색할 선수 이름의 일부
      let searchText = this.searchVal;
      if(this.searchVal.length == 0) {
        searchText = null
      }

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

      this.modals.loading = true;
      PlayerAPI.getPlayerList(
        {
          searchText: searchText,
          teams: teams,
          positions: positions
        },
        res => {
          this.playerList = res.playerList;
          this.resetPlayerListTable();
          this.modals.loading = false;
        },
        err => {
          console.log(err);
          this.modals.loading = false;

          if(err.msg == 'NoToken') {
            swal("경고", "세션만료! 다시 로그인 해주세요!", "warning");
            this.$store.commit('deleteUserInfo');
            this.$router.push({ name: "Login" });
          }
        }
      )
    },
    resetPlayerListTable() {
      this.total = this.playerList.length;
      this.from = 0;
      let to = 5;

      let v = this.total - 1;
      if(v < 0) v = 0;
      this.pageCount = parseInt(v / 5 + 1);
      this.pageVal = 1;

      if(to > this.total) to = this.total;

      this.playerListShowData = this.playerList.slice(this.from, to);
    },
    selectPlayer(index) {
      console.log(this.playerListShowData[index])
      this.playerName = this.playerListShowData[index].player_name;

      PlayerAPI.getPlayerStat(
        'num=' + this.playerListShowData[index].player_id,
        res => {
          if(res == null) {
            alert("선수의 스탯 정보가 없습니다!");
            res = {
              five_tool: {
                power: 0,
                speed: 0,
                contact: 0,
                defense: 0,
                shoulder: 0,
              },
              stats: [],
            };
          }

          this.playerStats = res;
        },
        err => {
          console.log(err);

          if(err.msg == 'NoToken') {
            swal("경고", "세션만료! 다시 로그인 해주세요!", "warning");
            this.$store.commit('deleteUserInfo');
            this.$router.push({ name: "Login" });
          }
        }
      )
    },

    changePositionFilter(idx) {
      this.positionFilter[idx] = !this.positionFilter[idx];
      this.positionRenderKey += 1;
    },
    changeTeamFilter(idx) {
      this.teamFilter[idx] = !this.teamFilter[idx];
      this.teamRenderKey += 1;
    },

    allCanclePosition() {
      for(let i in this.positionFilter) {
        this.positionFilter[i] = true;
      }
      this.positionRenderKey += 1;
    },
    allChoicePosition() {
      for(let i in this.positionFilter) {
        this.positionFilter[i] = false;
      }
      this.positionRenderKey += 1;
    },
    allCancleTeam() {
      for(let i in this.teamFilter) {
        this.teamFilter[i] = true;
      }
      this.teamRenderKey += 1;
    },
    allChoiceTeam() {
      for(let i in this.teamFilter) {
        this.teamFilter[i] = false;
      }
      this.teamRenderKey += 1;
    },

    clickFavorite(idx) {
      this.playerListShowData[idx].isFavorite = !this.playerListShowData[idx].isFavorite;
      this.tableRenderKey += 1;

      this.modifyFavorite(this.playerListShowData[idx].player_id, this.playerListShowData[idx].isFavorite);
    },
    modifyFavorite(id, s) {
      // 로그인이 안되어있다면 동작하지 않음
      if(this.$store.state.userInfo.id == undefined) {
        swal("경고", "로그인이 필요한 서비스입니다.", "warning");
        return;
      }

      // 별에 불들어왔다면 즐겨찾기에 추가
      if(s) {
        PlayerAPI.addFavorite(
          {
            player_id: id
          },
          res => {
            console.log('add favorites success', res);
          },
          err => {
            console.log(err);
            if(err.msg == 'NoToken') {
              swal("경고", "세션만료! 다시 로그인 해주세요!", "warning");
              this.$store.commit('deleteUserInfo');
              this.$router.push({ name: "Login" });
            }
          }
        )
      }
      // 별에 불 꺼졌다면 즐겨찾기에서 제거
      else {
        PlayerAPI.deleteFavorite(
          'player_id=' + id,
          res => {
            console.log('delete favorites success', res);
          },
          err => {
            console.log(err);
            if(err.msg == 'NoToken') {
              swal("경고", "세션만료! 다시 로그인 해주세요!", "warning");
              this.$store.commit('deleteUserInfo');
              this.$router.push({ name: "Login" });
            }
          }
        )
      }
    }
  },
  mounted() {},
};
</script>
<style></style>
