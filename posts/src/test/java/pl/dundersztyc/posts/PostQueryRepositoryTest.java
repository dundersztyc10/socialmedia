package pl.dundersztyc.posts;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dundersztyc.posts.dto.DateRange;
import pl.dundersztyc.posts.dto.PostDto;
import pl.dundersztyc.posts.dto.PostRequest;
import pl.dundersztyc.posts.dto.PostVisibility;

import java.time.*;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

class PostQueryRepositoryTest {

    private InMemoryPostRepository repository;
    private PostQueryRepository postQueryRepo;
    private PostFacade postFacade;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPostRepository();
        Clock clock = Clock.fixed(Instant.parse("2018-08-22T10:00:00Z"), ZoneOffset.UTC);
        postQueryRepo = new PostConfiguration().postQueryRepository(
                repository);
        postFacade = new PostConfiguration().postFacade(
                repository, clock);
    }

    @Test
    void shouldFindPostById() {
        var saved = addPost(
                new PostRequest("account1", PostVisibility.FRIENDS_OF_FRIENDS, "content"));

        var post = postQueryRepo.findPostById(saved.id());

        assertThat(post.accountId()).isEqualTo("account1");
    }

    @Test
    void shouldThrowWhenFindPostAndIdDoesNotExist() {
        assertThrows(EntityNotFoundException.class,
                () -> postQueryRepo.findPostById("nonExistingId"));
    }

    @Test
    void shouldFindPostsByAccountIdBetweenDates() {
        addDefaultPostWithDate(LocalDateTime.of(2020, 1, 1, 1, 1));
        addDefaultPostWithDate(LocalDateTime.of(2020, 2, 1, 1, 1));
        addDefaultPostWithDate(LocalDateTime.of(2020, 3, 1, 1, 1));

        var posts = postQueryRepo.findPostsByAccountBetweenDates("defaultId", new DateRange(
                LocalDateTime.of(2020, 1, 1, 1, 1),
                LocalDateTime.of(2020, 2, 1, 1, 1)));

        assertThat(posts).hasSize(2);
    }

    @Test
    void shouldFindPostsByAccountIdSince() {
        addDefaultPostWithDate(LocalDateTime.of(2020, 1, 1, 1, 1));
        addDefaultPostWithDate(LocalDateTime.of(2020, 2, 1, 1, 1));
        addDefaultPostWithDate(LocalDateTime.of(2020, 3, 1, 1, 1));

        var posts = postQueryRepo.findPostsByAccountSince("defaultId",
                LocalDateTime.of(2020, 2, 1, 1, 1));

        assertThat(posts).hasSize(2);
    }

    private PostDto addPost(PostRequest request) {
        return postFacade.addPost(request);
    }

    private void addDefaultPostWithDate(LocalDateTime date) {
        var old = postFacade;

        var zoneId = ZoneId.systemDefault();
        postFacade = new PostConfiguration().postFacade(repository,
                Clock.fixed(date.atZone(zoneId).toInstant(), zoneId));

        addPost(
                new PostRequest("defaultId", PostVisibility.FRIENDS, "defaultContent")
        );

        postFacade = old;
    }
}
