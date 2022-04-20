package com.wujt.study.common.keepalive;

import com.wujt.study.common.OperationResult;
import lombok.Data;

@Data
public class KeepaliveOperationResult extends OperationResult {

    private final long time;

}
