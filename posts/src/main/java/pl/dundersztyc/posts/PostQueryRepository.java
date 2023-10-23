package pl.dundersztyc.posts;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import pl.dundersztyc.posts.dto.DateRange;
import pl.dundersztyc.posts.dto.PostDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostQueryRepository {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostDto findPostById(String id) {
        return postRepository.findById(id)
                .map(postMapper::fromPost)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<PostDto> findPostsByAccountBetweenDates(String accountId, DateRange dateRange) {
        return postRepository.findByAccountIdBetweenDates(accountId, dateRange).stream()
                .map(postMapper::fromPost)
                .collect(Collectors.toList());
    }

    public List<PostDto> findPostsByAccountSince(String accountId, LocalDateTime since) {
        return postRepository.findByAccountIdSince(accountId, since).stream()
                .map(postMapper::fromPost)
                .collect(Collectors.toList());
    }
}
