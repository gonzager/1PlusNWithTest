#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE entitystore;
    CREATE USER fluxdb WITH ENCRYPTED PASSWORD 'fluxdbpwd';
    GRANT ALL PRIVILEGES ON DATABASE entitystore TO fluxdb;
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "entitystore" <<-EOSQL
    GRANT ALL PRIVILEGES ON SCHEMA public TO fluxdb;
    GRANT CREATE ON SCHEMA public TO fluxdb;
EOSQL