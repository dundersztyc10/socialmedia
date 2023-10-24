package pl.dundersztyc.posts;

import pl.dundersztyc.posts.dto.DateRange;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class InMemoryPostRepository implements PostRepository {

    private ConcurrentHashMap<String, Post> posts = new ConcurrentHashMap<>();

    @Override
    public Post save(Post post) {
        var id = findById(post.getId()).isPresent() ?
                post.getId() :
                UUID.randomUUID().toString();
        var commentsWithIds = post.getComments().stream()
                .map(comment -> new Comment(
                        UUID.randomUUID().toString(),
                        comment.getAccountId(),
                        comment.getContent(),
                        comment.getDate()))
                .collect(Collectors.toList());

        var toSave = new Post(
                id,
                post.getAccountId(),
                post.getVisibility(),
                post.getContent(),
                post.getDate(),
                commentsWithIds,
                post.getLikedBy()
        );
        posts.put(id, toSave);
        return toSave;
    }

    @Override
    public void deleteById(String id) {
        posts.remove(id);
    }

    @Override
    public Optional<Post> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(
                posts.get(id)
        );
    }

    @Override
    public List<Post> findByAccountIdBetweenDates(String accountId, DateRange dateRange) {
        return posts.values().stream()
                .filter(post -> post.getAccountId().equals(accountId))
                .filter(post -> dateRange.isInRange(post.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByAccountIdSince(String accountId, LocalDateTime since) {
        return posts.values().stream()
                .filter(post -> post.getAccountId().equals(accountId))
                .filter(post -> post.getDate().isAfter(since) || post.getDate().isEqual(since))
                .collect(Collectors.toList());
    }
}
