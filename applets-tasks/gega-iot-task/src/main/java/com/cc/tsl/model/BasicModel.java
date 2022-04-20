package com.cc.tsl.model;

import lombok.Data;

import java.util.List;

@Data
public class BasicModel {
    private String category;
    private List<ProductInfo> devices;
    private List<BasicProperties> models;
}
