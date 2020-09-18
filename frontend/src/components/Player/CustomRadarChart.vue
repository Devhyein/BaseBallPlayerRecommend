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
          display: true
        },
        responsive: true,
        scale: {
          ticks :{
            display: false,
          },
          yAxes: [{
            ticks: {
              beginAtZero: true,
              stepSize: 0.2,
              min: 0,
              max: 1
            }
          }]
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
      let dataset = [];
      dataset.push(this.data.data);

      if(this.data.data2) {
        dataset.push(this.data.data2);
      }

      let cData = {
        labels: this.data.label,
        datasets: dataset,
      };

      return cData;
    },
  },
};
</script>
<style></style>