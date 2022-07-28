package com.wcc.scada.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 *
 * </p>
 *
 * @author wcc
 * @since 2022-07-27
 */
@ApiModel(value = "Folder对象", description = "")
public class Folder {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("文件夹名称")
    private String name;

    @ApiModelProperty("文件夹类型：固定为 topology（“图纸”文件夹），user（“我创建的”文件夹）")
    private String type;

    @ApiModelProperty("所属用户")
    private Long userId;

    private Long tenantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "Folder{" +
            "id=" + id +
            ", name=" + name +
            ", type=" + type +
            ", userId=" + userId +
            ", tenantId=" + tenantId +
        "}";
    }
}
