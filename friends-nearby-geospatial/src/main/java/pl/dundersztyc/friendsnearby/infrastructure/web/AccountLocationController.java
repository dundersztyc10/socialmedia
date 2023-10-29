package pl.dundersztyc.friendsnearby.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.dundersztyc.common.CurrentAccountGetter;
import pl.dundersztyc.friendsnearby.AccountLocationFacade;
import pl.dundersztyc.friendsnearby.dto.AccountLocationDto;
import pl.dundersztyc.friendsnearby.dto.LocationRequest;

@RestController
@RequestMapping("api/v1/account-location")
@RequiredArgsConstructor
class AccountLocationController {

    private final AccountLocationFacade accountLocationFacade;


    @PostMapping("/{id}")
    public AccountLocationDto addAccountLocation(@PathVariable("id") String accountId,
                                                 @RequestBody @Valid LocationRequest locationRequest,
                                                 CurrentAccountGetter currentAccountGetter) {
        if (!accountId.equals(currentAccountGetter.getAccountId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return accountLocationFacade.saveAccountLocation(accountId, locationRequest);
    }
}
