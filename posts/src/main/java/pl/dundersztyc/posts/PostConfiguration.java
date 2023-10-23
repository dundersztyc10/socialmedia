package pl.dundersztyc.posts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class PostConfiguration {

    @Bean
    PostFacade postFacade(PostRepository postRepository, Clock clock) {
        var postMapper = new PostMapper();
        return new PostFacade(postRepository, clock, postMapper);
    }

    @Bean
    PostQueryRepository postQueryRepository(PostRepository postRepository) {
        var postMapper = new PostMapper();
        return new PostQueryRepository(postRepository, postMapper);
    }
}
