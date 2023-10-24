package pl.dundersztyc.posts.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostDto(String id,
                      String accountId,
                      PostVisibility visibility,
                      String content,
                      List<CommentDto> comments,
                      int likes,
                      LocalDateTime date) {
}
