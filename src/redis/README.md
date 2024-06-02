###  构建镜像
构建镜像命令
docker build -t redis:v7.0.15 -f Dockerfile .

### run一个镜像，启动一个容器，使用端口6379暴露服务

docker run --name myredis  -p 6379:6379 -d redis:v7.0.15 

### 进入容器

docker exec -it xxx  bash

### 使用redis-cli连接redis
redis-cli -h 127.0.0.1 -p 6379



