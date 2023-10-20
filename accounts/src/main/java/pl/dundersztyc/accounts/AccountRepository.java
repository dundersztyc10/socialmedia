package pl.dundersztyc.accounts;


import java.util.Optional;

interface AccountRepository{
    Account save(Account account);
    Optional<Account> findById(String id);
    Optional<Account> findByUsername(Username username);
}
