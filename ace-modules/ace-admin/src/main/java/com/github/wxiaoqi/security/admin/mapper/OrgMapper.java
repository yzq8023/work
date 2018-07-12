package com.github.wxiaoqi.security.admin.mapper;

import com.github.wxiaoqi.security.admin.entity.Org;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface OrgMapper extends Mapper<Org> {
    public void deleteOrgMembersById (@Param("orgId") int orgId);
    public void deleteOrgLeadersById (@Param("orgId") int orgId);
    public void insertOrgMembersById (@Param("orgId") int orgId,@Param("userId") int userId);
    public void insertOrgLeadersById (@Param("orgId") int orgId,@Param("userId") int userId);
}