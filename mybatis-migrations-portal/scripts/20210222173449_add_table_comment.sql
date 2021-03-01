-- // add_table_comment
-- Migration SQL that makes the change goes here.
COMMENT ON TABLE public.sys_dict IS '字典表';
comment ON COLUMN sys_dict.id is 'id';
comment ON COLUMN sys_dict.dict_category is '类别';
comment ON COLUMN sys_dict.dict_key is '字典key';
comment ON COLUMN sys_dict.dict_national_key is '字典key';
comment ON COLUMN sys_dict.dict_value1 is '值1';
comment ON COLUMN sys_dict.dict_value2 is '值2';
comment ON COLUMN sys_dict.dict_value3 is '值3';
comment ON COLUMN sys_dict.ord is '排序字段';
comment ON COLUMN sys_dict.active_flag is '是否启用';
comment ON COLUMN sys_dict.remark is '备注';
comment ON COLUMN sys_dict.create_user is '创建人';
comment ON COLUMN sys_dict.create_datetime is '创建时间';
comment ON COLUMN sys_dict.update_user is '更新人';
comment ON COLUMN sys_dict.update_datetime is '更新时间';
comment ON COLUMN sys_dict.delete_flag is '删除标识';

COMMENT ON TABLE public.sys_dict_category IS '字典类别表';
comment ON COLUMN sys_dict_category.id is 'id';
comment ON COLUMN sys_dict_category.dict_category_code is '字典编码';
comment ON COLUMN sys_dict_category.dict_category_name is '名称';
comment ON COLUMN sys_dict_category.dict_category_desc is '描述';
comment ON COLUMN sys_dict_category.ord is '排序字段';

COMMENT ON TABLE public.sys_regionalism IS '行政区划表';
comment ON COLUMN sys_regionalism.id is 'id';
comment ON COLUMN sys_regionalism.regionalism_code is '当前节点';
comment ON COLUMN sys_regionalism.regionalism_name is '区划名称';
comment ON COLUMN sys_regionalism.parent_code is '父节点';
comment ON COLUMN sys_regionalism.spell_short is '拼音';
comment ON COLUMN sys_regionalism.active_flag is '是否启用';
comment ON COLUMN sys_regionalism.remark is '备注';
comment ON COLUMN sys_regionalism.create_user is '创建人';
comment ON COLUMN sys_regionalism.create_datetime is '创建时间';
comment ON COLUMN sys_regionalism.update_user is '更新人';
comment ON COLUMN sys_regionalism.update_datetime is '更新时间';
comment ON COLUMN sys_regionalism.delete_flag is '删除标识';

COMMENT ON TABLE public.sys_role IS '角色表';
comment ON COLUMN sys_role.id is 'id';
comment ON COLUMN sys_role.role_name is '角色名';
comment ON COLUMN sys_role.role_desc is '描述';
comment ON COLUMN sys_role.active_flag is '是否启用';
comment ON COLUMN sys_role.role_ord is '排序字段';
comment ON COLUMN sys_role.create_datetime is '创建时间';
comment ON COLUMN sys_role.create_user is '创建人';
comment ON COLUMN sys_role.update_datetime is '更新时间';
comment ON COLUMN sys_role.update_user is '更新人';
comment ON COLUMN sys_role.ext_id is '外部id（mogoDB中的，现在仍使用）';

COMMENT ON TABLE public.sys_user_role IS '用户角色关系表';

COMMENT ON TABLE public.sys_user IS '用户表';
comment ON COLUMN sys_user.id is 'id';
comment ON COLUMN sys_user.ext_id is 'mogoDB中的id(数据库迁移)';
comment ON COLUMN sys_user.username is '用户名';
comment ON COLUMN sys_user.nickname is '姓名';
comment ON COLUMN sys_user.password is '密码';
comment ON COLUMN sys_user.province is '省份';
comment ON COLUMN sys_user.city is '市';
comment ON COLUMN sys_user.area is '区';
comment ON COLUMN sys_user.server_nos is '管辖地区';
comment ON COLUMN sys_user.id_card_no is '身份证';
comment ON COLUMN sys_user.birth_date is '生日';
comment ON COLUMN sys_user.gender is '性别';
comment ON COLUMN sys_user.phone is '手机号';
comment ON COLUMN sys_user.job is '工作岗位';
comment ON COLUMN sys_user.org_code is '单位编码';
comment ON COLUMN sys_user.lab_code is '废弃';
comment ON COLUMN sys_user.lab_name is '单位名称';
comment ON COLUMN sys_user.is_add_org is '是否为新增实验室';
comment ON COLUMN sys_user.org_add_code is '废弃';
comment ON COLUMN sys_user.org_add_name is '废弃';
comment ON COLUMN sys_user.remark is '备注';
comment ON COLUMN sys_user.status is '是否启用';
comment ON COLUMN sys_user.delete_flag is '删除标识';
comment ON COLUMN sys_user.create_datetime is '创建时间';
comment ON COLUMN sys_user.update_datetime is '更新时间';




-- //@UNDO
-- SQL to undo the change goes here.


