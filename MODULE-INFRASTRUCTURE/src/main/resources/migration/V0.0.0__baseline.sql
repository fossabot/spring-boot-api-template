# ========================= TABLE: USER =========================

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

# ========================= TABLE: USER =========================
# ========================= TABLE: ROLE =========================

create table ROLE
(
    ID   integer auto_increment,
    NAME varchar(50) not null,
    constraint ROLE_pk
        primary key (ID)
);

create unique index ROLE_NAME_uindex
    on ROLE (NAME);

# ========================= TABLE: ROLE =========================
# ========================= TABLE: USER_ROLE =========================

create table USER_ROLE
(
    USER_ID int null,
    ROLE_ID int null,
    constraint USER_ROLE_ROLE__fk
        foreign key (ROLE_ID) references role (ID)
            on delete cascade,
    constraint USER_ROLE_USER__fk
        foreign key (USER_ID) references user (id)
            on delete cascade,
    CONSTRAINT UNIQUE_USER_ROLE UNIQUE (USER_ID,ROLE_ID)
);

# ========================= TABLE: USER_ROLE =========================


-- ----------------------------------------------

INSERT INTO ROLE (NAME)
VALUES ('ADMIN');
INSERT INTO ROLE (NAME)
VALUES ('USER');
INSERT INTO ROLE (NAME)
VALUES ('GUEST');

