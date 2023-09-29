package pl.dundersztyc.accounts.dto;

public class UsernameNotFoundException extends UsernameException {
    public UsernameNotFoundException() {
        super("username not found");
    }
}
