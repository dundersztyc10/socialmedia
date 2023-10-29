package pl.dundersztyc.friendsnearby;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dundersztyc.friends.FriendshipQueryRepository;

import java.time.Clock;

@Configuration
class FriendsNearbyConfiguration {

    @Bean
    AccountLocationQueryRepository accountLocationQueryRepository(AccountLocationRepository accountLocationRepository,
                                                                  FriendshipQueryRepository friendshipQueryRepository,
                                                                  Clock clock) {
        return new AccountLocationQueryRepository(accountLocationRepository, friendshipQueryRepository, clock);
    }

    @Bean
    AccountLocationFacade accountLocationFacade(AccountLocationRepository accountLocationRepository,
                                                Clock clock) {
        return new AccountLocationFacade(accountLocationRepository, clock);
    }
}
