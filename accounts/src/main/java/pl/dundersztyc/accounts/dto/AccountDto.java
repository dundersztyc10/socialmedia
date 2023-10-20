package pl.dundersztyc.accounts.dto;

import java.util.UUID;

public record AccountDto(
        String id,
        String username
) {
}
