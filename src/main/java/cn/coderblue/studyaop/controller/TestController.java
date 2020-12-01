package cn.coderblue.studyaop.controller;

import cn.coderblue.studyaop.annotation.Log;
import cn.coderblue.studyaop.enums.BusinessType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author coderblue
 **/
@RestController
@RequestMapping("/")
public class TestController {

    @Log(title = "自定义log注解", businessType = BusinessType.INSERT)
    @RequestMapping("/log")
    public String myAopAnnotation() {
        return "success";
    }

}


