package pl.dundersztyc.friendsnearby;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dundersztyc.friendsnearby.dto.FriendNearby;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

interface PostGisAccountLocationRepository extends
        JpaRepository<AccountLocation, Long>,
        AccountLocationRepository,
        JpaSpecificationExecutor<AccountLocation> {

    Optional<AccountLocation> findFirstByAccountIdAndDateGreaterThanEqualOrderByDateDesc(String accountId, LocalDateTime since);

    @Query(value =
            "SELECT al.accountId AS accountId, ST_DistanceSphere(al.location, :accountLocation) AS distanceInMeters " +
            "FROM AccountLocation al " +
            "WHERE al IN :friendRecords " +
            "AND ST_DistanceSphere(al.location, :accountLocation) <= :maxDistanceInMeters " +
            "ORDER BY ST_DistanceSphere(al.location, :accountLocation)")
    List<FriendNearby> findFriendsNearby(@Param("accountLocation") Point accountLocation,
                                         @Param("friendRecords") List<AccountLocation> friendRecords,
                                         @Param("maxDistanceInMeters") double maxDistanceInMeters);

    default List<AccountLocation> findLatestFriendsRecordsSince(List<String> accountIds, LocalDateTime since) {
        Specification<AccountLocation> latestSpec = findLatestAfterOrEqualDate(accountIds, since);
        return findAll(latestSpec);
    }

    private static Specification<AccountLocation> findLatestAfterOrEqualDate(List<String> accountIds, LocalDateTime since) {
        return (root, query, criteriaBuilder) -> {

            // find last record by accountId
            Subquery<LocalDateTime> lastRecordSubquery = query.subquery(LocalDateTime.class);
            Root<AccountLocation> subRoot = lastRecordSubquery.from(AccountLocation.class);
            lastRecordSubquery.select(criteriaBuilder.greatest(subRoot.<LocalDateTime> get("date")))
                    .where(criteriaBuilder.equal(subRoot.get("accountId"), root.get("accountId")));

            // last record should be after or equal 'since' date
            Predicate latestAfterOrEqualDate = criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("date"), lastRecordSubquery),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("date"), since),
                    root.get("accountId").in(accountIds)
            );

            return latestAfterOrEqualDate;
        };
    }
}
