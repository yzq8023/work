package com.service.service.entity;

import com.service.service.Constants.Transport;
import com.service.service.utils.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * User preferences.
 *
 * @author James Moger
 *
 */
public class UserPreferences implements Serializable {

	private static final long serialVersionUID = 1L;

	public  String userId;

	private String locale;

	private Boolean emailMeOnMyTicketChanges;

	private Transport transport;

	private final Map<String, UserRepositoryPreferences> repositoryPreferences = new TreeMap<String, UserRepositoryPreferences>();

	public UserPreferences() {
	}

	public UserPreferences(String id) {
		this.userId = id;
	}

	public Locale getLocale() {
		if (StringUtils.isEmpty(locale)) {
			return null;
		}
		int underscore = locale.indexOf('_');
		if (underscore > 0) {
			String lang = locale.substring(0, underscore);
			String cc = locale.substring(underscore + 1);
			return new Locale(lang, cc);
		}
		return new Locale(locale);
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public UserRepositoryPreferences getRepositoryPreferences(String repositoryName) {
		String key = repositoryName.toLowerCase();
		if (!repositoryPreferences.containsKey(key)) {
			// default preferences
			UserRepositoryPreferences prefs = new UserRepositoryPreferences();
			prefs.userId = userId;
			prefs.repositoryName = repositoryName;
			repositoryPreferences.put(key, prefs);
		}
		return repositoryPreferences.get(key);
	}

	public void setRepositoryPreferences(UserRepositoryPreferences pref) {
		repositoryPreferences.put(pref.repositoryName.toLowerCase(), pref);
	}

	public boolean isStarredRepository(String repository) {
		if (repositoryPreferences == null) {
			return false;
		}
		String key = repository.toLowerCase();
		if (repositoryPreferences.containsKey(key)) {
			UserRepositoryPreferences pref = repositoryPreferences.get(key);
			return pref.starred;
		}
		return false;
	}

	public List<String> getStarredRepositories() {
		List<String> list = new ArrayList<String>();
		for (UserRepositoryPreferences prefs : repositoryPreferences.values()) {
			if (prefs.starred) {
				list.add(prefs.repositoryName);
			}
		}
		Collections.sort(list);
		return list;
	}

	public boolean isEmailMeOnMyTicketChanges() {
		if (emailMeOnMyTicketChanges == null) {
			return true;
		}
		return emailMeOnMyTicketChanges;
	}

	public void setEmailMeOnMyTicketChanges(boolean value) {
		this.emailMeOnMyTicketChanges = value;
	}

	public Transport getTransport() {
		return transport;
	}

	public void setTransport(Transport transport) {
		this.transport = transport;
	}
}
