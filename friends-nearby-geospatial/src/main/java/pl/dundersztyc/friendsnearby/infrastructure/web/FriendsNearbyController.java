package pl.dundersztyc.friendsnearby.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.dundersztyc.common.CurrentAccountGetter;
import pl.dundersztyc.friendsnearby.AccountLocationQueryRepository;
import pl.dundersztyc.friendsnearby.dto.FriendsNearbyDto;

@RestController
@RequestMapping("api/v1/friends-nearby")
@RequiredArgsConstructor
class FriendsNearbyController {

    private final AccountLocationQueryRepository accountLocationQueryRepo;

    @GetMapping("/{id}")
    public FriendsNearbyDto getFriendsNearby(@PathVariable("id") String accountId,
                                             CurrentAccountGetter currentAccountGetter) {
        if (!accountId.equals(currentAccountGetter.getAccountId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return accountLocationQueryRepo.findFriendsNearby(accountId);
    }
}
