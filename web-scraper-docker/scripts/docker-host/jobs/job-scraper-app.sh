#!/usr/bin/env bash

DOCKER_APP_ID=`docker ps | grep scraper-app | awk '{print $1}'`
