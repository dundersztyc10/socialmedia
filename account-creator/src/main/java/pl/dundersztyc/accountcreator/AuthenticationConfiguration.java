package pl.dundersztyc.accountcreator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@Configuration
class AuthenticationConfiguration {

    @Bean
    AuthenticationFacade authenticationFacade(AuthenticationManager authenticationManager,
                                              JwtEncoder jwtEncoder,
                                              PasswordEncoder passwordEncoder) {
       return new AuthenticationFacade(authenticationManager, jwtEncoder, passwordEncoder);
    }

}
