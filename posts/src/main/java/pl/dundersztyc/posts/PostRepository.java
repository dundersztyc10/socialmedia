package pl.dundersztyc.posts;

import pl.dundersztyc.posts.dto.DateRange;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

interface PostRepository {
    Post save(Post post);
    void deleteById(String id);
    Optional<Post> findById(String id);
    List<Post> findByAccountIdBetweenDates(String accountId, DateRange dateRange);
    List<Post> findByAccountIdSince(String accountId, LocalDateTime since);
}
