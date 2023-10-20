package pl.dundersztyc.accounts.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.dundersztyc.accounts.AccountQueryRepository;
import pl.dundersztyc.accounts.dto.AccountDto;
import pl.dundersztyc.accounts.dto.AccountNotFoundException;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
class AccountController {

    private final AccountQueryRepository accountQueryRepo;

    @GetMapping("/username/{username}")
    public AccountDto findAccountByUsername(@PathVariable("username") String username) {
        return accountQueryRepo.findAccountByUsername(username);
    }

    @ExceptionHandler({AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void handleAccountNotFoundException() {
    }
}
