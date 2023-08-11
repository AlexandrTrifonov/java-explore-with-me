package ru.practicum.mainservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.client", "ru.practicum.mainservice"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}