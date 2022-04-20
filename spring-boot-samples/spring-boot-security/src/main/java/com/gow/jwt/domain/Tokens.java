package com.gow.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gow
 * @date 2021/8/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tokens {

    private String token;
    private String refreshToken;

}