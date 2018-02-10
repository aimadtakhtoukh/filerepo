FROM mysql:5.7

ARG jdbcrootpassword
ARG jdbclogin
ARG jdbcpassword

# ROOT PASSWORD
ENV MYSQL_ROOT_PASSWORD=$jdbcrootpassword

ENV MYSQL_DATABASE=db
ENV MYSQL_USER=$jdbclogin
ENV MYSQL_PASSWORD=$jdbcpassword


ENV MYSQL_DATA_DIR=/var/lib/mysql \
    MYSQL_RUN_DIR=/run/mysqld \
    MYSQL_LOG_DIR=/var/log/mysql

COPY sql-scripts/1-schema.sql /docker-entrypoint-initdb.d
COPY sql-scripts/2-data.sql /docker-entrypoint-initdb.d

#PORT
EXPOSE 3306
