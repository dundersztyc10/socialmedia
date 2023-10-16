package pl.dundersztyc.accounts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class AccountConfiguration {

    @Bean
    AccountFacade accountFacade(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        AccountMapper accountMapper = new AccountMapper(passwordEncoder);
        return new AccountFacade(
                accountRepository,
                accountMapper
        );
    }


}
