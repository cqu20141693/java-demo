package com.cc.gb28181.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 云台操作
 *
 * wcc 2022/5/24
 */
@AllArgsConstructor
public enum Direct {
    UP(0x08),
    DOWN(0x04),
    LEFT(0x02),
    RIGHT(0x01),
    ZOOM_IN(0x10),
    ZOOM_OUT(0x20),
    STOP(0) {
        @Override
        public int merge(int code) {
            return code;
        }

        @Override
        public boolean match(int code) {
            return code == 0;
        }
    };

    @Getter
    private final int code;

    static int[] legalCodes = new int[]{
        //上
        UP.code,
        //下
        DOWN.code,
        //左
        LEFT.code,
        //右
        RIGHT.code,
        //放大
        ZOOM_IN.code,
        //缩小
        ZOOM_OUT.code,

        //左上
        LEFT.code | UP.code,
        //右上
        RIGHT.code | UP.code,
        //左下
        LEFT.code | DOWN.code,
        //右下
        RIGHT.code | DOWN.code,

        //向上&放大
        UP.code | ZOOM_IN.code,
        //向下&放大
        DOWN.code | ZOOM_IN.code,
        //向左&放大
        LEFT.code | ZOOM_IN.code,
        //向右&放大
        RIGHT.code | ZOOM_IN.code,

        //向上放大
        UP.code | ZOOM_OUT.code,
        //向上放大
        DOWN.code | ZOOM_OUT.code,
        //左放大
        LEFT.code | ZOOM_OUT.code,
        //右放大
        RIGHT.code | ZOOM_OUT.code,

        //左上放大
        LEFT.code | UP.code | ZOOM_IN.code,
        //右上放大
        RIGHT.code | UP.code | ZOOM_IN.code,
        //左下放大
        LEFT.code | DOWN.code | ZOOM_IN.code,
        //右下放大
        RIGHT.code | DOWN.code | ZOOM_IN.code,

        //左上缩小
        LEFT.code | UP.code | ZOOM_OUT.code,
        //右上缩小
        RIGHT.code | UP.code | ZOOM_OUT.code,
        //左下缩小
        LEFT.code | DOWN.code | ZOOM_OUT.code,
        //右下缩小
        RIGHT.code | DOWN.code | ZOOM_OUT.code,

    };
    ;

    public int merge(int code) {
        return code | this.code;
    }

    public boolean match(int code) {
        return (code & this.code) != 0;
    }

    public static boolean test(int code) {
        for (int legalCode : legalCodes) {
            if (code == legalCode) {
                return true;
            }
        }
        return false;
    }

    public static Integer[] codes() {
        return Arrays.stream(Direct.values())
                     .map(Direct::getCode)
                     .toArray(Integer[]::new);
    }

    public static Direct[] values(int code) {
        return Arrays
            .stream(values())
            .filter(direct -> direct.match(code))
            .toArray(Direct[]::new);
    }

    public static Direct[] values(String code) {
        String[] codes = code.toUpperCase().split(",");

        return Arrays
            .stream(codes)
            .map(Direct::valueOf)
            .toArray(Direct[]::new);
    }
}
