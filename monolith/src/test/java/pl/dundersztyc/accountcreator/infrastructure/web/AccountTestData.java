package pl.dundersztyc.accountcreator.infrastructure.web;

import pl.dundersztyc.accounts.dto.AccountRequest;

class AccountTestData {

    static AccountRequest defaultRequestWithUsername(String username) {
        return new AccountRequest(username, "firstname", "lastname", "password");
    }


}