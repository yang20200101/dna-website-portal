package com.highershine.portal.common.constants;

import java.io.File;

/**
 * 通用常量
 *
 * @author mizhanlei
 */
public final class CommonConstant {
    private CommonConstant() {

    }

    // JSESSIONID
    public static final String JSESSIONID = "jsessionid";

    //资源结果
    public static final String MODELREF_RESULT = "Result";

    //系统临时目录
    public static final String TMP_PATH = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
}
