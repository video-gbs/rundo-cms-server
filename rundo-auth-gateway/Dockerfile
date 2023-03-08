# 基础镜像
FROM adoptopenjdk/openjdk11:alpine
# 指定维护者的名称
MAINTAINER JiangYu 105194@runjian.com
# 声明服务使用的端口
EXPOSE 7720
# 配置源
RUN echo -e "https://mirror.tuna.tsinghua.edu.cn/alpine/v3.14/main\n\
    https://mirror.tuna.tsinghua.edu.cn/alpine/v3.14/community" > /etc/apk/repositories
# 处理字体问题
RUN apk add --update add curl bash ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
#将target目录下的 jar 包复制到docker容器的 /home/
ADD ./target/rundo-auth-gateway.jar /home/rundo/rundo-auth-gateway.jar
VOLUME ["/tmp"]

# 执行命令
CMD ["java","-jar","/home/rundo/rundo-auth-gateway.jar","--spring.profiles.active=${env}","-c"]