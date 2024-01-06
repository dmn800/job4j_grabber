create table post (
    id serial primary key,
    name text,
    description text,
    link text unique,
    created timestamp
);