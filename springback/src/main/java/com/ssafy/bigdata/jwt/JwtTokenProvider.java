package com.ssafy.bigdata.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String secretKey = "webfirewood";

    // 토큰 유효시간 30분
    private long tokenValidTime = 30 * 60 * 1000L;

    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    // @PostConstruct
    // protected void init() {
    // secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    // }

    // JWT 토큰 생성
    public String createToken(String userPk) {
        Date now = new Date();
        System.out.println("****여기****");
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("data", "My First JWT");
        Long expiredTime = 1000 * 60L * 60L * 2L; // 토큰 유효 시간 (2시간)

        Date ext = new Date(); // 토큰 만료 시간
        ext.setTime(ext.getTime() + expiredTime);
        System.out.println("*************************");
         // 토큰 Builder
         JwtBuilder jwtB = Jwts.builder()
         .setClaims(Jwts.claims().setSubject(userPk)) // Claims 설정
         .setSubject(userPk) // 토큰 용도 
         .setExpiration(ext) // 토큰 만료 시간 설정
         .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()); // HS256과 Key로 Sign
         System.out.println(jwtB);
         String jwt = jwtB.compact();
          // 토큰 생성
         System.out.println("*************************");
         // Claims claims = Jwts.claims().setSubject(userPk).setIssuedAt(now).setExpiration(new Date(now.getTime() + tokenValidTime)); // JWT payload 에 저장되는 정보단위
        // System.out.println(claims);
        // try {
        //     System.out.println(claims);
        //     System.out.println(Jwts.builder().setClaims(claims));
        //     System.out.println(Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey.getBytes()));
        //     String jwt = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey.getBytes()).compact();
        //     System.out.println("__________________________________________");
        //     System.out.println(jwt);
        //     System.out.println("*************************");

        // }catch(Exception e){
        //     System.out.println("또 여기");
        // } 
        return jwt;
        // return Jwts.builder()
        //         .setClaims(claims) // 정보 저장
        //         .setIssuedAt(now) // 토큰 발행 시간 정보
        //         .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
        //         .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과 
        //                                                         // signature 에 들어갈 secret값 세팅
        //         .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}