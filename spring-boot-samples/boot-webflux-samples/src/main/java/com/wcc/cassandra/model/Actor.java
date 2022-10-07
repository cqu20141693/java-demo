package com.wcc.cassandra.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * wcc 2022/9/28
 */
@Data
@Table("t_actor")
@NoArgsConstructor
@Accessors(chain = true)
public class Actor {
    @PrimaryKey
    private Long id;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
}
