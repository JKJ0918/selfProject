package com.single.sng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
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

    /*@Bean
    public RoleHierarchy roleHierarchy() {

        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();

        hierarchy.setHierarchy("ROLE_C > ROLE_B\n" + // 자동으로 계층의 등급을 인식함 아래 SecurityFilterChain 에 권한 부여할때 좋음
                "ROLE_B > ROLE_A");

        return hierarchy;
    }*/


    // 접근권한 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated() // 이외에 경로는 ~
                );

        httpSecurity // FormLogin 방식
                .formLogin((auth)->auth.loginPage("/login") // 인증이 필요한 페이지에 접근했지만 로그인 하지 않은 경우 redirect
                        .loginProcessingUrl("/loginProc") // 로그인 요청을 처리할 URL (스프링 시큐리티가 해당 URL로 들어오는 요청을 가로채고 인증을 처리함)
                        .failureUrl("/login?error=true") // 실패 시 리다이렉션 경로 2025 01 24 (로그인 과 연관 x)
                        .defaultSuccessUrl("/") // 성공 시 리다이렉션 경로 2025 01 24 (로그인 과 연관 x)
                        .permitAll()
                );

        // 로그아웃 설정 추가
        httpSecurity
                .logout((auth) -> auth
                        .logoutUrl("/logout") // 로그아웃 URL (기본값: /logout)
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 페이지
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .permitAll()
                );

        // httpSecurity // httpBasic 로그인 방식
                // .httpBasic(Customizer.withDefaults());


        // httpSecurity
        //         .csrf((auth) -> auth.disable()); // post 요청시 요구하는 csrf token 임시 무효

                        
        httpSecurity // 중복 로그인 구현
                .sessionManagement((auth) -> auth
                        .maximumSessions(1) // 다중 로그인 허용수 입력
                        .maxSessionsPreventsLogin(true)); // true - 초과시 새로운 로그인 차단 / false - 초과시 기존 세션 하나 삭제

        httpSecurity // 세션 고정 보호
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());



        return httpSecurity.build();
    }

}
