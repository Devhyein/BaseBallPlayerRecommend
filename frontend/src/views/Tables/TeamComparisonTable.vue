<template>
  <div class="card shadow"
       :class="type === 'dark' ? 'bg-default': ''">
    <div class="card-header border-0"
         :class="type === 'dark' ? 'bg-transparent': ''">
      <div class="row align-items-center">
        <div class="col">
          <h3 class="mb-0" :class="type === 'dark' ? 'text-white': ''">
            {{title}}
          </h3>
        </div>
        <div class="col text-right">
        </div>
      </div>
    </div>

    <div class="table-responsive">
      <base-table class="table align-items-center table-flush"
                  :class="type === 'dark' ? 'table-dark': ''"
                  :thead-classes="type === 'dark' ? 'thead-dark': 'thead-light'"
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
      tableTitle: String
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
