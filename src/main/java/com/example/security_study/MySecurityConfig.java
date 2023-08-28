package com.example.security_study;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MySecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize ->
                authorize
                        // 회원 가입, 회원 가입 처리 주소 접근 허용
                        .requestMatchers("/sign_up", "/sign_up_process").permitAll()
                        // 세션 관리 관련 주소 접근 허용
                        .requestMatchers("/session_expired").permitAll()
                        /* 기존 코드 */
                        .requestMatchers("/public_resource").permitAll()
                        .requestMatchers("/user_resource").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers("/admin_resource").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
        );
        // CSRF 설정 비활성화
        // https://stackoverflow.com/questions/35787798/logoutsuccessurl-not-working-in-spring-boot
        http.csrf(csrfConfigurer -> csrfConfigurer.disable());
        // 폼을 이용한 로그인 관련 설정
        http.formLogin(formLoginConfigurer -> {
            formLoginConfigurer
                    // (로그인 관련 폼을 보여줄) 로그인 페이지 주소
                    .loginPage("/login")
                    .usernameParameter("username") // input name 속성에 username 라고 써야함.
                    .passwordParameter("password")
                    .loginProcessingUrl("/login_proc") // actions url에 "/login_proc" 써야함
                    .defaultSuccessUrl("/user/hello") // login후 이동하는 redirect url
                    .failureUrl("/login?error") // (잘못된 아이디 혹은 비번 입력 등의 이유로) 로그인 실패한 후 리다이렉트 주소 설정
                    // 로그인 주소는 인증 없이도 접속 허용
                    .permitAll();
        });
        // 로그아웃 관련 설정
        http.logout(logoutConfigurer -> {
            logoutConfigurer
                    // "/my_logout" 주소로 이동할 경우 로그아웃 진행
                    // (단, 핸들러 구현을 직접하지는 않고 내부적으로 스프링에서 해당 주소로 접근할 경우 로그아웃 처리)
                    .logoutUrl("/my_logout")
                    .logoutSuccessUrl("/logout_success")
                    .clearAuthentication(true) // 유저정보 지우기
                    .invalidateHttpSession(true) // 세션 정보 삭제
                    .deleteCookies("JSESSIONID") // 로그아웃 시점에 쿠키 정보(세션 ID 정보) 삭제
                    // 로그아웃 주소는 인증 없이도 접속 허용
                    .permitAll();
        });

        return http.build();
    }
        @Bean
        public UserService userDetailsService() {
            return new UserService();
        }

        @Bean
        public AuthenticationManager authenticationManager() {
            // 인증 과정에서 UserDetailsService 구현체를 사용하는 DaoAuthenticationProvider 사용
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            // UserDetailsService, PasswordEncoder Bean 객체 제공
            authProvider.setUserDetailsService(userDetailsService());
            authProvider.setPasswordEncoder(passwordEncoder());
            return new ProviderManager(authProvider);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }

}
