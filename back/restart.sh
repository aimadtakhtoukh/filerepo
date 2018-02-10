#!/usr/bin/env bash
mvn clean package -DskipTests
docker stop filerepo-back
docker rm filerepo-back
docker build -t filerepo-back .
docker run -d --name filerepo-back --env-file ./env.properties -p 80:80 --restart=always filerepo-back