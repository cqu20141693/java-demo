package com.cc.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;


//@Entity
//@Table(name = "cuc_user")
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "status", nullable = false)
    private Boolean status;
    @Column(name = "create_at", nullable = false)
    private Date createAt;
    @Column(name = "update_at", nullable = false)
    private Date updateAt;
    @Column(name = "context")
    private Object context;
    @Column(name = "gis")
    private Object gis;
}