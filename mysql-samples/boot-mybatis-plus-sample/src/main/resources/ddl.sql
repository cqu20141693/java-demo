-- scada.command definition

CREATE TABLE `command` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `gmt_create` timestamp NULL DEFAULT NULL,
                           `gmt_modified` timestamp NULL DEFAULT NULL,
                           `group_key` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
                           `sn` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
                           `cmd_tag` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
                           `category` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
                           `status` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
                           `status_level` tinyint(4) NOT NULL,
                           `type` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
                           `data` text COLLATE utf8_unicode_ci,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1552464219553775619 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


-- scada.folder definition

CREATE TABLE `folder` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                          `name` varchar(255) DEFAULT NULL COMMENT '文件夹名称',
                          `type` varchar(255) DEFAULT NULL COMMENT '文件夹类型：固定为 topology（“图纸”文件夹），user（“我创建的”文件夹）',
                          `user_id` bigint(20) DEFAULT NULL COMMENT '所属用户',
                          `tenant_id` bigint(20) DEFAULT NULL,
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1552169119560773635 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
