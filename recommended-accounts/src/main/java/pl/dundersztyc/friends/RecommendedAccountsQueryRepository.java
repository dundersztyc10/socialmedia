package pl.dundersztyc.friends;

import lombok.RequiredArgsConstructor;
import pl.dundersztyc.friends.dto.AccountDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RecommendedAccountsQueryRepository {

    private final RecommendedAccountsRepository recommendedAccountsRepo;

    public List<AccountDto> findRecommendedAccountIds(String accountId, int limit) {
        return recommendedAccountsRepo.findRecommendedAccounts(accountId, limit)
               .stream()
               .map(account -> new AccountDto(account.getAccountId()))
               .collect(Collectors.toList());
    }
}
