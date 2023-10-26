package pl.dundersztyc.posts;

import pl.dundersztyc.posts.dto.CommentDto;
import pl.dundersztyc.posts.dto.PostDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PostMapper {

    PostDto fromPost(Post post) {
        List<CommentDto> comments = post.getComments() == null ? new ArrayList<>() :
                post.getComments().stream()
                        .map(comment -> new CommentDto(comment.getId(), comment.getAccountId(), comment.getContent(), comment.getDate()))
                        .collect(Collectors.toList());
        int likes = post.getLikedBy() == null ? 0 : post.getLikedBy().size();

        return new PostDto(
                post.getId(),
                post.getAccountId(),
                post.getVisibility(),
                post.getContent(),
                comments,
                likes,
                post.getDate()
        );
    }

}
