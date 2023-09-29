package pl.dundersztyc.accounts.dto;

public record AccountRequest(
        String username,
        String firstname,
        String lastname,
        String password
) {
}
