package com.runjian.parsing.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/1/13 14:18
 */
@Mapper
@Repository
public interface TaskMapper {

    String TASK_TABLE_NAME = "rundo_task";


}
