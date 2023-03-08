# 基础镜像
FROM adoptopenjdk/openjdk11:alpine
# 指定维护者的名称
MAINTAINER JiangYu 105194@runjian.com
# 声明服务使用的端口
EXPOSE 8071
# 处理时差问题
ENV TZ=Asia/Shanghai
# 配置源
RUN echo -e "https://mirror.tuna.tsinghua.edu.cn/alpine/v3.14/main\n\
    https://mirror.tuna.tsinghua.edu.cn/alpine/v3.14/community" > /etc/apk/repositories \
# 处理字体问题
RUN apk add --update add curl bash ttf-dejavu fontconfig && rm -rf /var/cache/apk/* \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etx/timezone

#将target目录下的 jar 包复制到docker容器的 /home/
ADD ./target/rundo-auth-server.jar /home/rundo/rundo-auth-server.jar
VOLUME /tmp

# 执行命令
CMD ["java","-jar","/home/rundo/rundo-auth-server.jar","--spring.profiles.active=${env}","-c"]