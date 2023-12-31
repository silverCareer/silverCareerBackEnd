package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("com.example.demo.global.config")
@SpringBootApplication
public class SilvercareerApplication {

    public static void main(String[] args) {

        SpringApplication.run(SilvercareerApplication.class, args);
        System.out.println("Build Success");
    }
}
