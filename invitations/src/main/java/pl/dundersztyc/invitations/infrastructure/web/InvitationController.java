package pl.dundersztyc.invitations.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.dundersztyc.common.CurrentAccountGetter;
import pl.dundersztyc.invitations.InvitationFacade;
import pl.dundersztyc.invitations.InvitationQueryRepository;
import pl.dundersztyc.invitations.dto.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/invitations")
@RequiredArgsConstructor
class InvitationController {

    private final InvitationFacade invitationFacade;
    private final InvitationQueryRepository invitationQueryRepository;

    @PostMapping
    public ResponseEntity<InvitationDto> createInvitation(@RequestBody InvitationRequest request,
                                          CurrentAccountGetter currentAccountGetter) {
        if (!request.senderId().equals(currentAccountGetter.getAccountId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<InvitationDto>(invitationFacade.addInvitation(request), HttpStatus.CREATED);
    }

    @PostMapping("/accept/{invitationId}")
    public InvitationDto acceptInvitation(@PathVariable("invitationId") String invitationId,
                                          CurrentAccountGetter currentAccountGetter) {
        return invitationFacade.acceptInvitation(invitationId, currentAccountGetter.getAccountId());
    }

    @PostMapping("/decline/{invitationId}")
    public InvitationDto declineInvitation(@PathVariable("invitationId") String invitationId,
                                                           CurrentAccountGetter currentAccountGetter) {
        return invitationFacade.declineInvitation(invitationId, currentAccountGetter.getAccountId());
    }

    @GetMapping("/sent/{accountId}")
    public List<InvitationDto> getSentInvitations(
            @PathVariable("accountId") String accountId,
            @RequestParam(name = "status", required = false) List<InvitationStatus> requestedStatuses,
            CurrentAccountGetter currentAccountGetter) {
        if (!currentAccountGetter.getAccountId().equals(accountId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (requestedStatuses == null) {
            requestedStatuses = List.of(InvitationStatus.values());
        }
        return invitationQueryRepository.findSentInvitationsWithStatuses(accountId, requestedStatuses);
    }

    @GetMapping("/received/{accountId}")
    public List<InvitationDto> getReceivedInvitations(
            @PathVariable("accountId") String accountId,
            @RequestParam(name = "status", required = false) List<InvitationStatus> requestedStatuses,
            CurrentAccountGetter currentAccountGetter) {
        if (!currentAccountGetter.getAccountId().equals(accountId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (requestedStatuses == null) {
            requestedStatuses = List.of(InvitationStatus.values());
        }
        return invitationQueryRepository.findReceivedInvitationsWithStatuses(accountId, requestedStatuses);
    }


    @ExceptionHandler({InvitationDoesNotExistException.class, InvalidInvitationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void handleInvitationDoesNotExistExceptionAndInvalidInvitationException() {
    }

    @ExceptionHandler({IllegalStateException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    void handleIllegalStateException() {
    }

}
