package pl.dundersztyc.accounts;

import lombok.RequiredArgsConstructor;
import pl.dundersztyc.accounts.dto.AccountDto;
import pl.dundersztyc.accounts.dto.AccountNotFoundException;

@RequiredArgsConstructor
public class AccountQueryRepository {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDto findAccountById(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("id not found"));
        return accountMapper.toDto(account);
    }

    public AccountDto findAccountByUsername(String username) {
        Account account = accountRepository.findByUsername(new Username(username))
                .orElseThrow(() -> new AccountNotFoundException("username not found"));
        return accountMapper.toDto(account);

    }

}
