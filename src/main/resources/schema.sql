drop table people if exists;

create table books (
    book_id identity not null primary key,
    title varchar(100),
    person_id int,
    ventas int default 0
);

create table people  (
    person_id identity not null primary key,
    name varchar(20),
    surname varchar(20),
    age int(3) default 18,
    book_id int,
    foreign key (book_id) references books (book_id)
);
