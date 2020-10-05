<template>
  <div>
    <!-- 헤더 -->
    <base-header type="translucent-default" class="pb-5 pt-5"></base-header>
    
    <!-- 1번째 행: 라인업과 그 라인업의 스탯 -->
    <div class="container-fluid mt-2 row">
      <!-- 1번째 열: 라인업 선택 드롭다운과 라인업의 선수 목록 -->
      <div class="col-lg mr-1 ml-1  text-center align-self-center">
        <div class="row pl-2 pr-3">
          <!-- 라인업 선택 드롭다운 -->
          <base-dropdown class="col">
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
            <span class="dropdown-item" @click="newLineUp">라인업 추가</span>
          </base-dropdown>
          <!-- 초기화, 저장 버튼 -->
          <!-- <base-button
            slot="title"
            type="secondary"
            class="ml-2"
            @click="resetLineup">
            초기화
          </base-button> -->
          <base-button
            slot="title"
            type="secondary"
            class="ml-2 col"
            @click="saveLineup">
            라인업 저장
          </base-button>
        </div>
        <div class="row mt-2 pl-2 pr-3">
          <base-button
            slot="title"
            type="secondary"
            class="ml-2 col"
            v-if="lineupId > 10 && lineupId < 100"
            @click="deleteLineup">
            라인업 삭제
          </base-button>
          <base-button
            slot="title"
            type="secondary"
            class="ml-2 col"
            v-if="lineupId > 10 && lineupId < 100"
            @click="changeLineupName">
            라인업 이름변경
          </base-button>
        </div>
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
                  <th></th>
                  <th v-for="column in tableColumns" :key="column">{{ column }}</th>
                  <th></th>
                </tr>
              </thead>
              <draggable v-model="lineupPlayers" tag="tbody">
                <tr v-for="(row, rowIdx) in lineupPlayers" :key="rowIdx">
                    <td class="text-red" @click="deleteFromLineup(rowIdx)"><i class="ni ni-fat-delete"></i></td>
                    <td @click="clickLineupPlayer(rowIdx)">{{atBat[rowIdx]}}</td>
                    <td @click="clickLineupPlayer(rowIdx)">{{row.player_name}}</td>
                    <td><i class="ni ni-align-center"></i></td>
                </tr>
              </draggable>
            </table>
          </div>
        </div>
        <!-- <custom-table
          class="custom-table mt-2"
          :tableTitle="lineupName"
          :tableData="lineupPlayerTableData"
          :cols="tableColumns"
          :selectedRow="lineupSel"
          @clickRow="clickLineupPlayer"
        /> -->
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

    <tabs fill class="flex-column flex-md-row container-fluid">
      <card shadow>
          <tab-pane title="searchPlayer">
              <span slot="title">
                  <i class="ni ni-cloud-upload-96" />
                  선수 검색
              </span>
              <div class="container-fluid mt-2 row">
                <card class="col ml-2 mr-2">
                  <form class="navbar-search form-inline" onsubmit="return false;">
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

                </card>
              </div>

              <!-- 3번째 행: 라인업에 추가할 선수 목록(검색) -->
              <div class="container-fluid mt-2 row">
                <div class="card custom-table col mr-2 ml-2">
                  <div class="card-header border-0">
                    <div class="row align-items-center">
                      <div class="col">
                        <h3 class="mb-0">검색된 선수 목록</h3>
                      </div>
                    </div>
                  </div>
                  <div class="table-responsive">
                    <table class="table tablesorter table-hover">
                      <thead class="thead-light">
                        <tr>
                          <th></th>
                          <th v-for="column in tableColumnsForSearch" :key="column">{{ column }}</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(row, rowIdx) in searchedPlayerTableData" :key="rowIdx">
                            <td class="text-blue" @click="addToLineup(rowIdx)"><i class="ni ni-fat-add"></i></td>
                            <td v-for="(val, valIdx) in row" :key="valIdx" @click="clickSearchedPlayer(rowIdx)">
                              {{val}}
                            </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
                <!-- <custom-table
                  class="custom-table col mr-2 ml-2"
                  tableTitle="검색된 선수 목록"
                  :tableData="searchedPlayerTableData"
                  :cols="tableColumnsForSearch"
                  :selectedRow="searchedSel"
                  @clickRow="clickSearchedPlayer"
                /> -->
              </div>
          </tab-pane>

          <tab-pane title="favoritePlayer">
              <span slot="title">
                  <i class="ni ni-bell-55 mr-2" />
                  즐겨찾기한 선수
              </span>
              <div class="container-fluid mt-2 row">
                <div class="card custom-table col mr-2 ml-2">
                  <div class="card-header border-0">
                    <div class="row align-items-center">
                      <div class="col">
                        <h3 class="mb-0">즐겨찾기 선수 목록</h3>
                      </div>
                    </div>
                  </div>
                  <div class="table-responsive">
                    <table class="table tablesorter table-hover">
                      <thead class="thead-light">
                        <tr>
                          <th></th>
                          <th v-for="column in tableColumnsForSearch" :key="column">{{ column }}</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(row, rowIdx) in searchedPlayerTableData" :key="rowIdx">
                            <td class="text-blue" @click="addToLineup(rowIdx)"><i class="ni ni-fat-add"></i></td>
                            <td v-for="(val, valIdx) in row" :key="valIdx" @click="clickSearchedPlayer(rowIdx)">
                              {{val}}
                            </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
                <!-- <custom-table
                  class="custom-table col mr-2 ml-2"
                  tableTitle="검색된 선수 목록"
                  :tableData="searchedPlayerTableData"
                  :cols="tableColumnsForSearch"
                  :selectedRow="searchedSel"
                  @clickRow="clickSearchedPlayer"
                /> -->
              </div>
          </tab-pane>
      </card>
    </tabs>

    <div class="container-fluid mt-1 row">
      <base-pagination
        class="col"
        :page-count="pageCount"
        v-model="pageVal"
        align="center"
      />
    </div>

  </div>
</template>

<script>
// Charts
import CustomRadarChart from "@/components/Player/CustomRadarChart";

// Alert
import swal from 'sweetalert';

// API
import PlayerAPI from "@/api/PlayerAPI";

// Drag and Drop
import draggable from 'vuedraggable'

export default {
  components: {
    CustomRadarChart,
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

      // 라인업 선수 목록
      lineupPlayers: [],

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
      lineupName: "라인업 선택",
      lineupId: 0,
      isNewLineup: false,

      // 라인업 선수 테이블 컬럼들
      tableColumns: [
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

      positionRenderKey: 0,
      teamRenderKey: 0
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
    resetLineup() {
      this.lineupPlayers = [];
    },
    saveLineup() {
      // 라인업이 선택되지 않았으면 아무 동작 안함
      if(this.lineupId <= 0) {
        return;
      }

      // 라인업이 비어있으면 경고
      if(this.lineupPlayers.length < 10) {
        swal("경고", "라인업에 선수가 부족합니다", "warning");
        return;
      }

      // 새 라인업 또는 기본 라인업에서 수정 => 다른이름으로 저장
      if(this.isNewLineup || this.lineupId <= 10) {
        this.saveAs();
      }
      // 전에 만들어놓았던 라인업 수정 => 저장
      else {
        this.modifyLineup();
      }
    },
    saveAs() {
      swal({
        text: '라인업 이름 입력',
        content: "input",
        button: {
          text: "저장",
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
          this.addLineup(name);
        }
      });
    },
    addLineup(name) {
      let arr = [];
      arr.push(name);

      for(let item of this.lineupPlayers) {
        arr.push(item.player_id);
      }

      PlayerAPI.addLineup(
        {
          user_id: this.$store.state.userInfo.id,
          lineup: arr
        },
        res => {
          console.log(res);
          swal('성공', '라인업 추가 성공!', 'success');

          // 라인업 리스트 가져와서 갱신하기
          PlayerAPI.getLineupList(
            "none=none",
            res => {
              this.lineupList = res;
            },
            err => {
              console.log(err);
            }
          );
        },
        err => {
          swal('실패', '라인업 추가 실패ㅠ', 'error');
          console.log(err);
        }
      )
    },
    modifyLineup() {
      let data = {};
      data.lineup_id = this.lineupId;
      data.user_id = this.$store.state.userInfo.id;

      data.hitter1 = this.lineupPlayers[0].player_id;
      data.hitter2 = this.lineupPlayers[1].player_id;
      data.hitter3 = this.lineupPlayers[2].player_id;
      data.hitter4 = this.lineupPlayers[3].player_id;
      data.hitter5 = this.lineupPlayers[4].player_id;
      data.hitter6 = this.lineupPlayers[5].player_id;
      data.hitter7 = this.lineupPlayers[6].player_id;
      data.hitter8 = this.lineupPlayers[7].player_id;
      data.hitter9 = this.lineupPlayers[8].player_id;
      data.pitcher = this.lineupPlayers[9].player_id;

      PlayerAPI.modifyLineup(
        data,
        res => {
          console.log(res);
          swal('성공', '라인업 추가 성공!', 'success');
        },
        err => {
          swal('실패', '라인업 저장 실패ㅠ', 'error');
          console.log(err);
        }
      )
    },
    changeLineupName() {
      swal({
        text: '수정할 이름 입력',
        content: "input",
        button: {
          text: "수정",
          closeModal: true,
        },
      })
      .then(name => {
        if (!name) {
          swal({
            title: "이름을 입력해주세요!",
            icon: "error"
          });
        } else {
          this.changeLIneupNameAsync(name);
        }
      });
    },
    changeLIneupNameAsync(name) {
      console.log('수정할 이름: ', name);
    },
    deleteLineup() {
      PlayerAPI.deleteLineup(
        'lineup=' + this.lineupId,
        res => {
          console.log(res);
          swal('성공', '라인업이 삭제되었습니다.', 'success');

          // Lineup 초기화
          this.lineupPlayers = [];
          this.lineupName = '라인업 선택';
          this.lineupId = 0;
          
          // 라인업 리스트 가져와서 갱신하기
          PlayerAPI.getLineupList(
            "none=none",
            res => {
              this.lineupList = res;
            },
            err => {
              console.log(err);
            }
          );
        },
        err => {
          swal('실패', '라인업 삭제 실패ㅠ', 'error');
          console.log(err);
        }
      )
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
    }
  }
};
</script>
<style scoped>
.custom-table {
  margin: auto;
}
</style>
