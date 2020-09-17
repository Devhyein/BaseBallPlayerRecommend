import http from "./http-common.js";
import header from "./header.js"

const getApi = (url, data, callback, errorCallback) => {
    http.get(url + '?' + data, header())
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
            err.msg = url + ': Exception Occurred';
            errorCallback(err);
        });
}

const postApi = (url, data, callback, errorCallback) => {
    http.post(url, data, header())
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
            err.msg = url + ': Exception Occurred';
            errorCallback(err);
        });
}

/*
const putApi = (url, data, callback, errorCallback) => {
    http.get(url, data, header())
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
            err.msg = url + ': Exception Occurred';
            errorCallback(err);
        });
}

const deleteApi = (url, data, callback, errorCallback) => {
    http.get(url, data, header())
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
            err.msg = url + ': Exception Occurred';
            errorCallback(err);
        });
}
*/

const PlayerAPI = {
    getPlayerList:(data,callback,errorCallback)=>getApi('/spring/info/playerlist',data,callback,errorCallback),
    getPlayerStat:(data,callback,errorCallback)=>getApi('/spring/info/player',data,callback,errorCallback),

    getLineupList:(data,callback,errorCallback)=>getApi('/spring/team/lineup',data,callback,errorCallback),
    getTeamStatWithRecommend:(data,callback,errorCallback)=>getApi('/spring/recommend1',data,callback,errorCallback),
    getTeamStat:(data,callback,errorCallback)=>postApi('/spring/recommend1/change',data,callback,errorCallback),
}

export default PlayerAPI