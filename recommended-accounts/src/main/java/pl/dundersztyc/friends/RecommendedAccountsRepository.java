package pl.dundersztyc.friends;

import java.util.List;
import java.util.Map;

interface RecommendedAccountsRepository {
    List<Account> findRecommendedAccounts(String accountId, int limit);
}
