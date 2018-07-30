/*
 * Copyright 2013 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.service.service.managers;

import com.service.service.*;
import com.service.service.entity.ServerSettings;
import com.service.service.entity.ServerStatus;
import com.service.service.entity.SettingModel;
import com.service.service.utils.StringUtils;
import com.service.service.utils.XssFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

@Component
public class RuntimeManager implements IRuntimeManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IStoredSettings settings;

    private final XssFilter xssFilter;

    private final ServerStatus serverStatus;

    private final ServerSettings settingsModel;

    private File baseFolder;

    private TimeZone timezone;

    @Autowired
    public RuntimeManager(XssFilter xssFilter) {
        this(xssFilter, null);
    }


    public RuntimeManager(XssFilter xssFilter, File baseFolder) {
        String folder = "data";
        Params.baseFolder = folder;
        Params params = new Params();
        this.settings = params.FILESETTINGS;
        this.settingsModel = new ServerSettings();
        this.serverStatus = new ServerStatus();
        this.xssFilter = xssFilter;
        this.baseFolder = baseFolder == null ? new File("") : baseFolder;
        this.start();
    }

    @Override
    public RuntimeManager start() {
        logger.info("Basefolder  : " + baseFolder.getAbsolutePath());
        logger.info("Settings    : " + settings.toString());
        logTimezone("JVM timezone: ", TimeZone.getDefault());
        logTimezone("App timezone: ", getTimezone());
        logger.info("JVM locale  : " + Locale.getDefault());
        logger.info("App locale  : " + (getLocale() == null ? "<client>" : getLocale()));
        return this;
    }

    @Override
    public RuntimeManager stop() {
        return this;
    }

    @Override
    public File getBaseFolder() {
        return baseFolder;
    }

    @Override
    public void setBaseFolder(File folder) {
        this.baseFolder = folder;
    }

    /**
     * 返回Gitblit服务器的启动日期。
     *
     * @return the boot date of Gitblit
     */
    @Override
    public Date getBootDate() {
        return serverStatus.bootDate;
    }

    @Override
    public ServerSettings getSettingsModel() {
        // 确保在设置模型中更新当前值
        for (String key : settings.getAllKeys(null)) {
            SettingModel setting = settingsModel.get(key);
            if (setting == null) {
                // 未引用设置,创建设置模型
                setting = new SettingModel();
                setting.name = key;
                settingsModel.add(setting);
            }
            setting.currentValue = settings.getString(key, "");
        }
//		settingsModel.pushScripts = getAllScripts();
        return settingsModel;
    }

    /**
     * 返回Gitblit实例的首选时区。
     *
     * @return a timezone
     */
    @Override
    public TimeZone getTimezone() {
        if (timezone == null) {
            String tzid = settings.getString(Keys.web.timezone, null);
            if (StringUtils.isEmpty(tzid)) {
                timezone = TimeZone.getDefault();
                return timezone;
            }
            timezone = TimeZone.getTimeZone(tzid);
        }
        return timezone;
    }

    private void logTimezone(String type, TimeZone zone) {
        SimpleDateFormat df = new SimpleDateFormat("z Z");
        df.setTimeZone(zone);
        String offset = df.format(new Date());
        logger.info("{}{} ({})", new Object[]{type, zone.getID(), offset});
    }

    @Override
    public Locale getLocale() {
        String lc = settings.getString(Keys.web.forceDefaultLocale, null);
        if (!StringUtils.isEmpty(lc)) {
            int underscore = lc.indexOf('_');
            if (underscore > 0) {
                String lang = lc.substring(0, underscore);
                String cc = lc.substring(underscore + 1);
                return new Locale(lang, cc);
            } else {
                return new Locale(lc);
            }
        }
        return null;
    }

    /**
     * 在调试模式下运行的是Gitblit吗?
     *
     * @return true if Gitblit is running in debug mode
     */
    @Override
    public boolean isDebugMode() {
        return settings.getBoolean(Keys.web.debugMode, false);
    }

    /**
     * 返回指定配置键的文件对象。
     *
     * @return the file
     */
    @Override
    public File getFileOrFolder(String key, String defaultFileOrFolder) {
        String fileOrFolder = settings.getString(key, defaultFileOrFolder);
        return getFileOrFolder(fileOrFolder);
    }

    /**
     * 返回该文件对象,该对象可能具有在云托管服务上运行的环境变量决定的基础路径。
     * Gitblit文件或文件夹检索(至少最初是最初)通过此方法通通,因此它是全局覆盖/更改文件系统的正确点
     * 基于环境或其他指标的访问。
     *
     * @return the file
     */
    @Override
    public File getFileOrFolder(String fileOrFolder) {
        return com.service.service.utils.FileUtils.resolveParameter(Constants.baseFolder$,
                baseFolder, fileOrFolder);
    }

    /**
     * 返回runtime设置。
     *
     * @return runtime settings
     */
    @Override
    public IStoredSettings getSettings() {
        return settings;
    }

    /**
     * 更新runtime设置。
     *
     * @param updatedSettings
     * @return true if the update succeeded
     */
    @Override
    public boolean updateSettings(Map<String, String> updatedSettings) {
        return settings.saveSettings(updatedSettings);
    }

    @Override
    public ServerStatus getStatus() {
        // update heap memory status
        serverStatus.heapAllocated = Runtime.getRuntime().totalMemory();
        serverStatus.heapFree = Runtime.getRuntime().freeMemory();
        return serverStatus;
    }

    /**
     * 返回XSS过滤器。
     *
     * @return the XSS filter
     */
    @Override
    public XssFilter getXssFilter() {
        return xssFilter;
    }

}
