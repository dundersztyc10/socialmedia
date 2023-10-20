package pl.dundersztyc.accounts.dto;

public class UsernameExistException extends RuntimeException {
    public UsernameExistException() {
        super("username already exist");
    }
}
