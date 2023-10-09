#!/bin/bash
INTENT=PREFIX.TOOL_NAME.COMMAND
CMD=$1
EXTRA=${2:-NONE}
device_name=${3:-"emulator-5554"}
EXTRA=${EXTRA//\"/\_\_\^\_\_}
EXTRA=${EXTRA//[[:space:]]/\_\_\^\^\_\_}
EXTRA=${EXTRA//\,/\_\_\^\^\^\_\_}
EXTRA=${EXTRA//\'/\_\_\^\_\^\_\_}
EXTRA=${EXTRA//\+/\_\_\^\-\^\_\_}
EXTRA=${EXTRA//\|/\_\_\^\^\^\^\_\_}
EXTRA=${EXTRA//\$/\_\_\^\_\^\^\_\_}
EXTRA=${EXTRA//\*/\_\_\^\-\^\^\_\_}
EXTRA=${EXTRA//\&/\_\_\^\^\_\^\_\_}
echo $EXTRA
echo "Send command '$CMD' to TOOL_NAMEService in device $device_name"
adb -s $device_name shell am broadcast -a PREFIX.TOOL_NAME.COMMAND --es command "$CMD" --es extra "$EXTRA"
