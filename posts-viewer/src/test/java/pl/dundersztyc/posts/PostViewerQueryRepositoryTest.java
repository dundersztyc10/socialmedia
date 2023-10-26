package pl.dundersztyc.posts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import pl.dundersztyc.friends.FriendshipQueryRepository;
import pl.dundersztyc.friends.dto.AccountDto;
import pl.dundersztyc.friends.dto.Depth;
import pl.dundersztyc.posts.dto.PostVisibility;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static pl.dundersztyc.posts.PostSampleData.postBuilder;

class PostViewerQueryRepositoryTest {

    private PostViewerQueryRepository postViewerQueryRepo;
    private InMemoryPostMetricsRepository postMetricsRepository;
    private FriendshipQueryRepository friendshipQueryRepo;
    private final Clock clock = Clock.fixed(Instant.parse("2018-08-22T10:00:00Z"), ZoneOffset.UTC);

    private final LocalDateTime DATE_IN_SEARCH_RANGE = LocalDateTime.now(clock)
            .minusDays(PostViewerQueryRepository.MAX_DAYS_SINCE_POST_CREATION).plusDays(1);

    private final LocalDateTime DATE_NOT_IN_SEARCH_RANGE = LocalDateTime.now(clock)
            .minusDays(PostViewerQueryRepository.MAX_DAYS_SINCE_POST_CREATION).minusDays(1);

    private final Pageable DEFAULT_PAGE = PageRequest.of(0, 20);

    @BeforeEach
    void setUp() {
        postMetricsRepository = new InMemoryPostMetricsRepository();
        friendshipQueryRepo = Mockito.mock(FriendshipQueryRepository.class);
        postViewerQueryRepo = new PostViewerConfiguration().postViewerQueryRepository(
               postMetricsRepository, friendshipQueryRepo, clock
        );
    }


    @Test
    void shouldFindAllPostsWhereDateInSearchRange() {
        givenAccountIdHaveFriends("accountId", List.of("account1"));
        savePosts(
                postBuilder().withAccountId("account1").withDate(DATE_IN_SEARCH_RANGE),
                postBuilder().withAccountId("account1").withDate(DATE_IN_SEARCH_RANGE),
                postBuilder().withAccountId("account1").withDate(DATE_NOT_IN_SEARCH_RANGE)
        );

        var posts = postViewerQueryRepo.findRecommendedPosts("accountId", PostVisibility.FRIENDS, DEFAULT_PAGE);

        assertThat(posts.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    void cannotFindPostsNotOfYourFriend() {
        givenAccountIdHaveFriends("accountId", Collections.emptyList());
        savePosts(
                postBuilder().withAccountId("account1").withDate(DATE_IN_SEARCH_RANGE)
        );

        var posts = postViewerQueryRepo.findRecommendedPosts("accountId", PostVisibility.FRIENDS, DEFAULT_PAGE);

        assertThat(posts.getNumberOfElements()).isEqualTo(0);
    }

    @Test
    void shouldFindSortedRecommendedPosts() {
        givenAccountIdHaveFriends("accountId", List.of("account1"));
        var defaultPost = postBuilder().withAccountId("account1").withDate(DATE_IN_SEARCH_RANGE);

        var mostRecommended = savePost(
                defaultPost.withLikes(5).withComments(5).withViews(10)
        );
        var betweenRecommended = savePost(
                defaultPost.withLikes(3).withComments(3).withViews(10)
        );
        var leastRecommended = savePost(
                defaultPost.withLikes(1).withComments(1).withViews(10)
        );

        var posts = postViewerQueryRepo.findRecommendedPosts("accountId", PostVisibility.FRIENDS, DEFAULT_PAGE)
                .getContent();

        assertThat(posts).hasSize(3);
        assertThat(posts.get(0).id()).isEqualTo(mostRecommended.getId());
        assertThat(posts.get(1).id()).isEqualTo(betweenRecommended.getId());
        assertThat(posts.get(2).id()).isEqualTo(leastRecommended.getId());
    }


    private void givenAccountIdHaveFriends(String accountId, List<String> friends) {
        given(friendshipQueryRepo.findFriendsOfAccountWithDepth(eq(accountId), any(Depth.class)))
                .willReturn(friends.stream().map(AccountDto::new).collect(Collectors.toList()));
    }

    private void savePosts(PostSampleData.PostBuilder... builders) {
        Arrays.stream(builders).forEach(this::savePost);
    }

    private Post savePost(PostSampleData.PostBuilder builder) {
        return postMetricsRepository.save(builder);
    }

}