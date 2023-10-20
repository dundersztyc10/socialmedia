package pl.dundersztyc.invitations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dundersztyc.accounts.AccountQueryRepository;
import pl.dundersztyc.common.events.EventPublisher;

import java.time.Clock;

@Configuration
class InvitationConfiguration {

    @Bean
    public InvitationFacade invitationFacade(InvitationRepository invitationRepository,
                                             AccountQueryRepository accountQueryRepository,
                                             EventPublisher eventPublisher,
                                             Clock clock) {
        return new InvitationFacade(invitationRepository, accountQueryRepository, eventPublisher, clock);
    }


    @Bean
    public InvitationQueryRepository invitationQueryRepository(InvitationRepository invitationRepository) {
        return new InvitationQueryRepository(invitationRepository);
    }
}
