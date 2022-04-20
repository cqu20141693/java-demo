package com.cc.link.model;

import lombok.Data;

/**
 * @author gow
 * @date 2022/2/17
 */
@Data
public class SrsResponse {
    private Integer code;
    private DataInfo data;

    @Data
    private static class DataInfo {
        private Query query;
    }

    @Data
    private static class Query {
        private Integer ssrc;
    }

    public Integer getSsrc() {
        return data.query.ssrc;
    }
}
