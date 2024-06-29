#!/bin/bash

docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres:16.3-alpine3.20

psql -h some-postgres -U postgres

# 查看所有schema
/