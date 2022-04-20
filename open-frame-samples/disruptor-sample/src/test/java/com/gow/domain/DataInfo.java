package com.gow.domain;

import lombok.Data;
import lombok.ToString;

/**
 * @author gow 2021/06/13
 */
@Data
@ToString
public class DataInfo {
    private Integer type;
    private Object value;

    public void clear() {

    }
}
