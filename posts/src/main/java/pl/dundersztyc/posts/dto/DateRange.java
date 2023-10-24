package pl.dundersztyc.posts.dto;

import lombok.NonNull;

import java.time.LocalDateTime;

public record DateRange(LocalDateTime startDate, LocalDateTime endDate) {
    public DateRange(@NonNull LocalDateTime startDate, @NonNull LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("start date is later than end date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isInRange(LocalDateTime date) {
        return (date.isAfter(startDate) || date.isEqual(startDate)) &&
                (date.isBefore(endDate) || date.isEqual(endDate));
    }
}
