<template>
  <card type="default" header-classes="bg-transparent">
    <div slot="header" class="row align-items-center">
      <div class="col">
        <h6 class="text-uppercase text-muted ls-1 mb-1">Stat graph</h6>
        <h5 class="h3 mb-0 text-secondary">5-Tools</h5>
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
    five_tool: Object,
  },
  computed: {
    chartData() {
      let five_tools = [
        this.five_tool.power,
        this.five_tool.speed,
        this.five_tool.contact,
        this.five_tool.defense,
        this.five_tool.shoulder,
      ];

      let cData = {
        labels: ["power", "speed", "contact", "defense", "shoulder"],
        datasets: [{
          data: five_tools,
        }],
      };
      console.log(five_tools);

      return cData;
    },
  },
  methods: {},
};
</script>
<style></style>