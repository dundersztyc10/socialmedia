package pl.dundersztyc.posts.dto;


import lombok.NonNull;

public record PostRequest(String accountId,
                          PostVisibility visibility,
                          String content) {
    public PostRequest(@NonNull String accountId, @NonNull PostVisibility visibility, @NonNull String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("invalid content");
        }
        this.accountId = accountId;
        this.visibility = visibility;
        this.content = content;
    }
}
