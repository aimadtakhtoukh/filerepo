#!/usr/bin/env bash
export $(cat ./database/env.properties | xargs)
export $(cat ./back/env.properties | xargs)
eval $(docker-machine env)
docker-compose down
docker-compose up