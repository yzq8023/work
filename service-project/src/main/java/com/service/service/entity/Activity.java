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
package com.service.service.entity;

import com.service.service.utils.StringUtils;
import com.service.service.utils.TimeUtils;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.Serializable;
import java.util.*;

/**
 * Model类用来表示跨多个存储库的提交活动。这
 * 类被活动页面使用。
 *
 * @author holykunge
 */
public class Activity implements Serializable, Comparable<Activity> {

	private static final long serialVersionUID = 1L;

	public final Date startDate;

	public final Date endDate;

	private final Set<RepositoryCommit> commits;

	private final Map<String, Metric> authorMetrics;

	private final Map<String, Metric> repositoryMetrics;

	private final Set<String> authorExclusions;

	/**
	 * 一天活动的构造函数。
	 *
	 * @param date
	 */
	public Activity(Date date) {
		this(date, TimeUtils.ONEDAY - 1);
	}

	/**
	 * 从开始日期起指定的活动持续时间的构造函数。
	 *
	 * @param date
	 *            the start date of the activity
	 * @param duration
	 *            the duration of the period in milliseconds
	 */
	public Activity(Date date, long duration) {
		startDate = date;
		endDate = new Date(date.getTime() + duration);
		commits = new LinkedHashSet<RepositoryCommit>();
		authorMetrics = new HashMap<String, Metric>();
		repositoryMetrics = new HashMap<String, Metric>();
		authorExclusions = new TreeSet<String>();
	}

	/**
	 * 将指定的作者排除在指标之外。
	 *
	 * @param authors
	 */
	public void excludeAuthors(Collection<String> authors) {
		for (String author : authors) {
			authorExclusions.add(author.toLowerCase());
		}
	}

	/**
	 * 只要提交不是副本,就向活动对象添加一个承诺。
	 *
	 * @param repository
	 * @param branch
	 * @param commit
	 * @return a RepositoryCommit, if one was added. Null if this is duplicate
	 *         commit
	 */
	public RepositoryCommit addCommit(String repository, String branch, RevCommit commit) {
		RepositoryCommit commitModel = new RepositoryCommit(repository, branch, commit);
		return addCommit(commitModel);
	}

	/**
	 * 只要提交不是副本,就向活动对象添加一个承诺。
	 *
	 * @param commitModel
	 * @return a RepositoryCommit, if one was added. Null if this is duplicate
	 *         commit
	 */
	public RepositoryCommit addCommit(RepositoryCommit commitModel) {
		if (commits.add(commitModel)) {
			String author = StringUtils.removeNewlines(commitModel.getAuthorIdent().getName());
			String authorName = author.toLowerCase();
			String authorEmail = StringUtils.removeNewlines(commitModel.getAuthorIdent().getEmailAddress()).toLowerCase();
			if (!repositoryMetrics.containsKey(commitModel.repository)) {
				repositoryMetrics.put(commitModel.repository, new Metric(commitModel.repository));
			}
			repositoryMetrics.get(commitModel.repository).count++;

			if (!authorExclusions.contains(authorName) && !authorExclusions.contains(authorEmail)) {
				if (!authorMetrics.containsKey(author)) {
					authorMetrics.put(author, new Metric(author));
				}
				authorMetrics.get(author).count++;
			}
			return commitModel;
		}
		return null;
	}

	public int getCommitCount() {
		return commits.size();
	}

	public List<RepositoryCommit> getCommits() {
		List<RepositoryCommit> list = new ArrayList<RepositoryCommit>(commits);
		Collections.sort(list);
		return list;
	}

	public Map<String, Metric> getAuthorMetrics() {
		return authorMetrics;
	}

	public Map<String, Metric> getRepositoryMetrics() {
		return repositoryMetrics;
	}

	@Override
	public int compareTo(Activity o) {
		// reverse chronological order
		return o.startDate.compareTo(startDate);
	}
}
