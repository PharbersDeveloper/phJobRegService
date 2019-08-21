package com.pharbers.spark.driver.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class Application {
//todo： springBoot配置log4j2，对过程加入log，过滤器等
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
