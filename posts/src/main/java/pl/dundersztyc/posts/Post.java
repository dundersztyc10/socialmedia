package pl.dundersztyc.posts;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pl.dundersztyc.posts.dto.PostVisibility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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

    private List<Comment> comments;

    private Set<String> likedBy;

    private Set<String> viewedBy;

    boolean addComment(@NonNull Comment comment) {
        return comments.add(comment);
    }

    boolean deleteCommentById(@NonNull String commentId, @NonNull String accountId) {
        return comments.removeIf(comment -> comment.getId().equals(commentId) && comment.getAccountId().equals(accountId));
    }

    boolean addLike(String accountId) {
        return likedBy.add(accountId);
    }

    boolean addViewedBy(String accountId) {
        return viewedBy.add(accountId);
    }

    boolean deleteLike(String accountId) {
        return likedBy.removeIf(id -> id.equals(accountId));
    }

    static Post withoutId(String accountId, PostVisibility visibility, String content, LocalDateTime date) {
        return new Post(null, accountId, visibility, content, date,
                new ArrayList<>(), new HashSet<>(), new HashSet<>());
    }

}
