package pl.dundersztyc.friends.dto;

import java.time.Instant;
import java.util.UUID;

public record FriendshipDto(Long id, String idFrom, String idTo) {
}
