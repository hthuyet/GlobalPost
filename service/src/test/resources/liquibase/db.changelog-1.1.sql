--liquibase formatted sql
--changeset thanhlx:1.1

CREATE TABLE users (
  id bigint identity primary key,
  user_name longvarchar,
  full_name longvarchar,
  email longvarchar,
  password longvarchar,
  role_ids longvarchar,
  role_names longvarchar,
  device_group_ids longvarchar,
  device_group_names longvarchar,
  avatar_url longvarchar,
  phone longvarchar,
  description longvarchar,
  forgot_pwd_token longvarchar,
  created bigint,
  updated bigint,
  forgot_pwd_token_requested bigint,
  operation_ids longvarchar
)

CREATE TABLE email_templates (
  id longvarchar,
  value longvarchar,
  description longvarchar,
  created bigint,
  updated bigint
)
