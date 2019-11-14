#!/usr/bin/env bash
ps -ef | grep starwarsapi
echo Entre com o ID do processo?
read varname
echo Parando API...
kill -9 $varname
ps -ef | grep starwarsapi
