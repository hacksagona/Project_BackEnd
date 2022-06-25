package com.project.hack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HackApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackApplication.class, args);
    }

}
