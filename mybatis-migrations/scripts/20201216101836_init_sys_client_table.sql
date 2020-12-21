-- // init_sys_client_table
-- Migration SQL that makes the change goes here.
CREATE TABLE oauth_access_token (
  token_id VARCHAR(256) NULL DEFAULT NULL,
  token TEXT NULL DEFAULT NULL,
  authentication_id VARCHAR(128) NOT NULL PRIMARY KEY,
  user_name VARCHAR(256) NULL DEFAULT NULL,
  client_id VARCHAR(256) NULL DEFAULT NULL,
  authentication text NULL DEFAULT NULL,
  refresh_token VARCHAR(256) NULL DEFAULT NULL);
ALTER TABLE public.oauth_access_token OWNER to dna_portal;

CREATE TABLE oauth_approvals (
  userId VARCHAR(256) NULL DEFAULT NULL,
  clientId VARCHAR(256) NULL DEFAULT NULL,
  scope VARCHAR(256) NULL DEFAULT NULL,
  status VARCHAR(10) NULL DEFAULT NULL,
  expiresAt time NULL DEFAULT NULL,
  lastModifiedAt time NULL DEFAULT NULL);
ALTER TABLE public.oauth_approvals OWNER to dna_portal;

CREATE TABLE oauth_client_details (
  client_id VARCHAR(128) NOT NULL PRIMARY KEY,
  resource_ids VARCHAR(256) NULL DEFAULT NULL,
  client_secret VARCHAR(256) NULL DEFAULT NULL,
  scope VARCHAR(256) NULL DEFAULT NULL,
  authorized_grant_types VARCHAR(256) NULL DEFAULT NULL,
  web_server_redirect_uri VARCHAR(256) NULL DEFAULT NULL,
  authorities VARCHAR(256) NULL DEFAULT NULL,
  access_token_validity INT8 NULL DEFAULT NULL,
  refresh_token_validity INT8 NULL DEFAULT NULL,
  additional_information VARCHAR(4096) NULL DEFAULT NULL,
  autoapprove VARCHAR(256) NULL DEFAULT NULL);
ALTER TABLE public.oauth_client_details OWNER to dna_portal;
insert into oauth_client_details (client_id,resource_ids,client_secret,scope,authorized_grant_types,
web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove) values
('website', 'website', '$2a$10$.ebjcgCVOHuEscJ6xLyQcu21nW93XuHZ2qk2TRbTofDLVhPY0C5S2', 'all', 'authorization_code,refresh_token',
'http://192.168.10.58/web-portal/su/token', 'admin,ROLE_admin',36000, null, null, true);

CREATE TABLE oauth_client_token (
  token_id VARCHAR(256) NULL DEFAULT NULL,
  token text NULL DEFAULT NULL,
  authentication_id VARCHAR(128) NOT NULL PRIMARY KEY,
  user_name VARCHAR(256) NULL DEFAULT NULL,
  client_id VARCHAR(256) NULL DEFAULT NULL);
ALTER TABLE public.oauth_client_token OWNER to dna_portal;

CREATE TABLE oauth_code (
  code VARCHAR(256) NULL DEFAULT NULL,
  authentication text NULL DEFAULT NULL);
ALTER TABLE public.oauth_code OWNER to dna_portal;

CREATE TABLE oauth_refresh_token (
  token_id VARCHAR(256) NULL DEFAULT NULL,
  token text NULL DEFAULT NULL,
  authentication text NULL DEFAULT NULL);
ALTER TABLE public.oauth_refresh_token OWNER to dna_portal;

CREATE TABLE IF NOT EXISTS  "sys_client" (
  "id" varchar(256) NOT NULL PRIMARY KEY,
  "client_name" varchar(256) NOT NULL,
  "role_url" varchar(256) NOT NULL,
  "sort" 	int4,
  "delete_flag" boolean DEFAULT false,
  "create_datetime" timestamp(6),
  "update_datetime" timestamp(6)
);
ALTER TABLE public.sys_client OWNER to dna_portal;
COMMENT on table sys_client is '系统清单表';
COMMENT ON COLUMN sys_client.id	 is  '系统id';
COMMENT ON COLUMN sys_client.client_name	 is  '系统名称';
COMMENT ON COLUMN sys_client.role_url	 is  '获取角色地址';
COMMENT ON COLUMN sys_client.sort	 is  '排序字段';
COMMENT ON COLUMN sys_client.delete_flag	 is  '删除标识';
COMMENT ON COLUMN sys_client.create_datetime	 is  '创建时间';
COMMENT ON COLUMN sys_client.update_datetime	 is  '修改时间';

INSERT INTO public.sys_client(id,client_name,role_url,sort,delete_flag,create_datetime, update_datetime)
VALUES ('website', '门户', '', 1, false, now(), now());
INSERT INTO public.sys_client(id,client_name,role_url,sort,delete_flag,create_datetime, update_datetime)
VALUES ('zthz', 'DNA会战平台', '', 2, false, now(), now());

ALTER TABLE sys_user_role ADD COLUMN IF NOT EXISTS client_id varchar(64);
ALTER TABLE sys_user_role ALTER role_id TYPE varchar(256);
ALTER TABLE sys_role ALTER id TYPE varchar(256);
update sys_user_role set client_id = 'website';
commit;

-- //@UNDO
-- SQL to undo the change goes here.
drop table if exists oauth_access_token;
drop table if exists oauth_approvals;
drop table if exists oauth_client_details;
drop table if exists oauth_client_token;
drop table if exists oauth_code;
drop table if exists oauth_refresh_token;
drop table if exists sys_client;

ALTER TABLE sys_user_role drop column  client_id;
ALTER TABLE sys_user_role ALTER role_id TYPE int8;
ALTER TABLE sys_role ALTER id TYPE int8;

