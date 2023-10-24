package pl.dundersztyc.posts;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.dundersztyc.posts.dto.PostDto;
import pl.dundersztyc.posts.dto.PostRequest;

import java.time.Clock;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PostFacade {

    private final PostRepository postRepository;
    private final Clock clock;
    private final PostMapper mapper;

    @Transactional
    public PostDto addPost(PostRequest request) {
        var post = Post.withoutId(request.accountId(), request.visibility(), request.content(), LocalDateTime.now(clock));
        var saved = postRepository.save(post);
        return mapper.fromPost(saved);
    }

    @Transactional
    public void deletePost(String id, String currentAccountId) {
        var post = postRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        if (!post.getAccountId().equals(currentAccountId)) {
            throw new IllegalStateException("cannot delete not your post");
        }
        postRepository.deleteById(id);
    }
}
