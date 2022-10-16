#!/bin/bash
set -e
set -x

work_space=$(cd $(dirname $0);pwd)

cd ${work_space}
cd ../
mvn clean package -Dmaven.test.skip=true
cp -f ./bin/auto_update.sh ./collect/target/collect-1.0-SNAPSHOT-jar-with-dependencies.jar  ~/index_investment/