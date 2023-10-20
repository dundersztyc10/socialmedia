package pl.dundersztyc.invitations;

import pl.dundersztyc.invitations.dto.InvitationStatus;

import java.util.List;
import java.util.Optional;

interface InvitationRepository {
    Invitation save(Invitation invitation);
    Optional<Invitation> findById(String id);
    List<Invitation> findBySenderIdAndStatusIn(String senderId, List<InvitationStatus> statusList);
    List<Invitation> findByReceiverIdAndStatusIn(String receiverId, List<InvitationStatus> statusList);
    boolean existsBySenderIdAndReceiverId(String senderId, String receiverId);
}
