#!/bin/bash
#
# This script needs to be run as 'postgres' user.
#

psql -e << END
CREATE DATABASE famility;
CREATE DATABASE familitytest;
CREATE USER famility NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT LOGIN PASSWORD 'TrustNo1';
CREATE USER familitytest NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT LOGIN PASSWORD 'TrustNo1';
GRANT ALL PRIVILEGES ON DATABASE famility TO famility;
GRANT ALL PRIVILEGES ON DATABASE familitytest TO familitytest;
\q	
END
