#!/bin/bash
for snapshot in $($TOOL_NAME_PATH/scripts/list_snapshots.sh); do
  echo "Updating $snapshot"
  $TOOL_NAME_PATH/scripts/update_snapshot.sh $snapshot
done
