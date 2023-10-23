package pl.dundersztyc.posts;

import pl.dundersztyc.posts.dto.PostDto;

class PostMapper {

    PostDto fromPost(Post post) {
        return new PostDto(
                post.getId(),
                post.getAccountId(),
                post.getVisibility(),
                post.getContent(),
                post.getDate()
        );
    }

}
