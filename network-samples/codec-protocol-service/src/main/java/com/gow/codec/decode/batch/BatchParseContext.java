package com.gow.codec.decode.batch;

import com.gow.codec.model.EncodeTypeEnum;
import lombok.Data;

@Data
public class BatchParseContext {
    private boolean useAllBizTime = false;

    private long allBizTime = 0;

    private int dataRoundNumber = 1;

    private boolean roundUseBizTime = false;

    private long roundBizTime = 0;

    private int roundStreamNameLength = 0;

    private String roundStreamName;

    private EncodeTypeEnum roundEncodeType = EncodeTypeEnum.UNKNOWN;

    private int roundDataLength = 0;

    private Object roundData;

    private int roundExecuted = 1;

    /**
     * 开
     */
    private int startIndex = 0;

    /**
     * 闭
     */
    private int endIndex = 0;
}
