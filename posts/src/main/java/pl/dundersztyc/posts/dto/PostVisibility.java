package pl.dundersztyc.posts.dto;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

public enum PostVisibility {
    FRIENDS(1),
    FRIENDS_OF_FRIENDS(2);

    PostVisibility(int depth) {
        this.depth = depth;
    }

    @Getter
    private final int depth;

}
