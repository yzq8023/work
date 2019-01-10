package com.chat.client.controller;

import com.chat.client.IworkClientStarter;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 忠
 * @program chat
 * @Description
 * @date 2019-01-08 09:04
 */
@RestController
@RequestMapping("testmessage")
public class testController {

    @RequestMapping("senMsg")
    public void senMsg() throws Exception {
        IworkClientStarter.sendMessage("来一个试一试！！");
    }
}
