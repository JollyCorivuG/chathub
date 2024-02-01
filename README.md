# chathub - 一个简化版qq的移动端项目

>聊天中心项目：仿照 qq 移动端的 UI 页面，实现了 qq 相关的核心功能！
>
>前后端全栈项目 By [JollyCorivuG](https://github.com/JollyCorivuG)

上线地址：[chathub.love](http://www.chathub.love/)

## 效果展示

![](E:\full_stack_project\chathub\docs\images\图片1.jpg)

![](E:\full_stack_project\chathub\docs\images\图片2.jpg)

![](E:\full_stack_project\chathub\docs\images\图片3.jpg)

![](E:\full_stack_project\chathub\docs\images\图片4.jpg)

## 功能大全

### 用户
- 用户注册、登录（含账号、手机验证码两种登录方式）
- 用户可以通过关键字搜索用户，并发送好友申请
- 用户可以查看通知，同意或拒绝好友申请
- 同一个账号只能在一个浏览器登录，否则原有账号会被强制下线
- 用户可以修改头像、用户名等个人信息

### 消息
- 用户可以发送消息给自己的好友（支持表情包、图片、文件等多种类型）
- 用户可以实时接收消息
- 消息面板自动刷新，会更新消息未读数
- 用户可以删除会话信息，并在有消息时重新显示

### 群组
- 用户可以创建群组，并指定群组相关信息（头像，最大人数限制等）
- 用户可以邀请自己的好友加入群组
- 用户可以在群组内发送消息，同属一个群组的人都能实时收到消息
- 用户可以同意、拒绝入群邀请

### 动态
- 用户可以发送动态，并可以选取相应的图片
- 用户可以点赞动态，并看到最新点赞的人和点赞总数
- 用户可以评论好友发送的动态，也可以进行评论的回复

## 技术栈
### 前端
- vue3 + ts
- Vant4 UI
- axios 请求库
- pinia 状态管理
- pinia 持久化插件
- Vue Router 路由管理

### 后端
- Java 8 + Spring Boot 框架（spring boot 2.6.7）
- Spring MVC + Mybatis Plus 框架
- Knife4j + Swagger 生成接口文档
- MySQL 8.x (数据存储) + Redis（缓存）
- Netty + WebSocket 实现即时通讯
- SSE 实现消息列表实时刷新
- RabbitMQ 消息队列
- MinIO 实现文件存储

## 系统架构

![](E:\full_stack_project\chathub\docs\images\系统架构图.jpg)

