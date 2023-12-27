package com.runjian.device.expansion.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.ChannelPresetLists;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
import com.runjian.device.expansion.vo.response.ChannelPresetListsResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestJson {

    @Autowired
    IChannelPresetService channelPresetService;

    @Test
    public void testPreset(){
        String aa = "{\n" +
                "  \"code\": 0,\n" +
                "  \"msg\": \"SUCCESS\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"presetId\": 1,\n" +
                "      \"presetName\": \"preset1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"presetId\": 2,\n" +
                "      \"presetName\": \"预置点2\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"error\": false\n" +
                "}";
        CommonResponse commonResponse = JSON.parseObject(aa, CommonResponse.class);

        List<ChannelPresetListsResp> feignPtzPresetList = JSONObject.parseArray(commonResponse.getData().toString(),ChannelPresetListsResp.class);
//        List<ChannelPresetLists> channelPresetLists = new ArrayList<>();
        List<ChannelPresetListsResp> channelPresetListsResps = new ArrayList<>();
        //插入数据库 并且返回数据
        List<ChannelPresetLists> channelPresetLists = BeanUtil.copyToList(feignPtzPresetList, ChannelPresetLists.class);

        channelPresetService.saveBatch(channelPresetLists);
        //返回数据
        channelPresetListsResps = feignPtzPresetList;
        log.info("sss={}",channelPresetListsResps);

    }

    @Test
    public void testDto(){
        DeviceChannelExpansion deviceExpansion = new DeviceChannelExpansion();
        deviceExpansion.setId(1L);;
        deviceExpansion.setVideoAreaId(1L);;

        System.out.println(deviceExpansion.toString());
    }

    @Test
    public void testStreamDto(){
        String aa = "{\n" +
                "      \"playProtocalType\": 0,\n" +
                "      \"streamId\": null,\n" +
                "      \"rtc\": \"https://xard-gbs-test.runjian.com:2280/index/api/webrtc?app=live&stream=AUDIO_LIVE_185&type=push\",\n" +
                "      \"rtcs\": null\n" +
                "      \n" +
                "}";
        StreamInfo streamInfo = JSONObject.parseObject(aa, StreamInfo.class);


        System.out.println(streamInfo.toString());
    }

    @Test
    public void testFulture(){
        AtomicInteger i = new AtomicInteger();
        // 创建一个异步任务
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            // 异步执行的代码
            while (true){
                if(i.getAndIncrement()>10){

                    return true;
                }
                i.getAndIncrement();
            }
        });

        try {
            // 设置超时时间为10秒，并获取结果
            Boolean result = future.get(10, TimeUnit.SECONDS);
            System.out.println("Result: " + result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // 如果在超时时间内没有完成，或者发生异常，则取消任务
            future.cancel(true);

        }

    }
}
