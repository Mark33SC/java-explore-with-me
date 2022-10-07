package ru.practicum.service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "lat", column = @Column(name = "location_lat")),
        @AttributeOverride(name = "lon", column = @Column(name = "location_lon"))
})
public class Location {

    @Column(name = "lat", nullable = false)
    private Double lat;

    @Column(name = "lon", nullable = false)
    private Double lon;

}
