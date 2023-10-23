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
class Post {

    @MongoId
    private String id;

    private String accountId;

    private PostVisibility visibility;

    private String content;

    private LocalDateTime date;

    static Post withoutId(String accountId, PostVisibility visibility, String content, LocalDateTime date) {
        return new Post(null, accountId, visibility, content, date);
    }

}
