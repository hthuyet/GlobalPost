--liquibase formatted sql
--changeset thanhlx:1.2

CREATE TABLE operations (
    id bigint identity primary key,
    name longvarchar,
    group_name longvarchar,
    description longvarchar,
    created bigint,
	updated bigint
);

CREATE TABLE permissions (
    id bigint primary key,
    name longvarchar,
    group_name longvarchar,
    description longvarchar,
    operation_ids longvarchar,
    created bigint,
	updated bigint
);
CREATE TABLE roles (
	id bigint identity primary key,
    name longvarchar,
    permissions_ids longvarchar,
    description longvarchar,
    operation_ids longvarchar,
    created bigint,
	updated bigint
);