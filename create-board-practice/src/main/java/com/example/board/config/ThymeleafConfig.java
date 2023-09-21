package com.example.board.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class ThymeleafConfig {
    // application.yml 에서 프로퍼티스로 만들지 않고
    // bean 으로 생성하였음.
    // 왜냐하면 spring properties 에서 기능을 제공하지 않기 때문에.. 이유는 모르겠음.
    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver(
            SpringResourceTemplateResolver defaultTemplateResolver,
            Thymeleaf3Properties thymeleaf3Properties
    ) {
        defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.isDecoupledLogic());

        return defaultTemplateResolver;
    }


    @RequiredArgsConstructor
    @Getter
//    @ConstructorBinding
    @ConfigurationProperties("spring.thymeleaf3")
    public static class Thymeleaf3Properties {
        /**
         * Use Thymeleaf 3 Decoupled Logic
         */
        private final boolean decoupledLogic;

        // 생성자
        // @RequiredArgsConstructor 대체
//        public Thymeleaf3Properties(boolean decoupledLogic) {
//            this.decoupledLogic = decoupledLogic;
//        }

        // getter
        // @Getter 로 대체
//        public boolean isDecoupledLogic() {
//            return this.decoupledLogic;
//        }
    }
}
