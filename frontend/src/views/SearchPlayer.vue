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
        <div class="col-xl-12 mb-5">
          <player-list-table :tableData="playerList" @clickRow="playerStat"/>
        </div>
      </div>
      <!--End table-->

      <!--Charts-->
      <div class="row">
        <!-- 5Tool Chart -->
        <div class="col-xl-4 mb-5 mb-xl-0">
          <player-radar-chart :five_tool="playerStats.five_tool" :type="chartType"></player-radar-chart>
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
import PlayerRadarChart from "@/components/Player/PlayerRadarChart";

// Tables
import PlayerListTable from "./Tables/PlayerListTable";
import PlayerStatTable from "./Tables/PlayerStatTable";

// API
import PlayerAPI from "@/api/PlayerAPI"

export default {
  components: {
    PlayerStatChart,
    PlayerRadarChart,

    PlayerListTable,
    PlayerStatTable,
  },
  data() {
    return {
      ////////////////////////////////////////////////////////////////////////////
      playerList: [],
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
        },
        err => {
          console.log(err);
        }
      )
    },
    playerStat(id) {
      alert(id);
    }
  },
  mounted() {},
};
</script>
<style></style>
