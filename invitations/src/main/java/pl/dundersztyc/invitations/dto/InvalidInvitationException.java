package pl.dundersztyc.invitations.dto;

public class InvalidInvitationException extends RuntimeException {
    public InvalidInvitationException(String message) {
        super(message);
    }
}
