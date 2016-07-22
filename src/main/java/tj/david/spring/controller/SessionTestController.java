package tj.david.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tj.david.spring.entity.TestEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by David on 2016/7/22.
 */
@Controller
public class SessionTestController {

    /**
     * 测试session是否获取正常
     */
    @RequestMapping("/sessionTest")
    public void sessionTestView() {}

    @RequestMapping("/setSession")
    @ResponseBody
    public String setSession(HttpServletRequest request) {

        //测试一、向session中放入实现序列化的实体类（我这儿是TestEntity）
        TestEntity testEntity= new TestEntity();
        testEntity.setUsername("david_zh");
        testEntity.setTrueName("TrueName");
        request.getSession().setAttribute("testEntity", testEntity);

        //测试二、向session中放入String字符串
        request.getSession().setAttribute("testString", "i am a string value!");

        return "success";
    }

}
