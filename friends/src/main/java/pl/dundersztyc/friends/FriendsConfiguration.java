package pl.dundersztyc.friends;

import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import pl.dundersztyc.common.events.EventPublisher;

@Configuration
class FriendsConfiguration {

    @Bean
    CustomFriendshipSearchRepository customFriendshipSearchRepository(Neo4jTemplate template) {
        return new CustomFriendshipSearchRepositoryImpl(template);
    }

    @Bean
    FriendshipFacade friendshipFacade(FriendshipRepository friendshipRepository, EventPublisher eventPublisher) {
        return new FriendshipFacade(friendshipRepository, eventPublisher);
    }

    @Bean
    FriendshipQueryRepository friendshipQueryRepository(FriendshipRepository friendshipRepository) {
        return new FriendshipQueryRepository(friendshipRepository);
    }

    @Bean
    org.neo4j.cypherdsl.core.renderer.Configuration configuration() {
        return org.neo4j.cypherdsl.core.renderer.Configuration.newConfig().withDialect(Dialect.NEO4J_5).build();
    }
}
