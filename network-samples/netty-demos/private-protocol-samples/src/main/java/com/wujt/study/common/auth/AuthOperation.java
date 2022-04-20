package com.wujt.study.common.auth;


import com.wujt.study.common.Operation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.java.Log;

@EqualsAndHashCode(callSuper = true)
@Data
@Log
public class AuthOperation extends Operation {

    private final String userName;
    private final String password;

    @Override
    public AuthOperationResult execute() {
        if ("admin".equalsIgnoreCase(this.userName)) {
            AuthOperationResult orderResponse = new AuthOperationResult(true);
            return orderResponse;
        }

        return new AuthOperationResult(false);
    }
}
