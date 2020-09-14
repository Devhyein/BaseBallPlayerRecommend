<template>
  <card type="default" header-classes="bg-transparent">
    <div slot="header" class="row align-items-center">
      <div class="col">
        <h6 class="text-uppercase text-muted ls-1 mb-1">Stat graph</h6>
        <h5 class="h3 mb-0 text-secondary">All Stats</h5>
      </div>
    </div>

    <bar-chart
      :height="200"
      ref="barChart"
      :chart-data="chartData"
      :extra-options="extraOptions"
    />
  </card>
</template>
<script>
  // Charts
  // import * as chartConfigs from '@/components/Charts/config';
  import BarChart from '@/components/Charts/BarChart';

  export default {
    components: {
      BarChart
    },
    data() {
      return {
        extraOptions: {
          maintainAspectRatio: false,
          legend: {
            display: false
          },
          responsive: true,
          tooltips: {
            callbacks: {
              label: (tooltipItem, data) => {
                let idx = tooltipItem['index'];
                return 'Value: ' + this.originVals[idx] 
                     + ' Rate: ' + data['datasets'][0]['data'][idx];
              }
            }
          }
        },
      };
    },
    props: {
      cols: Array
      , vals: Array
      , originVals: Array
    },
    computed: {
      chartData() {
        let cData = {
          labels: this.cols,
          datasets: [{
            label: 'Rate',
            data: this.vals
          }]
        };

        return cData;
      }
    },
    methods: {
    }
  };
</script>
<style></style>