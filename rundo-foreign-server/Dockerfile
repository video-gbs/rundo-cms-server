FROM harbor.runjian.com/syy-open/ubuntu-jdk-11:20.04
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Shanghai

EXPOSE 8061/tcp

ENV LC_ALL zh_CN.UTF-8
RUN apt update && apt install -y language-pack-zh-hans vim
RUN mkdir -pv /opt/rundo-foreign-server/logs/
COPY target/*.jar /opt/rundo-foreign-server
WORKDIR /opt/rundo-foreign-server



ENTRYPOINT ["/bin/bash","-c","java ${JAVA_OPTS} -jar /opt/rundo-foreign-server/*.jar  "]
