package com.gow.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gow
 * @date 2021/7/24
 */
@Controller
public class SSOController {

    @RequestMapping("/myLogin.html")
    public String myLogin() {
        return "myLogin";
    }

}
