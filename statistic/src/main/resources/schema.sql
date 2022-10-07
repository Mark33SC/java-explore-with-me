CREATE TABLE IF NOT EXISTS endpoint_hits
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    app
    VARCHAR
(
    255
) NOT NULL,
    uri TEXT NOT NULL,
    ip VARCHAR
(
    20
) NOT NULL,
    timestamp_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_endpoint_hits PRIMARY KEY
(
    id
)
    );