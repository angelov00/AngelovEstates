package com.softuni.angelovestates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AngelovEstatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(AngelovEstatesApplication.class, args);
    }
}
