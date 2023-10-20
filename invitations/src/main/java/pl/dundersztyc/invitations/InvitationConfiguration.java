package pl.dundersztyc.invitations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dundersztyc.accounts.AccountQueryRepository;

import java.time.Clock;

@Configuration
class InvitationConfiguration {

    @Bean
    public InvitationFacade invitationFacade(InvitationRepository invitationRepository,
                                             AccountQueryRepository accountQueryRepository,
                                             Clock clock) {
        return new InvitationFacade(invitationRepository, accountQueryRepository, clock);
    }


    @Bean
    public InvitationQueryRepository invitationQueryRepository(InvitationRepository invitationRepository) {
        return new InvitationQueryRepository(invitationRepository);
    }
}
