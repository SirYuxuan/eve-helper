package com.yuxuan66;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动
 * @author Sir丶雨轩
 * @since 2021/06/17
 */
@SpringBootApplication
@ServletComponentScan
@EnableAsync
@EnableScheduling
public class ConsoleApp {

    public static void main(String[] args) {
        SpringApplication.run(ConsoleApp.class,args);
    }
}
