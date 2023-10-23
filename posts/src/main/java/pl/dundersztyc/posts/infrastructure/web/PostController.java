package pl.dundersztyc.posts.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.dundersztyc.common.CurrentAccountGetter;
import pl.dundersztyc.posts.dto.DateRange;
import pl.dundersztyc.posts.PostFacade;
import pl.dundersztyc.posts.PostQueryRepository;
import pl.dundersztyc.posts.dto.PostDto;
import pl.dundersztyc.posts.dto.PostRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
class PostController {

    private final PostFacade postFacade;
    private final PostQueryRepository postQueryRepo;

    @PostMapping
    public PostDto createPost(@RequestBody PostRequest request,
                              CurrentAccountGetter currentAccountGetter) {
        if (!request.accountId().equals(currentAccountGetter.getAccountId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return postFacade.addPost(request);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") String id,
                           CurrentAccountGetter currentAccountGetter) {
        postFacade.deletePost(id, currentAccountGetter.getAccountId());
    }

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable("id") String id) {
        return postQueryRepo.findPostById(id);
    }

    @GetMapping("/between-dates")
    public List<PostDto> getPostsByAccountIdBetweenDates(@RequestParam("accountId") String accountId,
                                                         @RequestParam("startDate") LocalDateTime startDate,
                                                         @RequestParam("endDate") LocalDateTime endDate) {
        return postQueryRepo.findPostsByAccountBetweenDates(accountId, new DateRange(startDate, endDate));
    }

    @GetMapping("/since")
    public List<PostDto> getPostsByAccountIdSince(@RequestParam("accountId") String accountId,
                                                  @RequestParam("sinceDate") LocalDateTime sinceDate) {
        return postQueryRepo.findPostsByAccountSince(accountId, sinceDate);
    }
}
