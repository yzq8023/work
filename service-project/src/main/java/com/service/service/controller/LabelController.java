package com.service.service.controller;


import com.github.hollykunge.security.common.rest.BaseController;
import com.service.service.biz.LabelBiz;
import com.service.service.entity.LabelEntity;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("label")
@Api("标签")
public class LabelController extends BaseController<LabelBiz,LabelEntity> {

}