package pl.dundersztyc.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.Set;

interface PostMetricsRepository {
    Slice<Post> findRecommendedPostsWhereAccountIdInAndDateSince(Set<String> accountIds, LocalDateTime since, Pageable pageable);
}
