package pl.dundersztyc.accountcreator.infrastructure.web;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.dundersztyc.accountcreator.AuthenticationFacade;
import pl.dundersztyc.accountcreator.dto.AuthRequest;
import pl.dundersztyc.accounts.AccountFacade;
import pl.dundersztyc.accounts.AccountQueryRepository;
import pl.dundersztyc.accounts.dto.AccountDto;
import pl.dundersztyc.accounts.dto.AccountRequest;
import pl.dundersztyc.accounts.dto.UsernameExistException;


@RestController
@RequestMapping("api/v1/public")
class AccountCreatorController {

    private final AuthenticationFacade authenticationFacade;
    private final AccountFacade accountFacade;
    private final AccountQueryRepository accountQueryRepository;


    public AccountCreatorController(AuthenticationFacade authenticationFacade, AccountFacade accountFacade,
                                    AccountQueryRepository accountQueryRepository) {
        this.authenticationFacade = authenticationFacade;
        this.accountFacade = accountFacade;
        this.accountQueryRepository = accountQueryRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AccountDto> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authentication = authenticationFacade.authenticate(request);
            UserDetails user = (UserDetails) authentication.getPrincipal();
            String accountId = accountQueryRepository.findAccountByUsername(user.getUsername()).id();
            String token = authenticationFacade.provideJwt(authentication, accountId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(accountQueryRepository.findAccountByUsername(user.getUsername()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AccountDto> register(@RequestBody @Valid AccountRequest accountRequest) {
        AccountDto account;
        try {
            account = accountFacade.saveAccount(accountRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(account);
        }
        catch (UsernameExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
