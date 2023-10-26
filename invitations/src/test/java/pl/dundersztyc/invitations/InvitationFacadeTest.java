package pl.dundersztyc.invitations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dundersztyc.accounts.AccountQueryRepository;
import pl.dundersztyc.accounts.dto.AccountDto;
import pl.dundersztyc.common.events.EventPublisher;
import pl.dundersztyc.invitations.dto.InvalidInvitationException;
import pl.dundersztyc.invitations.dto.InvitationDto;
import pl.dundersztyc.invitations.dto.InvitationRequest;
import pl.dundersztyc.invitations.dto.InvitationStatus;
import pl.dundersztyc.invitations.events.InvitationAcceptedEvent;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class InvitationFacadeTest {

    private final Clock clock = Clock.fixed(Instant.parse("2018-08-22T10:00:00Z"), ZoneOffset.UTC);

    private AccountQueryRepository accountQueryRepository;
    private EventPublisher eventPublisher;
    private InvitationFacade invitationFacade;

    @BeforeEach
    public void setUp() {
        accountQueryRepository = mock(AccountQueryRepository.class);
        eventPublisher = mock(EventPublisher.class);
        invitationFacade = new InvitationConfiguration().invitationFacade(
                new InMemoryInvitationRepository(), accountQueryRepository, eventPublisher, clock);
        givenAccountsExist();
    }

    @Test
    void shouldAddInvitation() {
        var invitationDto = addInvitation("from", "to");

        assertThat(invitationDto.id()).isNotNull();
        assertThat(invitationDto.senderId()).isEqualTo("from");
        assertThat(invitationDto.receiverId()).isEqualTo("to");
    }

    @Test
    void shouldThrowWhenAddInvitationAndReceiverIsEqualToSender() {
        assertThrows(InvalidInvitationException.class,
                () -> addInvitation("from", "from"));
    }

    @Test
    void shouldThrowWhenAddInvitationAndAccountDoesNotExist() {
        givenAccountsDoesNotExist();

        assertThrows(InvalidInvitationException.class,
                () -> addInvitation("from", "to"));
    }

    @Test
    void shouldThrowWhenInvitationExist() {
        addInvitation("from", "to");

        assertThrows(InvalidInvitationException.class,
                () -> addInvitation("from", "to"));
        assertThrows(InvalidInvitationException.class,
                () -> addInvitation("to", "from"));
    }

    @Test
    void shouldAcceptInvitation() {
        var invitationDto = addInvitation("from", "to");

        var accepted = invitationFacade.acceptInvitation(invitationDto.id(), "to");

        verify(eventPublisher).raise(any(InvitationAcceptedEvent.class));
        assertThat(accepted.status()).isEqualTo(InvitationStatus.ACCEPTED);
    }

    @Test
    void shouldThrowWhenAcceptNotYourInvitation() {
        var invitationDto = addInvitation("from", "to");

        assertThrows(IllegalStateException.class,
                () -> invitationFacade.acceptInvitation(invitationDto.id(), "invalidId"));
    }

    @Test
    void shouldThrowWhenAcceptNotPendingInvitation() {
        var invitationDto = addInvitation("from", "to");
        invitationFacade.acceptInvitation(invitationDto.id(), invitationDto.receiverId());

        assertThrows(IllegalStateException.class,
                () -> invitationFacade.acceptInvitation(invitationDto.id(), invitationDto.receiverId()));
    }

    @Test
    void shouldDeclineInvitation() {
        var invitationDto = addInvitation("from", "to");

        var declined = invitationFacade.declineInvitation(invitationDto.id(), "to");

        assertThat(declined.status()).isEqualTo(InvitationStatus.DECLINED);
    }

    @Test
    void shouldThrowWhenDeclineNotYourInvitation() {
        var invitationDto = addInvitation("from", "to");

        assertThrows(IllegalStateException.class,
                () -> invitationFacade.declineInvitation(invitationDto.id(), "invalidId"));
    }

    @Test
    void shouldThrowWhenDeclineNotPendingInvitation() {
        var invitationDto = addInvitation("from", "to");
        invitationFacade.declineInvitation(invitationDto.id(), invitationDto.receiverId());

        assertThrows(IllegalStateException.class,
                () -> invitationFacade.declineInvitation(invitationDto.id(), invitationDto.receiverId()));
    }

    @Test
    void shouldDeleteInvitation() {
        var invitationDto = addInvitation("from", "to");

        invitationFacade.deleteInvitation(new InvitationRequest("from", "to"));

        // we can create same invitation when previous one is deleted
        addInvitation("from", "to");
    }

    private InvitationDto addInvitation(String idFrom, String idTo) {
        var request = new InvitationRequest(idFrom, idTo);
        return invitationFacade.addInvitation(request);
    }

    private void givenAccountsExist() {
        given(accountQueryRepository.findAccountById(any(String.class)))
                .willReturn(new AccountDto("id", "username"));
    }

    private void givenAccountsDoesNotExist() {
        given(accountQueryRepository.findAccountById(any(String.class)))
                .willReturn(null);
    }



}