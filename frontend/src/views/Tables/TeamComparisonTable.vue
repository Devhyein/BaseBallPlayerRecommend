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

        <template slot-scope="{row}">
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

        </template>

      </base-table>
    </div>


  </div>
</template>
<script>
  export default {
    name: 'team-comparison-table',
    props: {
      type: {
        type: String
      },
      title: String
    },
    data() {
      return {
        tableData: [
          {
            title: '상대팀보다 스피드가 매우 뛰어납니다',
            status: 'pending',
            completion: 60
          },
          {
            title: '상대팀보다 파워가 매우 떨어집니다.',
            status: 'completed',
            completion: 100
          },
          {
            title: '상대팀보다 안타율이 다소 낮습니다.',
            status: 'delayed',
            completion: 72
          },
          {
            title: '상대팀보다 장타율이 다소 높습니다.',
            status: 'on schedule',
            completion: 90
          },
          {
            title: '상대팀보다 수비가 다소 견고합니다.',
            status: 'completed',
            completion: 100
          }
        ]
      }
    }
  }
</script>
<style>
</style>
