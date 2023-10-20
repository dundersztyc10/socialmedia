package pl.dundersztyc.accounts;


import pl.dundersztyc.accounts.Account.Username;

import java.util.Optional;

interface AccountRepository{
    Account save(Account account);
    Optional<Account> findById(String id);
    Optional<Account> findByUsername(Username username);
}
