#!/usr/bin/env bash
mvn clean package
docker stop filerepo
docker rm filerepo
docker build -t filerepo .
docker run -d --name filerepo --env-file ./env.properties -p 80:80 -v --restart=always filerepo