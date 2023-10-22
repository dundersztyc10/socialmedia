package pl.dundersztyc.friends.dto;

public class FriendshipExistException extends RuntimeException {
    public FriendshipExistException(String message) {
        super(message);
    }
}
