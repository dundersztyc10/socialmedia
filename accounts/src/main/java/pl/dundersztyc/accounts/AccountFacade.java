package pl.dundersztyc.accounts;

import lombok.RequiredArgsConstructor;
import pl.dundersztyc.accounts.dto.*;

@RequiredArgsConstructor
public class AccountFacade {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDto findAccountByUsername(String username) {
        Account account = accountRepository.findByUsername(new Username(username))
                .orElseThrow(UsernameNotFoundException::new);
        return accountMapper.toDto(account);
    }

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
