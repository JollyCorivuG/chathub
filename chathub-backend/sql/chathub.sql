CREATE DATABASE chathub DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `account` varchar(10) NOT NULL COMMENT '账号',
    `password` varchar(128) NULL DEFAULT '' COMMENT '密码，加密存储',
    `phone` varchar(11) NOT NULL COMMENT '手机号码',
    `nick_name` varchar(32) NULL DEFAULT '' COMMENT '昵称',
    `avatar_url` VARCHAR(1024) NULL DEFAULT '' COMMENT '人物头像路径',
    `level` int NULL DEFAULT 1 COMMENT '人物等级',
    `friend_count` int NULL DEFAULT 0 COMMENT '好友数量',
    `group_count` int NULL DEFAULT 0 COMMENT '群组数量',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `unique_key_account`(`account`) USING BTREE
);

DROP TABLE IF EXISTS `tb_friend_relation`;
CREATE TABLE `tb_friend_relation`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id1` bigint(20) NOT NULL COMMENT '用户1的id',
    `user_id2` bigint(20) NOT NULL COMMENT '用户2的id',
    `room_id` bigint(20) NOT NULL COMMENT '会话id',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
);

DROP TABLE IF EXISTS `tb_friend_notice`;
CREATE TABLE `tb_friend_notice`  (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `connect_user_id` bigint(20) NOT NULL COMMENT '关联的用户id',
     `other_user_id` bigint(20) NOT NULL COMMENT '对方用户的id',
     `description` varchar(100) NULL DEFAULT '' COMMENT '描述',
     `notice_type` tinyint NOT NULL COMMENT '通知类型，0-添加别人，1-别人添加自己',
     `status_info` tinyint NOT NULL COMMENT '0-等待验证、1-验证通过、2-已拒绝、3-已接受',
     `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`) USING BTREE,
     INDEX `idx_connect_user_id`(`connect_user_id`) USING BTREE
);

DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`  (
       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
       `room_id` bigint(20) NOT NULL COMMENT '会话id',
       `from_user_id` bigint(20) NOT NULL COMMENT '发送者id',
       `content` varchar(1024) NULL COMMENT '消息内容',
       `msg_type` tinyint NOT NULL COMMENT '消息类型 0-正常文本 1-图片消息 2-文件消息',
       `extra` json NULL COMMENT '扩展信息',
       `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
       `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
       PRIMARY KEY (`id`) USING BTREE,
       INDEX `idx_room_id`(`room_id`) USING BTREE,
       INDEX `idx_from_uid`(`from_user_id`) USING BTREE
);

DROP TABLE IF EXISTS `tb_room`;
CREATE TABLE `tb_room`  (
       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
       `room_type` tinyint NOT NULL COMMENT '会话类型 0-单聊 1-群聊',
       `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
       `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
       PRIMARY KEY (`id`) USING BTREE
);

DROP TABLE IF EXISTS `tb_feeds`;
CREATE TABLE `tb_feeds`  (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `talk_id` bigint(20) NOT NULL COMMENT '说说的id',
     `connect_user_id` bigint(20) NOT NULL COMMENT '需要推送给的用户id',
     `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`) USING BTREE,
     INDEX `idx_connect_uid`(`connect_user_id`) USING BTREE
);

DROP TABLE IF EXISTS `tb_talk`;
CREATE TABLE `tb_talk`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `author_id` bigint(20) NOT NULL COMMENT '发布者id',
    `content` VARCHAR(1024) NULL DEFAULT '' COMMENT '文案内容',
    `extra` json NULL COMMENT '说说的额外内容，是一个json列表，可以是图片，视频',
    `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_author_uid`(`author_id`) USING BTREE
);

DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `sender_id` bigint(20) NOT NULL COMMENT '发送者id',
    `talk_id` bigint(20) NOT NULL COMMENT '说说id',
    `content` VARCHAR(300) NOT NULL COMMENT '评论内容',
    `reply_user_id` bigint(20) NULL DEFAULT 0 COMMENT '回复的用户id，如果是0，表示是对说说的评论',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_talk_uid`(`talk_id`) USING BTREE
);

DROP TABLE IF EXISTS `tb_like`;
CREATE TABLE `tb_like`  (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `talk_id` bigint(20) NOT NULL COMMENT '说说id',
   `user_id` bigint(20) NULL DEFAULT 0 COMMENT '点赞的用户id',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`) USING BTREE,
   INDEX `idx_talk_uid`(`talk_id`) USING BTREE
);
