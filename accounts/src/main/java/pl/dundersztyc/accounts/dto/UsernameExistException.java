package pl.dundersztyc.accounts.dto;

public class UsernameExistException extends UsernameException{
    public UsernameExistException() {
        super("username already exist");
    }
}
