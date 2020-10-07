export default {
    addUserInfo(state, userInfo) {
        state.userInfo = userInfo.userInfo;
    },
    deleteUserInfo(state) {
        state.userInfo = {};
    }
}