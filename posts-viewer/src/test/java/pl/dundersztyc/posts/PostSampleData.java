package pl.dundersztyc.posts;

import lombok.Getter;
import pl.dundersztyc.posts.dto.PostVisibility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PostSampleData {

    static PostBuilder postBuilder() {
        return new PostBuilder();
    }

    @Getter
    static class PostBuilder {
        private String accountId = "defaultAccountId";
        private LocalDateTime date = LocalDateTime.now();
        private List<Comment> comments = new ArrayList<>();
        private Set<String> likedBy = new HashSet<>();
        private Set<String> viewedBy = new HashSet<>();

        public PostBuilder() {
        }

        PostBuilder withAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        PostBuilder withDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        PostBuilder withComments(int numberOfComments) {
            this.comments = IntStream.range(0, numberOfComments)
                    .mapToObj(i -> defaultCommentWithId(String.valueOf(i)))
                    .collect(Collectors.toList());
            return this;
        }

        PostBuilder withLikes(int numberOfLikes) {
            this.likedBy = IntStream.range(0, numberOfLikes)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toSet());
            return this;
        }

        PostBuilder withViews(int numberOfViews) {
            this.viewedBy = IntStream.range(0, numberOfViews)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toSet());
            return this;
        }


        private Comment defaultCommentWithId(String id) {
            return new Comment(id, "accountId", "content", LocalDateTime.now());
        }
    }
}
