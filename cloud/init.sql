CREATE USER userservice WITH PASSWORD 'password';
CREATE USER eventservice WITH PASSWORD 'password';

CREATE DATABASE userservice;
GRANT ALL PRIVILEGES ON DATABASE userservice TO userservice;

CREATE DATABASE eventservice;
GRANT ALL PRIVILEGES ON DATABASE eventservice TO eventservice;