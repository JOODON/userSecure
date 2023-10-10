package com.example.provider;

import com.example.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "FIRpX30pMaDbiAkm1fArbrmVkDD4RqISskGZmBFax50GVxzXXWUzTR5JyskiHMTV9M10icegkpi46AdvrcX1E6CmTUBc6IFbTPiD";

    public String create(UserEntity userEntity) {
        // 토큰 만료일 설정 (현재 시간에서 1일 후로 설정)
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)
        );

        // JWT 토큰 생성
        return Jwts.builder()
                // HEADER 에 들어갈 내용 설정
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // 서명 알고리즘과 시크릿 키 설정
                // PAYLOAD 에 들어갈 내용 설정
                .setSubject(userEntity.getId()) // 토큰 주제 (일반적으로 사용자 ID 등)
                .setIssuer("demo app") // 토큰 발급자 설정
                .setIssuedAt(new Date()) // 토큰 발급 시간 설정
                .setExpiration(expiryDate) // 토큰 만료 시간 설정
                .compact();
    }


    //토큰 파싱 및 위조 여부 관리
    public String validateAndGetUserId(String token) {
        try {
            // 토큰을 파싱하고 서명을 확인하며 토큰의 내용을 얻어옴
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY) // 토큰 서명을 확인하기 위해 사용되는 시크릿 키
                    .parseClaimsJws(token) // JWT 토큰을 파싱
                    .getBody(); // 토큰의 내용을 얻어옴

            // 토큰의 주제 (Subject)에 포함된 사용자 ID 반환
            return claims.getSubject();
        } catch (Exception e) {
            // 토큰이 유효하지 않거나 검증에 실패한 경우 예외가 발생할 수 있음
            // 여기서는 예외가 발생하면 null을 반환하여 유효하지 않은 토큰을 나타냄
            return null;
        }
    }

}
