package pl.dundersztyc.posts;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.dundersztyc.posts.dto.DateRange;

import java.time.LocalDateTime;
import java.util.List;

interface MongoDbPostRepository extends MongoRepository<Post, String>, PostRepository {
    @Query("{'accountId': ?0, 'date' : { '$gte' : ?1.startDate, '$lte' : ?1.endDate }}")
    List<Post> findByAccountIdBetweenDates(String accountId, DateRange dateRange);

    @Query("{'accountId': ?0, 'date': { $gte: ?1 }}")
    List<Post> findByAccountIdSince(String accountId, LocalDateTime since);
}
