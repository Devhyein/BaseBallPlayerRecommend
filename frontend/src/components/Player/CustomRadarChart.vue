<template>
  <card :type="type" header-classes="bg-transparent">
    <div slot="header" class="row align-items-center">
      <div class="col">
        <h6 class="text-uppercase text-muted ls-1 mb-1">{{title}}</h6>
        <h5 class="h3 mb-0 text-default">{{subTitle}}</h5>
      </div>
    </div>

    <radar-chart :height="200" ref="radarChart" :chart-data="chartData" :extra-options="extraOptions"></radar-chart>
  </card>
</template>
<script>
// Charts
import RadarChart from "@/components/Charts/RadarChart";

export default {
  components: {
    RadarChart,
  },
  data() {
    return {
      extraOptions: {
          maintainAspectRatio: false,
          legend: {
            display: false
          },
          responsive: true,
          scale: {
            ticks :{
              display: false,
            } 
          }
        },
    };
  },
  props: {
    title: String,
    subTitle: String,
    data: Object,
    type: String
  },
  computed: {
    chartData() {
      let cData = {
        labels: this.data.label,
        datasets: [{
          data: this.data.data,
        }],
      };

      return cData;
    },
  },
};
</script>
<style></style>