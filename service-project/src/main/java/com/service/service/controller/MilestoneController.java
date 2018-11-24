package com.service.service.controller;



import com.github.hollykunge.security.common.rest.BaseController;
import com.service.service.biz.MilestoneBiz;
import com.service.service.entity.MilestoneEntity;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("milestone")
@Api("里程碑")
public class MilestoneController extends BaseController<MilestoneBiz,MilestoneEntity> {

}