package pl.dundersztyc.posts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dundersztyc.friends.FriendshipQueryRepository;

import java.time.Clock;

@Configuration
class PostViewerConfiguration {

    @Bean
    PostViewerQueryRepository postViewerQueryRepository(PostMetricsRepository postMetricsRepository,
                                                        FriendshipQueryRepository friendshipQueryRepository,
                                                        Clock clock) {
        var postMapper = new PostMapper();
        return new PostViewerQueryRepository(postMetricsRepository, friendshipQueryRepository, postMapper, clock);
    }

}
