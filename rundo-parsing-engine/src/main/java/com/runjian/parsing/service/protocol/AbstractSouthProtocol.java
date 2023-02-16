package com.runjian.parsing.service.protocol;



/**
 * 默认的字段名字
 * @author Miracle
 * @date 2023/1/29 20:27
 */
public abstract class AbstractSouthProtocol  implements SouthProtocol {

    @Override
    public void channelPtzControl(Long taskId, Object data) {
        customEvent(taskId, data);
    }

    @Override
    public void channelPlay(Long taskId, Object dataMap) {
        customEvent(taskId, dataMap);
    }

    @Override
    public void channelRecord(Long taskId, Object dataMap) {
        customEvent(taskId, dataMap);
    }

    @Override
    public void channelPlayback(Long taskId, Object dataMap) {
        customEvent(taskId, dataMap);
    }

}
