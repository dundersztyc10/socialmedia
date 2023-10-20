package pl.dundersztyc.invitations.dto;

public record InvitationDto(String id, String idFrom, String idTo, InvitationStatus status) {
}
