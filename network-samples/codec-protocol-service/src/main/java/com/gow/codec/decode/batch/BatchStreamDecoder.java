package com.gow.codec.decode.batch;

import com.gow.codec.ProtocolDecoder;
import com.gow.codec.base.ConvertResponse;
import com.gow.codec.base.TypeLongConvert;
import com.gow.codec.exception.DecodeException;
import com.gow.codec.model.EncodeTypeEnum;
import com.gow.codec.model.batch.BatchStreamModel;
import com.gow.codec.model.batch.SingleStreamModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gow
 * @date 2021/7/29
 */
public class BatchStreamDecoder implements ProtocolDecoder<BatchStreamModel> {

    public  BatchStreamModel decode(byte[] batchBytes) {

        List<SingleStreamModel> parsedData = new ArrayList<>();
        BatchStreamModel batchStreamModel = new BatchStreamModel();
        batchStreamModel.setSelf(parsedData);

        BatchParseContext parseContext = new BatchParseContext();
        BatchParsePhase phase = BatchParsePhase.ALL_HEADER_PARSE;
        while (phase != BatchParsePhase.END) {
            if (parseContext.getRoundExecuted() > parseContext.getDataRoundNumber()) {
                throw new DecodeException("batch data parse error.");
            }
            switch (phase) {
                case ALL_HEADER_PARSE: {
                    parseContext.setEndIndex(2);

                    allHeaderParse(batchBytes, parseContext);

                    if (parseContext.isUseAllBizTime()) {
                        phase = BatchParsePhase.ALL_BIZ_TIME_PARSE;
                    } else {
                        phase = BatchParsePhase.PART_HEADER_PARSE;
                    }
                    break;
                }
                case ALL_BIZ_TIME_PARSE: {
                    parseContext.setEndIndex(parseContext.getStartIndex() + 8);
                    parseContext.setAllBizTime(parseBizTime(batchBytes, parseContext));
                    phase = BatchParsePhase.PART_HEADER_PARSE;
                    break;
                }
                case PART_HEADER_PARSE: {
                    parseContext.setEndIndex(parseContext.getStartIndex() + 1);

                    partHeaderParse(batchBytes, parseContext);

                    if (parseContext.isRoundUseBizTime()) {
                        phase = BatchParsePhase.PART_BIZ_TIME_PARSE;
                    } else {
                        phase = BatchParsePhase.PART_STREAM_LENGTH_PARSE;
                    }
                    break;
                }
                case PART_BIZ_TIME_PARSE: {
                    parseContext.setEndIndex(parseContext.getStartIndex() + 8);
                    parseContext.setRoundBizTime(parseBizTime(batchBytes, parseContext));

                    phase = BatchParsePhase.PART_STREAM_LENGTH_PARSE;
                    break;
                }
                case PART_STREAM_LENGTH_PARSE: {
                    parseContext.setEndIndex(parseContext.getStartIndex() + 1);

                    partBizStreamLengthParse(batchBytes, parseContext);

                    phase = BatchParsePhase.PART_STREAM_PARSE;
                    break;
                }
                case PART_STREAM_PARSE: {
                    parseContext.setEndIndex(parseContext.getStartIndex() + parseContext.getRoundStreamNameLength());

                    partBizStreamParse(batchBytes, parseContext);

                    phase = BatchParsePhase.PART_DATA_LENGTH_PARSE;
                    break;
                }
                case PART_DATA_LENGTH_PARSE: {

                    partDataLengthParse(batchBytes, parseContext);

                    phase = BatchParsePhase.PART_DATA_PARSE;
                    break;
                }
                case PART_DATA_PARSE: {
                    parseContext.setEndIndex(parseContext.getStartIndex() + parseContext.getRoundDataLength());

                    partDataParse(batchBytes, parseContext);
                    parsedData.add(parseFrom(parseContext));

                    parseContext.setRoundExecuted(parseContext.getRoundExecuted() + 1);
                    if (parseContext.getRoundExecuted() > parseContext.getDataRoundNumber()) {
                        phase = BatchParsePhase.END;
                    } else {
                        phase = BatchParsePhase.PART_HEADER_PARSE;
                        refreshRoundContext(parseContext);
                    }
                    break;
                }
            }
            parseContext.setStartIndex(parseContext.getEndIndex());
        }
        if (parseContext.isUseAllBizTime()) {
            batchStreamModel.setBizTime(parseContext.getAllBizTime());
        }
        return batchStreamModel;
    }

    private static long parseBizTime(byte[] batchBytes, BatchParseContext parseContext) {
        if (parseContext.getEndIndex() > batchBytes.length) {
            throw new DecodeException("batch data parse error.");
        }
        byte[] allBizTimeBytes = new byte[8];
        System.arraycopy(batchBytes, parseContext.getStartIndex(), allBizTimeBytes, 0, 8);

        return TypeLongConvert.longFromBytes(allBizTimeBytes, 0, false);
    }

    private static void allHeaderParse(byte[] batchBytes, BatchParseContext parseContext) {
        if (parseContext.getEndIndex() > batchBytes.length) {
            throw new DecodeException("batch data parse error.");
        }
        parseContext.setUseAllBizTime((batchBytes[0] & 0x80) != 0);
        parseContext.setDataRoundNumber(((batchBytes[0] & 0x01) << 8) + batchBytes[1]);
    }

    private static void partHeaderParse(byte[] batchBytes, BatchParseContext parseContext) {
        if (parseContext.getEndIndex() > batchBytes.length) {
            throw new DecodeException("batch data parse error.");
        }

        int operateIndex = parseContext.getStartIndex();
        parseContext.setRoundUseBizTime((batchBytes[operateIndex] & 0x80) != 0);
        EncodeTypeEnum encodeTypeEnum = EncodeTypeEnum.parseFromCode((byte) (batchBytes[operateIndex] & 0x0F));
        if (encodeTypeEnum == EncodeTypeEnum.UNKNOWN) {
            throw new DecodeException("batch data parse error.");
        }
        parseContext.setRoundEncodeType(encodeTypeEnum);
    }

    private static void partBizTimeParse(byte[] batchBytes, BatchParseContext parseContext) {
        if (parseContext.getEndIndex() > batchBytes.length) {
            throw new DecodeException("batch data parse error.");
        }
        long partBizTime = 0;
        int operateIndex = parseContext.getStartIndex();
        while (operateIndex < parseContext.getEndIndex()) {
            partBizTime = (partBizTime << 8) + batchBytes[operateIndex];
            operateIndex++;
        }
        parseContext.setRoundBizTime(partBizTime);
    }

    private static void partBizStreamLengthParse(byte[] batchBytes, BatchParseContext parseContext) {
        if (parseContext.getEndIndex() > batchBytes.length) {
            throw new DecodeException("batch data parse error.");
        }
        int operateIndex = parseContext.getStartIndex();
        parseContext.setRoundStreamNameLength(batchBytes[operateIndex]);
    }

    private static void partBizStreamParse(byte[] batchBytes, BatchParseContext parseContext) {
        if (parseContext.getEndIndex() > batchBytes.length) {
            throw new DecodeException("batch data parse error.");
        }
        //SYSTEM CHARSET
        parseContext.setRoundStreamName(
                new String(batchBytes, parseContext.getStartIndex(), parseContext.getRoundStreamNameLength()));
    }

    private static void partDataLengthParse(byte[] batchBytes, BatchParseContext parseContext) {
        int operateIndex = parseContext.getStartIndex();
        int multiplier = 1;
        int value = 0;
        byte encodeByte;
        int max = 1 << 21;
        do {
            if (operateIndex >= batchBytes.length) {
                throw new DecodeException("batch data parse error.");
            }
            encodeByte = batchBytes[operateIndex];
            value += (encodeByte & 0x7f) * multiplier;

            if (multiplier > max) {
                // 128 * 128 * 128 = 2097152 ， 4 round
                throw new DecodeException("batch data parse error.");
            }
            multiplier = multiplier << 7;
            operateIndex++;
        } while ((encodeByte & 0x80) != 0);

        //动态编码长度， 设置结束index
        parseContext.setRoundDataLength(value);
        parseContext.setEndIndex(operateIndex);
    }

    private static void partDataParse(byte[] batchBytes, BatchParseContext parseContext) {
        byte[] data = new byte[parseContext.getRoundDataLength()];
        System.arraycopy(batchBytes, parseContext.getStartIndex(), data, 0, parseContext.getRoundDataLength());

        EncodeTypeEnum encodeTypeEnum = parseContext.getRoundEncodeType();
        ConvertResponse<?> response = encodeTypeEnum.getTypeConvert().rawDataConvert(data);
        if (!response.isSuccess()) {
            throw new DecodeException("batch data parse error.");
        }
        // 保证批量上报数据为String,BatchHandler 进行验证转换
        String convertStr = encodeTypeEnum.getTypeConvert().objectDataConvertStr(response.getConvertResult());
        parseContext.setRoundData(convertStr);
    }

    private static SingleStreamModel parseFrom(BatchParseContext parseContext) {
        SingleStreamModel singleStreamModel = new SingleStreamModel();
        singleStreamModel.setStream(parseContext.getRoundStreamName());
        singleStreamModel.setEncodeType((byte) parseContext.getRoundEncodeType().getTypeCode());
        singleStreamModel.setData(parseContext.getRoundData());

        if (parseContext.isRoundUseBizTime()) {
            singleStreamModel.setBizTime(parseContext.getRoundBizTime());
        }
        return singleStreamModel;
    }

    private static void refreshRoundContext(BatchParseContext parseContext) {
        parseContext.setRoundUseBizTime(false);
        parseContext.setRoundBizTime(0);
        parseContext.setRoundStreamNameLength(0);
        parseContext.setRoundStreamName(null);
        parseContext.setRoundDataLength(0);
        parseContext.setRoundEncodeType(EncodeTypeEnum.UNKNOWN);
        parseContext.setRoundData(null);
    }
}
