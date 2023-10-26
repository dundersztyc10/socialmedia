package pl.dundersztyc.friends;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

class RecommendedAccountsQueryRepositoryTest {

    private RecommendedAccountsQueryRepository recommendedAccountsQueryRepo;
    private InMemoryRecommendedAccountsRepository inMemoryRecommendedAccountsRepo;

    @BeforeEach
    void setUp() {
        inMemoryRecommendedAccountsRepo = new InMemoryRecommendedAccountsRepository();
        recommendedAccountsQueryRepo = new RecommendedAccountsConfiguration().recommendedAccountsQueryRepository(
                inMemoryRecommendedAccountsRepo
        );
    }

    @Test
    void shouldGetRecommendedFriends() {
        // two friends of A have E as a friend
        // one friend of A have C as a friend
        addFriendship("A", "B");
        addFriendship("A", "D");
        addFriendship("B", "C");
        addFriendship("B", "E");
        addFriendship("D", "E");

        var recommendedAccounts = inMemoryRecommendedAccountsRepo.findRecommendedAccounts("A", 10);

        assertThat(recommendedAccounts).hasSize(2);
        assertThat(recommendedAccounts.get(0).getAccountId()).isEqualTo("E");
        assertThat(recommendedAccounts.get(1).getAccountId()).isEqualTo("C");
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoFriendsOfFriends() {
        addFriendship("A", "B");
        addFriendship("A", "C");
        addFriendship("B", "C");

        var recommendedAccounts = inMemoryRecommendedAccountsRepo.findRecommendedAccounts("A", 10);

        assertThat(recommendedAccounts).hasSize(0);
    }

    private void addFriendship(String id1, String id2) {
        inMemoryRecommendedAccountsRepo.addFriendship(id1, id2);
    }

}