package cn.coderblue.studyaop.controller;

import cn.coderblue.studyaop.annotation.AutoInject;
import cn.coderblue.studyaop.annotation.Log;
import cn.coderblue.studyaop.entity.Student;
import cn.coderblue.studyaop.entity.User;
import cn.coderblue.studyaop.enums.BusinessType;
import cn.coderblue.studyaop.utils.ServletUtils;
import cn.coderblue.studyaop.utils.SnowflakeIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author coderblue
 **/
@RestController
@RequestMapping("/")
public class TestController {
    @Resource
    Student student;

    @AutoInject
    User user;

    // @AutoInject
    // SnowflakeIdUtils snowflakeIdUtils;

    @Log(title = "自定义log注解", businessType = BusinessType.INSERT)
    @RequestMapping("/log")
    public String myAopAnnotation() {
        return "success";
    }

}


