package pl.dundersztyc.friendsnearby;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
class AccountLocation {

    @Id
    @GeneratedValue
    private Long id;

    private String accountId;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    private LocalDateTime date;

    static AccountLocation withoutId(String accountId, Point location, LocalDateTime date) {
        return new AccountLocation(null, accountId, location, date);
    }


}
