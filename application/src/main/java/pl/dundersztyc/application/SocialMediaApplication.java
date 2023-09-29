package pl.dundersztyc.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan
@EnableAutoConfiguration
public class SocialMediaApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(SocialMediaApplication.class, args);
        System.out.println(ctx);
    }

}