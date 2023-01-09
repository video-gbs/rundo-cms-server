package com.runjian.device.dao;

import com.runjian.device.entity.DetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 设备或者通道的详细信息数据库操作类
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Mapper
@Repository
public interface DetailMapper {

    String DETAIL_TABLE_NAME = "rundo_detail";

    Optional<DetailInfo> selectById(Long id);

    void save(DetailInfo detailInfo);

    Optional<DetailInfo> selectByDcIdAndType(Long id, Integer code);

    void update(DetailInfo detailInfo);

    void deleteByDcIdAndType(Long id, Integer code);

    void batchSave(List<DetailInfo> detailSaveList);

    void batchUpdate(List<DetailInfo> detailUpdateList);
}
