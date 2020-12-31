create table USER
(
    id                 integer auto_increment,
    USERNAME           varchar(25)  DEFAULT null,
    EMAIL              varchar(255),
    PASSWORD           varchar(1024) NOT NULL,
    FIRST_NAME         varchar(255),
    LAST_NAME          varchar(255) DEFAULT null,
    CREATED_DATE       timestamp    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CREATED_BY         varchar(255) DEFAULT null,
    LAST_MODIFIED_DATE timestamp    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    LAST_MODIFIED_BY   varchar(255) DEFAULT null,
    constraint USER_pk
        primary key (id)
);

create unique index USER_EMAIL_uindex
    on USER (EMAIL);

create unique index USER_USERNAME_uindex
    on USER (USERNAME);


create table ROLE
(
    ID   integer auto_increment,
    NAME varchar(50) not null,
    constraint ROLE_pk
        primary key (ID)
);

create unique index ROLE_NAME_uindex
    on ROLE (NAME);

-- ----------------------------------------------

INSERT INTO ROLE (NAME)
VALUES ('ADMIN');
INSERT INTO ROLE (NAME)
VALUES ('USER');
INSERT INTO ROLE (NAME)
VALUES ('GUEST');

