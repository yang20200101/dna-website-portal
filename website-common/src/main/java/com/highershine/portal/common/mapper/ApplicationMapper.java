package com.highershine.portal.common.mapper;


import com.highershine.portal.common.entity.po.Application;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author xueboren
 * @Date 2020/11/26 15:57
 **/
@Repository
public interface ApplicationMapper {
    int deleteTrueByActivityId(Long id);

    int insert(Application record);

    int updateIsLatestByActivityId(Application record);
}
