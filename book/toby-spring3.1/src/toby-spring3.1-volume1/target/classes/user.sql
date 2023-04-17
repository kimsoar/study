create table users (
    id varchar(10) primary key,
    name varchar(20) not null,
    password varchar(10) not null
);

alter table users add level tinyint not null;
alter table users add login int not null;
alter table users add recommend int not null;



