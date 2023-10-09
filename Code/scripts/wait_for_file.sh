#!/bin/bash
search_file=$1
VERBOSE=${2-"0"}
while ! $TOOL_NAME_PATH/scripts/android_file_exists.sh $search_file ;
do
  if [[ $VERBOSE == 1 ]]; then
    echo "Wait...";
  fi
  sleep 1
done
tmp_file=mktemp
adb exec-out run-as PREFIX.TOOL_NAME cat files/$search_file > $tmp_file
cat $tmp_file
rm $tmp_file
adb exec-out run-as PREFIX.TOOL_NAME rm files/$search_file
