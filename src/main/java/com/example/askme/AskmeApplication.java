package com.example.askme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AskmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AskmeApplication.class, args);
    }
}
