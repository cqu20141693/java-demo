package com.cc.starter.controller.domain.vo;

import lombok.Data;
import org.elasticsearch.common.unit.DistanceUnit;

@Data
public class GeoDistanceReq {
    private Double lat;
    private Double lon;
    private Double distance;
    private DistanceUnit unit;
    private Integer page;
    private Integer size;
}
