package pl.dundersztyc.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Set;

interface MongoDbPostMetricsRepository extends MongoRepository<Post, String>, PostMetricsRepository {

    @Aggregation(pipeline = {
            "{ $match: { accountId : { $in : ?0 }, date : { $gte : ?1 } } }",
            "{ $replaceRoot: { newRoot: { $mergeObjects: [ { _id: '$_id', " +
                    "likesAndCommentsRatio: { $divide: [ { $add: [ { $size: '$likedBy' }, { $size: '$comments' } ] }, { $add: [ { $size: '$viewedBy' }, 1 ] } ] } }, " +
                    "'$$ROOT' ] } } }",
            "{ $sort: { likesAndCommentsRatio: -1 } }"
    })
    Slice<Post> findRecommendedPostsWhereAccountIdInAndDateSince(Set<String> accountIds, LocalDateTime since, Pageable pageable);

}
