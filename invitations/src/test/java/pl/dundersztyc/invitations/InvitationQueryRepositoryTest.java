package pl.dundersztyc.invitations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dundersztyc.invitations.dto.InvitationStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class InvitationQueryRepositoryTest {

    private InMemoryInvitationRepository invitationRepository;
    private InvitationQueryRepository invitationQueryRepository;

    @BeforeEach
    void setUp() {
        invitationRepository = new InMemoryInvitationRepository();
        invitationQueryRepository = new InvitationConfiguration().invitationQueryRepository(invitationRepository);
    }

    @Test
    void shouldFindAllSentInvitations() {
        saveInvitation("id1", "id2", InvitationStatus.ACCEPTED);
        saveInvitation("id1", "id3", InvitationStatus.DECLINED);
        saveInvitation("id1", "id4", InvitationStatus.PENDING);
        saveInvitation("id2", "id3", InvitationStatus.DECLINED);

        var invitationsSent = invitationQueryRepository.
                findSentInvitationsWithStatuses("id1", List.of(InvitationStatus.values()));

        assertThat(invitationsSent).hasSize(3);
    }

    @Test
    void shouldFindSentInvitationsWithStatuses() {
        saveInvitation("id1", "id2", InvitationStatus.ACCEPTED);
        saveInvitation("id1", "id3", InvitationStatus.DECLINED);
        saveInvitation("id1", "id4", InvitationStatus.PENDING);
        saveInvitation("id2", "id3", InvitationStatus.DECLINED);

        var invitationsSent = invitationQueryRepository
                .findSentInvitationsWithStatuses("id1", List.of(InvitationStatus.ACCEPTED, InvitationStatus.DECLINED));

        assertThat(invitationsSent).hasSize(2);
    }

    @Test
    void shouldFindAllReceivedInvitations() {
        saveInvitation("id2", "id1", InvitationStatus.ACCEPTED);
        saveInvitation("id3", "id1", InvitationStatus.DECLINED);
        saveInvitation("id4", "id1", InvitationStatus.PENDING);
        saveInvitation("id1", "id5", InvitationStatus.DECLINED);

        var invitationsSent = invitationQueryRepository.
                findReceivedInvitationsWithStatuses("id1", List.of(InvitationStatus.values()));

        assertThat(invitationsSent).hasSize(3);
    }

    @Test
    void shouldFindReceivedInvitationsWithStatuses() {
        saveInvitation("id2", "id1", InvitationStatus.ACCEPTED);
        saveInvitation("id3", "id1", InvitationStatus.DECLINED);
        saveInvitation("id4", "id1", InvitationStatus.PENDING);
        saveInvitation("id1", "id5", InvitationStatus.DECLINED);

        var invitationsSent = invitationQueryRepository
                .findReceivedInvitationsWithStatuses("id1", List.of(InvitationStatus.ACCEPTED, InvitationStatus.DECLINED));

        assertThat(invitationsSent).hasSize(2);
    }

    private void saveInvitation(String idFrom, String idTo, InvitationStatus status) {
        invitationRepository.save(new Invitation(null, idFrom, idTo, LocalDateTime.now(), status));
    }

}