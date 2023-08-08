# 利用node镜像构建项目
FROM node:14 as builder
# 设置工作目录
WORKDIR /app
# 拷贝依赖到工作目录
COPY PACKAGE*.json ./
# 安装项目依赖
RUN npm install
# 拷贝所有文件到工作目录
COPY . .
# 打包项目
RUN npm run build
# 利用nginx镜像运行项目
FROM nginx
# 拷贝打包后的文件到nginx目录下
COPY --from=builder /app/dist /usr/share/nginx/html
# 拷贝nginx配置文件到nginx目录下
COPY ./docker/nginx.conf /etc/nginx/conf.d/default.conf
# 暴露80端口
EXPOSE 80
# 运行nginx
CMD ["nginx", "-g", "daemon off;"]
