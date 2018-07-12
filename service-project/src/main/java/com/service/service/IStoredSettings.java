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
package com.service.service;

import com.service.service.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 存储设置的基类实现
 *
 * @author James Moger
 *
 */
public abstract class IStoredSettings {

	protected final Logger logger;

	protected final Properties overrides = new Properties();

	protected final Set<String> removals = new TreeSet<String>();

	public IStoredSettings(Class<? extends IStoredSettings> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}

	protected abstract Properties read();

	private Properties getSettings() {
		Properties props = read();
		props.putAll(overrides);
		return props;
	}

	/**
	 * 返回指定前缀的键的名称列表
	 * 如果前缀是 null or empty返回所有键值
	 *
	 * @param startingWith
	 * @return list of keys
	 */
	public List<String> getAllKeys(String startingWith) {
		List<String> keys = new ArrayList<String>();
		Properties props = getSettings();
		if (StringUtils.isEmpty(startingWith)) {
			keys.addAll(props.stringPropertyNames());
		} else {
			startingWith = startingWith.toLowerCase();
			for (Object o : props.keySet()) {
				String key = o.toString();
				if (key.toLowerCase().startsWith(startingWith)) {
					keys.add(key);
				}
			}
		}
		return keys;
	}

	/**
	 * 返回特殊键值的布尔类型
	 * 如果键值不存在或者无法翻译成布尔，返回defaultValue
	 *
	 * @param name
	 * @param defaultValue
	 * @return key value or defaultValue
	 */
	public boolean getBoolean(String name, boolean defaultValue) {
		Properties props = getSettings();
		if (props.containsKey(name)) {
			String value = props.getProperty(name);
			if (!StringUtils.isEmpty(value)) {
				return Boolean.parseBoolean(value.trim());
			}
		}
		return defaultValue;
	}

	/**
	 * 返回特殊键值的整数类型
	 * 如果键值不存在或者无法翻译成整数，返回defaultValue
	 *
	 * @param name
	 * @param defaultValue
	 * @return key value or defaultValue
	 */
	public int getInteger(String name, int defaultValue) {
		Properties props = getSettings();
		if (props.containsKey(name)) {
			try {
				String value = props.getProperty(name);
				if (!StringUtils.isEmpty(value)) {
					return Integer.parseInt(value.trim());
				}
			} catch (NumberFormatException e) {
				logger.warn("Failed to parse integer for " + name + " using default of "
						+ defaultValue);
			}
		}
		return defaultValue;
	}

	/**
	 * 返回特殊键值的long类型
	 * 如果键值不存在或者无法翻译成long，返回defaultValue
	 *
	 * @param name
	 * @param defaultValue
	 * @return key value or defaultValue
	 */
	public long getLong(String name, long defaultValue) {
		Properties props = getSettings();
		if (props.containsKey(name)) {
			try {
				String value = props.getProperty(name);
				if (!StringUtils.isEmpty(value)) {
					return Long.parseLong(value.trim());
				}
			} catch (NumberFormatException e) {
				logger.warn("Failed to parse long for " + name + " using default of "
						+ defaultValue);
			}
		}
		return defaultValue;
	}

	/**
	 * 以string类型返回文件长度 50m or 50mb
	 * @param name
	 * @param defaultValue
	 * @return an int filesize or defaultValue if the key does not exist or can
	 *         not be parsed
	 */
	public int getFilesize(String name, int defaultValue) {
		String val = getString(name, null);
		if (StringUtils.isEmpty(val)) {
			return defaultValue;
		}
		return com.service.service.utils.FileUtils.convertSizeToInt(val, defaultValue);
	}

	/**
	 * 以string类型返回文件长度 50m or 50mb
	 * @param key
	 * @param defaultValue
	 * @return a long filesize or defaultValue if the key does not exist or can
	 *         not be parsed
	 */
	public long getFilesize(String key, long defaultValue) {
		String val = getString(key, null);
		if (StringUtils.isEmpty(val)) {
			return defaultValue;
		}
		return com.service.service.utils.FileUtils.convertSizeToLong(val, defaultValue);
	}

	/**
	 * 返回特殊键值的char类型
	 * 如果键值不存在或者无法翻译成char，返回defaultValue
	 *
	 * @param name
	 * @param defaultValue
	 * @return key value or defaultValue
	 */
	public char getChar(String name, char defaultValue) {
		Properties props = getSettings();
		if (props.containsKey(name)) {
			String value = props.getProperty(name);
			if (!StringUtils.isEmpty(value)) {
				return value.trim().charAt(0);
			}
		}
		return defaultValue;
	}

	/**
	 * 返回特殊键值的string类型
	 * 如果键值不存在或者无法翻译成string，返回defaultValue
	 *
	 * @param name
	 * @param defaultValue
	 * @return key value or defaultValue
	 */
	public String getString(String name, String defaultValue) {
		Properties props = getSettings();
		if (props.containsKey(name)) {
			String value = props.getProperty(name);
			if (value != null) {
				return value.trim();
			}
		}
		return defaultValue;
	}

	/**
	 * 返回特殊键值的string类型
	 * 如果不存在抛出异常
	 *
	 * @param name
	 * @return key value
	 */
	public String getRequiredString(String name) {
		Properties props = getSettings();
		if (props.containsKey(name)) {
			String value = props.getProperty(name);
			if (value != null) {
				return value.trim();
			}
		}
		throw new RuntimeException("Property (" + name + ") does not exist");
	}

	/**
	 * Returns a list of space-separated strings from the specified key.
	 *
	 * @param name
	 * @return list of strings
	 */
	public List<String> getStrings(String name) {
		return getStrings(name, " ");
	}

	/**
	 * Returns a list of strings from the specified key using the specified
	 * string separator.
	 *
	 * @param name
	 * @param separator
	 * @return list of strings
	 */
	public List<String> getStrings(String name, String separator) {
		List<String> strings = new ArrayList<String>();
		Properties props = getSettings();
		if (props.containsKey(name)) {
			String value = props.getProperty(name);
			strings = StringUtils.getStringsFromValue(value, separator);
		}
		return strings;
	}

	/**
	 * Returns a list of space-separated integers from the specified key.
	 *
	 * @param name
	 * @return list of strings
	 */
	public List<Integer> getIntegers(String name) {
		return getIntegers(name, " ");
	}

	/**
	 * Returns a list of integers from the specified key using the specified
	 * string separator.
	 *
	 * @param name
	 * @param separator
	 * @return list of integers
	 */
	public List<Integer> getIntegers(String name, String separator) {
		List<Integer> ints = new ArrayList<Integer>();
		Properties props = getSettings();
		if (props.containsKey(name)) {
			String value = props.getProperty(name);
			List<String> strings = StringUtils.getStringsFromValue(value, separator);
			for (String str : strings) {
				try {
					int i = Integer.parseInt(str);
					ints.add(i);
				} catch (NumberFormatException e) {
				}
			}
		}
		return ints;
	}

	/**
	 * Returns a map of strings from the specified key.
	 *
	 * @param name
	 * @return map of string, string
	 */
	public Map<String, String> getMap(String name) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (String string : getStrings(name)) {
			String[] kvp = string.split("=", 2);
			String key = kvp[0];
			String value = kvp[1];
			map.put(key,  value);
		}
		return map;
	}

	/**
	 * Override the specified key with the specified value.
	 *
	 * @param key
	 * @param value
	 */
	public void overrideSetting(String key, String value) {
		overrides.put(key, value);
	}

	/**
	 * Override the specified key with the specified value.
	 *
	 * @param key
	 * @param value
	 */
	public void overrideSetting(String key, int value) {
		overrides.put(key, "" + value);
	}

	/**
	 * Override the specified key with the specified value.
	 *
	 * @param key
	 * @param value
	 */
	public void overrideSetting(String key, boolean value) {
		overrides.put(key, "" + value);
	}

	/**
	 * Tests for the existence of a setting.
	 *
	 * @param key
	 * @return true if the setting exists
	 */
	public boolean hasSettings(String key) {
		return getString(key, null) != null;
	}

	/**
	 * Remove a setting.
	 *
	 * @param key
	 */
	public void removeSetting(String key) {
		getSettings().remove(key);
		overrides.remove(key);
		removals.add(key);
	}

	/**
	 * Saves the current settings.
	 */
	public abstract boolean saveSettings();

	/**
	 * Updates the values for the specified keys and persists the entire
	 * configuration file.
	 *
	 * @param updatedSettings
	 *            of key, value pairs
	 * @return true if successful
	 */
	public abstract boolean saveSettings(Map<String, String> updatedSettings);

	/**
	 * 将设置参数中所有的实例合并到此设置中
	 *
	 * @param settings
	 */
	public void merge(IStoredSettings settings) {
		getSettings().putAll(settings.getSettings());
		overrides.putAll(settings.overrides);
	}
}