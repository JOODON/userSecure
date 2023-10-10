package com.example.config;

import com.example.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;


@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable() //csrf 비활성화
                .httpBasic().disable() //token 사용 basic 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //세션 기반 X 선언
                .and()
                .authorizeRequests().antMatchers("/","/auth/**") //위와 같은 경로는 접근 허용
                .permitAll()
                .anyRequest().authenticated(); //그 외는 전부다 인증 설정

        //매 요청 마다 CorsFilter.class 를 실행하고 JWT 필터 를 실행함
        //스프링 프레임워크에서 지원해주는 클래스를 사용할거!

        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);

    }
}
