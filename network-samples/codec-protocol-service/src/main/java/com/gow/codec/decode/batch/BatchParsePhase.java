package com.gow.codec.decode.batch;

public enum BatchParsePhase {
    ALL_HEADER_PARSE,
    ALL_BIZ_TIME_PARSE,
    PART_HEADER_PARSE,
    PART_BIZ_TIME_PARSE,
    PART_STREAM_LENGTH_PARSE,
    PART_STREAM_PARSE,
    PART_DATA_LENGTH_PARSE,
    PART_DATA_PARSE,
    END
}
