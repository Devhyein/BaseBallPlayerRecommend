<template>
  <div class="card">
    <div class="card-header border-0">
      <div class="row align-items-center">
        <div class="col">
          <h3 class="mb-0">{{tableTitle}}</h3>
        </div>
      </div>
    </div>

    <div class="table-responsive">
      <base-table thead-classes="thead-light"
                  :data="tableData"
                  type="hover"
                  @clickRow="clickRow">
        <template slot="columns">
          <template v-for="(c, idx) in cols">
            <th :key="idx">{{c}}</th>
          </template>
        </template>


        <template slot-scope="{row}">
          <template v-for="(r, idx) in row">
            <td :key="idx">
              {{r}}
            </td>
          </template>
        </template>

      </base-table>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'custom-table',
    data() {
      return {
      }
    },
    props: {
      tableTitle: String
      , tableData: Array
      , cols: Array
    },
    methods: {
      clickRow(index) {
        this.$emit("clickRow", index);
      }
    },
  }
</script>
<style>
</style>
