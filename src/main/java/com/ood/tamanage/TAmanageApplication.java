package com.ood.tamanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ood.tamanage.mapper")
public class TAmanageApplication {

    public static void main(String[] args) {
        SpringApplication.run(TAmanageApplication.class, args);
    }

}
