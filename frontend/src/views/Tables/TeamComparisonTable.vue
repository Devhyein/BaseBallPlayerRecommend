<template>
  <div class="card shadow">
    <div class="card-header border-0">
      <div class="row align-items-center">
        <div class="col">
          <h3 class="mb-0">
            {{title}}
          </h3>
        </div>
        <div class="col text-right">
        </div>
      </div>
    </div>

    <div class="table-responsive">
      <base-table class="table align-items-center table-flush"
                  tbody-classes="list"
                  :data="tableData">

              <template slot="columns">
                <template v-for="(c,idx) in cols">
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

        <!-- <template slot-scope="{row}">
          <th scope="row">
            <div class="media align-items-center">
              <div class="media-body">
                <span class="name mb-0 text-sm">{{row.title}}</span>
              </div>
            </div>
          </th>

          <td>
            <div class="d-flex align-items-center">
              <span class="completion mr-2">{{row.completion}}%</span>
              <div>
              </div>
            </div>
          </td>

        </template> -->

      </base-table>
    </div>


  </div>
</template>
<script>
  export default {
    name: 'team-comparison-table',
    props: {
      title: String
      , tableData: Array
      , cols: Array
      ,comparisonContent: Array
    },
    data() {
      return {

      }
    }
  }

</script>
<style>
</style>
