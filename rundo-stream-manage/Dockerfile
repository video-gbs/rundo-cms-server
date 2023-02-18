#FROM harbor.runjian.com/syy-open/ubuntu-jre-11:20.04
FROM harbor.runjian.com/syy-open/ubuntu-jdk-11:20.04
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Shanghai

EXPOSE 8092/tcp

ENV LC_ALL zh_CN.UTF-8
RUN apt update && apt install -y language-pack-zh-hans vim
RUN mkdir -pv /opt/stream-manage/logs/
COPY target/*.jar /opt/stream-manage
WORKDIR /opt/stream-manage



ENTRYPOINT ["/bin/bash","-c","java ${JAVA_OPTS} -jar /opt/stream-manage/*.jar  --spring.config.location=/opt/stream-manage/config/ ${0} ${@}"]
