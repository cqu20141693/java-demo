package com.cc.tsl.model;

import lombok.Data;

import java.util.Set;

@Data
public class ProductInfo {
    private String name;
    private Set<Integer> indexes;
}
