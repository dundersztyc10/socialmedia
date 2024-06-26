package pl.dundersztyc.accounts;


import pl.dundersztyc.accounts.Account.Password;
import pl.dundersztyc.accounts.Account.Username;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements AccountRepository {

    ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        var id = UUID.randomUUID().toString();
        var toSave = new Account(
                id,
                new Username(account.getUsername()),
                account.getFirstName(),
                account.getLastName(),
                new Password(account.getPassword()),
                account.getRoles()
        );
        accounts.put(id, toSave);
        return toSave;
    }

    @Override
    public Optional<Account> findById(String id) {
        return Optional.of(
                accounts.get(id)
        );
    }

    @Override
    public Optional<Account> findByUsername(Username username) {
        return accounts.values().stream()
                .filter(account -> new Username(account.getUsername()).equals(username))
                .findFirst();
    }

    int size() {
        return accounts.size();
    }
}
