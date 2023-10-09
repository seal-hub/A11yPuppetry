#!/bin/bash
device_name=${1:-"emulator-5554"}
adb -s $device_name shell settings put secure enabled_accessibility_services com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService:PREFIX.TOOL_NAME/PREFIX.TOOL_NAME.app.MyTOOL_NAMEService
