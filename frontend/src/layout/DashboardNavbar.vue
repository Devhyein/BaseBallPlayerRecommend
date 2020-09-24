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
                <span class="mb-0 text-sm font-weight-bold">유저 닉네임</span>
              </div>
            </div>

            <template>
              <div class="dropdown-header noti-title">
                <h6 class="text-overflow m-0">Welcome!</h6>
              </div>
              <router-link to="/profile" class="dropdown-item">
                <i class="ni ni-single-02"></i>
                <span>My profile</span>
              </router-link>
              <div class="dropdown-divider"></div>
              <router-link to="/profile" class="dropdown-item">
                <i class="ni ni-user-run"></i>
                <span>Logout</span>
              </router-link>
            </template>
          </base-dropdown>
        </template>
        <template v-else>
          <base-button block type="secondary"  @click="showModal = true">
              Login
          </base-button>

          <modal :show.sync="showModal"
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
          </modal>
        </template>
      </li>
    </ul>
  </base-nav>
</template>
<script>
// Alert
import swal from 'sweetalert';

export default {
  data() {
    return {
      activeNotifications: false,
      showMenu: false,
      searchQuery: "",

      showModal: false,

      isInit: false,
    };
  },
  mounted() {
    // 구글 로그인이 준비가 되었는지 1초 간격으로 확인
    let that = this;
    let checkGauthLoad = setInterval(function(){
      that.isInit = that.$gAuth.isInit
      // 준비 되었으면 확인 그만하기
      if(that.isInit) clearInterval(checkGauthLoad)
    }, 1000);
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
    googleLogin() {
      if(!this.isInit) {
        swal("경고", "구글 로그인이 아직 준비되지 않았습니다.", "warning");
        return;
      }

      // 구글 로그인 동작
      this.$gAuth.signIn()
        .then(GoogleUser => {
          let profile = GoogleUser.getBasicProfile();

          let email= profile.getEmail();
          let name= profile.getName();
          let image = profile.getImageUrl();

          this.login(email, name, image);
        })
        .catch(error => {
          console.log('구글 signIn() 실패', error);
        });
    },
    login(email, name, image) {
      // 콘솔에 찍어보기
      // 나중엔 여기서 백에 요청 보내줘야함
      console.log(email);
      console.log(name);
      console.log(image);
    }
  },
  computed: {
    isLogin() {
      return (this.$store.state.userInfo.email != undefined);
    }
  },
};
</script>
