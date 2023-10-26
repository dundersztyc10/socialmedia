package pl.dundersztyc.friends;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Map;

interface Neo4jRecommendedAccountsRepository extends Neo4jRepository<Account, Long>, RecommendedAccountsRepository {

    @Query("MATCH (a:Account)-[f:FRIEND_OF*2]-(a1:Account) WHERE a.accountId=$accountId " +
            "AND a1.accountId <> $accountId " +
            "WITH a1, COUNT(a1.accountId) as frequency " +
            "ORDER BY frequency DESC " +
            "LIMIT $limit " +
            "RETURN a1")
    List<Account> findRecommendedAccounts(String accountId, int limit);

}
