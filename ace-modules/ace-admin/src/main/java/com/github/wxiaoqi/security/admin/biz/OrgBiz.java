package com.github.wxiaoqi.security.admin.biz;


import com.ace.cache.annotation.CacheClear;
import com.github.wxiaoqi.security.admin.entity.Org;
import com.github.wxiaoqi.security.admin.mapper.MenuMapper;
import com.github.wxiaoqi.security.admin.mapper.OrgMapper;
import com.github.wxiaoqi.security.admin.mapper.ResourceAuthorityMapper;
import com.github.wxiaoqi.security.admin.mapper.UserMapper;
import com.github.wxiaoqi.security.admin.vo.OrgUsers;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrgBiz extends BaseBiz<OrgMapper, Org> {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ResourceAuthorityMapper resourceAuthorityMapper;
    @Autowired
    private MenuMapper menuMapper;
    /**
     * 变更组织所分配用户
     *
     * @param orgId
     * @param members
     * @param leaders
     */
    @CacheClear(pre = "permission")
    public void modifyOrgUsers(int orgId, String members, String leaders) {
        mapper.deleteOrgLeadersById(orgId);
        mapper.deleteOrgMembersById(orgId);
        if (!StringUtils.isEmpty(members)) {
            String[] mem = members.split(",");
            for (String m : mem) {
                mapper.insertOrgMembersById(orgId, Integer.parseInt(m));
            }
        }
        if (!StringUtils.isEmpty(leaders)) {
            String[] mem = leaders.split(",");
            for (String m : mem) {
                mapper.insertOrgLeadersById(orgId, Integer.parseInt(m));
            }
        }
    }

    /**
     * 获取组织关联用户
     *
     * @param orgId
     * @return
     */
    public OrgUsers getOrgUsers(Integer orgId) {
        return new OrgUsers(userMapper.selectMemberByOrgId(orgId), userMapper.selectLeaderByOrgId(orgId));
    }
}
