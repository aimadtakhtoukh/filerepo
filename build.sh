#!/usr/bin/env bash
export $(cat ./database/env.properties | xargs)
export $(cat ./back/env.properties | xargs)
docker-compose build