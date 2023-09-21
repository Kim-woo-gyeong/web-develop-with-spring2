package com.example.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CreateBoardPracticeApplication {

    public static void main(String[] args) {
        // 인텔리제이 이전 버전에서 run 이 주황밀줄이 쳐지는 버그가 발생했었음.
        // 현재는 없어짐.
        SpringApplication.run(CreateBoardPracticeApplication.class, args);
    }

}
