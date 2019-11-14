#!/usr/bin/env bash
echo Iniciando API...
cd target
nohup java -jar starwarsapi-1.2.0-SNAPSHOT.jar & tail -f nohup.out
