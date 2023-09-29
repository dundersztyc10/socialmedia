package pl.dundersztyc.accounts;


import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryAccountRepository implements AccountRepository {

    ConcurrentHashMap<UUID, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        accounts.put(UUID.randomUUID(), account);
        return account;
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
