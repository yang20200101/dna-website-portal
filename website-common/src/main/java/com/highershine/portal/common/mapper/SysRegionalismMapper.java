package com.highershine.portal.common.mapper;

import com.highershine.portal.common.entity.po.SysRegionalism;
import com.highershine.portal.common.entity.vo.SysRegionalismVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 行政区划mapper
 * @Author: xueboren
 * @Date: 2019/11/27 19:30
 */
@Repository
public interface SysRegionalismMapper {


    /**
     * 根据父行政区划或者子行政区划列表
     *
     * @param code
     * @return
     */
    List<SysRegionalismVo> selectSysRegionalismVoListByParentCode(String code);

    /**
     * 根据父行政区划或者子行政区划列表(直辖市)
     *
     * @param code
     * @return
     */
    List<SysRegionalismVo> selectMunicipalitySysRegionalismVoListByParentCode(String code);

    /**
     * 查询所有行政区划列表
     *
     * @return
     */
    List<SysRegionalismVo> selectSysRegionalismVoList();

    /**
     * 查询所有行政区划PO列表
     * @return
     */
    List<SysRegionalism> selectAllSysRegionalismList();
}
