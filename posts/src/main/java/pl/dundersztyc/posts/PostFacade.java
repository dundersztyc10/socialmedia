package pl.dundersztyc.posts;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.dundersztyc.posts.dto.CommentRequest;
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

    @Transactional
    public PostDto addComment(CommentRequest request, String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
        var comment = Comment.withoutId(request.accountId(), request.content(), LocalDateTime.now(clock));
        post.addComment(comment);
        return mapper.fromPost(
                postRepository.save(post)
        );
    }

    @Transactional
    public boolean deleteComment(String postId, String commentId, String currentAccountId) {
        var post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
        boolean result = post.deleteCommentById(commentId, currentAccountId);
        postRepository.save(post);
        return result;
    }

    @Transactional
    public boolean addLike(String postId, String accountId) {
        var post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
        boolean result = post.addLike(accountId);
        postRepository.save(post);
        return result;
    }

    @Transactional
    public boolean deleteLike(String postId, String accountId) {
        var post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
        boolean result = post.deleteLike(accountId);
        postRepository.save(post);
        return result;
    }

    @Transactional
    public boolean markAsViewed(String postId, String accountId) {
        var post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
        boolean result = post.addViewedBy(accountId);
        postRepository.save(post);
        return result;
    }
}
