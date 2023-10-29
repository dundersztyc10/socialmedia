package pl.dundersztyc.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"pl.dundersztyc.**"})
@EntityScan(basePackages = {"pl.dundersztyc.**"})
@EnableMongoRepositories(basePackages = {"pl.dundersztyc.**"})
@EnableNeo4jRepositories(basePackages = {"pl.dundersztyc.**"})
@EnableJpaRepositories(basePackages = {"pl.dundersztyc.**"})
public class SocialMediaApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(SocialMediaApplication.class, args);
        System.out.println(ctx);
    }

}