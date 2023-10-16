package pl.dundersztyc.accountcreator.web;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import pl.dundersztyc.accountcreator.AuthenticationFacade;
import pl.dundersztyc.accountcreator.dto.AuthRequest;
import pl.dundersztyc.accounts.AccountFacade;
import pl.dundersztyc.accounts.dto.AccountDto;
import pl.dundersztyc.accounts.dto.AccountRequest;
import pl.dundersztyc.accounts.dto.UsernameExistException;


@RestController
@RequestMapping("api/v1/public")
class AccountCreatorController {

    private final AuthenticationFacade authenticationFacade;
    private final AccountFacade accountFacade;


    public AccountCreatorController(AuthenticationFacade authenticationFacade, AccountFacade accountFacade) {
        this.authenticationFacade = authenticationFacade;
        this.accountFacade = accountFacade;
    }

    // TODO: remove
    @PostConstruct
    protected void iamAlive(){
        Logger log = LoggerFactory.getLogger(AccountCreatorController.class);
        log.info("CONTROLLER LOADED");
    }

    @PostMapping("/login")
    public ResponseEntity<AccountDto> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authentication = authenticationFacade.authenticate(request);
            User user = (User) authentication.getPrincipal();
            String token = authenticationFacade.provideJwt(authentication);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(accountFacade.findAccountByUsername(user.getUsername()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AccountDto> register(@RequestBody @Valid AccountRequest accountRequest) {
        LoggerFactory.getLogger(AccountCreatorController.class).info("AAAAAAAAAAAA");
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
