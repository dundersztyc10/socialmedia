package pl.dundersztyc.posts.infrastructure.web;

import jakarta.ws.rs.Path;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.dundersztyc.common.CurrentAccountGetter;
import pl.dundersztyc.posts.PostViewerQueryRepository;
import pl.dundersztyc.posts.dto.PostDto;
import pl.dundersztyc.posts.dto.PostVisibility;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/post-viewer")
@RequiredArgsConstructor
class PostViewerController {

    private final PostViewerQueryRepository postViewerQueryRepo;

    @GetMapping("/{id}")
    public Page<PostDto> getRecommendedPosts(@PathVariable("id") String accountId,
                                             @RequestParam("visibility") PostVisibility visibility,
                                             Pageable pageable,
                                             CurrentAccountGetter currentAccountGetter) {
        if (!accountId.equals(currentAccountGetter.getAccountId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return postViewerQueryRepo.findRecommendedPosts(accountId, visibility, pageable);
    }

}
