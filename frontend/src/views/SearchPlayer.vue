<template>
    <div>
        <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
            
        </base-header>

        <!--Charts-->
        <div class="container-fluid mt--7">
            <!--Search form-->
            <div class="row">
              <base-dropdown class="col-lg-2 mb-2">
                <base-button slot="title" type="secondary" class="dropdown-toggle">
                  {{searchType}}
                </base-button>
                <span class="dropdown-item" @click="changeSearchType('Name')">Name</span>
                <span class="dropdown-item" @click="changeSearchType('Team')">Team</span>
                <span class="dropdown-item" @click="changeSearchType('Position')">Position</span>
              </base-dropdown>
              <base-input placeholder="Search"
                          class="input-group-alternative col-lg-10"
                          alternative=""
                          addon-right-icon="fas fa-search"
                          :value="searchVal"
                          @input="setSearchVal"
                          @clickIcon="search">
              </base-input>
            </div>
            <!--End search form-->

            <!--Table-->
            <div class="row mt-5">
              <div class="col-xl-12 mb-5">
                <player-list-table
                  :tableData="playerList"
                />
              </div>
            </div>
            <!--End table-->

            <!--Charts-->
            <div class="row">
                <!--Line Chart-->
                <div class="col-xl-4 mb-5 mb-xl-0">
                    <card type="default" header-classes="bg-transparent">
                        <div slot="header" class="row align-items-center">
                            <div class="col">
                                <h6 class="text-light text-uppercase ls-1 mb-1">Overview</h6>
                                <h5 class="h3 text-white mb-0">Sales value</h5>
                            </div>
                        </div>
                        <line-chart
                                :height="200"
                                ref="bigChart"
                                :chart-data="bigLineChart.chartData"
                                :extra-options="bigLineChart.extraOptions"
                        >
                        </line-chart>
                    </card>
                </div>

                <!--Bar Chart-->
                <div class="col-xl-8">
                    <player-stat-chart
                      :cols="cols"
                      :vals="vals"
                      :originVals="originVals"
                    />
                </div>
            </div>
            <!-- End charts-->

            <!--Table-->
            <div class="row mt-5">
              <div class="col-xl-12 mb-5">
                <player-stat-table
                  :cols="cols"
                  :tableData="statTableData"
                />
              </div>
            </div>
            <!--End table-->
        </div>
    </div>
</template>
<script>
  // Charts
  import * as chartConfigs from '@/components/Charts/config';
  import LineChart from '@/components/Charts/LineChart';

  import PlayerStatChart from '@/components/Player/PlayerStatChart';

  // Tables
  import PlayerListTable from './Tables/PlayerListTable';
  import PlayerStatTable from './Tables/PlayerStatTable';

  export default {
    components: {
      LineChart,
      PlayerStatChart,
      PlayerListTable,
      PlayerStatTable,
    },
    data() {
      return {
        bigLineChart: {
          allData: [
            [0, 20, 10, 30, 15, 40, 20, 60, 60],
            [0, 20, 5, 25, 10, 30, 15, 40, 40]
          ],
          activeIndex: 0,
          chartData: {
            datasets: [],
            labels: [],
          },
          extraOptions: chartConfigs.blueChartOptions,
        },

        ////////////////////////////////////////////////////////////////////////////
        playerList: [
          {
            player_id: 1234,
            player_name: '서민성',
            player_team: '한화이글스',
            player_num: 4,
            player_age: 27,
            position: '좌익수'
          },
          {
            player_id: 2214,
            player_name: '김민건',
            player_team: '한화이글스',
            player_num: 14,
            player_age: 27,
            position: '우익수'
          },
          {
            player_id: 121,
            player_name: '유지민',
            player_team: '한화이글스',
            player_num: 114,
            player_age: 25,
            position: '포수'
          },
          {
            player_id: 41,
            player_name: '전혜민',
            player_team: '한화이글스',
            player_num: 24,
            player_age: 24,
            position: '투수'
          },
          {
            player_id: 23,
            player_name: '윤수민',
            player_team: '한화이글스',
            player_num: 34,
            player_age: 27,
            position: '유격수'
          },
          {
            player_id: 1,
            player_name: '스프링',
            player_team: '한화이글스',
            player_num: 94,
            player_age: 58,
            position: '프레임웤'
          },
        ],
        playerStats: {
          five_tool: {
            power: 0.79,
            speed: 0.58,
            contact: 0.92,
            defense: 0.30,
            shoulder: 0.24
          },
          // 투수 스탯이라고 가정
          stats: [
              {stat_name: 'g',
              stat_value: 25,
              stat_mean: 25,
              stat_std: 0.5}
              , {stat_name: 'cg',
              stat_value: 5,
              stat_mean: 7,
              stat_std: 0.4}
              , {stat_name: 'sho',
              stat_value: 3,
              stat_mean: 1,
              stat_std: 0.9}
              , {stat_name: 'gs',
              stat_value: 25,
              stat_mean: 25,
              stat_std: 0.5}
              , {stat_name: 'w',
              stat_value: 16,
              stat_mean: 8,
              stat_std: 0.7}
              , {stat_name: 'l',
              stat_value: 4,
              stat_mean: 6,
              stat_std: 0.3}
              , {stat_name: 'sv',
              stat_value: 0,
              stat_mean: 1,
              stat_std: 0}
              , {stat_name: 'hld',
              stat_value: 0,
              stat_mean: 1,
              stat_std: 0}
              , {stat_name: 'ip',
              stat_value: 192.2,
              stat_mean: 200,
              stat_std: 0.4}
              , {stat_name: 'r',
              stat_value: 42,
              stat_mean: 60,
              stat_std: 0.35}
              , {stat_name: 'er',
              stat_value: 39,
              stat_mean: 65,
              stat_std: 0.2}
              , {stat_name: 'h',
              stat_value: 147,
              stat_mean: 200,
              stat_std: 0.3}
              , {stat_name: '2b',
              stat_value: 0,
              stat_mean: 1,
              stat_std: 0}
              , {stat_name: '3b',
              stat_value: 0,
              stat_mean: 1,
              stat_std: 0}
              , {stat_name: 'homerun',
              stat_value: 11,
              stat_mean: 20,
              stat_std: 0.33}
              , {stat_name: 'bb',
              stat_value: 45,
              stat_mean: 50,
              stat_std: 0.4}
              , {stat_name: 'ibb',
              stat_value: 2,
              stat_mean: 5,
              stat_std: 0.2}
              , {stat_name: 'hbp',
              stat_value: 9,
              stat_mean: 20,
              stat_std: 0.1}
              , {stat_name: 'so',
              stat_value: 187,
              stat_mean: 80,
              stat_std: 0.8}
              , {stat_name: 'bk',
              stat_value: 1,
              stat_mean: 1,
              stat_std: 0.5}
              , {stat_name: 'wp',
              stat_value: 6,
              stat_mean: 5,
              stat_std: 0.55}
              , {stat_name: 'era',
              stat_value: 1.82,
              stat_mean: 1.82,
              stat_std: 0.5}
              , {stat_name: 'fip',
              stat_value: 2.90,
              stat_mean: 2.80,
              stat_std: 0.52}
              , {stat_name: 'whip',
              stat_value: 1.01,
              stat_mean: 0.6,
              stat_std: 0.7}
              , {stat_name: 'era_plus',
              stat_value: 253.6,
              stat_mean: 255,
              stat_std: 0.46}
              , {stat_name: 'fip_plus',
              stat_value: 159.5,
              stat_mean: 160,
              stat_std: 0.49}
              , {stat_name: 'war',
              stat_value: 9.20,
              stat_mean: 10,
              stat_std: 0.48}
              , {stat_name: 'wpa',
              stat_value: 0,
              stat_mean: 1,
              stat_std: 0}
          ]
        },
        ////////////////////////////////////////////////////////////////////////////

        searchType: 'Name',
        searchVal: ''
      };
    },
    computed: {
      cols() {
        let arr = [];
        for(let stat of this.playerStats.stats) {
          arr.push(stat.stat_name);
        }
        return arr;
      }
      , vals() {
        let arr = [];
        for(let stat of this.playerStats.stats) {
          arr.push(stat.stat_std);
        }
        return arr;
      }
      , originVals() {
        let arr = [];
        for(let stat of this.playerStats.stats) {
          arr.push(stat.stat_value);
        }
        return arr;
      }
      , statTableData() {
        // data = [[], [], []]
        let arr = [];
        let mean_arr = [];
        
        for(let stat of this.playerStats.stats) {
          mean_arr.push(stat.stat_mean);
        }
        arr.push(this.originVals);
        arr.push(mean_arr);
        arr.push(this.vals);
        return arr;
      }
    },
    methods: {
      initBigChart(index) {
        let chartData = {
          datasets: [
            {
              label: 'Performance',
              data: this.bigLineChart.allData[index]
            }
          ],
          labels: ['May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        };
        this.bigLineChart.chartData = chartData;
        this.bigLineChart.activeIndex = index;
      },
      changeSearchType(t) {
        this.searchType = t;
      },
      setSearchVal(s) {
        this.searchVal = s
      },
      search() {
        alert(this.searchVal);
      }
    },
    mounted() {
      this.initBigChart(0);
    }
  };
</script>
<style></style>
