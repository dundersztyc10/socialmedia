package pl.dundersztyc.friends;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.dundersztyc.common.events.EventPublisher;
import pl.dundersztyc.friends.dto.FriendshipDto;
import pl.dundersztyc.friends.dto.FriendshipExistException;
import pl.dundersztyc.friends.dto.FriendshipRequest;
import pl.dundersztyc.friends.events.FriendshipRemovedEvent;

@RequiredArgsConstructor
public class FriendshipFacade {

    private final FriendshipRepository friendshipRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public FriendshipDto addFriendship(FriendshipRequest request) {
        Account sender = friendshipRepository.findByAccountId(request.idFrom())
                .orElseGet(() -> saveAccount(request.idFrom()));
        Account receiver = friendshipRepository.findByAccountId(request.idTo())
                .orElseGet(() -> saveAccount(request.idTo()));
        if (sender.getFriends().contains(receiver)) {
            throw new FriendshipExistException("friendship already exists");
        }
        sender.addFriend(receiver);
        receiver.addFriend(sender);

        var saved = friendshipRepository.save(sender);
        friendshipRepository.save(receiver);
        return new FriendshipDto(saved.getId(), request.idFrom(), request.idTo());
    }

    @Transactional
    public void deleteFriendship(FriendshipRequest request) {
        var from = friendshipRepository.findByAccountId(request.idFrom()).orElseThrow(EntityNotFoundException::new);
        var to = friendshipRepository.findByAccountId(request.idTo()).orElseThrow(EntityNotFoundException::new);
        from.deleteFriend(to);
        to.deleteFriend(from);
        friendshipRepository.save(from);
        friendshipRepository.save(to);
        eventPublisher.raise(new FriendshipRemovedEvent(request.idFrom(), request.idTo()));
    }

    private Account saveAccount(String accountId) {
        var account = new Account(accountId);
        return friendshipRepository.save(account);
    }
}
