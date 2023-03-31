package com.runjian.stream.vo.request;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.utils.SpringContextUtils;
import com.runjian.common.validator.ValidationResult;
import com.runjian.common.validator.ValidatorFunction;
import com.runjian.stream.dao.StreamMapper;
import com.runjian.stream.entity.StreamInfo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/7 20:46
 */
@Data
public class PutStreamOperationReq implements ValidatorFunction {

    /**
     * 流id
     */
    @NotBlank(message = "流Id不能为空")
    private String streamId;

    /**
     * 通道id
     */
    private Long channelId;


    @Override
    public void validEvent(ValidationResult result, Object data, Object matchData) throws BusinessException {
        if(Objects.isNull(channelId)){
            return;
        }
        StreamMapper streamMapper = SpringContextUtils.getBean(StreamMapper.class);
        Optional<StreamInfo> streamInfoOp = streamMapper.selectByStreamId(streamId);
        if (streamInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "streamId不存在");
        }
        StreamInfo streamInfo = streamInfoOp.get();
        if (!streamInfo.getChannelId().equals(channelId)){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "channelId与streamId不对应");
        }

    }
}
