package pl.dundersztyc.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import pl.dundersztyc.accounts.dto.AccountDto;

import java.util.Map;

@Component
public class CurrentAccountGetter {

    public AccountDto getCurrentAccount() {
        var claims = getClaims();
        return new AccountDto(
                (String) claims.get("accountid"),
                (String) claims.get("username"));
    }

    public String getAccountId() {
        return (String) getClaims().get("accountid");
    }

    private Map<String, Object> getClaims() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaims();
    }

}
