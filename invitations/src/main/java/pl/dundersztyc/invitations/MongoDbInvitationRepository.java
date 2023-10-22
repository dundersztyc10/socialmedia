package pl.dundersztyc.invitations;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

interface MongoDbInvitationRepository extends MongoRepository<Invitation, String>, InvitationRepository {
    @Query("{$or: [{$and: [{'senderId': ?0}, {'receiverId': ?1}]}, {$and: [{'senderId': ?1}, {'receiverId': ?0}]}]}")
    Optional<Invitation> findInvitationBetweenAccounts(String senderId, String receiverId);
}
