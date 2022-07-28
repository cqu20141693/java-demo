package com.wcc.scada.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author wcc
 * @since 2022-07-27
 */
@Getter
@Setter
@TableName("command")
@ApiModel(value = "Command对象", description = "")
public class Command {

    @TableId(value = "id")
    private Long id;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @TableField("group_key")
    private String groupKey;

    @TableField("sn")
    private String sn;

    @TableField("cmd_tag")
    private String cmdTag;

    @TableField("category")
    private String category;

    @TableField("status")
    private String status;

    @TableField("status_level")
    private Integer statusLevel;

    @TableField("type")
    private String type;

    @TableField("data")
    private String data;


}
