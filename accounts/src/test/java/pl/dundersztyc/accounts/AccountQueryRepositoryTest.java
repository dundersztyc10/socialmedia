package pl.dundersztyc.accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.dundersztyc.accounts.dto.AccountNotFoundException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static pl.dundersztyc.accounts.AccountTestData.defaultAccountWithUsername;

class AccountQueryRepositoryTest {

    private InMemoryAccountRepository accountRepository;
    private AccountQueryRepository accountQueryRepository;

    @BeforeEach
    void setUp() {
        accountRepository = new InMemoryAccountRepository();
        accountQueryRepository = new AccountConfiguration()
                .accountQueryRepository(accountRepository, new BCryptPasswordEncoder());
    }

    @Test
    void shouldFindAccountByUsername() {
        accountRepository.accounts.put(UUID.randomUUID().toString(),
                defaultAccountWithUsername(new Username("account")));

        var accountDto = accountQueryRepository.findAccountByUsername("account");

        assertThat(accountDto.username()).isEqualTo("account");
    }

    @Test
    void shouldThrowWhenFindAccountAndUsernameDoesNotExist() {
        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class,
                () -> accountQueryRepository.findAccountByUsername("account"));
        assertThat(ex.getMessage()).isEqualTo("username not found");
    }

}
