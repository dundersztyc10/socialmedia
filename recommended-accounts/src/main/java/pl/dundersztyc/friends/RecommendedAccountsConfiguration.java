package pl.dundersztyc.friends;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RecommendedAccountsConfiguration {

    @Bean
    RecommendedAccountsQueryRepository recommendedAccountsQueryRepository(RecommendedAccountsRepository recommendedAccountsRepo) {
        return new RecommendedAccountsQueryRepository(recommendedAccountsRepo);
    }

}
