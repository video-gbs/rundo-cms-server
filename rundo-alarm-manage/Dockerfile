#FROM harbor.runjian.com/syy-open/ubuntu-jre-11:20.04
FROM harbor.runjian.com/syy-open/ubuntu-jdk-11:20.04
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Shanghai

EXPOSE 8094/tcp

ENV LC_ALL zh_CN.UTF-8
RUN apt update && apt install -y language-pack-zh-hans vim
RUN mkdir -pv /opt/alarm-manage/logs/
COPY target/*.jar /opt/alarm-manage
WORKDIR /opt/alarm-manage



ENTRYPOINT ["/bin/bash","-c","java ${JAVA_OPTS} -jar /opt/alarm-manage/*.jar"]
