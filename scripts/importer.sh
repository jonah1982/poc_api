#!/bin/bash

SCRIPT_DIR=$(cd $(dirname $0) && pwd)
PROCESS_KEYWORDS='FDXOutboundAgentServer'

echo "checking $PROCESS_KEYWORDS"

if ps -ef | grep $PROCESS_KEYWORDS  | grep -v grep;
then
	echo "The importing process ($PROCESS_KEYWORDS) is running";
else
	echo "There is no importing process, start $SCRIPT_DIR/importer_startup.sh";
	$SCRIPT_DIR"/importer_startup.sh"
fi
