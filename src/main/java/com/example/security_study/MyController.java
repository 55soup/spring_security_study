package com.example.security_study;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
public class MyController {
    // 로그인 후 접근 가능
    @GetMapping("/resource")
    public String resource() {
        return "resource";
    }

    // Authentication 객체를 통해서 유저 정보 접근 가능
    @GetMapping("/principal")
    public String principal(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        // 유저 이름, 패스워드 출력
        // (단, 보안을 위해서 패스워드는 로그인 이후 메모리에서 삭제되므로 null이 출력됨)
        return user.getUsername() + "," + user.getPassword();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        // UserDetailsService 임시 구현체 반환
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                // 일반적으로 여기서 username을 이용하여 데이터베이스에서 유저를 가져오고 반환하는 작업을 진행함
//                // (만약 유저를 찾지 못하면 UsernameNotFoundException 예외를 던져줘야 함)
//                // 여기서는 임시로 유저 정보를 만들어 반환함
//                UserDetails user = User
//                        .withUsername("hello") // 유저 이름
//                        .password("1234") // 패스워드
//                        .roles("user") // 인가 수준 / 권한.
//                        .build();
//                return user;
//            }
//        };
//    }

//    // 기존의 UserDetailService 구현 관련 메서드을 제거하고 다음으로 대체
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        // 두 명의 테스트용 유저 추가
//        UserDetails user = User.builder()
//                .username("user")
//                .password("1111")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("2222")
//                .roles("USER", "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // 기본적으로는 BCryptPasswordEncoder가 사용되지만 여기서는 아무런 부호화(인코딩), 복호화(디코딩) 작업을 진행하지 않는 NoOpPasswordEncoder 사용
//        // (이 경우 암호가 평문으로 저장됨)
//        return NoOpPasswordEncoder.getInstance();
//    }

    // 인증 과정 없이도 접근 가능
    @GetMapping("/public/resource")
    public String res1() {
        return "/public/resource";
    }
    @GetMapping("/public/nested/resource")
    public String res2() {
        return "/public/nested/resource";
    }

    // USER 권한이 있어야 접근 가능
    @GetMapping("/user/resource")
    public String res3() {
        return "/user/resource";
    }
    @GetMapping("/user/nested/resource")
    public String res4() {
        return "/user/nested/resource";
    }

    // ADMIN 권한이 있어야 접근 가능
    @GetMapping("/admin/resource")
    public String res5() {
        return "/admin/resource";
    }
    @GetMapping("/admin/nested/resource")
    public String res6() {
        return "/admin/nested/resource";
    }

}

