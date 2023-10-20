package pl.dundersztyc.accounts;

import pl.dundersztyc.accounts.Account.FirstName;
import pl.dundersztyc.accounts.Account.LastName;
import pl.dundersztyc.accounts.Account.Password;
import pl.dundersztyc.accounts.Account.Username;
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
