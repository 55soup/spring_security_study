package com.example.security_study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller // view return
public class MyController2 {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout_success")
    public String logoutSuccess() {
        return "logout_success";
    }

    @GetMapping("/user/hello")
    public String hello(
            Principal principal,
            @AuthenticationPrincipal User user,
            Model m) {
        m.addAttribute("user", user);
        return "hello";
    }

    @RequestMapping("/sign_up")
    public String signUp() {
        return "sign_up";
    }

    @PostMapping("/sign_up_process")
    public RedirectView signUpProcess(
            @RequestParam("username") String userName, // query string, form data도 받아올 수 있음
            @RequestParam("password") String password,
            @RequestParam("roles") ArrayList<String> roles) { // 회원 권한을 배열 형식으로
        String result = roles.stream().map(r -> "ROLE_" + r).collect(Collectors.joining(","));

        // *** 패스워드 인코더 객체를 통해서 패스워드를 해싱한 후 저장
        userRepository.save(new User(userName, passwordEncoder.encode(password), result));

        return new RedirectView("/login");
    }
}
