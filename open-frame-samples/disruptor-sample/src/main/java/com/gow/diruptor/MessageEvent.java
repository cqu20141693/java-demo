package com.gow.diruptor;

import lombok.Data;

/**
 * @author gow  2021/5/9
 * 消事件
 */
@Data
public class MessageEvent<T> {

    private T message;
}
