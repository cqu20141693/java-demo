package com.wujt.type;

import lombok.Data;

/**
 * wcc 2022/5/7
 */
@Data
public class NumberDemo {

    public static void main(String[] args) {
        short aShort = Short.parseShort("0032", 16);
        System.out.println(aShort);
    }
}
