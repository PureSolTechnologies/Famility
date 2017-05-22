#!/bin/bash
#
# This script needs to be run as 'postgres' user.
#

psql -e << END
DROP USER IF EXISTS familitytest;
DROP USER IF EXISTS famility;
DROP DATABASE IF EXISTS familitytest;
DROP DATABASE IF EXISTS famility;
\q	
END
