package com.service.service;

/*
 * Copyright 2011 gitblit.com.
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

import com.service.service.utils.FileUtils;
import com.service.service.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 通过跟踪上次修改日期, 动态加载和重新加载属性文件。
 *
 * @author James Moger
 *
 */

public class FileSettings extends IStoredSettings {

    protected File propertiesFile;

    private final Properties properties = new Properties();

    private volatile long lastModified;

    private volatile boolean forceReload;

    public FileSettings() {
        super(FileSettings.class);
    }

    public FileSettings(String file) {
        this();
        load(file);
    }

    public void load(String file) {
        this.propertiesFile = new File(file);
    }

    /**
     * 将提供的设置合并到此实例中。这也将为这个实例设置目标文件,它是unset,而合并源也是一个FileSettings.
     */
    @Override
    public void merge(IStoredSettings settings) {
        super.merge(settings);

        // sneaky: set the target file from the merge source
        if (propertiesFile == null && settings instanceof FileSettings) {
            this.propertiesFile = ((FileSettings) settings).propertiesFile;
        }
    }

    /**
     * Returns a properties object which contains the most recent contents of
     * the properties file.
     */
    @Override
    protected synchronized Properties read() {
        if (propertiesFile != null && propertiesFile.exists() && (forceReload || (propertiesFile.lastModified() > lastModified))) {
            FileInputStream is = null;
            try {
                logger.debug("loading {}", propertiesFile);
                Properties props = new Properties();
                is = new FileInputStream(propertiesFile);
                props.load(is);

                // ticket-110
                props = readIncludes(props);

                // load properties after we have successfully read file
                properties.clear();
                properties.putAll(props);
                lastModified = propertiesFile.lastModified();
                forceReload = false;
            } catch (FileNotFoundException f) {
                // IGNORE - won't happen because file.exists() check above
            } catch (Throwable t) {
                logger.error("Failed to read " + propertiesFile.getName(), t);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Throwable t) {
                        // IGNORE
                    }
                }
            }
        }
        return properties;
    }

    /**
     * Recursively read "include" properties files.
     *
     * @param properties
     * @return
     * @throws IOException
     */
    private Properties readIncludes(Properties properties) throws IOException {

        Properties baseProperties = new Properties();

        String include = (String) properties.remove("include");
        if (!StringUtils.isEmpty(include)) {

            // allow for multiples
            List<String> names = StringUtils.getStringsFromValue(include, ",");
            for (String name : names) {

                if (StringUtils.isEmpty(name)) {
                    continue;
                }

                // try co-located
                File file = new File(propertiesFile.getParentFile(), name.trim());
                if (!file.exists()) {
                    // try absolute path
                    file = new File(name.trim());
                }

                if (!file.exists()) {
                    logger.warn("failed to locate {}", file);
                    continue;
                }

                // load properties
                logger.debug("loading {}", file);
                try (FileInputStream iis = new FileInputStream(file)) {
                    baseProperties.load(iis);
                }

                // read nested includes
                baseProperties = readIncludes(baseProperties);

            }

        }

        // includes are "default" properties, they must be set first and the
        // props which specified the "includes" must override
        Properties merged = new Properties();
        merged.putAll(baseProperties);
        merged.putAll(properties);

        return merged;
    }

    @Override
    public boolean saveSettings() {
        String content = FileUtils.readContent(propertiesFile, "\n");
        for (String key : removals) {
            String regex = "(?m)^(" + regExEscape(key) + "\\s*+=\\s*+)"
                    + "(?:[^\r\n\\\\]++|\\\\(?:\r?\n|\r|.))*+$";
            content = content.replaceAll(regex, "");
        }
        removals.clear();

        FileUtils.writeContent(propertiesFile, content);
        // manually set the forceReload flag because not all JVMs support real
        // millisecond resolution of lastModified. (issue-55)
        forceReload = true;
        return true;
    }

    /**
     * 在设置文件中更新指定的设置。
     */
    @Override
    public synchronized boolean saveSettings(Map<String, String> settings) {
        String content = FileUtils.readContent(propertiesFile, "\n");
        for (Map.Entry<String, String> setting:settings.entrySet()) {
            String regex = "(?m)^(" + regExEscape(setting.getKey()) + "\\s*+=\\s*+)"
                    + "(?:[^\r\n\\\\]++|\\\\(?:\r?\n|\r|.))*+$";
            String oldContent = content;
            content = content.replaceAll(regex, setting.getKey() + " = " + setting.getValue());
            if (content.equals(oldContent)) {
                // did not replace value because it does not exist in the file
                // append new setting to content (issue-85)
                content += "\n" + setting.getKey() + " = " + setting.getValue();
            }
        }
        FileUtils.writeContent(propertiesFile, content);
        // manually set the forceReload flag because not all JVMs support real
        // millisecond resolution of lastModified. (issue-55)
        forceReload = true;
        return true;
    }

    private String regExEscape(String input) {
        return input.replace(".", "\\.").replace("$", "\\$").replace("{", "\\{");
    }

    /**
     * @return the last modification date of the properties file
     */
    protected long lastModified() {
        return lastModified;
    }

    /**
     * @return the state of the force reload flag
     */
    protected boolean forceReload() {
        return forceReload;
    }

    @Override
    public String toString() {
        return propertiesFile.getAbsolutePath();
    }
}

