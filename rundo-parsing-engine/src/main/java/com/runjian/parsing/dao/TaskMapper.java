package com.runjian.parsing.dao;

import com.runjian.parsing.entity.TaskInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/13 14:18
 */
@Mapper
@Repository
public interface TaskMapper {

    String TASK_TABLE_NAME = "rundo_task";


    void save(TaskInfo taskInfo);

    void updateTaskState(Long taskId, String detail);

    Optional<TaskInfo> selectById(Long id);

    void update(TaskInfo taskInfo);
}
