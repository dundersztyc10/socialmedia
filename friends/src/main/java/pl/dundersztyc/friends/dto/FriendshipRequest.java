package pl.dundersztyc.friends.dto;

import lombok.NonNull;


public record FriendshipRequest(String idFrom, String idTo) {
    public FriendshipRequest(@NonNull String idFrom, @NonNull String idTo) {
        if (idFrom.equals(idTo)) {
            throw new IllegalArgumentException("idFrom cannot be equal to idTo");
        }
        this.idFrom = idFrom;
        this.idTo = idTo;
    }
}
