create table USER
(
    id         integer auto_increment,
    USERNAME   varchar(25)  null,
    EMAIL      varchar(255) null,
    FIRST_NAME varchar(255) null,
    LAST_NAME  varchar(255) null,
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

