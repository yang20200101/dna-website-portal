package com.highershine.portal.common.service;

import com.highershine.portal.common.constants.RegionalismConstant;
import com.highershine.portal.common.entity.po.SysRegionalism;
import com.highershine.portal.common.entity.vo.SysRegionalismVo;
import com.highershine.portal.common.mapper.SysRegionalismMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 行政区划业务层
 * @Author: mizhanlei
 * @Date: 2019/11/30 9:19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRegionalismServiceImpl implements SysRegionalismService {

    @Autowired
    private SysRegionalismMapper sysRegionalismMapper;

    // 缓存行政区划列表
    private List<SysRegionalismVo> cacheSysRegionalismList = new ArrayList<>();
    private Map<String, Map<String, String>> cacheCodeNameMap = new HashMap<>();

    @PostConstruct
    public void init() {
        cacheSysRegionalismList = this.sysRegionalismMapper.selectSysRegionalismVoList();
        cacheCodeNameMap = this.getSysRegionalismCodeNameMap();
    }


    /**
     * 根据父行政区划或者子行政区划列表
     *
     * @param code
     * @return
     */
    @Override
    public List<SysRegionalismVo> selectSysRegionalismVoListByParentCode(String code) {
        List<SysRegionalismVo> sysRegionalismVoList;

        // 判断是否已经缓存
        if (CollectionUtils.isEmpty(cacheSysRegionalismList)) {
            if (RegionalismConstant.getMunicipalityServerCode().contains(code)) {
                sysRegionalismVoList = this.sysRegionalismMapper.selectMunicipalitySysRegionalismVoListByParentCode(
                        code.substring(0, 2));
            } else {
                sysRegionalismVoList = this.sysRegionalismMapper.selectSysRegionalismVoListByParentCode(code);
            }
        } else {
            if (RegionalismConstant.getMunicipalityServerCode().contains(code)) {
                sysRegionalismVoList = cacheSysRegionalismList
                        .parallelStream()
                        .filter(e -> e.getParentCode().startsWith(code.substring(0, 2)))
                        .sorted(Comparator.comparing(SysRegionalismVo::getRegionalismCode))
                        .collect(Collectors.toList());
            } else {
                sysRegionalismVoList = cacheSysRegionalismList
                        .parallelStream()
                        .filter(e -> e.getParentCode().equals(code))
                        .sorted(Comparator.comparing(SysRegionalismVo::getRegionalismCode))
                        .collect(Collectors.toList());
            }
        }
        return sysRegionalismVoList;
    }

    /**
     * 查询行政区划树
     *
     * @return
     */
    @Override
    public List<SysRegionalismVo> selectSysRegionalismVoTree() {

        // 查询顶节点列表
        List<SysRegionalismVo> sysRegionalismVoList = this.selectSysRegionalismVoListByParentCode(
                RegionalismConstant.TOP_REGIONALISM_PARENT_CODE);
        for (SysRegionalismVo sysRegionalismVo : sysRegionalismVoList) {
            // 查询子节点列表
            List<SysRegionalismVo> children = this.selectSysRegionalismVoListByParentCode(sysRegionalismVo.getRegionalismCode());

            // 调用递归算法查询市以下的区县
            diguiSysRegionalism(children);

            sysRegionalismVo.setChildren(children);
        }
        return sysRegionalismVoList;
    }

    /**
     * 获取行政区划编号名称对应关系  130100:{"parent":"130000",name:"廊坊市"}
     * @return
     */
    @Override
    public Map<String, Map<String, String>> getSysRegionalismCodeNameMap() {
        // 判断是否已经缓存
        if (CollectionUtils.isEmpty(cacheCodeNameMap)) {
            List<SysRegionalism> sysRegionalismList = sysRegionalismMapper.selectAllSysRegionalismList();
            sysRegionalismList.stream().forEach(sysRegionalism -> {
                Map<String, String> map = new HashMap<>();
                //直辖市
                if (RegionalismConstant.getMunicipalityServerCode().contains(sysRegionalism.getRegionalismCode().substring(0, 2) + "0000")) {
                    map.put(RegionalismConstant.REGIONALISM_TYPE_PARENTCODE, sysRegionalism.getParentCode().substring(0, 2) + "0000");
                } else {
                    map.put(RegionalismConstant.REGIONALISM_TYPE_PARENTCODE, sysRegionalism.getParentCode());
                }
                map.put(RegionalismConstant.REGIONALISM_TYPE_NAME, sysRegionalism.getRegionalismName());
                cacheCodeNameMap.put(sysRegionalism.getRegionalismCode(), map);
            });
        }
        return cacheCodeNameMap;
    }

    /**
     * 根据code查询行政区划
     *
     * @param code
     * @return
     */
    @Override
    public SysRegionalismVo selectSysRegionalismByCode(String code) {
        List<SysRegionalismVo> collect = this.cacheSysRegionalismList
                .parallelStream()
                .filter(e -> e.getRegionalismCode().equals(code))
                .collect(Collectors.toList());
        return CollectionUtils.isEmpty(collect) ? null : collect.get(0);
    }

    /**
     * 调用递归算法查询市以下的区县
     *
     * @param children
     */
    private void diguiSysRegionalism(List<SysRegionalismVo> children) {
        for (SysRegionalismVo sysRegionalismVo : children) {
            // 查询子节点列表
            List<SysRegionalismVo> children2 = this.selectSysRegionalismVoListByParentCode(
                    sysRegionalismVo.getRegionalismCode());
            if (!CollectionUtils.isEmpty(children2)) {
                // 调用递归算法查询市以下的区县
                diguiSysRegionalism(children2);
                sysRegionalismVo.setChildren(children2);
            }
        }
    }

    /**
     * 刷新缓存
     */
    @Override
    public void reloadCache() {
        cacheSysRegionalismList = this.sysRegionalismMapper.selectSysRegionalismVoList();
        cacheCodeNameMap = new HashMap<>();
    }

    /**
     * 获取行政区划的名称
     * @param code
     * @return
     */
    @Override
    public String getNameByCode(String code) {
        Map<String, Map<String, String>> sysRegionalismCodeNameMap = this.getSysRegionalismCodeNameMap();
        Map<String, String> map = sysRegionalismCodeNameMap.get(code);
        String result = "";
        if (map != null) {
            result = map.get(RegionalismConstant.REGIONALISM_TYPE_NAME);
        }
        return result;
    }
}
