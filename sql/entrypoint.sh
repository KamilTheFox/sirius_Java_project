#!/bin/bash
python /docker-entrypoint-initdb.d/generate_data.py
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f /docker-entrypoint-initdb.d/init.sql