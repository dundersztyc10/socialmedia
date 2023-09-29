package pl.dundersztyc.accounts;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.dundersztyc.accounts.AccountRepository;

import java.util.UUID;

interface MongoDbAccountRepository extends MongoRepository<Account, UUID>, AccountRepository {
}
