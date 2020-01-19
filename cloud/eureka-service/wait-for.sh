#!/usr/bin/env bash

TIMEOUT=20000
eval "array=($1)"

for var in "${array[@]}"
do
    echo "Waiting ${TIMEOUT}ms for: http://${var}"
    wait-on -t $TIMEOUT http-get://$var
done
