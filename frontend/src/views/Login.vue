<template>

  <div>
    <base-header type="translucent-default" class="pb-5 pt-5"></base-header>
    
    <div class="row justify-content-center container-fluid mt-2">
        <div class="col-lg-5 col-md-7">
        <div class="card bg-secondary shadow border-0">
            <div class="card-header bg-transparent pb-5">
            <div class="text-muted text-center mt-2 mb-3">
                <small>Sign in with</small>
            </div>
            <div class="btn-wrapper text-center">
                <!-- <a href="#" class="btn btn-neutral btn-icon">
                                    <span class="btn-inner--icon"><img src="img/icons/common/github.svg"></span>
                                    <span class="btn-inner--text">Github</span>
                </a>-->
                <button class="btn btn-neutral btn-icon" @click="googleLogin">
                  <span class="btn-inner--icon">
                      <img src="img/icons/common/google.svg" />
                  </span>
                  <span class="btn-inner--text">Google</span>
                </button>
                <button class="btn btn-neutral btn-icon" @click="testLogin">
                  <span class="btn-inner--text">Test Login</span>
                </button>
            </div>
            </div>
            <!-- <div class="card-body px-lg-5 py-lg-5">
                            <div class="text-center text-muted mb-4">
                                <small>Or sign in with credentials</small>
                            </div>
                            <form role="form">
                                <base-input class="input-group-alternative mb-3"
                                            placeholder="Email"
                                            addon-left-icon="ni ni-email-83"
                                            v-model="model.email">
                                </base-input>

                                <base-input class="input-group-alternative"
                                            placeholder="Password"
                                            type="password"
                                            addon-left-icon="ni ni-lock-circle-open"
                                            v-model="model.password">
                                </base-input>

                                <base-checkbox class="custom-control-alternative">
                                    <span class="text-muted">Remember me</span>
                                </base-checkbox>
                                <div class="text-center">
                                    <base-button type="primary" class="my-4">Sign in</base-button>
                                </div>
                            </form>
            </div>-->
        </div>
        <!-- <div class="row mt-3">
                        <div class="col-6">
                            <a href="#" class="text-light"><small>Forgot password?</small></a>
                        </div>
                        <div class="col-6 text-right">
                            <router-link to="/register" class="text-light"><small>Create new account</small></router-link>
                        </div>
        </div>-->
        </div>
    </div>
  </div>
</template>
<script>
import swal from "sweetalert";
import PlayerAPI from "@/api/PlayerAPI";

export default {
  name: "login",
  data() {
    return {
      model: {
        isInit: false,
      },
    };
  },
  mounted() {
    // 구글 로그인이 준비가 되었는지 1초 간격으로 확인
    let that = this;
    let checkGauthLoad = setInterval(function () {
      that.isInit = that.$gAuth.isInit;
      // 준비 되었으면 확인 그만하기
      if (that.isInit) clearInterval(checkGauthLoad);
    }, 1000);
  },
  methods: {
    googleLogin() {
      console.log("Google Login Button Clicked");
      if (!this.isInit) {
        swal("경고", "구글 로그인이 아직 준비되지 않았습니다.", "warning");
        return;
      }

      // 구글 로그인 동작
      this.$gAuth
        .signIn()
        .then((GoogleUser) => {
          let profile = GoogleUser.getBasicProfile();

          let email = profile.getEmail();
          let name = profile.getName();
          let image = profile.getImageUrl();

          this.login(email, name, image);
        })
        .catch((error) => {
          console.log("구글 signIn() 실패", error);
        });
    },
    login(email, name, image) {
      // 콘솔에 찍어보기
      console.log(email);
      console.log(name);
      console.log(image);

      PlayerAPI.googleLogin(
        {
          email: email,
          name: name,
          picture: image,
        },
        (res) => {
          console.log(res);
          this.$store.commit("addUserInfo", res);

          // 로그인 완료 시 세션 저장소에 받은 토큰 정보 저장
          sessionStorage.setItem('jwt-token', res.token);

          this.$router.push({name: "main"});
        },
        (err) => {
          console.log(err);
          swal("실패", "구글 로그인 실패", "error");
          this.$router.push({name: "main"});
        }
      );
    },
    testLogin() {
      PlayerAPI.testLogin(
        null,
        res => {
          console.log(res);
          this.$store.commit("addUserInfo", res);
          
          // 로그인 완료 시 세션 저장소에 받은 토큰 정보 저장
          sessionStorage.setItem('jwt-token', res.token);

          this.$router.push({name: "main"});
        },
        err => {
          console.log(err);
          swal("실패", "테스트 로그인 실패(왜?)", "error");
          this.$router.push({name: "main"});
        }
      )
    }
  },
};
</script>
<style>
</style>
