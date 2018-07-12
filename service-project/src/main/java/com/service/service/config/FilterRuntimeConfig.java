package com.service.service.config;

import com.service.service.IStoredSettings;
import com.service.service.managers.IRuntimeManager;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * @auther: dk
 * @date: 2018/7/6
 * @des: 包装筛选器配置, 如果可用, 则更喜欢从 IStoredSettings 检索的设置。
 */
public class FilterRuntimeConfig implements FilterConfig {

    final IRuntimeManager runtime;
    final IStoredSettings settings;
    final String namespace;
    final FilterConfig config;

    public FilterRuntimeConfig(IRuntimeManager runtime, String namespace, FilterConfig config) {
        this.runtime = runtime;
        this.settings = runtime.getSettings();
        this.namespace = namespace;
        this.config = config;
    }

    @Override
    public String getFilterName() {
        return config.getFilterName();
    }

    @Override
    public ServletContext getServletContext() {
        return config.getServletContext();
    }

    @Override
    public String getInitParameter(String s) {
        String key = namespace + "." + s;
        if (settings.hasSettings(key)) {
            String value = settings.getString(key, null);
            return value;
        }
        return config.getInitParameter(s);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return config.getInitParameterNames();
    }
}
