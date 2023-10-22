package pl.dundersztyc.friends;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface Neo4jFriendshipRepository extends
        Neo4jRepository<Account, Long>,
        FriendshipRepository,
        CustomFriendshipSearchRepository {
    @Query("MATCH (a:Account)-[f:FRIEND_OF*1]-(a1:Account) WHERE a.accountId=$accountId RETURN DISTINCT a1")
    List<Account> findFriendsOfAccountId(@Param("accountId") String accountId);
}


