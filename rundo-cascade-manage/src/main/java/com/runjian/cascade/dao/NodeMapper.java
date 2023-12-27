package com.runjian.cascade.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/12/22 14:58
 */
@Mapper
@Repository
public interface NodeMapper {

    String NODE_TABLE_NAME = "rundo_node";

}
