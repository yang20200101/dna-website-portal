package com.highershine.portal.common.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 行政区划常量
 *
 * @author mizhanlei
 */
public final class RegionalismConstant {
    private RegionalismConstant() {

    }
    // 行政区划顶节点父编码
    public static final String TOP_REGIONALISM_PARENT_CODE = "000000";

    //北京
    public static final String SERVER_NO_BEIJING = "110000";
    //天津
    public static final String SERVER_NO_TIANJIN = "120000";
    //上海
    public static final String SERVER_NO_SHANGHAI = "310000";
    //重庆
    public static final String SERVER_NO_CHONGQING = "500000";

    //行政规划名称
    public static final String REGIONALISM_TYPE_NAME = "name";
    //行政规划父编码
    public static final String REGIONALISM_TYPE_PARENTCODE = "parentCode";

    // 直辖市
    private static List<String> municipalityServerCode;

    public static List<String> getMunicipalityServerCode() {
        if (municipalityServerCode == null) {
            municipalityServerCode = new ArrayList<>();
            municipalityServerCode.add(SERVER_NO_BEIJING);
            municipalityServerCode.add(SERVER_NO_TIANJIN);
            municipalityServerCode.add(SERVER_NO_SHANGHAI);
            municipalityServerCode.add(SERVER_NO_CHONGQING);
        }
        return municipalityServerCode;
    }
}
