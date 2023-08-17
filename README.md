# chathub - 一个简化版qq的移动端项目

>聊天中心项目：仿照qq移动端的UI页面，实现了qq相关的核心功能！
>
>前后端全栈项目 By [JollyCorivuG](https://github.com/JollyCorivuG)

上线地址：[chathub.love](http://www.chathub.love/)

## 功能大全
### 用户
- 用户注册、登录（含账号、手机验证码两种登录方式）
- 用户可以通过关键字搜索用户，并发送好友申请
- 用户可以查看通知，同意或拒绝好友申请

### 消息
- 用户可以发送消息给自己的好友（支持表情包、图片、文件等多种类型）
- 用户可以实时接收消息
- 消息面板自动刷新，会更新消息未读数

### 动态
- 用户可以发送动态，并可以选取相应的图片
- 用户可以点赞动态，并看到最新点赞的人和点赞总数
- 用户可以评论好友发送的动态，也可以进行评论的回复

## 技术栈
### 前端
- vue3 + ts
- Vant4 UI
- axios请求库
- pinia状态管理

### 后端
- JDK17 + Spring Boot框架（spring boot3.1）
- Spring MVC + Mybatis Plus框架
- Knife4j + Swagger 生成接口文档
- mysql8.x (数据存储) + redis（缓存）
- Netty + Websocket 实现即时通讯
- SSE 实现消息列表实时刷新
- RabbitMQ 消息队列
- MinIO 实现文件存储

