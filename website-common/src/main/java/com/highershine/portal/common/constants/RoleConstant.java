package com.highershine.portal.common.constants;

import java.util.Map;

public final class RoleConstant {
    private RoleConstant() {

    }

    //管理员
    public static final String ROLE_ADMIN = "1";
    // 二所管理员
    public static final String ROLE_SECOND_ADMIN = "2";
    // 省级管理员
    public static final String ROLE_PROVINCE = "3";
    // 普通用户
    public static final String ROLE_AVERAGE = "4";
    // 试剂盒推荐专家
    public static final String ROLE_EXPERT = "5";

    //管理员
    public static final String ROLE_EXT_ADMIN = "admin";
    // 二所管理员
    public static final String ROLE_EXT_SECOND_ADMIN = "secondAdmin";
    // 省级管理员
    public static final String ROLE_EXT_PROVINCE = "province";
    // 普通用户
    public static final String ROLE_EXT_AVERAGE = "averageUser";
    // 试剂盒推荐专家
    public static final String ROLE_EXT_EXPERT = "expert";


    private static Map<String, String> roleMap;

    public static String getRoleExtId(String roleId) {
        if (roleMap == null) {
            roleMap.put(ROLE_ADMIN, ROLE_EXT_ADMIN);
            roleMap.put(ROLE_SECOND_ADMIN, ROLE_EXT_SECOND_ADMIN);
            roleMap.put(ROLE_PROVINCE, ROLE_EXT_PROVINCE);
            roleMap.put(ROLE_AVERAGE, ROLE_EXT_AVERAGE);
            roleMap.put(ROLE_EXPERT, ROLE_EXT_EXPERT);
        }
        return roleMap.get(roleId);
    }
}
