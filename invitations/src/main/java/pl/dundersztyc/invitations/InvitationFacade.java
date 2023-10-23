package pl.dundersztyc.invitations;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.dundersztyc.accounts.AccountQueryRepository;
import pl.dundersztyc.common.events.EventPublisher;
import pl.dundersztyc.invitations.dto.*;
import pl.dundersztyc.invitations.events.InvitationAcceptedEvent;

import java.time.Clock;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class InvitationFacade {

    private final InvitationRepository invitationRepository;
    private final AccountQueryRepository accountQueryRepository;
    private final EventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public InvitationDto addInvitation(InvitationRequest request) {
        if (request.senderId().equals(request.receiverId())) {
            throw new InvalidInvitationException("the receiver cannot be the same as the sender");
        }
        if (!doAccountsExist(request.senderId(), request.receiverId())) {
            throw new InvalidInvitationException("recipient or sender does not exist");
        }
        if (doesInvitationExistBetweenAccounts(request.senderId(), request.receiverId())) {
            throw new InvalidInvitationException("invitation exist");
        }
        Invitation invitation = Invitation.withoutId(
                request.senderId(), request.receiverId(), LocalDateTime.now(clock), InvitationStatus.PENDING);
        var saved = invitationRepository.save(invitation);
        return new InvitationDto(saved.getId(), saved.getSenderId(), saved.getReceiverId(), saved.getStatus());
    }

    @Transactional
    public InvitationDto acceptInvitation(String invitationId, String accountId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationDoesNotExistException("invalid invitationId"));
        if (!invitation.getReceiverId().equals(accountId)) {
            throw new IllegalStateException("cannot accept not your invitation");
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new IllegalStateException("cannot accept not pending invitation");
        }
        invitation.setStatus(InvitationStatus.ACCEPTED);
        var saved = invitationRepository.save(invitation);
        eventPublisher.raise(new InvitationAcceptedEvent(clock.instant(), saved.getSenderId(), saved.getReceiverId()));
        return new InvitationDto(saved.getId(), saved.getSenderId(), saved.getReceiverId(), saved.getStatus());
    }

    @Transactional
    public InvitationDto declineInvitation(String invitationId, String accountId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationDoesNotExistException("invalid invitationId"));
        if (!invitation.getReceiverId().equals(accountId)) {
            throw new IllegalStateException("cannot accept not your invitation");
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new IllegalStateException("cannot decline not pending invitation");
        }
        invitation.setStatus(InvitationStatus.DECLINED);
        var saved = invitationRepository.save(invitation);
        return new InvitationDto(saved.getId(), saved.getSenderId(), saved.getReceiverId(), saved.getStatus());
    }

    public void deleteInvitation(InvitationRequest request) {
        Invitation invitation = findInvitationBetweenAccounts(request.senderId(), request.receiverId());
        invitationRepository.delete(invitation);
    }

    private Invitation findInvitationBetweenAccounts(String senderId, String receiverId) {
        return invitationRepository.findInvitationBetweenAccounts(senderId, receiverId).orElseThrow(EntityNotFoundException::new);
    }

    private boolean doAccountsExist(String idFrom, String idTo) {
        return accountQueryRepository.findAccountById(idFrom) != null &&
                accountQueryRepository.findAccountById(idTo) != null;
    }

    private boolean doesInvitationExistBetweenAccounts(String idFrom, String idTo) {
        return invitationRepository.findInvitationBetweenAccounts(idFrom, idTo).isPresent();
    }

}
