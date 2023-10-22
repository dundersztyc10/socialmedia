package pl.dundersztyc.friends;

import org.springframework.data.neo4j.core.Neo4jTemplate;

import java.util.List;

class CustomFriendshipSearchRepositoryImpl implements CustomFriendshipSearchRepository {

    private final Neo4jTemplate template;

    public CustomFriendshipSearchRepositoryImpl(Neo4jTemplate template) {
        this.template = template;
    }

    @Override
    public List<Account> findFriendsOfAccountIdWithDepth(String accountId, int depth) {
        String query = "match (a:Account)-[f:FRIEND_OF*1.." + String.valueOf(depth) + "]-(a1:Account) where a.accountId=\"" + accountId + "\" and a1.accountId <> \"" + accountId + "\" return distinct a1 ";
        return template.findAll(query, Account.class);
    }
}
