package pl.dundersztyc.posts.dto;

import lombok.NonNull;

public record CommentRequest(String accountId,
                             String content) {
    public CommentRequest(@NonNull String accountId, @NonNull String content) {
        if (content.isEmpty()) {
            throw new IllegalArgumentException("invalid content");
        }
        this.accountId = accountId;
        this.content = content;
    }
}
