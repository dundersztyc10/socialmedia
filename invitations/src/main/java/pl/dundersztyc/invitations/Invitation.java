package pl.dundersztyc.invitations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pl.dundersztyc.invitations.dto.InvitationStatus;

import java.time.LocalDateTime;

@Document
@AllArgsConstructor
@Getter
class Invitation {

    @MongoId
    private String id;

    private String senderId;
    private String receiverId;
    private LocalDateTime date;

    @Setter
    private InvitationStatus status;

    static Invitation withoutId(String senderId, String receiverId, LocalDateTime date, InvitationStatus status) {
        return new Invitation(null, senderId, receiverId, date, status);
    }
}
