package ru.practicum.ewm;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan(basePackages = {"ru.practicum.ewm", "ru.practicum.client", "ru.practicum.dto"})
@EnableAspectJAutoProxy
@Configuration
@SpringBootApplication
public class ExploreWithMeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMeServiceApplication.class, args);
    }

}
