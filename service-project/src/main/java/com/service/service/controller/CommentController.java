package com.service.service.controller;


import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import com.service.service.biz.CommentBiz;
import com.service.service.entity.CommentEntity;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("comment")
@Api("评论")
public class CommentController extends BaseController<CommentBiz,CommentEntity> {


}