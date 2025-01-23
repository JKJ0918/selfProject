package com.single.sng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 클래스 등록
@EnableWebSecurity // 스프링 시큐리티에서 관리된다.
public class SecurityConfig {

    // BCrypt 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 접근권한 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .requestMatchers("/my/**").hasAnyRole("ADMIN","USER")
                    .anyRequest().authenticated()
                );

        httpSecurity
                .formLogin((auth)->auth.loginPage("/login") // 인증이 필요한 페이지에 접근했지만 로그인 하지 않은 경우 redirect
                        .loginProcessingUrl("loginProc") // 로그인 요청을 처리할 URL (스프링 시큐리티가 해당 URL로 들어오는 요청을 가로채고 인증을 처리함)
                        .permitAll()
                );


        httpSecurity
                .csrf((auth) -> auth.disable()); // post 요청시 요구하는 csrf token 임시 무효

        return httpSecurity.build();
    }

}
