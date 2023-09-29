package pl.dundersztyc.accounts;

import org.springframework.security.core.parameters.P;
import pl.dundersztyc.accounts.dto.AccountRequest;

import java.util.Set;

class AccountTestData {

    static AccountRequest defaultRequestWithUsername(String username) {
        return new AccountRequest(username, "firstname", "lastname", "password");
    }

    static Account defaultAccountWithUsername(Username username) {
        return Account.withoutId(
                username, new FirstName("firstname"), new LastName("lastname"), new Password("password"),
                Set.of(new Role(Role.USER_LOGGED))
        );
    }
}
