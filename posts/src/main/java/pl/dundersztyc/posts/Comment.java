package pl.dundersztyc.posts;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pl.dundersztyc.posts.dto.PostVisibility;

import java.time.LocalDateTime;

@Document
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
class Comment {

    @MongoId
    private String id;

    private String accountId;

    private String content;

    private LocalDateTime date;

    static Comment withoutId(String accountId, String content, LocalDateTime date) {
        return new Comment(null, accountId, content, date);
    }
}
