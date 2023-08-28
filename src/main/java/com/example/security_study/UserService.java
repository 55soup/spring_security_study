package com.example.security_study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// UserDetailsService를 구현하는 서비스 클래스 작성
@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository repository;

    // loadUserByUsername 메소드 오버라이드
    // (기본적으로 해당 메소드의 반환 타입은 UserDetails이지만 여기서는 UserDetails 상속받은 User로 반환 타입을 지정 (다운 캐스팅되어 반환됨))
    // 공변 반환 타입 (https://www.baeldung.com/java-covariant-return-type)
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);

        if(user == null) {
            // 사용자 찾기에 실패한 경우 UsernameNotFoundException 예외를 던지기
            throw new UsernameNotFoundException(email);
        } else {
            return user;
        }
    }
}
