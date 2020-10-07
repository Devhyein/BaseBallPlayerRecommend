<template>
  <div>
    <base-header type="translucent-default" class="pb-5 pt-5">
    </base-header>
    <div class="p-3 mt-5">
      <main-carousel/>
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
          title="Player Recommendation"
          type="gradient-red"
          sub-title="35,390명의 선수"
          icon="ni ni-user-run"
          class="mb-4 mb-xl-0"
        >
          <template slot="footer">
            <span class="text-success mr-2">
              Recommendation service
              <br />according to position and specific player
            </span>
          </template>
        </stats-card>
      </div>

      <div class="col-xl-3 col-lg-6">
        <stats-card
          title="Lineup service"
          type="gradient-green"
          sub-title="비교할 수 있는 라인업"
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
          sub-title="당신이 찾는 그 선수"
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
<style>
</style>
