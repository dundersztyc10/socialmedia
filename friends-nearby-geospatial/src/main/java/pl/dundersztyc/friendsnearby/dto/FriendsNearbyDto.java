package pl.dundersztyc.friendsnearby.dto;

import java.util.List;

public record FriendsNearbyDto(
        String accountId,
        List<FriendNearby> friendsNearby
) {
}
