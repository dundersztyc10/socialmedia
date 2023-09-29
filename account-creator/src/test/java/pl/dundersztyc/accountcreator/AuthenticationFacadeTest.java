package pl.dundersztyc.accountcreator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import pl.dundersztyc.JwtConfig;
import pl.dundersztyc.RsaConfig;
import pl.dundersztyc.SecurityConfig;
import pl.dundersztyc.accounts.Role;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {AuthenticationFacade.class, SecurityConfig.class, RsaConfig.class, JwtConfig.class})
class AuthenticationFacadeTest {

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Autowired
    JwtDecoder jwtDecoder;

    private final static String USERNAME = "USERNAME123";

    @Test
    @WithMockUser(roles = {Role.USER_LOGGED, Role.USER_PREMIUM}, username = USERNAME)
    void shouldProvideJwtWhenUserIsAuthenticated() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String token = authenticationFacade.provideJwt(auth);

        Jwt jwt = jwtDecoder.decode(token);
        String sub = jwt.getClaim("sub");
        String roles = jwt.getClaim("roles");

        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();
        assertThat(sub).isEqualTo(USERNAME);
        assertThat(roles).isEqualTo("ROLE_USER_LOGGED ROLE_USER_PREMIUM");
    }

    @Test
    void shouldThrowWhenProvideJwtWithNullAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertThrows(BadCredentialsException.class, () -> {
            String token = authenticationFacade.provideJwt(auth);
        });

    }

    @Test
    @WithMockUser(roles = Role.USER_LOGGED)
    void shouldThrowWhenProvideJwtWithInvalidAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.setAuthenticated(false);

        assertThrows(BadCredentialsException.class, () -> {
            String token = authenticationFacade.provideJwt(auth);
        });

    }

}