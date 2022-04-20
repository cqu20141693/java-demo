package com.cc.core.geo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoJson;
import org.springframework.data.geo.Point;

import java.util.Arrays;
import java.util.List;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-shape.html#_circle
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeoJsonCircle implements GeoJson<List<Double>> {
    private Point coordinates;
    private String radius;
    public static final String TYPE = "Circle";

    public String getType() {
        return TYPE;
    }

    public List<Double> getCoordinates() {
        return Arrays.asList(coordinates.getX(), coordinates.getY());
    }

}
