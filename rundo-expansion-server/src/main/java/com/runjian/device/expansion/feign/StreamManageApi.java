package com.runjian.device.expansion.feign;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.StreamManageApiFallbackFactory;
import com.runjian.device.expansion.vo.feign.request.*;
import com.runjian.device.expansion.vo.feign.response.GetDispatchNameRsp;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
import com.runjian.device.expansion.vo.request.RecordStreamOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSeekOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSpeedOperationReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "stream-manage",fallbackFactory= StreamManageApiFallbackFactory.class)
public interface StreamManageApi {

    /**
     * 点播接口
     * @param playFeignReq
     * @return
     */
    @PostMapping(value = "/stream/north/play/live",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<StreamInfo> play(@RequestBody PlayFeignReq playFeignReq);

    /**
     * 回放接口
     * @param playBackReq
     * @return
     */
    @PostMapping(value = "/stream/north/play/record",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<StreamInfo> playBack(@RequestBody PlayBackFeignReq playBackReq);
    /**
     * 开启录像
     * @param req
     * @return
     */
    @PutMapping("/stream/north/record/pause")
    public CommonResponse<Boolean> recordPause(@RequestBody RecordStreamOperationReq req);


    /**
     * 停止录像
     * @param req
     * @return
     */
    @PutMapping("/stream/north/record/resume")
    public CommonResponse<Boolean> recordResume(@RequestBody RecordStreamOperationReq req);


    /**
     * 调整录播的播放速度
     * @param req
     * @return
     */
    @PutMapping("/stream/north/record/speed")
    public CommonResponse<?> recordSpeed(@RequestBody RecordStreamSpeedOperationReq req);

    /**
     * 录像拖拉
     * @param req
     * @return
     */
    @PutMapping("/stream/north/record/seek")
    public CommonResponse<?> recordSeek(@RequestBody RecordStreamSeekOperationReq req);

    /**
     * 流信息获取
     * @param req
     * @return
     */
    @GetMapping("/stream/north/stream/media/info")
    public CommonResponse<JSONObject> getStreamMediaInfo(@SpringQueryMap FeignStreamOperationReq req);

    /**
     * 停止播放
     * @param req
     * @return
     */
    @PutMapping("/stream/north/play/stop")
    CommonResponse<?> stopPlay(@RequestBody PutStreamOperationReq req);

    /**
     * 获取所有调度服务的名称
     * @return
     */
    @GetMapping("/dispatch/north/name")
    public CommonResponse<List<GetDispatchNameRsp>> getDispatchName();

    /**
     * 获取调度服务信息
     * @param page 页码
     * @param num 每页数量
     * @param name 名称
     * @return 分页数据
     */
    @GetMapping("/dispatch/north/page")
    public CommonResponse<Object> getDispatchByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer num,  @RequestParam(required = false) String name);

    /**
     * 修改额外的名字与可访问URL
     * @param req 请求体
     */
    @PutMapping("/dispatch/north/update")
    public CommonResponse<?> updateExtraData(@RequestBody PutDispatchExtraDataReq req);


    /*******************************网关与调度--绑定相关*******************************************************************/

    /**
     * 获取网关绑定的流媒体服务id
     * @param gatewayId 网关id
     * @return
     */
    @GetMapping("/gateway-dispatch/north/gateway/data")
    public CommonResponse<Long> getDispatchIdByGatewayId(@RequestParam Long gatewayId);

    /**
     * 获取流媒体服务绑定的网关
     * @param page
     * @param num
     * @param dispatchId
     * @param name
     * @return
     */
    @GetMapping("/gateway-dispatch/north/dispatch/data/in")
    public CommonResponse<Object> getGatewayByDispatchIdIn(@RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "10") int num,
                                                                                 @RequestParam Long dispatchId, @RequestParam(required = false) String name);

    /**
     * 获取流媒体服务未绑定的网关
     * @param page
     * @param num
     * @param dispatchId
     * @param name
     * @return
     */
    @GetMapping("/gateway-dispatch/north/dispatch/data/not-in")
    public CommonResponse<Object> getGatewayByDispatchIdNotIn(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num,
                                                                                    @RequestParam Long dispatchId,@RequestParam(required = false) String name);


    /**
     * 网关绑定调度服务
     * @param req 网关绑定流媒体服务请求体
     * @return
     */
    @PostMapping("/gateway-dispatch/north/gateway/binding")
    public CommonResponse<?> gatewayBindingDispatch(@RequestBody PostGatewayBindingDispatchReq req);

    /**
     * 调度服务绑定网关
     * @param req 流媒体服务绑定网关请求体
     * @return
     */
    @PostMapping("/gateway-dispatch/north/dispatch/binding")
    public CommonResponse<?> dispatchBindingGateway(@RequestBody PostDispatchBindingGatewayReq req);

    /**
     * 调度服务取消绑定网关
     * @param req 流媒体服务绑定网关请求体
     * @return
     */
    @PostMapping("/gateway-dispatch/north/dispatch/unbinding")
    public CommonResponse<?> dispatchUnBindingGateway(@RequestBody PostDispatchBindingGatewayReq req);

    /**
     * webrtc接口
     * @param webRtcAudioReq
     * @return
     */
    @PostMapping(value = "/stream/north/webrtc/audio",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<String> webrtcAudio(@RequestBody WebRtcAudioReq webRtcAudioReq);

}
