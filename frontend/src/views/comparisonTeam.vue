<template>
  <div>
    <base-header type="translucent-default" class="pb-5 pt-5"></base-header>

    <div class="container-fluid mt-2 row">
      <div class="col mr-1 ml-1 text-center align-self-center">
        <!-- 상대 구단 라인업 -->
        <base-dropdown>
          <base-button slot="title" type="secondary" class="dropdown-toggle">{{
            lineup
          }}</base-button>
          <template v-for="lineup in lineupList">
            <span
              :key="lineup.id"
              class="dropdown-item"
              @click="changeLineup(lineup.id, lineup.name)"
              >{{ lineup.name }}</span
            >
          </template>
        </base-dropdown>
      </div>
      <div class="col text-center align-self-center">
        <h3>팀 스탯 정보</h3>
      </div>
      <div class="col mr-1 ml-1 text-center align-self-center">
        <base-dropdown>
          <base-button slot="title" type="secondary" class="dropdown-toggle">{{
            userLineup
          }}</base-button>
          <template v-for="MyLineup in MyLineupList">
            <span
              :key="MyLineup.id"
              class="dropdown-item"
              @click="changeMyLineup(MyLineup.id, MyLineup.name)"
              >{{ MyLineup.name }}</span
            >
          </template>
        </base-dropdown>
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
        <!-- 구단 스탯 그래프 -->
        <div class="row">
          <custom-radar-chart
            class="col"
            title="Team Stat"
            :subTitle="lineupName"
            :data="CommonTeamStatData"
            :type="chartType"
          />
        </div>
        <!-- 구단에 대한 설명 -->
        <div class="row mt-2">
          <div class="col">
            <team-comparison-table title="구단 특징" :tableData="compareTableData"></team-comparison-table>
          </div>
        </div>
      </div>

      <!-- right -->
      <div class="col-xl mr-1 ml-1">
        <!-- 선택된 라인업 선수 목록 -->
        <div class="row">
          <custom-table
            class="custom-table"
            :tableTitle="MyLineupName"
            :tableData="MyLineupPlayerTableData"
            :cols="tableColumns"
            :selectedRow="MyLineupSel"
            @clickRow="clickMyLineupPlayer"
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
import TeamComparisonTable from "@/views/Tables/TeamComparisonTable";

// API
import PlayerAPI from "@/api/PlayerAPI";

// Alert
import swal from 'sweetalert';

export default {
  components: {
    CustomRadarChart,
    CustomTable,
    TeamComparisonTable,
  },
  data() {
    return {
      // 나의팀 스탯
      MyTeamStats: {
        era: 0,
        health: 0,
        control: 0,
        stability: 0,
        deterrent: 0,
        power: 0,
        speed: 0,
        contact: 0,
        defense: 0,
        shoulder: 0,
      },

      // 팀 스탯
      teamStats: {
        era: 0,
        health: 0,
        control: 0,
        stability: 0,
        deterrent: 0,
        power: 0,
        speed: 0,
        contact: 0,
        defense: 0,
        shoulder: 0,
      },

      // 나의 플레이어 스탯
      MyPlayerStats: {
        five_tool: {
          power: 0,
          speed: 0,
          contact: 0,
          defense: 0,
          shoulder: 0,
        },
        stats: [],
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
      MyLineupList: [],
      lineupList: [],
      isModifiedMyTeamStat: false,

      // 라인업 선수 목록
      MyLineupPlayers: [],
      lineupPlayers: [],
      isModifiedTeamStat: false,

      // 라인업과 추천선수 목록에서
      // 선택된 행을 기억하는 변수
      MyLineupSel: -1,
      lineupSel: -1,

      // 드롭다운으로 라인업 선택하는 동작을 위한 변수
      MyLineupName: "나의 라인업 선택",
      MyLineupId: 0,
      lineupName: "상대 라인업 선택",
      lineupId: 0,

      // 라인업 선수 테이블 컬럼들
      tableColumns: ["At bat", "Position", "Name"],
      MytableColumns: ["At bat", "Position", "Name"],

      compareTableData: [],

      // 선택한 선수의 이름 저장(스탯 보여주기 용)
      playerName: "Select player",
      MyplayerName: "Select player",

      // 그래프 타입(배경 색)
      chartType: "secondary",

      // 테이블을 위한 데이터
      lineupPlayerTableData: [],
      MyLineupPlayerTableData: [],

      // 차트를 위한 데이터
      MyTeamStatData: {},
      MyPlayerStatData: {},
      teamStatData: {},
      playerStatData: {},
      CommonTeamStatData: {},

      // 특징을 위한 데이터
      comparisonContent: {
        Dif: {
          eraDif: 0,
          healthDif: 0,
          controlDif: 0,
          stabilityDif: 0,
          deterrentDif: 0,
          powerDif: 0,
          speedDif: 0,
          contactDif: 0,
          defenseDif: 0,
          shoulderDif: 0,
        },
        absoluteValue: {
          era: 0,
          health: 0,
          control: 0,
          stability: 0,
          deterrent: 0,
          power: 0,
          speed: 0,
          contact: 0,
          defense: 0,
          shoulder: 0,
        },
      },
    };
  },
  created() {
    if(this.$store.state.userInfo.user_id == undefined) {
      swal("경고", "로그인이 필요한 서비스입니다.", "warning");
      this.$router.push({name: "Login"});
      return;
    }

    PlayerAPI.getLineupList(
      "user_id=" + this.$store.state.userInfo.user_id,
      (res) => {
        this.lineupList = res;
        this.MyLineupList = res;
      },
      (err) => {
        console.log(err);
      }
    );

    this.MyTeamStatData = this.computeMyTeamStatData();
    this.MyPlayerStatData = this.computeMyPlayerStatData();
    this.teamStatData = this.computeTeamStatData();
    this.playerStatData = this.computePlayerStatData();
    this.comparisonContent = {
      Dif: {
        eraDif: this.MyTeamStatData.era - this.teamStatData.era,
        healthDif: this.MyTeamStatData.health - this.teamStatData.health,
        controlDif: this.MyTeamStatData.control - this.teamStatData.control,
        stabilityDif:
          this.MyTeamStatData.stability - this.teamStatData.stability,
        deterrentDif:
          this.MyTeamStatData.deterrent - this.teamStatData.deterrent,
        powerDif: this.MyTeamStatData.power - this.teamStatData.power,
        speedDif: this.MyTeamStatData.speed - this.teamStatData.speed,
        contactDif: this.MyTeamStatData.contact - this.teamStatData.contact,
        defenseDif: this.MyTeamStatData.defense - this.teamStatData.defense,
        shoulderDif: this.MyTeamStatData.shoulder - this.teamStatData.shoulder,
      },
      absoluteValue: {
        era: Math.abs(this.comparisonContent.Dif.eraDif),
        health: Math.abs(this.comparisonContent.Dif.healthDif),
        control: Math.abs(this.comparisonContent.Dif.controlDif),
        stability: Math.abs(this.comparisonContent.Dif.stabilityDif),
        deterrent: Math.abs(this.comparisonContent.Dif.deterrentDif),
        power: Math.abs(this.comparisonContent.Dif.powerDif),
        speed: Math.abs(this.comparisonContent.Dif.speedDif),
        contact: Math.abs(this.comparisonContent.Dif.controlDif),
        defense: Math.abs(this.comparisonContent.Dif.defenseDif),
        shoulder: Math.abs(this.comparisonContent.Dif.shoulderDif),
      },
    };
    console.log(this.comparisonContent);
  },

  methods: {
    // sortArrays(arrays) {
    //   return this.arrays.sort((a, b) => a - b );
    // },
    computeLineupPlayerTableData() {
      let arr = [];
      for (let player of this.lineupPlayers) {
        let atBat = player.player_position + "번 타자";
        if (player.player_position == 10) {
          atBat = "투수";
        }

        arr.push([atBat, player.position, player.player_name]);
      }
      return arr;
    },
    computeMyLineupPlayerTableData() {
      let arr = [];
      for (let player of this.MyLineupPlayers) {
        let atBat = player.player_position + "번 타자";
        if (player.player_position == 10) {
          atBat = "투수";
        }

        arr.push([atBat, player.position, player.player_name]);
      }
      return arr;
    },
    computeTeamStatData() {
      let obj = {};
      let label = [];
      let data = [];

      label = Object.keys(this.teamStats);
      obj.label = label;

      for (let key of label) {
        data.push(this.teamStats[key]);
      }
      obj.data = {
        label: "상대 팀",
        backgroundColor: "rgba(255, 0, 0, 0.2)",
        borderColor: "rgb(255, 0, 0)",
        borderWidth: 1,
        pointBackgroundColor: "rgb(255, 0, 0)",
        data: data,
      };
      if (this.isModifiedMyTeamStat) {
        let data2 = [];
        for (let key of label) {
          data2.push(this.MyTeamStats[key]);
        }
        obj.data2 = {
          label: "나의 팀",
          backgroundColor: "rgba(100, 100, 255, 0.2)",
          borderColor: "rgb(100, 100, 255)",
          borderWidth: 1,
          pointBackgroundColor: "rgb(100, 100, 255)",
          data: data2,
        };
      }

      return obj;
    },
    computeMyTeamStatData() {
      let obj = {};
      let label = [];
      let data = [];

      label = Object.keys(this.MyTeamStats);
      obj.label = label;

      for (let key of label) {
        data.push(this.MyTeamStats[key]);
      }
      obj.data = {
        label: "나의 팀",
        backgroundColor: "rgba(100, 100, 255, 0.2)",
        borderColor: "rgb(100, 100, 255)",
        borderWidth: 1,
        pointBackgroundColor: "rgb(100, 100, 255)",
        data: data,
      };

      if (this.isModifiedTeamStat) {
        let data2 = [];
        for (let key of label) {
          data2.push(this.teamStats[key]);
        }
        obj.data2 = {
          label: "상대 팀",
          backgroundColor: "rgba(255, 0, 0, 0.2)",
          borderColor: "rgb(255, 0, 0)",
          borderWidth: 1,
          pointBackgroundColor: "rgb(255, 0, 0)",
          data: data2,
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

      for (let key of label) {
        data.push(this.playerStats.five_tool[key]);
      }
      obj.data = {
        label: "Player stat",
        backgroundColor: "rgba(255, 0, 0, 0.2)",
        borderColor: "rgb(255, 0, 0)",
        borderWidth: 1,
        pointBackgroundColor: "rgb(255, 0, 0)",
        data: data,
      };

      return obj;
    },
    computeMyPlayerStatData() {
      let obj = {};
      let label = [];
      let data = [];

      label = Object.keys(this.MyPlayerStats.five_tool);
      obj.label = label;

      for (let key of label) {
        data.push(this.MyPlayerStats.five_tool[key]);
      }
      obj.data = {
        label: "MyPlayer stat",
        backgroundColor: "rgba(255, 0, 0, 0.2)",
        borderColor: "rgb(255, 0, 0)",
        borderWidth: 1,
        pointBackgroundColor: "rgb(255, 0, 0)",
        data: data,
      };

      return obj;
    },
    changeLineup(id, name) {
      this.lineupId = id;
      this.lineupName = name;

      PlayerAPI.getTeamStatWithRecommend(
        "lineup=" + id,
        (res) => {
          this.lineupPlayers = res.playerList;
          this.teamStats = res.teamStat;

          // teamStats 에 team_id 가 포함되어있다 이거 빼야한다
          delete this.teamStats.team_id;

          this.lineupPlayerTableData = this.computeLineupPlayerTableData();
          this.CommonTeamStatData = this.computeTeamStatData();
          this.isModifiedTeamStat = true;
          this.comparisonContent = {
            Dif: {
              eraDif: this.MyTeamStats.era - this.teamStats.era,
              healthDif: this.MyTeamStats.health - this.teamStats.health,
              controlDif: this.MyTeamStats.control - this.teamStats.control,
              stabilityDif:
                this.MyTeamStats.stability - this.teamStats.stability,
              deterrentDif:
                this.MyTeamStats.deterrent - this.teamStats.deterrent,
              powerDif: this.MyTeamStats.power - this.teamStats.power,
              speedDif: this.MyTeamStats.speed - this.teamStats.speed,
              contactDif: this.MyTeamStats.contact - this.teamStats.contact,
              defenseDif: this.MyTeamStats.defense - this.teamStats.defense,
              shoulderDif: this.MyTeamStats.shoulder - this.teamStats.shoulder,
            },
            absoluteValue: {
              era: Math.abs(this.comparisonContent.Dif.eraDif),
              health: Math.abs(this.comparisonContent.Dif.healthDif),
              control: Math.abs(this.comparisonContent.Dif.controlDif),
              stability: Math.abs(this.comparisonContent.Dif.stabilityDif),
              deterrent: Math.abs(this.comparisonContent.Dif.deterrentDif),
              power: Math.abs(this.comparisonContent.Dif.powerDif),
              speed: Math.abs(this.comparisonContent.Dif.speedDif),
              contact: Math.abs(this.comparisonContent.Dif.controlDif),
              defense: Math.abs(this.comparisonContent.Dif.defenseDif),
              shoulder: Math.abs(this.comparisonContent.Dif.shoulderDif),
            },
          };
          console.log(this.comparisonContent);
          console.log(res);
        },
        (err) => {
          console.log(err);
        }
      );
    },
    changeMyLineup(id, name) {
      this.MyLineupId = id;
      this.MyLineupName = name;

      PlayerAPI.getTeamStatWithRecommend(
        "lineup=" + id,
        (res) => {
          this.MyLineupPlayers = res.playerList;
          this.MyTeamStats = res.teamStat;

          // teamStats 에 team_id 가 포함되어있다 이거 빼야한다
          delete this.MyTeamStats.team_id;

          this.MyLineupPlayerTableData = this.computeMyLineupPlayerTableData();
          this.CommonTeamStatData = this.computeMyTeamStatData();
          this.isModifiedMyTeamStat = true;

          (this.comparisonContent = {
            Dif: {
              eraDif: this.MyTeamStats.era - this.teamStats.era,
              healthDif: this.MyTeamStats.health - this.teamStats.health,
              controlDif: this.MyTeamStats.control - this.teamStats.control,
              stabilityDif:
                this.MyTeamStats.stability - this.teamStats.stability,
              deterrentDif:
                this.MyTeamStats.deterrent - this.teamStats.deterrent,
              powerDif: this.MyTeamStats.power - this.teamStats.power,
              speedDif: this.MyTeamStats.speed - this.teamStats.speed,
              contactDif: this.MyTeamStats.contact - this.teamStats.contact,
              defenseDif: this.MyTeamStats.defense - this.teamStats.defense,
              shoulderDif: this.MyTeamStats.shoulder - this.teamStats.shoulder,
            },
            absoluteValue: {
              era: Math.abs(this.comparisonContent.Dif.eraDif),
              health: Math.abs(this.comparisonContent.Dif.healthDif),
              control: Math.abs(this.comparisonContent.Dif.controlDif),
              stability: Math.abs(this.comparisonContent.Dif.stabilityDif),
              deterrent: Math.abs(this.comparisonContent.Dif.deterrentDif),
              power: Math.abs(this.comparisonContent.Dif.powerDif),
              speed: Math.abs(this.comparisonContent.Dif.speedDif),
              contact: Math.abs(this.comparisonContent.Dif.controlDif),
              defense: Math.abs(this.comparisonContent.Dif.defenseDif),
              shoulder: Math.abs(this.comparisonContent.Dif.shoulderDif),
            },
          }),
            console.log(this.comparisonContent),
            console.log(res);
        },
        (err) => {
          console.log(err);
        }
      );
    },
    getPlayerStat(id) {
      PlayerAPI.getPlayerStat(
        "num=" + id,
        (res) => {
          console.log(res);
          this.playerStats = res;
          this.playerStatData = this.computePlayerStatData();
        },
        (err) => {
          console.log(err);
        }
      );
    },
    getMyPlayerStat(id) {
      PlayerAPI.getPlayerStat(
        "num=" + id,
        (res) => {
          console.log(res);
          this.MyplayerStats = res;
          this.MyPlayerStatData = this.computeMyPlayerStatData();
        },
        (err) => {
          console.log(err);
        }
      );
    },
    clickLineupPlayer(index) {
      if (this.lineupSel != index) {
        this.playerName = this.lineupPlayers[index].name;
        this.lineupSel = index;

        this.getPlayerStat(this.lineupPlayers[index].player_id);
        this.playerName = this.lineupPlayers[index].player_name;
      }
    },

    clickMyLineupPlayer(index) {
      if (this.MyLineupSel != index) {
        this.MyPlayerName = this.MyLineupPlayers[index].name;
        this.MyLineupSel = index;

        this.getPlayerStat(this.MyLineupPlayers[index].player_id);
        this.playerName = this.MyLineupPlayers[index].player_name;
      }
    },
  },
};
</script>
<style scoped>
.custom-table {
  margin: auto;
}
</style>
