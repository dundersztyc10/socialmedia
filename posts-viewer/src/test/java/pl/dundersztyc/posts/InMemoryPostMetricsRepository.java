package pl.dundersztyc.posts;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import pl.dundersztyc.posts.dto.PostVisibility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class InMemoryPostMetricsRepository implements PostMetricsRepository {

    private ConcurrentHashMap<String, Post> posts = new ConcurrentHashMap<>();

    @Override
    public Slice<Post> findRecommendedPostsWhereAccountIdInAndDateSince(Set<String> accountIds, LocalDateTime since, Pageable pageable) {
        var recommended = posts.values().stream()
                .filter(post -> accountIds.contains(post.getAccountId()))
                .filter(post -> post.getDate().isAfter(since) || post.getDate().isEqual(since))
                .sorted((o1, o2) -> Float.compare(
                        getRecommendationRate(o2),
                        getRecommendationRate(o1)
                ))
                .collect(Collectors.toList());
        return new PageImpl<>(recommended, pageable, recommended.size());
    }

    private float getRecommendationRate(Post post) {
        return (float) (post.getLikedBy().size() + post.getComments().size()) / (post.getViewedBy().size() + 1);
    }

    Post save(PostSampleData.PostBuilder postBuilder) {
        var id = UUID.randomUUID().toString();
        var toSave = new Post(
                id,
                postBuilder.getAccountId(),
                PostVisibility.FRIENDS_OF_FRIENDS,
                "defaultContent",
                postBuilder.getDate(),
                postBuilder.getComments(),
                postBuilder.getLikedBy(),
                postBuilder.getViewedBy()
        );
        posts.put(id, toSave);
        return toSave;
    }
}
