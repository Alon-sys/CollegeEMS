package com.example.collegeems.Controller;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @ResponseBody
    @RequestMapping("/Hello")
    public String sayHello(){
       return "你好世界";
    }

}
