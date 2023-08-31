package com.example.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {
    public AuditorAware<String> auditorAware(){
        return () -> Optional.of("kwgyeong"); // TODO:스프링시큐리티로 인증기능을 사용할때 수정예정.
    }
}
