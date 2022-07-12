#!/bin/bash

version=$1
docker build -f Dockerfile --no-cache -t wujt114655/giot:${version} .
docker tag wujt114655/giot:${version} wujt114655/demo:${version}
docker push wujt114655/demo:${version}
if [ $? -eq 0 ]; then
 echo "push Success"
else
 echo "push failed"
fi
