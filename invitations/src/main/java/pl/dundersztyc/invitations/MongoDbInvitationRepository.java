package pl.dundersztyc.invitations;

import org.springframework.data.mongodb.repository.MongoRepository;

interface MongoDbInvitationRepository extends MongoRepository<Invitation, String>, InvitationRepository {
}
