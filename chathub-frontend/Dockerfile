# 基础镜像nginx
FROM nginx
# 工作目录
WORKDIR /usr/share/nginx/html/
# 管理员
USER root
# 拷贝文件
COPY ./docker/nginx.conf /etc/nginx/conf.d/default.conf
COPY ./dist /usr/share/nginx/html/
COPY ./src/assets /usr/share/nginx/html/assets
# 暴露端口
EXPOSE 80
# 启动命令
CMD ["nginx", "-g", "daemon off;"]

