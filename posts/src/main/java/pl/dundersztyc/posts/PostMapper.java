package pl.dundersztyc.posts;

import pl.dundersztyc.posts.dto.CommentDto;
import pl.dundersztyc.posts.dto.PostDto;

import java.util.stream.Collectors;

class PostMapper {

    PostDto fromPost(Post post) {
        return new PostDto(
                post.getId(),
                post.getAccountId(),
                post.getVisibility(),
                post.getContent(),
                post.getComments().stream()
                        .map(comment -> new CommentDto(comment.getId(), comment.getAccountId(), comment.getContent(), comment.getDate()))
                        .collect(Collectors.toList()),
                post.getLikedBy().size(),
                post.getDate()
        );
    }

}
