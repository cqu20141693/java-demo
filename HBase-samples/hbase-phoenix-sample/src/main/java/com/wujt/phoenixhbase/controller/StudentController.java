package com.wujt.phoenixhbase.controller;


import com.wujt.phoenixhbase.entity.Student;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luolh
 * @version 1.0
 * @since 2020/9/2 9:25
 */
@RestController
@RequestMapping("hbase/student")
public class StudentController {

    @PostMapping("addOne")
    public boolean addOne(@RequestBody Student student){
        return false;
    }
}
