package pl.dundersztyc.accounts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Document
@RequiredArgsConstructor
@Getter
class Account implements UserDetails {

    private final UUID id;
    private final Username username;
    private final FirstName firstName;
    private final LastName lastName;
    private final Password password;
    private final Set<Role> roles;

    static Account withoutId(
            Username username, FirstName firstName, LastName lastName, Password password, Set<Role> roles) {
        return new Account(null, username, firstName, lastName, password, roles);
    }

    @Override
    public String getUsername() {
        return username.value();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password.value();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
