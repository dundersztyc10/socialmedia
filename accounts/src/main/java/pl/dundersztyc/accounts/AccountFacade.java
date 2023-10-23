package pl.dundersztyc.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.dundersztyc.accounts.Account.Username;
import pl.dundersztyc.accounts.dto.*;

@RequiredArgsConstructor
public class AccountFacade {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountDto saveAccount(AccountRequest accountRequest) {
        if (usernameExist(new Username(accountRequest.username()))) {
            throw new UsernameExistException();
        }
        Account account = accountMapper.fromRequest(accountRequest);
        Account saved = accountRepository.save(account);
        return accountMapper.toDto(saved);
    }

    private boolean usernameExist(Username username) {
        return accountRepository.findByUsername(username).isPresent();
    }

}
