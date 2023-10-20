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
                invitation.getIdFrom(),
                invitation.getIdTo(),
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
    public List<Invitation> findByIdFromAndStatusIn(String idFrom, List<InvitationStatus> statusList) {
        return invitations.values().stream()
                .filter(invitation -> invitation.getIdFrom().equals(idFrom))
                .filter(invitation -> statusList.contains(invitation.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Invitation> findByIdToAndStatusIn(String idTo, List<InvitationStatus> statusList) {
        return invitations.values().stream()
                .filter(invitation -> invitation.getIdTo().equals(idTo))
                .filter(invitation -> statusList.contains(invitation.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByIdFromAndIdTo(String idFrom, String idTo) {
        return invitations.values().stream()
                .anyMatch(invitation ->
                        invitation.getIdFrom().equals(idFrom) &&
                        invitation.getIdTo().equals(idTo)
                );
    }
}
