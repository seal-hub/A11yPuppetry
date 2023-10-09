#!/bin/bash

rsync -avzhe ssh root@137.184.188.248:/root/workspace/TOOL_NAMELibrary/oserver_results $TOOL_NAME_PATH
rsync -avzhe ssh root@137.184.188.248:/root/workspace/TOOL_NAMELibrary/otmp_results $TOOL_NAME_PATH
