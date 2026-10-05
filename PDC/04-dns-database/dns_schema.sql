-- DNS database for experiment 04.
-- Load with:  mysql -u root < dns_schema.sql
-- Adjust the database name if your lab asks you to use your roll number.

CREATE DATABASE IF NOT EXISTS dns;
USE dns;

-- Authoritative tables, one per top-level domain.
DROP TABLE IF EXISTS root;
CREATE TABLE root (
    name  VARCHAR(64) PRIMARY KEY,
    port  VARCHAR(8),
    ipadd VARCHAR(64)
);
INSERT INTO root (name, port, ipadd) VALUES
    ('in',   '5123', '13'),
    ('com',  '5123', '12'),
    ('edu',  '5123', '14');

DROP TABLE IF EXISTS yahoo;
CREATE TABLE yahoo (
    name  VARCHAR(64) PRIMARY KEY,
    port  VARCHAR(8),
    ipadd VARCHAR(64)
);
INSERT INTO yahoo (name, port, ipadd) VALUES
    ('yahoo', '5124', '87'),
    ('www',   '5124', '88');

DROP TABLE IF EXISTS google;
CREATE TABLE google (
    name  VARCHAR(64) PRIMARY KEY,
    port  VARCHAR(8),
    ipadd VARCHAR(64)
);
INSERT INTO google (name, port, ipadd) VALUES
    ('google', '5125', '64'),
    ('www',    '5125', '65');

-- Resolver cache.
DROP TABLE IF EXISTS client;
CREATE TABLE client (
    name  VARCHAR(64) PRIMARY KEY,
    ipadd VARCHAR(64),
    port  VARCHAR(8)
);
