package com.monk;

import com.monk.service.FileServiceI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MonkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonkApplication.class);
    }
}