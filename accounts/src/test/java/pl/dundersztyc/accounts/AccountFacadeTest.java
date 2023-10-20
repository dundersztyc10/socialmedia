package pl.dundersztyc.accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.dundersztyc.accounts.dto.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
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

        UsernameExistException ex = assertThrows(UsernameExistException.class,
                () -> accountFacade.saveAccount(accountRequest));
        assertThat(ex.getMessage()).isEqualTo("username already exist");

    }
}