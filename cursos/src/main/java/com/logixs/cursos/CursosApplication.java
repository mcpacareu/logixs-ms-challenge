package com.logixs.cursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.logixs.cursos.rest.client")
public class CursosApplication {

    public static void main(String[] args) {
        SpringApplication.run(CursosApplication.class, args);
    }

}
