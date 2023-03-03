package com.runjian.device.feign.fallback;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.feign.ParsingEngineApi;
import com.runjian.device.vo.feign.DeviceControlReq;
import com.runjian.device.vo.response.ChannelSyncRsp;
import com.runjian.device.vo.response.DeviceSyncRsp;
import com.runjian.device.vo.response.VideoPlayRsp;
import com.runjian.device.vo.response.VideoRecordRsp;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/3/3 11:32
 */
public class ParsingEngineFallback implements FallbackFactory<ParsingEngineApi> {


    @Override
    public ParsingEngineApi create(Throwable cause) {
        return new ParsingEngineApi() {
            @Override
            public void deviceTotalSync(Set<Long> gatewayIds) {

            }

            @Override
            public CommonResponse<DeviceSyncRsp> deviceSync(Long deviceId) {
                return null;
            }

            @Override
            public CommonResponse<Long> deviceAdd(DeviceControlReq req) {
                return null;
            }

            @Override
            public CommonResponse<Boolean> deviceDelete(Long deviceId) {
                return null;
            }

            @Override
            public CommonResponse<ChannelSyncRsp> channelSync(Long deviceId) {
                return null;
            }

            @Override
            public CommonResponse<?> channelPtzControl(DeviceControlReq req) {
                return null;
            }

            @Override
            public CommonResponse<VideoPlayRsp> channelPlay(DeviceControlReq req) {
                return null;
            }

            @Override
            public CommonResponse<VideoRecordRsp> channelRecord(DeviceControlReq req) {
                return null;
            }

            @Override
            public CommonResponse<VideoPlayRsp> channelPlayback(DeviceControlReq req) {
                return null;
            }
        };
    }
}
