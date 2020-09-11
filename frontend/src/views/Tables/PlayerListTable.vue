<template>
  <div class="card">
    <div class="card-header border-0">
      <div class="row align-items-center">
        <div class="col">
          <h3 class="mb-0">Player List</h3>
        </div>
      </div>
    </div>

    <div class="table-responsive">
      <base-table thead-classes="thead-light"
                  :data="showData"
                  type="hover">
        <template slot="columns">
          <th>Name</th>
          <th>Team</th>
          <th>Position</th>
          <th>Number</th>
          <th>Age</th>
        </template>

        <template slot-scope="{row}">
          <th scope="row">
            {{row.player_name}}
          </th>
          <td>
            {{row.player_team}}
          </td>
          <td>
            {{row.position}}
          </td>
          <td>
            {{row.player_num}}
          </td>
          <td>
            {{row.player_age}}
          </td>
        </template>

      </base-table>
    </div>
    <div>
      <base-pagination
        :page-count="pageCount"
        v-model="pageVal"
        align="center"
      />
    </div>

  </div>
</template>
<script>
  export default {
    name: 'page-visits-table',
    data() {
      return {
        showData: [],
        from: 0,
        to: 0,
        total: 0,

        pageCount: 1,
        pageVal: 1
      }
    },
    props: [
      'tableData'
    ],
    watch: {
      tableData() {
        this.renewTable();
      },
      pageVal(newVal) {
        // 1: 0 to 5
        // 2: 5 to 10
        // 3: 10 to 15
        this.from = (newVal - 1) * 5
        this.to = this.from + 5
        if(this.to > this.total) this.to = this.total;
        this.showData = this.tableData.slice(this.from, this.to);
      }
    },
    created() {
      this.renewTable();
    },
    methods: {
      renewTable() {
        this.total = this.tableData.length;
        this.from = 0;
        this.to = 5;

        let v = this.total - 1;
        if(v < 0) v = 0;
        this.pageCount = v / 5 + 1;
        this.pageVal = 1;

        if(this.to > this.total) this.to = this.total;

        this.showData = this.tableData.slice(this.from, this.to);
      }
    },
  }
</script>
<style>
</style>
