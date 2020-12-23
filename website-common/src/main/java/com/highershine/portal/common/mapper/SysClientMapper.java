package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.po.SysClient;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description
 * @Author xueboren
 * @Date 2020/12/23 14:48
 **/
@Repository
public interface SysClientMapper {
   List<SysClient> getClientList();
}
