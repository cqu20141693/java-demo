package com.wcc.scada.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-08-03
 */
@Getter
@Setter
@TableName("image_components")
@ApiModel(value = "ImageComponents对象", description = "")
public class ImageComponents {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("图片url地址")
    @TableField("image")
    private String image;

    @ApiModelProperty("所属文件夹")
    @TableField("folder_id")
    private Long folderId;

    @ApiModelProperty("所属用户")
    @TableField("user_id")
    private Long userId;


}
