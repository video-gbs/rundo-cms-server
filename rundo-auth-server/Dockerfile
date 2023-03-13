# 基础镜像
FROM adoptopenjdk/openjdk11:alpine
# 指定维护者的名称
MAINTAINER JiangYu 105194@runjian.com
# 声明服务使用的端口
EXPOSE 8071
# 添加阿里源
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
# 处理字体问题
RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
#将target目录下的 jar 包复制到docker容器的 /home/
ADD ./target/rundo-auth-server.jar /home/rundo/rundo-auth-server.jar
VOLUME /tmp

# 执行命令
CMD ["java","-jar","/home/rundo/rundo-auth-server.jar","--spring.profiles.active=${env}","-c"]