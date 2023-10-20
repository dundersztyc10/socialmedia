package pl.dundersztyc.invitations;

import pl.dundersztyc.invitations.dto.InvitationStatus;

import java.util.List;
import java.util.Optional;

interface InvitationRepository {
    Invitation save(Invitation invitation);
    Optional<Invitation> findById(String id);
    List<Invitation> findByIdFromAndStatusIn(String idFrom, List<InvitationStatus> statusList);
    List<Invitation> findByIdToAndStatusIn(String idTo, List<InvitationStatus> statusList);
    boolean existsByIdFromAndIdTo(String idFrom, String idTo);
}
