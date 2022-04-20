package com.wujt.disruptor.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author wujt  2021/5/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Log {
    private Integer value;
    private String date;
}
