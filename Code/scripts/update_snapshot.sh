#!/bin/bash
SNAPSHOT_NAME=$1
$TOOL_NAME_PATH/scripts/load_snapshot.sh $SNAPSHOT_NAME
sleep 4
adb install -r -g $TOOL_NAME_PATH/Setup/TOOL_NAME.apk
sleep 2
$TOOL_NAME_PATH/scripts/enable-service.sh
sleep 3
$TOOL_NAME_PATH/scripts/disable-service.sh
sleep 2
$TOOL_NAME_PATH/scripts/save_snapshot.sh $SNAPSHOT_NAME

