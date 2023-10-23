package pl.dundersztyc.friends.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.dundersztyc.common.CurrentAccountGetter;
import pl.dundersztyc.friends.FriendshipFacade;
import pl.dundersztyc.friends.FriendshipQueryRepository;
import pl.dundersztyc.friends.dto.AccountDto;
import pl.dundersztyc.friends.dto.Depth;
import pl.dundersztyc.friends.dto.FriendshipRequest;

import java.util.List;

@RestController
@RequestMapping("api/v1/friends")
@RequiredArgsConstructor
class FriendshipController {

    private final FriendshipFacade friendshipFacade;
    private final FriendshipQueryRepository friendshipQueryRepo;

    @DeleteMapping
    public void deleteFriendship(@RequestBody FriendshipRequest friendshipRequest,
                                 CurrentAccountGetter currentAccountGetter) {
        if (!friendshipRequest.hasId(currentAccountGetter.getAccountId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        friendshipFacade.deleteFriendship(friendshipRequest);
    }

    @GetMapping("/{accountId}")
    public List<AccountDto> getFriendsOfAccount(@PathVariable("accountId") String accountId) {
        return friendshipQueryRepo.findFriendsOfAccount(accountId);
    }

    @GetMapping("/{accountId}/depth/{depth}")
    public List<AccountDto> getFriendsOfAccountWithDepth(@PathVariable("accountId") String accountId,
                                                         @PathVariable("depth") int depth) {
        return friendshipQueryRepo.findFriendsOfAccountWithDepth(accountId, new Depth(depth));
    }

}
