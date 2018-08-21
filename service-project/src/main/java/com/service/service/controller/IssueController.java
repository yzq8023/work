package com.service.service.controller;


import com.github.wxiaoqi.security.common.rest.BaseController;
import com.service.service.biz.IssueBiz;
import com.service.service.entity.IssueEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("issue")
public class IssueController extends BaseController<IssueBiz,IssueEntity> {

}