#!/usr/bin/env bash
./clean.sh
docker-compose up -d
mvn clean package spring-boot:run
