#!/bin/sh

SCRIPT_DIR=$(cd $(dirname $0) && pwd)

echo $SCRIPT_DIR

APP_DIR=$(cd $SCRIPT_DIR/.. && pwd)

echo $APP_DIR

LIB_DIR=$APP_DIR/lib

echo $LIB_DIR

CLASS_PATH=$APP_DIR/classes

echo $CLASS_PATH

for i in `ls ${LIB_DIR}/*.jar`
do
  CLASS_PATH=${CLASS_PATH}:${i}
done

echo $CLASS_PATH

NOW=$(date +"%Y-%m-%d-%H%M%S")

java -cp "$CLASS_PATH" com.soprasteria.project.bxru.app.BXRUPressureImporter bin /ns/sensor/site1/pressurehr &
