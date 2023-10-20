package pl.dundersztyc.invitations.infrastructure.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import pl.dundersztyc.accountcreator.infrastructure.web.AbstractIntegrationTest;
import pl.dundersztyc.invitations.InvitationFacade;
import pl.dundersztyc.invitations.InvitationQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;

class InvitationControllerTest extends AbstractIntegrationTest {

    private static final String INVITATION_URL = "/api/v1/invitations";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private InvitationFacade invitationFacade;

    @Autowired
    private InvitationQueryRepository invitationQueryRepo;

    @BeforeEach
    void setUp() {
        mongoTemplate.getDb().drop();
    }

    @Test
    @WithMockUser
    void shouldAcceptInvitation() {
        assertThat(true).isFalse();
    }



}
