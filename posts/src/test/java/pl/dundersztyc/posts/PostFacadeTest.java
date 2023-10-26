package pl.dundersztyc.posts;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dundersztyc.posts.dto.CommentRequest;
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
    void shouldThrowWhenDeletePostAndPostCreatorIsNotEqualToCurrentAccountId() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");

        assertThrows(IllegalStateException.class,
                () -> postFacade.deletePost(postDto.id(), "account2"));
    }

    @Test
    void shouldAddComment() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");

        var postWithComment = postFacade.addComment(defaultCommentRequest(), postDto.id());

        assertThat(postWithComment.comments()).hasSize(1);
    }

    @Test
    void shouldThrowWhenAddCommentAndPostIdDoesNotExist() {
        assertThrows(EntityNotFoundException.class,
                () -> postFacade.addComment(defaultCommentRequest(), "invalidId"));
    }

    @Test
    void shouldDeleteComment() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");
        var comment = postFacade.addComment(defaultCommentRequest(), postDto.id())
                .comments().get(0);

        var isDeleted = postFacade.deleteComment(postDto.id(), comment.id(), comment.accountId());

        assertThat(isDeleted).isTrue();
    }

    @Test
    void shouldThrowWhenDeleteCommentAndPostIdDoesNotExist() {
        assertThrows(EntityNotFoundException.class,
                () -> postFacade.deleteComment("invalidId", "commentId", "accountId"));
    }

    @Test
    void shouldAddLike() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");

        var isAdded = postFacade.addLike(postDto.id(), "accountId");

        assertThat(isAdded).isTrue();
    }

    @Test
    void cannotAddLikeTwice() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");

        var firstAddition = postFacade.addLike(postDto.id(), "accountId");
        var secondAddition = postFacade.addLike(postDto.id(), "accountId");

        assertThat(firstAddition).isTrue();
        assertThat(secondAddition).isFalse();
    }

    @Test
    void shouldThrowWhenAddLikeAndPostIdDoesNotExist() {
        assertThrows(EntityNotFoundException.class,
                () -> postFacade.addLike("invalidId", "accountId"));
    }

    @Test
    void shouldDeleteLike() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");
        postFacade.addLike(postDto.id(), "accountId");

        var isDeleted = postFacade.deleteLike(postDto.id(), "accountId");

        assertThat(isDeleted).isTrue();
    }

    @Test
    void shouldThrowWhenDeleteLikeAndPostIdDoesNotExist() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");
        postFacade.addLike(postDto.id(), "accountId");

        assertThrows(EntityNotFoundException.class,
                () -> postFacade.deleteLike("invalidPostId", "accountId"));
    }

    @Test
    void cannotDeleteNonExistingLike() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");

        var isDeleted = postFacade.deleteLike(postDto.id(), "notExisting");

        assertThat(isDeleted).isFalse();
    }

    @Test
    void cannotDeleteLikeTwice() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");
        postFacade.addLike(postDto.id(), "accountId");

        var firstDeletion = postFacade.deleteLike(postDto.id(), "accountId");
        var secondDeletion = postFacade.deleteLike(postDto.id(), "accountId");

        assertThat(firstDeletion).isTrue();
        assertThat(secondDeletion).isFalse();
    }

    @Test
    void shouldMarkAsViewed() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");

        var isAdded = postFacade.markAsViewed(postDto.id(), "accountId");

        assertThat(isAdded).isTrue();
    }

    @Test
    void shouldThrowWhenMarkAsViewedAndPostIdDoesNotExist() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");

        assertThrows(EntityNotFoundException.class,
                () -> postFacade.markAsViewed("invalidPostId", "accountId"));
    }

    @Test
    void cannotMarkAsViewedTwice() {
        var postDto = addPost("account1", PostVisibility.FRIENDS, "content");

        var firstAddition = postFacade.markAsViewed(postDto.id(), "accountId");
        var secondAddition = postFacade.markAsViewed(postDto.id(), "accountId");

        assertThat(firstAddition).isTrue();
        assertThat(secondAddition).isFalse();
    }

    private PostDto addPost(String accountId, PostVisibility visibility, String content) {
        var request = new PostRequest(accountId, visibility, content);
        return postFacade.addPost(request);
    }

    private CommentRequest defaultCommentRequest() {
        return new CommentRequest("account2", "content2");
    }

}