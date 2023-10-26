package pl.dundersztyc.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.dundersztyc.friends.FriendshipQueryRepository;
import pl.dundersztyc.friends.dto.AccountDto;
import pl.dundersztyc.friends.dto.Depth;
import pl.dundersztyc.posts.dto.PostDto;
import pl.dundersztyc.posts.dto.PostVisibility;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostViewerQueryRepository {

    private final PostMetricsRepository postMetricsRepo;
    private final FriendshipQueryRepository friendshipQueryRepo;
    private final PostMapper postMapper;
    private final Clock clock;

    final static long MAX_DAYS_SINCE_POST_CREATION = 7;


    @Cacheable(cacheNames = {"recommendedPosts"})
    public Page<PostDto> findRecommendedPosts(String accountId, PostVisibility visibility, Pageable pageable) {
        var sendersId = friendshipQueryRepo.findFriendsOfAccountWithDepth(accountId, new Depth(visibility.getDepth()))
                .stream()
                .map(AccountDto::accountId)
                .collect(Collectors.toSet());
        LocalDateTime dateSince = LocalDateTime.now(clock).minusDays(MAX_DAYS_SINCE_POST_CREATION);

        var posts = postMetricsRepo.findRecommendedPostsWhereAccountIdInAndDateSince(sendersId, dateSince, pageable);
        var postDtos = posts.getContent().stream()
                .map(postMapper::fromPost)
                .collect(Collectors.toList());
        return new PageImpl<>(
                postDtos,
                posts.getPageable(),
                posts.getSize());
    }

}
