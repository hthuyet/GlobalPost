--liquibase formatted sql
--changeset vietnq:1.1
create table demos (
  id bigint identity primary key,
  name varchar(30),
  created bigint,
  updated bigint,
);