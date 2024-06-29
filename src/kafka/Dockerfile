FROM apache/kafka:3.7.0
# 确保在使用apk前设置正确的用户权限
USER root

# install tools
RUN apk add curl --no-cache &&\
    apk add bash --no-cache

WORKDIR /opt/kafka

ENTRYPOINT [ "bin/kafka-server-start.sh", "config/kraft/server.properties"]