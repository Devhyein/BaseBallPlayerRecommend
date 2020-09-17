package com.ssafy.bigdata.controller;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.bigdata.dto.MattermostMsg;
import com.ssafy.bigdata.dto.RestResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {
    private static String webhookURL = "https://meeting.ssafy.com/hooks/f3qhjm8gopgcfbpntre1e8ksfr";

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object exceptionHandling(Exception e, HttpServletRequest req) {

        // 이 컨트롤러에서 반환할 메시지
        String returnMsg = "Exception 발생";

        // 예외 발생시 요청된 파라미터들 가져오기
        StringBuilder params = new StringBuilder();
        Enumeration<String> keys = req.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            params.append("- ").append(key).append(" : ").append(req.getParameter(key)).append("\n");
        }

        // 요청 uri 와 파라미터 설정
        String uri = req.getRequestURI();
        String param = params.toString();

        // 메터모스트 메시지 객체 생성, 채널 등 설정
        MattermostMsg msg = new MattermostMsg();
        // msg.channel = "A110(log)";
        // msg.username = "Spring Backend";

        // 메터모스트에 띄울 메시지 설정
        StringBuilder sb = new StringBuilder();
        sb.append("== Error msg ==\n").append(e.getMessage()).append("\n").append("== Request URL ==\n").append(uri)
                .append("\n").append("== Request Params ==\n").append(param);

        msg.text = sb.toString();

        // 메터모스트 메시지 오브젝트를 json 형식 문자열로 변환
        String jsonMsg = "Msg to Json Error";
        try {
            jsonMsg = new ObjectMapper().writeValueAsString(msg);
            System.out.println(jsonMsg);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
            returnMsg = "Json 변환에서 에러 발생";
        }

        // http 요청으로 웹훅 보내기
        HttpURLConnection conn = null;
        try {
            URL url = new URL(webhookURL);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(jsonMsg);
            wr.flush();
            wr.close();
        } catch (Exception e2) {
            e2.printStackTrace();
            returnMsg = "Http 요청에서 에러 발생";
        }
        
        // 웹훅 요청이 정상적으로 처리됐는지 확인
        try {
            if(conn != null) {
                int httpResult = conn.getResponseCode();
                if(httpResult != HttpURLConnection.HTTP_OK) {
                    returnMsg = "응답코드가 200이 아님: " + httpResult;
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            returnMsg = "응답코드 확인에 오류 발생";
        }

        RestResponse response = new RestResponse();
        response.status = false;
        response.msg = returnMsg;
        return response;
    }
}