#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE USER saasapp WITH ENCRYPTED PASSWORD 'saasapp';
	CREATE DATABASE saasapp;
	GRANT ALL PRIVILEGES ON DATABASE saasapp TO saasapp;
    GRANT ALL ON SCHEMA public TO saasapp;
    ALTER DATABASE saasapp OWNER TO saasapp;
EOSQL