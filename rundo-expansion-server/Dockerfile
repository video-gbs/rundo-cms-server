# 基础镜像
FROM adoptopenjdk/openjdk11:alpine
# 指定维护者的名称
MAINTAINER chenjialing 97180@runjian.com
# 声明服务使用的端口
EXPOSE 8090
#将target目录下的 jar 包复制到docker容器的 /home/
ADD ./target/rundo-expansion-server.jar /home/rundo/rundo-expansion-server.jar

# 执行命令
CMD ["java","-jar","/home/rundo/rundo-expansion-server.jar.jar"]