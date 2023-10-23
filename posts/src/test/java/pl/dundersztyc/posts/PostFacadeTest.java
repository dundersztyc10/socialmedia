package pl.dundersztyc.posts;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dundersztyc.posts.dto.PostDto;
import pl.dundersztyc.posts.dto.PostRequest;
import pl.dundersztyc.posts.dto.PostVisibility;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

class PostFacadeTest {

    private final Clock clock = Clock.fixed(Instant.parse("2018-08-22T10:00:00Z"), ZoneOffset.UTC);

    private PostFacade postFacade;

    @BeforeEach
    public void setUp() {
        postFacade = new PostConfiguration().postFacade(
                new InMemoryPostRepository(), clock
        );
    }

    @Test
    void shouldAddPost() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");

        assertThat(postDto.id()).isNotNull();
        assertThat(postDto.accountId()).isEqualTo("account1");
    }

    @Test
    void shouldDeletePost() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");

        postFacade.deletePost(postDto.id(), "account1");
    }

    @Test
    void shouldThrowWhenDeleteNonExistingPost() {
        assertThrows(EntityNotFoundException.class,
                () -> postFacade.deletePost("notExistingId", "account1"));
    }

    @Test
    void shouldDeleteWhenPostCreatorIsNotEqualToCurrentAccountId() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");

        assertThrows(IllegalStateException.class,
                () -> postFacade.deletePost(postDto.id(), "account2"));
    }

    private PostDto addPost(String accountId, PostVisibility visibility, String content) {
        var request = new PostRequest(accountId, visibility, content);
        return postFacade.addPost(request);
    }

}