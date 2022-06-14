package com.cc.things.codec;

public interface Encoder<T> {

    Payload encode(T body);

}
