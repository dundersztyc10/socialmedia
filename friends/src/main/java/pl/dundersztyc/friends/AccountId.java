package pl.dundersztyc.friends;

import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("account")
@AllArgsConstructor
class AccountId {

    @Id
    private String id;
}