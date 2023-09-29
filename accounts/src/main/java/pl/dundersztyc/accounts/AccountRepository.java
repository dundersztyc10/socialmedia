package pl.dundersztyc.accounts;


import java.util.Optional;

public interface AccountRepository{
    Account save(Account account);
    Optional<Account> findByUsername(Username username);
}
