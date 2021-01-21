package com.highershine.portal.common.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InterfacePropertyConverter {
    @Value("${website.managePath}")
    private String managePath;

    //校验注册的用户是否在填报系统中对应实验室填报过
    public String getValidPersonInLabUrl() {
        return "http://" + managePath + "/su/register/valid";
    }

    //用户注册同步到填报子系统
    public String getUserSyncStatisticsUrl() {
        return "http://" + managePath + "/su/submitLogin";
    }

    //用户删除同步到填报子系统
    public String getUserDelStatisticsUrl() {
        return "http://" + managePath + "/su/delete/";
    }

    //新增实验室
    public String getSaveLabUrl() {
        return "http://" + managePath + "/sysLabInfos/save";
    }
}
