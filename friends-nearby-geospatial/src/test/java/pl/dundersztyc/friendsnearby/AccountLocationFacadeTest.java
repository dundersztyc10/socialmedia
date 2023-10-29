package pl.dundersztyc.friendsnearby;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dundersztyc.friendsnearby.dto.LocationRequest;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class AccountLocationFacadeTest {

    private AccountLocationFacade accountLocationFacade;
    private final Clock clock = Clock.fixed(Instant.parse("2018-08-22T10:00:00Z"), ZoneOffset.UTC);

    @BeforeEach
    void setUp() {
        accountLocationFacade = new FriendsNearbyConfiguration().accountLocationFacade(
                new InMemoryAccountLocationRepository(), clock);
    }

    @Test
    void shouldSaveAccountLocation() {
        var saved = accountLocationFacade.saveAccountLocation("1", new LocationRequest(1.5, 2.5));

        assertThat(saved.accountId()).isEqualTo("1");
        assertThat(saved.date()).isEqualTo(LocalDateTime.now(clock));
    }
}