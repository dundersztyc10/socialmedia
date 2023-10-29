package pl.dundersztyc.friendsnearby.dto;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public record LocationRequest(double longitude,
                              double latitude) {
    public LocationRequest(double longitude, double latitude) {
        if (isLongitudeIncorrect(longitude) || isLatitudeIncorrect(latitude)) {
            throw new IllegalArgumentException("invalid longitude or latitude");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Point toPoint() {
        Coordinate coordinate = new Coordinate(longitude, latitude);
        return new GeometryFactory().createPoint(coordinate);
    }

    private static boolean isLongitudeIncorrect(double longitude) {
        return longitude < -180 || longitude > 180;
    }

    private static boolean isLatitudeIncorrect(double latitude) {
        return latitude < -90 || latitude > 90;
    }
}
