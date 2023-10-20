package pl.dundersztyc.invitations.dto;

public record InvitationDto(String id, String senderId, String receiverId, InvitationStatus status) {
}
