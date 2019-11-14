#!/usr/bin/env bash
echo Iniciando API...
cd target
nohup java -jar starwarsapi-1.4.0-SNAPSHOT.jar & tail -f nohup.out
