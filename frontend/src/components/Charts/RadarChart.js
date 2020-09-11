import { Radar, mixins } from 'vue-chartjs';
import globalOptionsMixin from "@/components/Charts/globalOptionsMixin";

export default {
  name: 'radar-chart',
  extends: Radar,
  mixins: [mixins.reactiveProp, globalOptionsMixin],
  props: {
    extraOptions: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      ctx: null
    };
  },
  options: {
    scale: {
      ticks :{
        showLabelBackdrop: false
      } 
    }
 },
  mounted() {
    this.$watch(
      'chartData',
      (newVal, oldVal) => {
        if (!oldVal) {
          this.renderChart(this.chartData, this.extraOptions);
        }
      },
      { immediate: true }
    );
  }
};
