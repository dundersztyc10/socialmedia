package pl.dundersztyc.accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.dundersztyc.accounts.dto.*;
import pl.dundersztyc.accounts.dto.UsernameException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static pl.dundersztyc.accounts.AccountTestData.defaultAccountWithUsername;
import static pl.dundersztyc.accounts.AccountTestData.defaultRequestWithUsername;

class AccountFacadeTest {

    private InMemoryAccountRepository accountRepository;
    private AccountFacade accountFacade;

    @BeforeEach
    void setUp() {
        accountRepository = new InMemoryAccountRepository();
        accountFacade = new AccountConfiguration().accountFacade(accountRepository, new BCryptPasswordEncoder());
    }

    @Test
    void shouldSaveAccount() {
        AccountRequest accountRequest = defaultRequestWithUsername("account");

        AccountDto saved = accountFacade.saveAccount(accountRequest);

        assertThat(saved.username()).isEqualTo("account");
        assertThat(accountRepository.accounts).hasSize(1);
    }

    @Test
    void cannotSaveAccountWhenUsernameExists() {
        AccountRequest accountRequest = defaultRequestWithUsername("account");
        accountFacade.saveAccount(accountRequest);

        UsernameException ex = assertThrows(UsernameExistException.class,
                () -> accountFacade.saveAccount(accountRequest));
        assertThat(ex.getMessage()).isEqualTo("username already exist");

    }

    @Test
    void shouldFindAccountByUsername() {
        accountRepository.accounts.put(UUID.randomUUID(),
                defaultAccountWithUsername(new Username("account")));

        var accountDto = accountFacade.findAccountByUsername("account");

        assertThat(accountDto.username()).isEqualTo("account");
    }

    @Test
    void shouldThrowWhenFindAccountAndUsernameDoesNotExist() {
        UsernameException ex = assertThrows(UsernameNotFoundException.class,
                () -> accountFacade.findAccountByUsername("account"));
        assertThat(ex.getMessage()).isEqualTo("username not found");
    }
}