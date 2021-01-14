DROP database TEMPLATE;
DROP user 'template'@'localhost';

CREATE DATABASE TEMPLATE;
CREATE user 'template'@'localhost' IDENTIFIED by 'Abc123456';

GRANT ALL ON TEMPLATE.* TO 'template'@'localhost';
GRANT FILE ON *.* TO 'template'@'localhost';
FLUSH PRIVILEGES;