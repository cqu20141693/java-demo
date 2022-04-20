package com.wujt.并发编程.volatiles;

import lombok.Data;

/**
 * @author wujt
 */
@Data
public class VolatileEntity {
    public VolatileEntity() {
    }

    public VolatileEntity(int max, Integer init_value) {
        this.max = max;
        this.init_value = init_value;
    }

    private int max;
    private Integer init_value;

}
