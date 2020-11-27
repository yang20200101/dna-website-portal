package com.highershine.portal.common.service;

import com.highershine.portal.common.entity.vo.SysRegionalismVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 行政区划业务层
 * @Author: mizhanlei
 * @Date: 2019/11/30 9:19
 */
public interface SysRegionalismService {

    /**
     * 根据父行政区划或者子行政区划列表
     *
     * @param code
     * @return
     */
    List<SysRegionalismVo> selectSysRegionalismVoListByParentCode(String code);

    /**
     * 查询行政区划树
     *
     * @return
     */
    List<SysRegionalismVo> selectSysRegionalismVoTree();

    /**
     * 获取行政区划编码名称对应关系
     *
     * @return
     */
    Map<String, Map<String, String>> getSysRegionalismCodeNameMap();

    /**
     * 根据code查询行政区划
     *
     * @param code
     * @return
     */
    SysRegionalismVo selectSysRegionalismByCode(String code);

    /**
     * 刷新缓存
     */
    void reloadCache();

    /**
     * 根据code获取名称
     */
     String getNameByCode(String code);
}
