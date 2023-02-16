#FROM harbor.runjian.com/syy-open/ubuntu-jre-11:20.04
FROM harbor.runjian.com/syy-open/ubuntu-jdk-11:20.04
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Shanghai

EXPOSE 8090/tcp

ENV LC_ALL zh_CN.UTF-8
RUN apt update && apt install -y language-pack-zh-hans vim
RUN mkdir -pv /opt/device-control/logs/
COPY target/*.jar /opt/device-control
WORKDIR /opt/device-control



ENTRYPOINT ["/bin/bash","-c","java ${JAVA_OPTS} -jar /opt/device-control/*.jar  --spring.config.location=/opt/device-control/config/ ${0} ${@}"]
