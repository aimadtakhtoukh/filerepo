#!/usr/bin/env bash
export $(cat ./env.properties | xargs)
docker stop filerepo-database
docker rm filerepo-database
docker image rm filerepo-database
docker build -t filerepo-database --build-arg jdbcrootpassword --build-arg jdbclogin --build-arg jdbcpassword .
docker run -d --name filerepo-database -p 3306:3306 --env-file ./env.properties filerepo-database