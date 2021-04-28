package cn.coderblue.studyaop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author coderblue
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
public class StudyAopApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyAopApplication.class, args);
    }

}
