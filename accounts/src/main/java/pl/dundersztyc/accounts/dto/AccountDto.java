package pl.dundersztyc.accounts.dto;

import java.util.UUID;

public record AccountDto(
        UUID id,
        String username
) {
}
