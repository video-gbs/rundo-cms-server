package com.runjian.stream.service.common.impl;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.stream.dao.StreamMapper;
import com.runjian.stream.entity.StreamInfo;
import com.runjian.stream.feign.ParsingEngineApi;
import com.runjian.stream.service.common.DataBaseService;
import com.runjian.stream.service.common.StreamBaseService;
import com.runjian.stream.service.north.StreamNorthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/2/14 14:33
 */
@Slf4j
@Service
public class StreamBaseServiceImpl implements StreamBaseService {

    @Autowired
    private StreamMapper streamMapper;

    @Autowired
    private StreamNorthService streamNorthService;

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private ParsingEngineApi parsingEngineApi;


    @Override
    @PostConstruct
    public void init() {
        initClearPrepareStream();
    }

    @Override
    @Scheduled(fixedRate = 1000)
    public void checkOutTimeStream() {
        Set<String> idList = STREAM_OUT_TIME_ARRAY.pullAndNext();
        if (Objects.isNull(idList) || idList.isEmpty()){
            return;
        }
        idList.forEach(streamId -> streamNorthService.stopPlay(streamId));
    }

    @Override
    @Scheduled(fixedDelay = 180000)
    public void checkPlayingStream(){
        checkStreamState();
        checkRecordState();
    }

    @Override
    public void checkStreamState(){
        List<StreamInfo> streamInfoList = streamMapper.selectByStreamState(CommonEnum.ENABLE.getCode());
        Map<Long, List<StreamInfo>> dispatchRecordMap = streamInfoList.stream().collect(Collectors.groupingBy(StreamInfo::getDispatchId));
        if (dispatchRecordMap.size() > 0){
            List<Long> noStreamIds = new ArrayList<>();
            for (Map.Entry<Long, List<StreamInfo>> entry : dispatchRecordMap.entrySet()){
                dataBaseService.getDispatchInfo(entry.getKey());
                CommonResponse<List<String>> commonResponse = parsingEngineApi.streamCheckStreamStatus(entry.getKey(), entry.getValue().stream().map(StreamInfo::getStreamId).collect(Collectors.toList()));
                if (commonResponse.isError()){
                    log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "定时检测流播放状态服务", "流媒体交互失败", String.format("流媒体id:%s", entry.getKey()), commonResponse.getMsg());
                    break;
                }
                List<String> playingStreamId = commonResponse.getData();
                if (Objects.nonNull(playingStreamId) && playingStreamId.size() > 0){
                    List<Long> dispatchNoRecordIds = entry.getValue().stream().filter(streamInfo -> playingStreamId.contains(streamInfo.getStreamId())).map(StreamInfo::getId).collect(Collectors.toList());
                    noStreamIds.addAll(dispatchNoRecordIds);
                }else {
                    noStreamIds.addAll(entry.getValue().stream().map(StreamInfo::getId).collect(Collectors.toList()));
                }
            }
            if (noStreamIds.size() > 0){
                streamMapper.deleteByIds(noStreamIds);
            }

        }
    }



    @Override
    public void checkRecordState() {
        List<StreamInfo> streamInfoList = streamMapper.selectByRecordStateAndStreamState(CommonEnum.ENABLE.getCode(), CommonEnum.ENABLE.getCode());
        Map<Long, List<StreamInfo>> dispatchRecordMap = streamInfoList.stream().collect(Collectors.groupingBy(StreamInfo::getDispatchId));
        if (dispatchRecordMap.size() > 0){
            List<Long> noRecordIds = new ArrayList<>();
            for (Map.Entry<Long, List<StreamInfo>> entry : dispatchRecordMap.entrySet()){
                dataBaseService.getDispatchInfo(entry.getKey());
                CommonResponse<List<String>> commonResponse = parsingEngineApi.streamCheckRecordStatus(entry.getKey(), entry.getValue().stream().map(StreamInfo::getStreamId).collect(Collectors.toList()));
                if (commonResponse.isError()){
                    log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "定时检测流播放状态服务", "流媒体交互失败", String.format("流媒体id:%s", entry.getKey()), commonResponse.getMsg());
                    break;
                }
                List<String> recordingStreamIds = commonResponse.getData();
                if (Objects.nonNull(recordingStreamIds) && recordingStreamIds.size() > 0){
                    List<Long> dispatchNoRecordIds = entry.getValue().stream().filter(streamInfo -> recordingStreamIds.contains(streamInfo.getStreamId())).map(StreamInfo::getId).collect(Collectors.toList());
                    noRecordIds.addAll(dispatchNoRecordIds);
                }else {
                    noRecordIds.addAll(entry.getValue().stream().map(StreamInfo::getId).collect(Collectors.toList()));
                }
            }
            if (noRecordIds.size() > 0){
                streamMapper.batchUpdateRecordState(noRecordIds, CommonEnum.DISABLE.getCode(), LocalDateTime.now());
            }
        }
    }

    @Override
    public void initClearPrepareStream() {
        // 清空所有“准备中”的流
        List<Long> idList = streamMapper.selectIdByStreamState(CommonEnum.DISABLE.getCode());
        if (Objects.isNull(idList) || idList.isEmpty()){
            return;
        }
        streamMapper.deleteByIds(idList);
    }
}
