CREATE USER 'speech.application'@'%' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON *.* TO 'speech.application'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

CREATE DATABASE speech;
