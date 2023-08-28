package com.example.security_study;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
// 세션 내부에서 User 객체를 키로 사용하므로 반드시 equals와 hashcode 메소드를 구현해야 함
@EqualsAndHashCode
// UserDetails 인터페이스 구현하는 User 클래스 정의
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(unique=true)
    @NotNull
    private String email;

    @NonNull
    @NotNull
    private String password;

    @NonNull
    @NotNull
    private String userAuthorities;

    // 추가 정보 저장용 필드 (세션에 저장할 사용자 정보)
    @Transient
    private String extraInfo;

    // 해당 사용자가 가지고 있는 모든 역할 정보를 반환할 getAuthorities 메소드 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> roles = new ArrayList<>();

        for(String role : userAuthorities.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }

        return roles;
    }

    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return getEmail(); }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
