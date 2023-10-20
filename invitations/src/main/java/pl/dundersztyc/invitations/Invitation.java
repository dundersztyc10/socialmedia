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

    private String idFrom;
    private String idTo;
    private LocalDateTime date;

    @Setter
    private InvitationStatus status;

    static Invitation withoutId(String idFrom, String idTo, LocalDateTime date, InvitationStatus status) {
        return new Invitation(null, idFrom, idTo, date, status);
    }
}
