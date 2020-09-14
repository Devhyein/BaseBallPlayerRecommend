import http from "./http-common.js";
import header from "./header.js"

const getPlayerList = (data,callback,errorCallback) => {
    http.get('/spring/info/playerlist?search=' + data, header())
        .then(res => {
            // RestAPI 서버가 null 을 응답하는 경우
            if(res == null) {
                let error = {msg : 'Server response is NULL'};
                errorCallback(error);
            }
            // RestAPI 에서 null 이 아닌 응답이 왔을 때
            else {
                // res.data 가 Rest 서버에서 반환한 객체
                let responseData = res.data

                // 서버의 수행이 성공적이었다면 callback 수행
                if(responseData.status) {
                    callback(responseData.data);
                }
                // 서버에서 오류가 발생했다면
                else {
                    // 그 오류메시지로 errorCallback 부름
                    let error = {msg : responseData.msg};
                    errorCallback(error);
                }
            }
        })
        .catch(err => {
            err.msg = 'getPlayerList Exception Occurred';
            errorCallback(err);
        });
}

const PlayerAPI = {
    getPlayerList:(data,callback, errorCallback)=>getPlayerList(data,callback, errorCallback)
}

export default PlayerAPI