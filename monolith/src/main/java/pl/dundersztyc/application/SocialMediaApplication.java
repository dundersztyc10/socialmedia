package pl.dundersztyc.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import pl.dundersztyc.friends.FriendshipFacade;

@SpringBootApplication
@ComponentScan(basePackages = {"pl.dundersztyc.**"})
@EntityScan(basePackages = {"pl.dundersztyc.**"})
@EnableMongoRepositories(basePackages = {"pl.dundersztyc.**"})
@EnableNeo4jRepositories(basePackages = {"pl.dundersztyc.**"})
public class SocialMediaApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(SocialMediaApplication.class, args);
        System.out.println(ctx);
    }

}