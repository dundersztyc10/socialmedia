package pl.dundersztyc.invitations.dto;

public class InvitationDoesNotExistException extends RuntimeException {
    public InvitationDoesNotExistException(String message) {
        super(message);
    }
}
