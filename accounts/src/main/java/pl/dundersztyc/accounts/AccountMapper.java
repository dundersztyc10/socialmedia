package pl.dundersztyc.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dundersztyc.accounts.dto.AccountDto;
import pl.dundersztyc.accounts.dto.AccountRequest;

import java.util.Set;

@RequiredArgsConstructor
class AccountMapper {

    private final PasswordEncoder passwordEncoder;

    AccountDto toDto(Account account) {
        return new AccountDto(account.getId(), account.getUsername());
    }

    public Account fromRequest(AccountRequest accountRequest) {
        return Account.withoutId(
                new Username(accountRequest.username()),
                new FirstName(accountRequest.firstname()),
                new LastName(accountRequest.lastname()),
                new Password(passwordEncoder.encode(accountRequest.password())),
                Set.of(new Role(Role.USER_LOGGED))
        );
    }
}
