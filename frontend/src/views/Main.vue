<template>
  <div>
    <base-header type="translucent-default" class="pb-5 pt-5">
    </base-header>
    <div class="pl-5 pr-5 pt-5">
      <div class="pl-5 pr-5 pt-2">
        <div class="pl-3 pl-3">
          <main-carousel/>
        </div>
      </div>
    </div>
    <br>
     <!-- Card stats -->
    <div class="row p-3">
      <div class="col-xl-3 col-lg-6">
        <stats-card
          title="Team analysis"
          type="gradient-orange"
          sub-title="10개의 구단"
          icon="ni ni-trophy"
          class="mb-4 mb-xl-0"
        >
          <template slot="footer">
            <span class="text-success mr-2">
              Analysis of strengths
              <br />and weaknesses of all clubs
            </span>
          </template>
        </stats-card>
      </div>

      <div class="col-xl-3 col-lg-6">
        <stats-card
          title="Recommendation"
          type="gradient-red"
          sub-title="35,390명의 선수"
          icon="ni ni-user-run"
          class="mb-4 mb-xl-0"
        >
          <template slot="footer">
            <span class="text-success mr-2">
              Recommendation service
              <br />according to team stat
            </span>
          </template>
        </stats-card>
      </div>

      <div class="col-xl-3 col-lg-6">
        <stats-card
          title="Lineup service"
          type="gradient-green"
          sub-title="라인업 비교"
          icon="ni ni-align-left-2"
          class="mb-4 mb-xl-0"
        >
          <template slot="footer">
            <span class="text-success mr-2">
              Create the best team
              <br />with your own lineup
            </span>
          </template>
        </stats-card>
      </div>

      <div class="col-xl-3 col-lg-6">
        <stats-card
          title="Custom service"
          type="gradient-info"
          sub-title="선수 검색"
          icon="ni ni-circle-08"
          class="mb-4 mb-xl-0"
        >
          <template slot="footer">
            <span class="text-success mr-2">
              Custom player
              <br />recommendations for each club
            </span>
          </template>
        </stats-card>
      </div>
    </div>
  </div>
</template>
<script>
import MainCarousel from "@/components/Main/MainCarousel";
export default {
  created() {
    console.log("Spring URL: " + process.env.VUE_APP_SPRING_ADDR);
  },
  mounted() {},
  components: {
    MainCarousel,
  },
};
</script>
<style scoped>
</style>
