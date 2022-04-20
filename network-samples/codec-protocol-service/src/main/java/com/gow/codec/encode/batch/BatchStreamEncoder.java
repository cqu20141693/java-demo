package com.gow.codec.encode.batch;

import static com.gow.codec.model.EncodeTypeEnum.TYPE_SHORT;
import static com.gow.codec.model.EncodeTypeEnum.UNKNOWN;
import static com.gow.codec.util.CodecUtils.getVariableNumberBytes;
import com.gow.codec.ProtocolEncoder;
import com.gow.codec.base.TypeLongConvert;
import com.gow.codec.base.TypeShortConvert;
import com.gow.codec.base.TypeStringConvert;
import com.gow.codec.exception.EncodeException;
import com.gow.codec.model.batch.BatchStreamModel;
import com.gow.codec.model.EncodeTypeEnum;
import com.gow.codec.model.batch.SingleStreamModel;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gow
 * @date 2021/7/29
 */
@Slf4j
public class BatchStreamEncoder implements ProtocolEncoder<BatchStreamModel> {

    private final static short MAX_DATA_SIZE = 1 << 9;

    public byte[] encode(BatchStreamModel model,Byte version) {

        BatchEncodeContext context = new BatchEncodeContext();

        if (model == null) {
            log.info("model is null");
            return null;
        }
        if (model.getSelf().size() >= MAX_DATA_SIZE) {
            log.info("model data size must be less than {}", MAX_DATA_SIZE);
            return null;
        }
        Short totalControl = getTotalControl(model);

        log.debug("total control={},bytes={}", totalControl,
                Arrays.toString(TYPE_SHORT.getTypeConvert().convertToBytes(totalControl)));
        context.setTotalControl(new TypeShortConvert().convertToBytes(totalControl));
        if (totalControl < 0) {
            context.setTotalBizTime(new TypeLongConvert().convertToBytes(model.getBizTime()));
        }
        // 开始解析数据list
        model.getSelf().forEach(singleModel -> {
            EncodeTypeEnum encodeType = EncodeTypeEnum.parseFromCode(singleModel.getEncodeType());
            if (encodeType == UNKNOWN) {
                throw new EncodeException("model=" + singleModel.getStream() + " encode type is unknown");
            }
            StreamLoop streamLoop = new StreamLoop();
            byte control = getControl(singleModel);
            streamLoop.setControl(control);
            if (control < 0) {
                streamLoop.setBizTime(new TypeLongConvert().convertToBytes(singleModel.getBizTime()));
            }
            byte[] bytes = encodeType.getTypeConvert().convertToBytes(singleModel.getData());
            byte[] lengthBytes = getVariableNumberBytes(bytes.length);
            streamLoop.setStreamBytes(new TypeStringConvert().convertToBytes(singleModel.getStream()));
            streamLoop.setLengthBytes(lengthBytes);
            streamLoop.setData(bytes);
            context.addStreamLoop(streamLoop);
        });

        return context.encode();
    }


    private static byte getControl(SingleStreamModel model) {
        byte control = 0;
        if (model.getBizTime() != null) {
            control |= 0x80;
        }
        control |= model.getEncodeType();

        return control;
    }

    private static Short getTotalControl(BatchStreamModel model) {
        short totalControl = 0;
        if (model.getBizTime() != null) {
            totalControl |= 0x8000;
        }

        totalControl |= model.getSelf().size();

        return totalControl;
    }


}