package pl.dundersztyc.invitations;

import pl.dundersztyc.invitations.dto.InvitationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class InMemoryInvitationRepository implements InvitationRepository {

    ConcurrentHashMap<String, Invitation> invitations = new ConcurrentHashMap<>();

    @Override
    public Invitation save(Invitation invitation) {
        var id = UUID.randomUUID().toString();
        invitations.put(id, invitation);
        return new Invitation(
                id,
                invitation.getSenderId(),
                invitation.getReceiverId(),
                invitation.getDate(),
                invitation.getStatus()
        );
    }

    @Override
    public Optional<Invitation> findById(String id) {
        return Optional.of(
                invitations.get(id)
        );
    }

    @Override
    public List<Invitation> findBySenderIdAndStatusIn(String senderId, List<InvitationStatus> statusList) {
        return invitations.values().stream()
                .filter(invitation -> invitation.getSenderId().equals(senderId))
                .filter(invitation -> statusList.contains(invitation.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Invitation> findByReceiverIdAndStatusIn(String receiverId, List<InvitationStatus> statusList) {
        return invitations.values().stream()
                .filter(invitation -> invitation.getReceiverId().equals(receiverId))
                .filter(invitation -> statusList.contains(invitation.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsBySenderIdAndReceiverId(String senderId, String receiverId) {
        return invitations.values().stream()
                .anyMatch(invitation ->
                        invitation.getSenderId().equals(senderId) &&
                        invitation.getReceiverId().equals(receiverId)
                );
    }
}
