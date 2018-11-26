package com.github.hollykunge.security.auth.biz;

import com.github.hollykunge.security.auth.entity.Client;
import com.github.hollykunge.security.auth.entity.ClientService;
import com.github.hollykunge.security.auth.mapper.ClientMapper;
import com.github.hollykunge.security.auth.mapper.ClientServiceMapper;
import com.github.hollykunge.security.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 
 *
 * @author hollykunge
 * @date 2017-12-26 19:43:46
 */
@Service
public class ClientBiz extends BaseBiz<ClientMapper,Client> {
    @Autowired
    private ClientServiceMapper clientServiceMapper;
    @Autowired
    private ClientServiceBiz clientServiceBiz;

    public List<Client> getClientServices(int id) {
        return mapper.selectAuthorityServiceInfo(id);
    }

    public void modifyClientServices(int id, String clients) {
        clientServiceMapper.deleteByServiceId(id);
        if (!StringUtils.isEmpty(clients)) {
            String[] mem = clients.split(",");
            for (String m : mem) {
                ClientService clientService = new ClientService();
                clientService.setServiceId(m);
                clientService.setClientId(id+"");
                clientServiceBiz.insertSelective(clientService);
            }
        }
    }

    @Override
    protected String getPageName() {
        return null;
    }
}