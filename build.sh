#!/usr/bin/env bash
export $(cat ./database/env.properties | xargs)
export $(cat ./back/env.properties | xargs)
mvn clean install -U -DskipTests
eval $(docker-machine env)
docker-compose build