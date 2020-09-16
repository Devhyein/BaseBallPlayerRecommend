<template>
  <div>
    <base-header type="gradient-default" class="pb-5 pt-5"></base-header>

    <!--Content-->
    <div class="container-fluid mt-4">
      <!--Search form-->
        <form class="navbar-search form-inline">
            <base-dropdown class="text-center">
              <base-button slot="title" type="secondary" class="dropdown-toggle">{{searchType}}</base-button>
              <span class="dropdown-item" @click="changeSearchType('Name')">Name</span>
              <span class="dropdown-item" @click="changeSearchType('Team')">Team</span>
              <span class="dropdown-item" @click="changeSearchType('Position')">Position</span>
            </base-dropdown>
            <div class="form-group mb-0">
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
          <custom-table
            tableTitle="Player List"
            :tableData="playerListTableData"
            :cols="playerListTableCols"
            @clickRow="selectPlayer"
          />
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
          <player-stat-table :cols="cols" :tableData="statTableData" />
        </div>
      </div>
      <!--End table-->
    </div>
  </div>
</template>

<script>
// Charts
import PlayerStatChart from "@/components/Player/PlayerStatChart";
import CustomRadarChart from "@/components/Player/CustomRadarChart";

// Tables
import CustomTable from "@/views/Tables/CustomTable";
import PlayerStatTable from "./Tables/PlayerStatTable";

// API
import PlayerAPI from "@/api/PlayerAPI"

export default {
  components: {
    PlayerStatChart,
    CustomRadarChart,

    CustomTable,
    PlayerStatTable,
  },
  data() {
    return {
      ////////////////////////////////////////////////////////////////////////////
      playerList: [],
      playerListTableCols: [
        "Name"
        , "Team"
        , "Position"
        , "Number"
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
        // 투수 스탯이라고 가정
        stats: [],
      },
      ////////////////////////////////////////////////////////////////////////////

      searchType: "Name",
      searchVal: "",

      playerName: "Select player",

      chartType: "secondary"
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
      // data = [[], [], []]
      let arr = [];
      let mean_arr = [];

      for (let stat of this.playerStats.stats) {
        mean_arr.push(stat.stat_mean);
      }
      arr.push(this.originVals);
      arr.push(mean_arr);
      arr.push(this.vals);
      return arr;
    },
    statRadarData() {
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
      PlayerAPI.getPlayerList(
        this.searchVal,
        res => {
          this.playerList = res;
          this.resetPlayerListTable();
        },
        err => {
          console.log(err);
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
    }
  },
  mounted() {},
};
</script>
<style></style>
