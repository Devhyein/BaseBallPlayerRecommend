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
                    // 서버의 수행이 성공적이었다면 토큰이 넘어옴
                    sessionStorage.setItem('jwt-token', responseData.data.token);

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
                    // 서버의 수행이 성공적이었다면 토큰이 넘어옴
                    sessionStorage.setItem('jwt-token', responseData.data.token);

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

const putApi = (url, data, callback, errorCallback) => {
    http.put(url, data, header())
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
                    // 서버의 수행이 성공적이었다면 토큰이 넘어옴
                    sessionStorage.setItem('jwt-token', responseData.data.token);

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
    http.delete(url + '?' + data, header())
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
                    // 서버의 수행이 성공적이었다면 토큰이 넘어옴
                    sessionStorage.setItem('jwt-token', responseData.data.token);
                    
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

const PlayerAPI = {
    getPlayerList:(data,callback,errorCallback)=>postApi('/spring/info/playerlist',data,callback,errorCallback),
    getPlayerStat:(data,callback,errorCallback)=>getApi('/spring/info/player',data,callback,errorCallback),

    getLineupList:(data,callback,errorCallback)=>getApi('/spring/lineupList',data,callback,errorCallback),
    getTeamStatWithRecommend:(data,callback,errorCallback)=>getApi('/spring/recommend1',data,callback,errorCallback),
    getTeamStat:(data,callback,errorCallback)=>postApi('/spring/recommend1/change',data,callback,errorCallback),
    getSimilarPlayers:(data,callback,errorCallback)=>getApi('/spring/recommend/player',data,callback,errorCallback),

    getLineupPlayerWithTeamStat:(data,callback,errorCallback)=>getApi('/spring/lineup',data,callback,errorCallback),
    addLineup:(data,callback,errorCallback)=>postApi('/spring/lineup/insert',data,callback,errorCallback),
    modifyLineup:(data,callback,errorCallback)=>putApi('/spring/lineup/update',data,callback,errorCallback),
    modifyLineupName:(data,callback,errorCallback)=>putApi('/spring/lineup/changename',data,callback,errorCallback),
    deleteLineup:(data,callback,errorCallback)=>deleteApi('/spring/lineup/delete',data,callback,errorCallback),

    readFavorite:(data,callback,errorCallback)=>getApi('/spring/favList',data,callback,errorCallback),
    addFavorite:(data,callback,errorCallback)=>postApi('/spring/favorites/insert',data,callback,errorCallback),
    deleteFavorite:(data,callback,errorCallback)=>deleteApi('/spring/favorites/delete',data,callback,errorCallback),

    googleLogin:(data,callback,errorCallback)=>postApi('/spring/login',data,callback,errorCallback),
    testLogin:(data,callback,errorCallback)=>postApi('/spring/login/temp',data,callback,errorCallback),
}

export default PlayerAPI