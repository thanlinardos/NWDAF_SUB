FROM timescale/timescaledb:latest-pg12

RUN apk update && apk add --no-cache --virtual .build-deps \
                      git make gcc musl-dev clang15 llvm15

RUN git clone https://github.com/citusdata/pg_cron.git
RUN cd pg_cron && make && make install

RUN cd / && \
        rm -rf /pg_cron && \
        apk del .build-deps

COPY init-db /docker-entrypoint-initdb.d