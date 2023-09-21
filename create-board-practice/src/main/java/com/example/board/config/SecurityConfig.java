package com.example.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// spring boot 2.7 이후로 변화가 많이 있었음.
// 최신 인텔리제이에서 오류가 많았음.
// 그 중 하나가. auto-configuration 오류 -> 경로가 바꼈음.
// web security -> filter security 로 바꼈음.
//public class SecurityConfig extends WebSecurityConfigureAdapter{ // 원래방식.
//@WebSecurity // 넣어도되고 안넣어도됨.
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth.anyRequest().permitAll())
                .formLogin(Customizer.withDefaults());
        return http.build();
    }
}
