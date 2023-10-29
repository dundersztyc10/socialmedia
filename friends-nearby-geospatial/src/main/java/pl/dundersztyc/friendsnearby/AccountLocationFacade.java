package pl.dundersztyc.friendsnearby;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.transaction.annotation.Transactional;
import pl.dundersztyc.friendsnearby.dto.AccountLocationDto;
import pl.dundersztyc.friendsnearby.dto.LocationRequest;

import java.time.Clock;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class AccountLocationFacade {

    private final AccountLocationRepository accountLocationRepo;
    private final Clock clock;

    @Transactional
    public AccountLocationDto saveAccountLocation(String accountId, LocationRequest locationRequest) {
        var accountLocation = AccountLocation.withoutId(accountId, locationRequest.toPoint(), LocalDateTime.now(clock));
        AccountLocation saved = accountLocationRepo.save(accountLocation);
        return new AccountLocationDto(saved.getAccountId(), saved.getDate());
    }
}
