<template>
  <base-nav class="navbar-top navbar-dark" id="navbar-main" :show-toggle-button="false" expand>
    <ul class="navbar-nav align-items-center d-none d-md-flex ml-auto">
      <li class="nav-item dropdown">
        <template v-if="isLogin">
          <base-dropdown class="nav-link pr-0">
            <div class="media align-items-center" slot="title">
              <span class="avatar avatar-sm rounded-circle">
                <img alt="Image placeholder" src="img/theme/team-4-800x800.jpg" />
              </span>
              <div class="media-body ml-2 d-none d-lg-block">
                <span class="mb-0 text-sm font-weight-bold">{{userName}}</span>
              </div>
            </div>

            <template>
              <div class="dropdown-header noti-title">
                <h6 class="text-overflow m-0">Welcome!</h6>
              </div>
              <!-- <router-link to="/profile" class="dropdown-item">
                <i class="ni ni-single-02"></i>
                <span>My profile</span>
              </router-link>
              <div class="dropdown-divider"></div> -->
              <div class="dropdown-item">
                <i class="ni ni-user-run"></i>
                <span @click="logout">Logout</span>
              </div>
            </template>
          </base-dropdown>
        </template>
        <template v-else>
          <base-button block type="secondary"  @click="goLogin">
              Login
          </base-button>

          <!-- <modal :show.sync="showModal"
                body-classes="p-0"
                modal-classes="modal-dialog-centered modal-sm">
              <card type="secondary" shadow
                    header-classes="bg-white pb-5"
                    body-classes="px-lg-5 py-lg-5"
                    class="border-0">
                  <template>
                      <div class="text-muted text-center mb-3">
                          <small>Sign in with</small>
                      </div>
                      <div class="btn-wrapper text-center">
                          <base-button type="neutral" @click="googleLogin">
                              <img slot="icon" src="https://demos.creative-tim.com/argon-design-system/assets/img/icons/common/google.svg">
                              Google
                          </base-button>
                      </div>
                  </template>
              </card>
          </modal> -->
        </template>
      </li>
    </ul>
  </base-nav>
</template>
<script>
export default {
  data() {
    return {
      activeNotifications: false,
      showMenu: false,
      searchQuery: "",

      showModal: false,
    };
  },
  methods: {
    toggleSidebar() {
      this.$sidebar.displaySidebar(!this.$sidebar.showSidebar);
    },
    hideSidebar() {
      this.$sidebar.displaySidebar(false);
    },
    toggleMenu() {
      this.showMenu = !this.showMenu;
    },
    goLogin() {
      this.$router.push({name: "Login"});
    },
    logout() {
      this.$store.commit('deleteUserInfo');
    }
  },
  computed: {
    isLogin() {
      return (this.$store.state.userInfo.email != undefined);
    },
    userName() {
      return this.$store.state.userInfo.email;
    }
  },
};
</script>
