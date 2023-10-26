package pl.dundersztyc.accountcreator;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@Configuration
public class AuthenticationConfiguration {

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
