package com.cc.cassandra.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;

@Data
@Builder
@UserDefinedType("superpower")
public class SuperPowers implements Serializable {

    private String strength;

    private String durability;

    private boolean canFly;
}
