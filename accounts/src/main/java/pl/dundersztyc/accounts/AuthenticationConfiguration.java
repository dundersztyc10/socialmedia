package pl.dundersztyc.accounts;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import pl.dundersztyc.SecurityConfig;

@Configuration
class AuthenticationConfiguration {

    @Bean
    public AuthenticationManager authenticationManager(AccountRepository accountRepository,
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
    AuthenticationFacade authenticationFacade(AuthenticationManager authenticationManager,
                                              JwtEncoder jwtEncoder,
                                              PasswordEncoder passwordEncoder) {
       return new AuthenticationFacade(authenticationManager, jwtEncoder, passwordEncoder);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
