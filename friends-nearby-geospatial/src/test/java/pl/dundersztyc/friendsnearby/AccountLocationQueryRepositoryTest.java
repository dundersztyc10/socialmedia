package pl.dundersztyc.friendsnearby;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.Mockito;
import pl.dundersztyc.friends.FriendshipQueryRepository;
import pl.dundersztyc.friends.dto.AccountDto;
import pl.dundersztyc.friendsnearby.dto.FriendsNearbyDto;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AccountLocationQueryRepositoryTest {

    private AccountLocationQueryRepository accountLocationQueryRepo;
    private FriendshipQueryRepository friendshipQueryRepo;
    private InMemoryAccountLocationRepository inMemoryAccountLocationRepo;
    private final Clock clock = Clock.fixed(Instant.parse("2018-08-22T10:00:00Z"), ZoneOffset.UTC);

    private final LocalDateTime VALID_DATE = LocalDateTime.now(clock).minusMinutes(AccountLocationQueryRepository.MAX_MINUTES_SINCE_LAST_ACCOUNT_RECORD);
    private final LocalDateTime INVALID_DATE = LocalDateTime.now(clock).minusMinutes(AccountLocationQueryRepository.MAX_MINUTES_SINCE_LAST_ACCOUNT_RECORD + 1);

    @BeforeEach
    void setUp() {
        inMemoryAccountLocationRepo = new InMemoryAccountLocationRepository();
        friendshipQueryRepo = mock(FriendshipQueryRepository.class);
        accountLocationQueryRepo = new FriendsNearbyConfiguration().accountLocationQueryRepository(
                inMemoryAccountLocationRepo, friendshipQueryRepo, clock);
    }

    @Test
    void shouldFindFriendsNearby() {
        saveAccountLocation("A", createPoint(1, 1), LocalDateTime.now(clock));
        saveAccountLocation("B", createPoint(1.001, 1.001), VALID_DATE);
        saveAccountLocation("C", createPoint(1.002, 1.002), VALID_DATE);
        givenAccountIdHaveFriends("A", List.of("B", "C"));

        FriendsNearbyDto friendsNearbyAccount = accountLocationQueryRepo.findFriendsNearby("A");

        assertThat(friendsNearbyAccount.accountId()).isEqualTo("A");
        assertThat(friendsNearbyAccount.friendsNearby()).hasSize(2);
        assertThat(friendsNearbyAccount.friendsNearby().get(0).getAccountId()).isEqualTo("B");
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoFriendsNearby() {
        Point outOfRange = createPoint(90, 90);
        saveAccountLocation("A", createPoint(1, 1), LocalDateTime.now(clock));
        saveAccountLocation("B", outOfRange, VALID_DATE);
        saveAccountLocation("C", outOfRange, VALID_DATE);
        givenAccountIdHaveFriends("A", List.of("B", "C"));

        FriendsNearbyDto friendsNearbyAccount = accountLocationQueryRepo.findFriendsNearby("A");

        assertThat(friendsNearbyAccount.accountId()).isEqualTo("A");
        assertThat(friendsNearbyAccount.friendsNearby()).hasSize(0);
    }

    @Test
    void shouldReturnEmptyListWhenAccountHasNoFriends() {
        saveAccountLocation("A", createPoint(1, 1), LocalDateTime.now(clock));
        saveAccountLocation("B", createPoint(1.001, 1.001), VALID_DATE);
        saveAccountLocation("C", createPoint(1.002, 1.002), VALID_DATE);
        givenAccountIdHaveFriends("A", Collections.emptyList());

        FriendsNearbyDto friendsNearbyAccount = accountLocationQueryRepo.findFriendsNearby("A");

        assertThat(friendsNearbyAccount.accountId()).isEqualTo("A");
        assertThat(friendsNearbyAccount.friendsNearby()).hasSize(0);
    }

    @Test
    void shouldReturnEmptyListWhenFriendsDontHaveCurrentData() {
        saveAccountLocation("A", createPoint(1, 1), LocalDateTime.now(clock));
        saveAccountLocation("B", createPoint(1.001, 1.001), INVALID_DATE);
        saveAccountLocation("C", createPoint(1.002, 1.002), INVALID_DATE);
        givenAccountIdHaveFriends("A", List.of("B", "C"));

        FriendsNearbyDto friendsNearbyAccount = accountLocationQueryRepo.findFriendsNearby("A");

        assertThat(friendsNearbyAccount.accountId()).isEqualTo("A");
        assertThat(friendsNearbyAccount.friendsNearby()).hasSize(0);
    }

    private AccountLocation saveAccountLocation(String accountId, Point location, LocalDateTime date) {
        return inMemoryAccountLocationRepo.save(AccountLocation.withoutId(accountId, location, date));
    }

    private void givenAccountIdHaveFriends(String accountId, List<String> friendIds) {
        given(friendshipQueryRepo.findFriendsOfAccount(eq(accountId)))
                .willReturn(friendIds.stream().map(AccountDto::new).collect(Collectors.toList()));
    }

    private Point createPoint(double longitude, double latitude) {
        return new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
    }

}