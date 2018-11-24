package com.github.hollykunge.security.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hollykunge
 * @date 2017年08月25日
 */
@SpringBootApplication
@MapperScan("com.github.hollykunge.security.generator.mapper")
public class GeneratorBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(GeneratorBootstrap.class, args);
    }
}
