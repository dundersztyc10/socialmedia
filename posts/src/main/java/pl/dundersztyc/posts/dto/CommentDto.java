package pl.dundersztyc.posts.dto;

import java.time.LocalDateTime;

public record CommentDto(String id,
                  String accountId,
                  String content,
                  LocalDateTime date) {
}
