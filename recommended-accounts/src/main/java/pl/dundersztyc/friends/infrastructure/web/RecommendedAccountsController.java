package pl.dundersztyc.friends.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.dundersztyc.friends.RecommendedAccountsQueryRepository;
import pl.dundersztyc.friends.dto.AccountDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/recommended-accounts")
@RequiredArgsConstructor
class RecommendedAccountsController {

    private final RecommendedAccountsQueryRepository recommendedAccountsQueryRepo;

    @GetMapping("/{id}")
    public List<AccountDto> findRecommendedAccounts(@PathVariable("id") String accountId,
                                                    @RequestParam("limit") int limit) {
        return recommendedAccountsQueryRepo.findRecommendedAccountIds(accountId, limit);
    }
}
