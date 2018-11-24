package com.github.hollykunge.security.admin.rest;

import com.github.hollykunge.security.admin.biz.OrgBiz;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.vo.OrgTree;
import com.github.hollykunge.security.admin.vo.OrgUsers;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.TreeUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dk
 */
@Controller
@RequestMapping("org")
@Api("组织管理")
public class OrgController extends BaseController<OrgBiz, Org> {
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Org> list(String name, String orgtype) {
        if(StringUtils.isBlank(name)&&StringUtils.isBlank(orgtype)) {
            return new ArrayList<Org>();
        }
        Example example = new Example(Org.class);
        if (StringUtils.isNotBlank(name)) {
            example.createCriteria().andLike("name", "%" + name + "%");
        }
        if (StringUtils.isNotBlank(orgtype)) {
            example.createCriteria().andEqualTo("orgtype", orgtype);
        }

        return baseBiz.selectByExample(example);
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifiyUsers(@PathVariable int id, String members, String leaders){
        baseBiz.modifyOrgUsers(id, members, leaders);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<OrgUsers> getUsers(@PathVariable int id){
        return new ObjectRestResponse<OrgUsers>().rel(true).data(baseBiz.getOrgUsers(id));
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public List<OrgTree> tree(String name, String orgType) {
        if(StringUtils.isBlank(name)&&StringUtils.isBlank(orgType)) {
            return new ArrayList<OrgTree>();
        }
        Example example = new Example(Org.class);
        if (StringUtils.isNotBlank(name)) {
            example.createCriteria().andLike("name", "%" + name + "%");
        }
        if (StringUtils.isNotBlank(orgType)) {
            example.createCriteria().andEqualTo("orgType", orgType);
        }
        return  getTree(baseBiz.selectByExample(example), AdminCommonConstant.ROOT);
    }
    private List<OrgTree> getTree(List<Org> orgs,int root) {
        List<OrgTree> trees = new ArrayList<OrgTree>();
        OrgTree node = null;
        for (Org org : orgs) {
            node = new OrgTree();
            node.setLabel(org.getOrgname());
            BeanUtils.copyProperties(org, node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees,root) ;
    }
}
