package pl.dundersztyc.accounts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Document
@AllArgsConstructor
@Getter
class Account implements UserDetails {

    @MongoId
    private String id;

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

    record FirstName(String value) {
    }

    record LastName(String value) {
    }

    record Password(String value) {
    }

    record Username(String value) {
    }

}
