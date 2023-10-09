#!/bin/bash
search_file=$1
tmp_file=mktemp
adb exec-out run-as PREFIX.TOOL_NAME cat files/$search_file > $tmp_file
cat $tmp_file | grep -q "No such file or directory" && exit 1 || exit 0
