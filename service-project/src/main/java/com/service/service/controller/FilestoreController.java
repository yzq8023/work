package com.service.service.controller;

import com.service.service.Constants;
import com.service.service.IStoredSettings;
import com.service.service.managers.IWorkHub;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: dk
 * @date: 2018/7/6
 * @des: 处理客户端请求
 */
@RestController
@RequestMapping("filestore")
@Api("客户端接口")
public class FilestoreController {

    private static final long serialVersionUID = 1L;

    protected final Logger logger;

    private static IWorkHub gitblit;

    @Autowired
    public FilestoreController(IStoredSettings settings, IWorkHub gitblit) {

        super();
        logger = LoggerFactory.getLogger(getClass());

        FilestoreController.gitblit = gitblit;
    }


}
