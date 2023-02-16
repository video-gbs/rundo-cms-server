#FROM harbor.runjian.com/syy-open/ubuntu-jre-11:20.04
FROM harbor.runjian.com/syy-open/ubuntu-jdk-11:20.04
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Shanghai

EXPOSE 8091/tcp

ENV LC_ALL zh_CN.UTF-8
RUN apt update && apt install -y language-pack-zh-hans vim
RUN mkdir -pv /opt/parsing-engine/logs/
COPY target/*.jar /opt/parsing-engine
WORKDIR /opt/parsing-engine



ENTRYPOINT ["/bin/bash","-c","java ${JAVA_OPTS} -jar /opt/parsing-engine/*.jar  --spring.config.location=/opt/parsing-engine/config/ ${0} ${@}"]
