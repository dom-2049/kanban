# Pull in the from the official nginx image.
FROM postgres

COPY initial-structure.sql /docker-entrypoint-initdb.d/initial-structure.sql
EXPOSE 5432

ENV POSTGRES_PASSWORD password