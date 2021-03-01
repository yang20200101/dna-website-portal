-- // init_sys_menu
-- Migration SQL that makes the change goes here.

CREATE TABLE IF NOT EXISTS sys_menu (
    id SERIAL8 NOT NULL PRIMARY KEY,
		menu_name varchar(256) NOT NULL,
		root_flag boolean default false,
		parent_id int8,
    active_flag boolean default true,
    CREATE_DATETIME TIMESTAMP NOT NULL,
	  CREATE_USER  	 varchar(256) NOT NULL,
		UPDATE_DATETIME TIMESTAMP,
	  UPDATE_USER  	 varchar(256)
);
ALTER TABLE public.sys_menu OWNER to dna_portal;
COMMENT ON TABLE public.sys_menu IS '菜单表';
comment ON COLUMN sys_menu.id is 'id';
comment ON COLUMN sys_menu.menu_name is '菜单名称';
comment ON COLUMN sys_menu.root_flag is '是否为根节点';
comment ON COLUMN sys_menu.parent_id is '父节点';
comment ON COLUMN sys_menu.active_flag is '是否启用';
comment ON COLUMN sys_menu.CREATE_DATETIME is '创建时间';
comment ON COLUMN sys_menu.CREATE_USER is '创建人';
comment ON COLUMN sys_menu.UPDATE_DATETIME is '更新时间';
comment ON COLUMN sys_menu.UPDATE_USER is '更新人';

-- 创建权限表
 CREATE TABLE IF NOT EXISTS sys_permission (
    id SERIAL8 NOT NULL PRIMARY KEY,
		menu_id int8 not null,
		role_id varchar(64) not null
);
ALTER TABLE public.sys_permission OWNER to dna_portal;
COMMENT ON TABLE public.sys_permission IS '权限表';
comment ON COLUMN sys_permission.id is 'id';
comment ON COLUMN sys_permission.menu_id is '菜单id';
comment ON COLUMN sys_permission.role_id is '角色id';

INSERT INTO public.sys_menu VALUES (1, '首页', 't', 0, 't', now(), 'system', now(), 'system');
INSERT INTO public.sys_menu VALUES (2, '内容管理', 't', 0, 't', now(), 'system', now(), 'system');
INSERT INTO public.sys_menu VALUES (3, '系统管理', 't', 0, 't', now(), 'system', now(), 'system');
INSERT INTO public.sys_menu VALUES (4, '文章管理', 'f', 2, 't', now(), 'system', now(), 'system');
INSERT INTO public.sys_menu VALUES (5, '活动管理', 'f', 2, 't', now(), 'system', now(), 'system');
INSERT INTO public.sys_menu VALUES (6, '栏目管理', 'f', 2, 't', now(), 'system', now(), 'system');
INSERT INTO public.sys_menu VALUES (7, '飘窗管理', 'f', 2, 't', now(), 'system', now(), 'system');
INSERT INTO public.sys_menu VALUES (8, '用户管理', 'f', 3, 't', now(), 'system', now(), 'system');

INSERT INTO public.sys_permission (menu_id, role_id) VALUES (1, 'admin');
INSERT INTO public.sys_permission (menu_id, role_id) VALUES (2, 'admin');
INSERT INTO public.sys_permission (menu_id, role_id) VALUES (3, 'admin');
INSERT INTO public.sys_permission (menu_id, role_id) VALUES (4, 'admin');
INSERT INTO public.sys_permission (menu_id, role_id) VALUES (5, 'admin');
INSERT INTO public.sys_permission (menu_id, role_id) VALUES (6, 'admin');
INSERT INTO public.sys_permission (menu_id, role_id) VALUES (7, 'admin');
INSERT INTO public.sys_permission (menu_id, role_id) VALUES (8, 'admin');
INSERT INTO public.sys_permission (menu_id, role_id) VALUES (1, 'province');
INSERT INTO public.sys_permission (menu_id, role_id) VALUES (3, 'province');
INSERT INTO public.sys_permission (menu_id, role_id) VALUES (8, 'province');

INSERT INTO public.sys_dict("dict_category", "dict_key", "dict_national_key", "dict_value1", "dict_value2", "dict_value3", "ord", "active_flag","remark", "create_user", "create_datetime", "update_user", "update_datetime", "delete_flag") VALUES ('LAB_JOB', '4', '4', '实验室技术领导', NULL, NULL, 4, 't', NULL, 'admin', now(), NULL, NULL, 'f');
commit;

-- //@UNDO
-- SQL to undo the change goes here.
drop table if exists sys_menu;
drop table if exists sys_permission;

delete from sys_dict where dict_category = 'LAB_JOB' and dict_key = '4';
commit;
