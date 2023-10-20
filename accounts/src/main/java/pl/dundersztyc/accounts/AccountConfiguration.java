package pl.dundersztyc.accounts;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class AccountConfiguration {

    @Bean
    AuthenticationManager authenticationManager(AccountRepository accountRepository,
                                                PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(
                username -> accountRepository.findByUsername(new Username(username))
                        .orElseThrow(EntityNotFoundException::new)
        );
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    AccountFacade accountFacade(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        AccountMapper accountMapper = new AccountMapper(passwordEncoder);
        return new AccountFacade(
                accountRepository,
                accountMapper
        );
    }

    @Bean
    AccountQueryRepository accountQueryRepository(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        AccountMapper accountMapper = new AccountMapper(passwordEncoder);
        return new AccountQueryRepository(
                accountRepository,
                accountMapper
        );
    }


}
