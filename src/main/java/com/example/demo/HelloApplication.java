package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource; 

@SpringBootApplication 
@SpringBootConfiguration
//@PropertySource(value = {"classpath:implementation.properties"},ignoreResourceNotFound=true)
public class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }
}
