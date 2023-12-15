package com.runjian.device.service.south.impl;


import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.device.constant.*;
import com.runjian.common.constant.SignState;
import com.runjian.device.dao.ChannelMapper;
import com.runjian.device.dao.DetailMapper;
import com.runjian.device.dao.DeviceMapper;
import com.runjian.device.dao.NodeMapper;
import com.runjian.device.entity.ChannelInfo;
import com.runjian.device.entity.DetailInfo;
import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.entity.NodeInfo;
import com.runjian.device.service.common.DetailBaseService;
import com.runjian.device.service.common.MessageBaseService;
import com.runjian.device.service.north.ChannelNorthService;
import com.runjian.device.service.south.DeviceSouthService;
import com.runjian.device.vo.request.PostChannelDetailReq;
import com.runjian.device.vo.request.PostDeviceSignInReq;
import com.runjian.device.vo.request.PostNodeReq;
import com.runjian.device.vo.response.ChannelDetailRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备南向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceSouthServiceImpl implements DeviceSouthService {


    private final DeviceMapper deviceMapper;


    private final ChannelMapper channelMapper;


    private final ChannelNorthService channelNorthService;


    private final MessageBaseService messageBaseService;


    private final DetailBaseService detailBaseService;

    private final NodeMapper nodeMapper;

    private final DetailMapper detailMapper;

    /**
     * 设备添加注册
     * @param id 设备id
     * @param gatewayId 网关id
     * @param onlineState 在线状态
     * @param deviceType 设备类型
     * @param ip ip地址
     * @param port 端口
     */
    @Override
    public void signIn(Long id, Long gatewayId, String originId, Integer onlineState, Integer deviceType, String ip, String port,
                       String name, String manufacturer, String model, String firmware, Integer ptzType, String username, String password) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(id);
        LocalDateTime nowTime = LocalDateTime.now();
        if (deviceInfoOp.isEmpty()){
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setId(id);
            deviceInfo.setGatewayId(gatewayId);
            deviceInfo.setDeviceType(deviceType);
            deviceInfo.setOnlineState(onlineState);
            deviceInfo.setCreateTime(nowTime);
            deviceInfo.setUpdateTime(nowTime);
            deviceInfo.setIsSubscribe(CommonEnum.DISABLE.getCode());
            deviceInfo.setStreamType(StreamType.UDP.getCode());
            deviceInfo.setSignState(SignState.TO_BE_ADD.getCode());
            deviceMapper.save(deviceInfo);
            if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                messageBaseService.msgDistribute(SubMsgType.DEVICE_ONLINE_STATE, Map.of(deviceInfo.getId(), deviceInfo.getOnlineState()));
            }
        }else {
            DeviceInfo deviceInfo = deviceInfoOp.get();
            deviceInfo.setUpdateTime(nowTime);
            boolean isAutoSignIn = false;
            // 判断是否是待注册状态
            if (deviceInfo.getSignState().equals(SignState.TO_BE_SIGN_IN.getCode())){
                // 注册成功
                deviceInfo.setSignState(SignState.SUCCESS.getCode());
                isAutoSignIn = true;
            }

            // 设备从离线到在线，进行通道同步
            if (onlineState.equals(CommonEnum.ENABLE.getCode()) && deviceInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
                // 对通道同步
                deviceInfo.setOnlineState(onlineState);
                deviceMapper.update(deviceInfo);
                if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                    messageBaseService.msgDistribute(SubMsgType.DEVICE_ONLINE_STATE, Map.of(deviceInfo.getId(), deviceInfo.getOnlineState()));
                    Constant.poolExecutor.execute(() -> channelNorthService.channelSync(deviceInfo.getId()));
                }
            } else if (onlineState.equals(CommonEnum.DISABLE.getCode()) && deviceInfo.getOnlineState().equals(CommonEnum.ENABLE.getCode())) {
                // 将通道全部离线
                deviceInfo.setOnlineState(onlineState);
                deviceMapper.update(deviceInfo);
                channelMapper.updateOnlineStateByDeviceId(id, onlineState, nowTime);
                if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                    messageBaseService.msgDistribute(SubMsgType.DEVICE_ONLINE_STATE, Map.of(deviceInfo.getId(), CommonEnum.DISABLE.getCode()));
                    Map<Long, Object> channelInfoMap = channelMapper.selectByDeviceIdAndSignState(deviceInfo.getId(), SignState.SUCCESS.getCode()).stream().collect(Collectors.toMap(ChannelInfo::getId, channelInfo -> CommonEnum.DISABLE.getCode()));
                    messageBaseService.msgDistribute(SubMsgType.CHANNEL_ONLINE_STATE, channelInfoMap);
                }
            } else if (isAutoSignIn && onlineState.equals(CommonEnum.ENABLE.getCode())){
                deviceInfo.setOnlineState(onlineState);
                deviceMapper.update(deviceInfo);
                messageBaseService.msgDistribute(SubMsgType.DEVICE_ONLINE_STATE, Map.of(deviceInfo.getId(), deviceInfo.getOnlineState()));
                Constant.poolExecutor.execute(() -> channelNorthService.channelSync(deviceInfo.getId()));
            }
        }
        detailBaseService.saveOrUpdateDetail(id, originId, DetailType.DEVICE.getCode(), ip, port, name, manufacturer, model, firmware, ptzType, nowTime, username, password);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signInBatch(List<PostDeviceSignInReq> req) {
        if (req.isEmpty()){
            return;
        }
        log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "设备南向服务", "接收到全量同步信息", req);
        Map<Long, PostDeviceSignInReq> postDeviceSignInReqMap = req.stream().collect(Collectors.toMap(PostDeviceSignInReq::getDeviceId, postDeviceSignInReq -> postDeviceSignInReq));
        // 查询已存在的设备信息
        List<DeviceInfo> oldDeviceInfoList = deviceMapper.selectByIds(postDeviceSignInReqMap.keySet());

        // 初始化批量处理容器
        List<DeviceInfo> updateDeviceList = new ArrayList<>(oldDeviceInfoList.size());
        List<DeviceInfo> saveDeviceList = new ArrayList<>(postDeviceSignInReqMap.size() - oldDeviceInfoList.size());
        List<DetailInfo> detailInfoList = new ArrayList<>(postDeviceSignInReqMap.size());
        List<Long> offLineDeviceIdList = new ArrayList<>(oldDeviceInfoList.size());
        List<Long> needChannelSyncDevice = new ArrayList<>(oldDeviceInfoList.size());
        Map<Long, Object> updateDeviceRedisMap = new HashMap<>(postDeviceSignInReqMap.size());

        LocalDateTime nowTime = LocalDateTime.now();
        // 修改
        for (DeviceInfo deviceInfo : oldDeviceInfoList){
            PostDeviceSignInReq postDeviceSignInReq = postDeviceSignInReqMap.remove(deviceInfo.getId());
            deviceInfo.setUpdateTime(nowTime);
            boolean isAutoSignIn = false;
            // 判断是否是待注册状态
            if (deviceInfo.getSignState().equals(SignState.TO_BE_SIGN_IN.getCode())){
                // 注册成功
                deviceInfo.setSignState(SignState.SUCCESS.getCode());
                isAutoSignIn = true;
            }

            // 设备从离线到在线，进行通道同步
            if (postDeviceSignInReq.getOnlineState().equals(CommonEnum.ENABLE.getCode()) && deviceInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
                // 对通道同步
                deviceInfo.setOnlineState(postDeviceSignInReq.getOnlineState());
                updateDeviceList.add(deviceInfo);
                if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                    updateDeviceRedisMap.put(deviceInfo.getId(), deviceInfo.getOnlineState());
                    needChannelSyncDevice.add(deviceInfo.getId());
                }
            } else if (postDeviceSignInReq.getOnlineState().equals(CommonEnum.DISABLE.getCode()) && deviceInfo.getOnlineState().equals(CommonEnum.ENABLE.getCode())) {
                // 将通道全部离线
                deviceInfo.setOnlineState(postDeviceSignInReq.getOnlineState());
                updateDeviceList.add(deviceInfo);
                if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                    updateDeviceRedisMap.put(deviceInfo.getId(), deviceInfo.getOnlineState());
                }
                offLineDeviceIdList.add(deviceInfo.getId());
            } else if (isAutoSignIn && postDeviceSignInReq.getOnlineState().equals(CommonEnum.ENABLE.getCode())){
                deviceInfo.setOnlineState(postDeviceSignInReq.getOnlineState());
                updateDeviceList.add(deviceInfo);
                updateDeviceRedisMap.put(deviceInfo.getId(), deviceInfo.getOnlineState());
                needChannelSyncDevice.add(deviceInfo.getId());
            }
            detailInfoList.add(getNewDetailInfo(postDeviceSignInReq, nowTime));
        }

        // 新增
        for (PostDeviceSignInReq request : postDeviceSignInReqMap.values()){
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setId(request.getDeviceId());
            deviceInfo.setGatewayId(request.getGatewayId());
            deviceInfo.setDeviceType(request.getDeviceType());
            deviceInfo.setOnlineState(request.getOnlineState());
            deviceInfo.setCreateTime(nowTime);
            deviceInfo.setUpdateTime(nowTime);
            deviceInfo.setSignState(SignState.TO_BE_ADD.getCode());
            saveDeviceList.add(deviceInfo);
            if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                updateDeviceRedisMap.put(deviceInfo.getId(), deviceInfo.getOnlineState());
            }
            detailInfoList.add(getNewDetailInfo(request, nowTime));
        }

        if (!updateDeviceList.isEmpty())
            deviceMapper.batchUpdateOnlineState(updateDeviceList);
        if (!saveDeviceList.isEmpty())
            deviceMapper.batchSave(saveDeviceList);
        if (!detailInfoList.isEmpty())
            detailBaseService.batchSaveOrUpdate(detailInfoList, DetailType.DEVICE);
        if (!updateDeviceRedisMap.isEmpty())
            messageBaseService.msgDistribute(SubMsgType.DEVICE_ONLINE_STATE, updateDeviceRedisMap);
        if (!offLineDeviceIdList.isEmpty())
            channelMapper.batchUpdateOnlineStateByDeviceIds(offLineDeviceIdList, CommonEnum.DISABLE.getCode(), nowTime);
        for (Long deviceId : needChannelSyncDevice){
            Constant.poolExecutor.execute(() -> channelNorthService.channelSync(deviceId));
        }

    }

    @Override
    public void nodeSync(Long deviceId, List<PostNodeReq> req) {
        if (req.isEmpty()){
            return;
        }
        List<NodeInfo> nodeInfoList = nodeMapper.selectByDeviceId(deviceId);
        LocalDateTime nowTime = LocalDateTime.now();
        if (nodeInfoList.isEmpty()){
            List<NodeInfo> newNodeInfoList = new ArrayList<>();
            for (PostNodeReq nodeSyncReq : req){
                newNodeInfoList.add(nodeSyncReq.toNodeInfo(deviceId, nowTime));
            }
            nodeMapper.batchSave(newNodeInfoList);
        } else {
            Map<String, NodeInfo> nodeInfoMap = nodeInfoList.stream().collect(Collectors.toMap(nodeInfo1 -> nodeInfo1.getDeviceId() + MarkConstant.MARK_SPLIT_SEMICOLON + nodeInfo1.getOriginId(), nodeInfo1 -> nodeInfo1));
            List<NodeInfo> newNodeInfoList = new ArrayList<>();
            List<NodeInfo> updateNodeInfoList = new ArrayList<>();
            List<NodeInfo> deleteNodeInfoList = new ArrayList<>();
            for (PostNodeReq nodeSyncReq : req){
                NodeInfo oldNodeInfo = nodeInfoMap.remove(deviceId + MarkConstant.MARK_SPLIT_SEMICOLON + nodeSyncReq.getNodeId());
                if (Objects.isNull(oldNodeInfo)){

                    newNodeInfoList.add(nodeSyncReq.toNodeInfo(deviceId, nowTime));
                } else{
                    oldNodeInfo.setNodeName(nodeSyncReq.getNodeName());
                    oldNodeInfo.setParentId(nodeSyncReq.getParentId());
                    oldNodeInfo.setUpdateTime(nowTime);
                    updateNodeInfoList.add(oldNodeInfo);
                }
            }
            if (!nodeInfoMap.isEmpty()){
               deleteNodeInfoList.addAll(nodeInfoMap.values());
            }
            if (!newNodeInfoList.isEmpty()){
                nodeMapper.batchSave(newNodeInfoList);
            }
            if (!updateNodeInfoList.isEmpty()){
                nodeMapper.batchUpdate(updateNodeInfoList);
            }
            if (!deleteNodeInfoList.isEmpty()){
                nodeMapper.batchDelete(deleteNodeInfoList.stream().map(NodeInfo::getId).collect(Collectors.toList()));
            }
        }
    }

    @Override
    public void channelSubscribe(Long deviceId, Integer subscribeType, List<PostChannelDetailReq> channelDetailRspList) {
        if (channelDetailRspList.isEmpty()){
            return;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        switch (SubscribeType.getType(subscribeType)){
            case UNKNOWN:
                return;
            case ONLINE:
                channelMapper.batchUpdateOnlineStateByIds(channelDetailRspList.stream().map(PostChannelDetailReq::getChannelId).collect(Collectors.toList()), CommonEnum.ENABLE.getCode(), nowTime);
                messageBaseService.msgDistribute(SubMsgType.CHANNEL_ONLINE_STATE, channelDetailRspList.stream().collect(Collectors.toMap(PostChannelDetailReq::getChannelId, channelDetailRsp -> CommonEnum.ENABLE.getCode())));
                return;
            case OFFLINE:
                channelMapper.batchUpdateOnlineStateByIds(channelDetailRspList.stream().map(PostChannelDetailReq::getChannelId).collect(Collectors.toList()), CommonEnum.DISABLE.getCode(), nowTime);
                messageBaseService.msgDistribute(SubMsgType.CHANNEL_ONLINE_STATE, channelDetailRspList.stream().collect(Collectors.toMap(PostChannelDetailReq::getChannelId, channelDetailRsp -> CommonEnum.ENABLE.getCode())));
                return;
            case ADD:
                List<ChannelInfo> saveChannelInfoList = new ArrayList<>(channelDetailRspList.size());
                List<DetailInfo> detailSaveList = new ArrayList<>(channelDetailRspList.size());
                List<DetailInfo> detailUpdateList = new ArrayList<>(channelDetailRspList.size());
                for (PostChannelDetailReq channelDetailRsp : channelDetailRspList){
                    Optional<ChannelInfo> channelInfoOp = channelMapper.selectById(channelDetailRsp.getChannelId());
                    if (channelInfoOp.isPresent()){
                        continue;
                    }
                    ChannelInfo channelInfo = new ChannelInfo();
                    channelInfo.setDeviceId(deviceId);
                    channelInfo.setChannelType(channelDetailRsp.getChannelType());
                    channelInfo.setOnlineState(channelDetailRsp.getOnlineState());
                    channelInfo.setSignState(SignState.TO_BE_ADD.getCode());
                    channelInfo.setCreateTime(nowTime);
                    channelInfo.setUpdateTime(nowTime);
                    saveChannelInfoList.add(channelInfo);
                    saveOrUpdateDetail(channelDetailRsp, nowTime, detailSaveList, detailUpdateList);
                }
                channelMapper.batchSave(saveChannelInfoList);
                if (!detailSaveList.isEmpty())
                    detailMapper.batchSave(detailSaveList);
                if (!detailUpdateList.isEmpty())
                    detailMapper.batchUpdate(detailUpdateList);
                messageBaseService.msgDistribute(SubMsgType.CHANNEL_ADD_OR_DELETE_STATE, saveChannelInfoList.stream().collect(Collectors.toMap(ChannelInfo::getId, channelInfo -> channelInfo)));
                break;
            case DELETE:
                for (PostChannelDetailReq channelDetailRsp : channelDetailRspList){
                    channelNorthService.channelDeleteSoft(channelDetailRsp.getChannelId());
                }
                break;
            case UPDATE:
                List<ChannelInfo> updateChannelInfoList = new ArrayList<>(channelDetailRspList.size());
                List<DetailInfo> detailSaveList1 = new ArrayList<>(channelDetailRspList.size());
                List<DetailInfo> detailUpdateList1 = new ArrayList<>(channelDetailRspList.size());
                for (PostChannelDetailReq channelDetailRsp : channelDetailRspList){
                    Optional<ChannelInfo> channelInfoOp = channelMapper.selectById(channelDetailRsp.getChannelId());
                    if (channelInfoOp.isEmpty()){
                        continue;
                    }
                    ChannelInfo channelInfo = channelInfoOp.get();
                    channelInfo.setChannelType(channelDetailRsp.getChannelType());
                    channelInfo.setOnlineState(channelDetailRsp.getOnlineState());
                    channelInfo.setUpdateTime(nowTime);
                    updateChannelInfoList.add(channelInfo);
                    saveOrUpdateDetail(channelDetailRsp, nowTime, detailSaveList1, detailUpdateList1);
                }
                channelMapper.batchUpdate(updateChannelInfoList);
                if (!detailSaveList1.isEmpty())
                    detailMapper.batchSave(detailSaveList1);
                if (!detailUpdateList1.isEmpty())
                    detailMapper.batchUpdate(detailUpdateList1);
                break;
        }
    }

    private void saveOrUpdateDetail(PostChannelDetailReq channelDetailRsp, LocalDateTime nowTime, List<DetailInfo> detailSaveList, List<DetailInfo> detailUpdateList) {
        Optional<DetailInfo> detailInfoOp = detailMapper.selectByDcIdAndType(channelDetailRsp.getChannelId(), DetailType.CHANNEL.getCode());
        DetailInfo detailInfo = detailInfoOp.orElse(new DetailInfo());
        detailInfo.setDcId(channelDetailRsp.getChannelId());
        detailInfo.setOriginId(channelDetailRsp.getOriginId());
        detailInfo.setIp(channelDetailRsp.getIp());
        detailInfo.setPort(channelDetailRsp.getPort());
        detailInfo.setName(channelDetailRsp.getName());
        detailInfo.setManufacturer(channelDetailRsp.getManufacturer());
        detailInfo.setModel(channelDetailRsp.getModel());
        detailInfo.setFirmware(channelDetailRsp.getFirmware());
        detailInfo.setPtzType(channelDetailRsp.getPtzType());
        detailInfo.setUpdateTime(nowTime);
        // 判断数据是否为空，根据情况保存或者更新
        if (detailInfoOp.isEmpty()) {
            detailInfo.setType(DetailType.CHANNEL.getCode());
            detailInfo.setCreateTime(nowTime);
            detailSaveList.add(detailInfo);
        } else {
            detailUpdateList.add(detailInfo);
        }
    }

    @Override
    public void nodeSubscribe(Long deviceId, Integer subscribeType, List<PostNodeReq> nodeSyncReqList) {
        if (nodeSyncReqList.isEmpty()){
            return;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        switch (SubscribeType.getType(subscribeType)){
            case UNKNOWN:
            case OFFLINE:
            case ONLINE:
                return;
            case ADD:
                List<NodeInfo> nodeInfoList = nodeMapper.selectByDeviceIdAndOriginIds(deviceId, nodeSyncReqList.stream().map(PostNodeReq::getNodeId).collect(Collectors.toList()));
                List<NodeInfo> newNodeInfoList = new ArrayList<>(nodeSyncReqList.size());
                if (nodeInfoList.isEmpty()){
                    for (PostNodeReq nodeSyncReq : nodeSyncReqList){
                        newNodeInfoList.add(nodeSyncReq.toNodeInfo(deviceId, nowTime));
                    }
                } else {
                    Map<String, NodeInfo> nodeInfoMap = nodeInfoList.stream().collect(Collectors.toMap(nodeInfo1 -> nodeInfo1.getDeviceId() + MarkConstant.MARK_SPLIT_SEMICOLON + nodeInfo1.getOriginId(), nodeInfo1 -> nodeInfo1));
                    for (PostNodeReq nodeSyncReq : nodeSyncReqList){
                        NodeInfo oldNodeInfo = nodeInfoMap.remove(deviceId + MarkConstant.MARK_SPLIT_SEMICOLON + nodeSyncReq.getNodeId());
                        if (Objects.isNull(oldNodeInfo)){
                            newNodeInfoList.add(nodeSyncReq.toNodeInfo(deviceId, nowTime));
                        }
                    }
                }
                if (!newNodeInfoList.isEmpty()){
                    nodeMapper.batchSave(newNodeInfoList);
                }
                break;
            case UPDATE:
                List<NodeInfo> nodeInfoList1 = nodeMapper.selectByDeviceIdAndOriginIds(deviceId, nodeSyncReqList.stream().map(PostNodeReq::getNodeId).collect(Collectors.toList()));
                List<NodeInfo> updateNodeInfoList = new ArrayList<>(nodeSyncReqList.size());
                if (nodeInfoList1.isEmpty()){
                    return;
                }
                Map<String, NodeInfo> nodeInfoMap = nodeInfoList1.stream().collect(Collectors.toMap(nodeInfo1 -> nodeInfo1.getDeviceId() + MarkConstant.MARK_SPLIT_SEMICOLON + nodeInfo1.getOriginId(), nodeInfo1 -> nodeInfo1));
                for (PostNodeReq nodeSyncReq : nodeSyncReqList){
                    NodeInfo oldNodeInfo = nodeInfoMap.remove(deviceId + MarkConstant.MARK_SPLIT_SEMICOLON + nodeSyncReq.getNodeId());
                    if (Objects.nonNull(oldNodeInfo)){
                        oldNodeInfo.setNodeName(nodeSyncReq.getNodeName());
                        oldNodeInfo.setParentId(nodeSyncReq.getParentId());
                        oldNodeInfo.setUpdateTime(nowTime);
                        updateNodeInfoList.add(oldNodeInfo);
                    }
                }
                if (!updateNodeInfoList.isEmpty()){
                    nodeMapper.batchUpdate(updateNodeInfoList);
                }
                break;
            case DELETE:
                nodeMapper.batchDeleteByDeviceIdAndOriginIds(deviceId, nodeSyncReqList.stream().map(PostNodeReq::getNodeId).collect(Collectors.toList()));
                break;
        }
    }

    private DetailInfo getNewDetailInfo(PostDeviceSignInReq req, LocalDateTime nowTime){
        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setDcId(req.getDeviceId());
        detailInfo.setOriginId(req.getOriginId());
        detailInfo.setType(DetailType.DEVICE.getCode());
        detailInfo.setIp(req.getIp());
        detailInfo.setPort(req.getPort());
        detailInfo.setName(req.getName());
        detailInfo.setManufacturer(req.getManufacturer());
        detailInfo.setModel(req.getModel());
        detailInfo.setFirmware(req.getFirmware());
        detailInfo.setUsername(req.getUsername());
        detailInfo.setPassword(req.getPassword());
        detailInfo.setUpdateTime(nowTime);
        return detailInfo;
    }

}
