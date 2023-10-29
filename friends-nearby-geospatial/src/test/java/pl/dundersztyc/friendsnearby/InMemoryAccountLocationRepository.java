package pl.dundersztyc.friendsnearby;

import org.apache.lucene.util.SloppyMath;
import org.locationtech.jts.geom.Point;
import pl.dundersztyc.friendsnearby.dto.FriendNearby;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

class InMemoryAccountLocationRepository implements AccountLocationRepository {

    private ConcurrentHashMap<Long, AccountLocation> accountLocations = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public AccountLocation save(AccountLocation accountLocation) {
        var id = idGenerator.getAndIncrement();
        var toSave = new AccountLocation(
                id,
                accountLocation.getAccountId(),
                accountLocation.getLocation(),
                accountLocation.getDate()
        );
        accountLocations.put(id, toSave);
        return toSave;
    }

    @Override
    public List<AccountLocation> findLatestFriendsRecordsSince(List<String> accountIds, LocalDateTime since) {
        Map<String, List<AccountLocation>> groupedById = accountLocations.values().stream()
                .filter(accountLocation -> accountIds.contains(accountLocation.getAccountId()))
                .filter(accountLocation -> accountLocation.getDate().isAfter(since) || accountLocation.getDate().isEqual(since))
                .collect(Collectors.groupingBy(AccountLocation::getAccountId));

        Map<String, Optional<AccountLocation>> lastRecordsGrouped = groupedById.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .max(Comparator.comparing(AccountLocation::getDate))
                ));

        List<AccountLocation> latestFriendsRecords = new ArrayList<>();
        lastRecordsGrouped.forEach((key, value) -> value.ifPresent(latestFriendsRecords::add));

        return latestFriendsRecords;
    }

    @Override
    public Optional<AccountLocation> findFirstByAccountIdAndDateGreaterThanEqualOrderByDateDesc(String accountId, LocalDateTime dateTime) {
        return accountLocations.values().stream()
                .filter(accountLocation -> accountLocation.getAccountId().equals(accountId))
                .filter(accountLocation -> accountLocation.getDate().isAfter(dateTime) || accountLocation.getDate().isEqual(dateTime))
                .max(Comparator.comparing(AccountLocation::getDate));
    }

    @Override
    public List<FriendNearby> findFriendsNearby(Point accountLocation, List<AccountLocation> friendRecords, double maxDistanceInMeters) {
        return accountLocations.values().stream()
                .filter(friendRecords::contains)
                .filter(elem -> distanceBetweenPointsInM(accountLocation, elem.getLocation()) <= maxDistanceInMeters)
                .sorted(Comparator.comparing(elem -> distanceBetweenPointsInM(accountLocation, elem.getLocation())))
                .map(elem -> new FriendNearby() {
                    @Override
                    public String getAccountId() {
                        return elem.getAccountId();
                    }

                    @Override
                    public double getDistanceInMeters() {
                        return distanceBetweenPointsInM(accountLocation, elem.getLocation());
                    }
                })
                .collect(Collectors.toList());
    }

    private double distanceBetweenPointsInM(Point p1, Point p2) {
        return SloppyMath.haversinMeters(p1.getY(), p1.getX(), p2.getY(), p2.getX());
    }
}
