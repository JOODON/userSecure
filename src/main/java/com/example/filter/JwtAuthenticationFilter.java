package com.example.filter;

import com.example.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Authorization 헤더에서 Bearer 토큰 추출
            String token = parseBearerToken(request);
            log.info("Filter is running");

            if (token != null && !token.equalsIgnoreCase("null")) {
                // 사용자 ID 가져오기
                String userID = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated User ID: " + userID);

                // 사용자를 인증하는 토큰 생성 (일반적으로 UserDetails 객체를 사용)
                AbstractAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userID, null, AuthorityUtils.NO_AUTHORITIES);

                // 인증된 사용자의 인증 정보 설정
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                // 보안 컨텍스트에 인증 정보 설정
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
        }

        // 다음 필터로 이동 또는 요청 처리 계속
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        // Authorization 헤더에서 Bearer 토큰 추출
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
