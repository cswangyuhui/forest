package com.forest.controller;

import com.forest.common.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wechat")
public class WeChatController {
//    @Autowired
//    private weChatRepository weChatRepository;
    private static final Logger logger = LoggerFactory.getLogger(TreeController.class);

    @RequestMapping(value="/liveTree",method=RequestMethod.GET)
    @ResponseBody
    ResponseResult findDetailByNumber(@RequestParam String number){
        logger.debug("*findDetailByNumber*"+number);
        //return new ResponseResult().success("success",weChatRepository.findDetailByNumber(number));
        return new ResponseResult().success("success","success");
    }
}
