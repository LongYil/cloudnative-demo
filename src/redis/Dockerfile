FROM redis:7.0.15-alpine3.20
# install tools
RUN apk add curl --no-cache &&\
    apk add bash --no-cache

COPY redis.conf /usr/local/etc/redis/redis.conf

CMD [ "redis-server", "/usr/local/etc/redis/redis.conf"]