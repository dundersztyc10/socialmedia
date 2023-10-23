package pl.dundersztyc.posts.dto;

import java.time.LocalDateTime;

public record PostDto(String id,
                      String accountId,
                      PostVisibility visibility,
                      String content,
                      LocalDateTime date) {
}
