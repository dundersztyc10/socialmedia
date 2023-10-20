package pl.dundersztyc.invitations;

import lombok.RequiredArgsConstructor;
import pl.dundersztyc.invitations.dto.InvitationDto;
import pl.dundersztyc.invitations.dto.InvitationStatus;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InvitationQueryRepository {

    private final InvitationRepository invitationRepository;

    public List<InvitationDto> findSentInvitationsWithStatuses(String accountId, List<InvitationStatus> requestedStatuses) {
        return invitationRepository.findBySenderIdAndStatusIn(accountId, requestedStatuses).stream()
                .map(invitation -> new InvitationDto(invitation.getId(), invitation.getSenderId(), invitation.getReceiverId(), invitation.getStatus()))
                .collect(Collectors.toList());
    }

    public List<InvitationDto> findReceivedInvitationsWithStatuses(String accountId, List<InvitationStatus> requestedStatuses) {
        return invitationRepository.findByReceiverIdAndStatusIn(accountId, requestedStatuses).stream()
                .map(invitation -> new InvitationDto(invitation.getId(), invitation.getSenderId(), invitation.getReceiverId(), invitation.getStatus()))
                .collect(Collectors.toList());
    }

}
