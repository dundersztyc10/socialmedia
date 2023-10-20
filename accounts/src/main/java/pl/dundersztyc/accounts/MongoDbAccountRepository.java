package pl.dundersztyc.accounts;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import pl.dundersztyc.accounts.AccountRepository;

import java.util.UUID;

interface MongoDbAccountRepository extends MongoRepository<Account, String>, AccountRepository {
}
