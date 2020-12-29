-- // init_portal_table
-- Migration SQL that makes the change goes here.
CREATE TABLE IF NOT EXISTS  "sys_user" (
  "id" serial8 NOT NULL PRIMARY KEY,
  "ext_id" varchar(256),
  "username" varchar(256) NOT NULL,
  "nickname" varchar(256) NOT NULL,
  "password" varchar(256) NOT NULL,
  "province" varchar(256),
	"city" varchar(64),
  "area"  varchar(64),
  "server_nos" varchar(256),
  "id_card_no" varchar(256),
  "birth_date" timestamp(6),
  "gender" varchar(64),
  "phone" varchar(64),
	"job"  varchar(64),
  "org_code" varchar(64),
	"lab_code"  varchar(256),
  "lab_name" varchar(64),
	"is_add_org" boolean DEFAULT false,
	"org_add_code"  varchar(256),
  "org_add_name"  varchar(256),
  "remark" varchar(64),
  "status" boolean DEFAULT true,
  "delete_flag" boolean DEFAULT false,
  "create_datetime" timestamp(6),
  "update_datetime" timestamp(6)
);
ALTER TABLE public.sys_user OWNER to dna_portal;


CREATE TABLE IF NOT EXISTS sys_role (
    id serial8 NOT NULL PRIMARY KEY,
    role_name varchar(256) NOT NULL,
		role_desc text,
    active_flag boolean default true,
		role_ord INTEGER,
    CREATE_DATETIME TIMESTAMP NOT NULL,
	  CREATE_USER  	 varchar(256) NOT NULL,
		UPDATE_DATETIME TIMESTAMP,
	  UPDATE_USER  	 varchar(256),
	  EXT_ID   varchar(64)
);
ALTER TABLE public.sys_role OWNER to dna_portal;

CREATE TABLE IF NOT EXISTS sys_user_role (
    id SERIAL8 NOT NULL PRIMARY KEY,
		user_id int8 not null,
		role_id varchar(64) not null
);
ALTER TABLE public.sys_user_role OWNER to dna_portal;


CREATE TABLE IF NOT EXISTS "po_activity" (
  "id" serial8 NOT NULL PRIMARY KEY,
  "ext_id" varchar(64),
  "title" varchar(255),
  "thumbnail_id" int8,
  "date" timestamp(6),
  "description" varchar(1000),
  "content" text,
  "apply_number" int4 DEFAULT 0,
  "created_at" timestamp(6),
  "updated_at" timestamp(6),
  "deleted" boolean DEFAULT false
);
ALTER TABLE public.po_activity OWNER to dna_portal;
COMMENT ON COLUMN "po_activity"."id" IS '主键';
COMMENT ON COLUMN "po_activity"."ext_id" IS '源数据ID';
COMMENT ON COLUMN "po_activity"."title" IS '标题';
COMMENT ON COLUMN "po_activity"."thumbnail_id" IS '缩略图Id';
COMMENT ON COLUMN "po_activity"."date" IS '截至日期';
COMMENT ON COLUMN "po_activity"."description" IS '描述';
COMMENT ON COLUMN "po_activity"."content" IS '活动内容';
COMMENT ON COLUMN "po_activity"."created_at" IS '创建日期';
COMMENT ON COLUMN "po_activity"."updated_at" IS '更新日期';
COMMENT ON COLUMN "po_activity"."deleted" IS 'true-已删除、默认false';
COMMENT ON COLUMN "po_activity"."apply_number" IS '报名人数';

CREATE TABLE  IF NOT EXISTS  "po_activity_user" (
  "id" serial8 NOT NULL PRIMARY KEY,
  "activity_id" int8,
  "ext_activity_id" varchar(64),
  "user_id" int8,
  "ext_user_id" varchar(64)
);
ALTER TABLE public.po_activity_user OWNER to dna_portal;
COMMENT ON COLUMN "po_activity_user"."id" IS '主键';
COMMENT ON COLUMN "po_activity_user"."activity_id" IS '活动ID';
COMMENT ON COLUMN "po_activity_user"."ext_activity_id" IS '源活动ID';
COMMENT ON COLUMN "po_activity_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "po_activity_user"."ext_user_id" IS '源用户ID';

CREATE TABLE  IF NOT EXISTS  "po_advertisement" (
  "id" serial8 NOT NULL PRIMARY KEY,
  "ext_id" varchar(64),
  "title" varchar(255),
  "link" varchar(255),
  "thumbnail_id" int8,
  "position" varchar(255) NOT NULL,
  "level" int4,
  "is_publish" bool,
  "created_at" timestamp(6),
  "updated_at" timestamp(6),
  "deleted"  boolean DEFAULT false
);
ALTER TABLE public.po_advertisement OWNER to dna_portal;
COMMENT ON COLUMN "po_advertisement"."id" IS '主键';
COMMENT ON COLUMN "po_advertisement"."ext_id" IS '源数据ID';
COMMENT ON COLUMN "po_advertisement"."title" IS '标题';
COMMENT ON COLUMN "po_advertisement"."link" IS '点击跳转的活动链接';
COMMENT ON COLUMN "po_advertisement"."thumbnail_id" IS '缩略图id';
COMMENT ON COLUMN "po_advertisement"."position" IS '飘窗位置 left-左上、right-右上、float-飘窗、ad-广告位';
COMMENT ON COLUMN "po_advertisement"."level" IS '飘窗等级';
COMMENT ON COLUMN "po_advertisement"."is_publish" IS 'true-已发布、false-未发布';
COMMENT ON COLUMN "po_advertisement"."created_at" IS '创建日期';
COMMENT ON COLUMN "po_advertisement"."updated_at" IS '更新日期';
COMMENT ON COLUMN "po_advertisement"."deleted" IS 'true-已删除、默认false';

CREATE TABLE  IF NOT EXISTS  "po_article" (
  "id" serial8 NOT NULL PRIMARY KEY,
  "ext_id" varchar(64),
  "publish_date" timestamp(6) NOT NULL,
  "category_ext_id" varchar(64) ,
  "category_id" int8,
  "level" int4,
  "is_publish" bool DEFAULT true,
  "is_need_update" bool DEFAULT false,
  "is_top" bool DEFAULT false,
  "title" varchar(512)  NOT NULL,
  "source" varchar(256) ,
  "link" varchar(512) ,
  "description" varchar(2000) ,
  "thumbnail_id" int8,
  "is_focus" bool DEFAULT false,
  "content" text,
  "draft_ext_id" varchar(64) ,
  "draft_id" int8,
  "deleted" bool DEFAULT false,
  "created_at" timestamp(6),
  "updated_at" timestamp(6)
);
ALTER TABLE public.po_article OWNER to dna_portal;
COMMENT ON TABLE public.po_article  IS '文章表';
COMMENT ON COLUMN po_article.id	 is 'id';
COMMENT ON COLUMN po_article.ext_id	 is '源id';
COMMENT ON COLUMN po_article.draft_id	 is '草稿id';
COMMENT ON COLUMN po_article.draft_ext_id	 is '草稿源id';
COMMENT ON COLUMN po_article.publish_date	 is '发布日期';
COMMENT ON COLUMN po_article.category_ext_id	 is '栏目源id';
COMMENT ON COLUMN po_article.level	 is '控制文章排序、1级排序最靠前，50-1级、40-2级、30-3级、20-4级、10-5级';
COMMENT ON COLUMN po_article.category_id	 is '栏目id';
COMMENT ON COLUMN po_article.is_publish	 is '是否发布';
COMMENT ON COLUMN po_article.is_need_update	 is '是否需要更新;标记前台文章是否为最新发布的，false-已更新、true-待更新';
COMMENT ON COLUMN po_article.is_top	 is '开启后【已置顶且等级为1级】的将排在最上面，不选择等级则最新发布的排在最上面;';
COMMENT ON COLUMN po_article.title	 is '标题';
COMMENT ON COLUMN po_article.source	 is '来源';
COMMENT ON COLUMN po_article.link	 is '链接';
COMMENT ON COLUMN po_article.description	 is '描述';
COMMENT ON COLUMN po_article.thumbnail_id	 is '附件id';
COMMENT ON COLUMN po_article.is_focus	 is '是否设置为焦点图';
COMMENT ON COLUMN po_article.content	 is '文章内容';
COMMENT ON COLUMN po_article.deleted	 is '删除标识';
COMMENT ON COLUMN po_article.created_at	 is '创建时间';
COMMENT ON COLUMN po_article.updated_at	 is '更新时间';

CREATE TABLE  IF NOT EXISTS  "po_category" (
  "id" serial8 NOT NULL PRIMARY KEY,
  "ext_id" varchar(64),
  "name" varchar(64) NOT NULL,
  "alias" varchar(64) NOT NULL,
  "sort" int4 NOT NULL,
  "remark" varchar(2000),
  "deleted" bool DEFAULT false,
  "status" bool DEFAULT true,
  "created_at" timestamp(6),
  "updated_at" timestamp(6)
);
ALTER TABLE public.po_category OWNER to dna_portal;
COMMENT ON TABLE public.po_category  IS '栏目表';
COMMENT ON COLUMN po_category.id	 is 'id';
COMMENT ON COLUMN po_category.ext_id	 is '源id';
COMMENT ON COLUMN po_category.name	 is '栏目名称';
COMMENT ON COLUMN po_category.alias	 is '栏目别名';
COMMENT ON COLUMN po_category.sort	 is '栏目排序';
COMMENT ON COLUMN po_category.remark	 is '栏目备注';
COMMENT ON COLUMN po_category.deleted	 is '删除标识';
COMMENT ON COLUMN po_category.status	 is '状态;启用后新建文章可选择此栏目';
COMMENT ON COLUMN po_category.created_at	 is '创建时间';
COMMENT ON COLUMN po_category.updated_at	 is '更新时间';



CREATE TABLE  IF NOT EXISTS  "po_draft_article" (
  "id" serial8 NOT NULL PRIMARY KEY,
  "ext_id" varchar(64),
  "publish_date" timestamp(6) NOT NULL,
  "category_ext_id" varchar(64),
  "level" int4,
  "category_id" int8,
  "is_publish" bool DEFAULT true,
  "is_need_update" bool DEFAULT false,
  "is_top" bool DEFAULT false,
  "title" varchar(512) NOT NULL,
  "source" varchar(256),
  "link" varchar(512),
  "description" varchar(2000),
  "thumbnail_id" int8,
  "is_focus" bool DEFAULT false,
  "content"  text,
  "deleted" bool DEFAULT false,
  "created_at" timestamp(6),
  "updated_at" timestamp(6)
);
ALTER TABLE public.po_draft_article OWNER to dna_portal;
COMMENT ON TABLE public.po_draft_article  IS '草稿表';
COMMENT ON COLUMN po_draft_article.id	 is 'id';
COMMENT ON COLUMN po_draft_article.ext_id	 is '源id';
COMMENT ON COLUMN po_draft_article.publish_date	 is '发布日期';
COMMENT ON COLUMN po_draft_article.category_ext_id	 is '栏目源id';
COMMENT ON COLUMN po_draft_article.level	 is '控制文章排序、1级排序最靠前，50-1级、40-2级、30-3级、20-4级、10-5级';
COMMENT ON COLUMN po_draft_article.category_id	 is '栏目id';
COMMENT ON COLUMN po_draft_article.is_publish	 is '是否发布';
COMMENT ON COLUMN po_draft_article.is_need_update	 is '是否需要更新;标记前台文章是否为最新发布的，false-已更新、true-待更新';
COMMENT ON COLUMN po_draft_article.is_top	 is '开启后【已置顶且等级为1级】的将排在最上面，不选择等级则最新发布的排在最上面;';
COMMENT ON COLUMN po_draft_article.title	 is '标题';
COMMENT ON COLUMN po_draft_article.source	 is '来源';
COMMENT ON COLUMN po_draft_article.link	 is '链接';
COMMENT ON COLUMN po_draft_article.description	 is '描述';
COMMENT ON COLUMN po_draft_article.thumbnail_id	 is '附件id';
COMMENT ON COLUMN po_draft_article.is_focus	 is '是否设置为焦点图';
COMMENT ON COLUMN po_draft_article.content	 is '文章内容';
COMMENT ON COLUMN po_draft_article.deleted	 is '删除标识';
COMMENT ON COLUMN po_draft_article.created_at	 is '创建时间';
COMMENT ON COLUMN po_draft_article.updated_at	 is '更新时间';

CREATE TABLE  IF NOT EXISTS  "po_thumbnail" (
  "id" serial8 NOT NULL PRIMARY KEY,
  "bucket_name" varchar(256) NOT NULL,
  "file_name" varchar(256) NOT NULL,
  "url" varchar(1000),
  "deleted" bool DEFAULT false,
  "created_at" timestamp(6),
  "updated_at" timestamp(6)
);
ALTER TABLE public.po_thumbnail OWNER to dna_portal;
COMMENT ON TABLE public.po_thumbnail  IS '附件表';
COMMENT ON COLUMN "po_thumbnail"."id" IS 'id';
COMMENT ON COLUMN "po_thumbnail"."bucket_name" IS '桶名';
COMMENT ON COLUMN "po_thumbnail"."file_name" IS '文件名';
COMMENT ON COLUMN "po_thumbnail"."url" IS '地址';
COMMENT ON COLUMN "po_thumbnail"."deleted" IS '软删除';
COMMENT ON COLUMN "po_thumbnail"."created_at" IS '创建时间';
COMMENT ON COLUMN "po_thumbnail"."updated_at" IS '更新时间';

CREATE TABLE  IF NOT EXISTS  "po_application" (
   "id" serial8 NOT NULL PRIMARY KEY,
   "ext_id" varchar(64),
   "user_id" int8,
   "user_ext_id" varchar(64),
   "activity_id" int8,
   "activity_ext_id" varchar(64),
   "thumbnail_id" int8,
   "is_latest" bool DEFAULT false,
   "deleted" bool DEFAULT false,
   "created_at" timestamp(6),
   "updated_at" timestamp(6)
);
ALTER TABLE public.po_application OWNER to dna_portal;
COMMENT ON TABLE public.po_application  IS '申请表';
COMMENT ON COLUMN "po_application"."id" IS 'id';
COMMENT ON COLUMN "po_application"."ext_id" IS '源id';
COMMENT ON COLUMN "po_application"."user_id" IS '用户id';
COMMENT ON COLUMN "po_application"."user_ext_id" IS '用户源id';
COMMENT ON COLUMN "po_application"."activity_id" IS '活动id';
COMMENT ON COLUMN "po_application"."activity_ext_id" IS '活动源id';
COMMENT ON COLUMN "po_application"."thumbnail_id" IS '附件id';
COMMENT ON COLUMN "po_application"."is_latest" IS '是否是最新上传的报名表';
COMMENT ON COLUMN "po_application"."deleted" IS 'true-已删除、默认false';
COMMENT ON COLUMN "po_application"."created_at" IS '创建时间';
COMMENT ON COLUMN "po_application"."updated_at" IS '更新时间';

 CREATE TABLE IF NOT EXISTS SYS_REGIONALISM
(
	id SERIAL8 NOT NULL PRIMARY KEY,
	regionalism_code VARCHAR(12),
	regionalism_name VARCHAR(300),
	parent_code      VARCHAR(12),
	spell_short      VARCHAR(300),
	active_flag      BOOLEAN default TRUE not null,
	delete_flag      BOOLEAN default FALSE not null,
	remark           VARCHAR(500),
	CREATE_USER	VARCHAR(64) NOT NULL,
	CREATE_DATETIME	TIMESTAMP NOT NULL,
	UPDATE_USER	VARCHAR(64),
	UPDATE_DATETIME	TIMESTAMP
);
ALTER TABLE public.SYS_REGIONALISM OWNER to dna_portal;

 CREATE TABLE IF NOT EXISTS SYS_DICT_CATEGORY (
    id SERIAL8 NOT NULL PRIMARY KEY,
		DICT_CATEGORY_CODE	VARCHAR(64) NOT NULL,
		DICT_CATEGORY_NAME	VARCHAR(256) NOT NULL,
		DICT_CATEGORY_DESC	VARCHAR(256),
		ord	INT8 NOT NULL
);
ALTER TABLE public.SYS_DICT_CATEGORY
    OWNER to dna_portal;

-- 字典详情表
 CREATE TABLE IF NOT EXISTS SYS_DICT (
 id SERIAL8 NOT NULL PRIMARY KEY,
	DICT_CATEGORY	VARCHAR(64) NOT NULL,
	DICT_KEY	VARCHAR(64),
	DICT_NATIONAL_KEY	VARCHAR(64),
	DICT_VALUE1	VARCHAR(512),
	DICT_VALUE2	VARCHAR(2000),
	DICT_VALUE3	VARCHAR(512),
	ORD	INT8 NOT NULL,
	ACTIVE_FLAG	BOOLEAN,
	REMARK	VARCHAR(256),
	CREATE_USER	VARCHAR(64) NOT NULL,
	CREATE_DATETIME	TIMESTAMP NOT NULL,
	UPDATE_USER	VARCHAR(64),
	UPDATE_DATETIME	TIMESTAMP,
	DELETE_FLAG	BOOLEAN DEFAULT FALSE
);
ALTER TABLE public.SYS_DICT
    OWNER to dna_portal;

INSERT INTO public.sys_dict_category(dict_category_code,dict_category_name,dict_category_desc,ord)
VALUES ('GENDER', '性别', '', 1);
INSERT INTO public.sys_dict("dict_category", "dict_key", "dict_national_key", "dict_value1", "dict_value2", "dict_value3", "ord", "active_flag","remark", "create_user", "create_datetime", "update_user", "update_datetime", "delete_flag")
VALUES ('GENDER', '1', '1', '男', NULL, NULL, 1, 't', NULL, 'admin', now(), 'admin', now(), 'f');
INSERT INTO public.sys_dict("dict_category", "dict_key", "dict_national_key", "dict_value1", "dict_value2", "dict_value3", "ord", "active_flag","remark", "create_user", "create_datetime", "update_user", "update_datetime", "delete_flag")
VALUES ('GENDER', '2', '2', '女', NULL, NULL, 2, 't', NULL, 'admin', now(), 'admin', now(), 'f');
INSERT INTO public.sys_dict_category(dict_category_code,dict_category_name,dict_category_desc,ord)
VALUES ('LAB_JOB', '工作岗位', '', 1);
INSERT INTO public.sys_dict("dict_category", "dict_key", "dict_national_key", "dict_value1", "dict_value2", "dict_value3", "ord", "active_flag","remark", "create_user", "create_datetime", "update_user", "update_datetime", "delete_flag")
VALUES ('LAB_JOB', '1', '1', 'DNA实验室负责人', NULL, NULL, 1, 't', NULL, 'admin', now(), 'admin', now(), 'f');
INSERT INTO public.sys_dict("dict_category", "dict_key", "dict_national_key", "dict_value1", "dict_value2", "dict_value3", "ord", "active_flag","remark", "create_user", "create_datetime", "update_user", "update_datetime", "delete_flag")
VALUES ('LAB_JOB', '2', '2', '数据库管理员', NULL, NULL, 2, 't', NULL, 'admin', now(), 'admin', now(), 'f');
INSERT INTO public.sys_dict("dict_category", "dict_key", "dict_national_key", "dict_value1", "dict_value2", "dict_value3", "ord", "active_flag","remark", "create_user", "create_datetime", "update_user", "update_datetime", "delete_flag")
VALUES ('LAB_JOB', '3', '3', '检验人员', NULL, NULL, 3, 't', NULL, 'admin', now(), 'admin', now(), 'f');

commit;

-- //@UNDO
-- SQL to undo the change goes here.


