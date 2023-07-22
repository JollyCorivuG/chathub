DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `account` varchar(10) NOT NULL COMMENT '账号',
    `password` varchar(128) NULL DEFAULT '' COMMENT '密码，加密存储',
    `phone` varchar(11) NOT NULL COMMENT '手机号码',
    `nick_name` varchar(32) NULL DEFAULT '' COMMENT '昵称',
    `avatar_url` varchar(255) NULL DEFAULT '' COMMENT '人物头像路径',
    `level` int NULL DEFAULT 1 COMMENT '人物等级',
    `friend_count` int NULL DEFAULT 0 COMMENT '好友数量',
    `group_count` int NULL DEFAULT 0 COMMENT '群组数量',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `unique_key_account`(`account`) USING BTREE
);