<template>
  <div>
    <base-header type="gradient-default" class="pb-6 pb-8 pt-5 pt-md-8"></base-header>

    <!--Charts-->
    <div class="container-fluid mt--7">
      <!--Search form-->
      <div class="row">
        <base-dropdown class="col-lg-2 mb-2">
          <base-button slot="title" type="secondary" class="dropdown-toggle">{{searchType}}</base-button>
          <span class="dropdown-item" @click="changeSearchType('Name')">Name</span>
          <span class="dropdown-item" @click="changeSearchType('Team')">Team</span>
          <span class="dropdown-item" @click="changeSearchType('Position')">Position</span>
        </base-dropdown>
      </div>
      <!--End search form-->

      <!--Table-->
      <div class="row mt-5">
        <div class="col-xl-12 mb-5">
          <projects-table />
        </div>
      </div>
      <!--End table-->

      <!--Charts-->
      <div class="row">
        <!-- 5Tool Chart -->
        <div class="col-xl-4 mb-5 mb-xl-0">
          <player-radar-chart :five_tool="playerStats.five_tool" :type="chartType"></player-radar-chart>
        </div>
      </div>
      <!-- End charts-->

      <!--Table-->
      <div class="row mt-5">
        <div class="col-xl-12 mb-5">
          <projects-table />
        </div>
      </div>
      <!--End table-->
    </div>
  </div>
</template>

<script>
// Charts
import PlayerRadarChart from "@/components/Player/PlayerRadarChart";

// Tables
import ProjectsTable from "@/views/Tables/ProjectsTable";

export default {
  components: {
    PlayerRadarChart,
    ProjectsTable
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
    }
  },
  computed: {
  },
  methods: {
    changeSearchType(t) {
      this.searchType = t;
    }
  }
};
</script>
<style></style>
