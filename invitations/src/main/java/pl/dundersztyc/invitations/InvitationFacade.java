package pl.dundersztyc.invitations;

import lombok.RequiredArgsConstructor;
import pl.dundersztyc.accounts.AccountQueryRepository;
import pl.dundersztyc.invitations.dto.*;

import java.time.Clock;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class InvitationFacade {

    private final InvitationRepository invitationRepository;
    private final AccountQueryRepository accountQueryRepository;
    private final Clock clock;

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

    public InvitationDto acceptInvitation(String invitationId, String accountId) {
        // TODO: call friend facade / publish event +
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
        return new InvitationDto(saved.getId(), saved.getSenderId(), saved.getReceiverId(), saved.getStatus());
    }

    public InvitationDto declineInvitation(String invitationId, String accountId) {
        // TODO: call friend facade / publish event +
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

    private boolean doAccountsExist(String idFrom, String idTo) {
        return accountQueryRepository.findAccountById(idFrom) != null &&
                accountQueryRepository.findAccountById(idTo) != null;
    }

    private boolean doesInvitationExistBetweenAccounts(String idFrom, String idTo) {
        return invitationRepository.existsBySenderIdAndReceiverId(idFrom, idTo) ||
                invitationRepository.existsBySenderIdAndReceiverId(idTo, idFrom);

    }

}
