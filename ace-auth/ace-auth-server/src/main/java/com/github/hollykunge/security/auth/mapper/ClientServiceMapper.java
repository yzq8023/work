package com.github.hollykunge.security.auth.mapper;

import com.github.hollykunge.security.auth.entity.ClientService;
import tk.mybatis.mapper.common.Mapper;

public interface ClientServiceMapper extends Mapper<ClientService> {
    void deleteByServiceId(int id);
}