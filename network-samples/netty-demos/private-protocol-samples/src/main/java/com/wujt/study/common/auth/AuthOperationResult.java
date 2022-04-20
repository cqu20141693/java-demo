package com.wujt.study.common.auth;

import com.wujt.study.common.OperationResult;
import lombok.Data;

@Data
public class AuthOperationResult extends OperationResult {

    private final boolean passAuth;

}
