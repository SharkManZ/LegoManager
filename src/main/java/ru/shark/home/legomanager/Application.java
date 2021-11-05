package ru.shark.home.legomanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.shark.home.legomanager")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
