package io.github.cc53453;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;

import io.github.cc53453.sm4.annotation.EnableSm4Encrypt;
import lombok.extern.slf4j.Slf4j;

@EnableConfigurationProperties
@ComponentScan("io.github.cc53453")
@EnableSm4Encrypt(filesPath = {"classpath:application.yml"})
@SpringBootApplication
@Slf4j
@EnableAutoConfiguration(exclude = {MybatisPlusAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class Main implements CommandLineRunner {
    @Value("${test.password:demo}")
    String testPassword;
    
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.warn("test.password: {}", testPassword);
    }
}
