package pl.dundersztyc.friendsnearby.dto;

import java.time.LocalDateTime;

public record AccountLocationDto(String accountId, LocalDateTime date) {
}
