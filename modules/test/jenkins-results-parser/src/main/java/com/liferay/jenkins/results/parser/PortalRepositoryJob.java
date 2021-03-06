/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.jenkins.results.parser;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @author Michael Hashimoto
 */
public abstract class PortalRepositoryJob extends RepositoryJob {

	@Override
	public List<String> getBatchNames() {
		String testBatchNames = portalTestProperties.getProperty(
			"test.batch.names");

		return getListFromString(testBatchNames);
	}

	@Override
	public List<String> getDistTypes() {
		String testBatchDistAppServers = portalTestProperties.getProperty(
			"test.batch.dist.app.servers");

		return getListFromString(testBatchDistAppServers);
	}

	public String getPoshiQuery(String testBatchName) {
		String propertyName = JenkinsResultsParserUtil.combine(
			"test.batch.run.property.query[", testBatchName, "]");

		if (portalTestProperties.containsKey(propertyName)) {
			String propertyValue = portalTestProperties.getProperty(
				propertyName);

			if (!propertyValue.isEmpty()) {
				return propertyValue;
			}
		}

		return null;
	}

	protected PortalRepositoryJob(String jobName) {
		super(jobName);

		branchName = _getBranchName();
		gitWorkingDirectory = _getGitWorkingDirectory();

		portalTestProperties = getGitWorkingDirectoryProperties(
			"test.properties");
	}

	protected List<String> getListFromString(String string) {
		if (string == null) {
			return Collections.emptyList();
		}

		List<String> list = new ArrayList<>();

		for (String item : StringUtils.split(string, ",")) {
			if (list.contains(item) || item.startsWith("#")) {
				continue;
			}

			list.add(item);
		}

		Collections.sort(list);

		return list;
	}

	protected final Properties portalTestProperties;

	private String _getBranchName() {
		Matcher matcher = _pattern.matcher(jobName);

		if (matcher.find()) {
			return matcher.group("branchName");
		}

		return "master";
	}

	private GitWorkingDirectory _getGitWorkingDirectory() {
		String branchName = _getBranchName();
		String workingDirectoryPath = "/opt/dev/projects/github/liferay-portal";

		if (!branchName.equals("master")) {
			workingDirectoryPath = JenkinsResultsParserUtil.combine(
				workingDirectoryPath, "-", branchName);
		}

		try {
			return new GitWorkingDirectory(branchName, workingDirectoryPath);
		}
		catch (IOException ioe) {
			throw new RuntimeException(
				"Invalid Git working directory " + workingDirectoryPath, ioe);
		}
	}

	private static final Pattern _pattern = Pattern.compile(
		"[^\\(]+\\((?<branchName>[^\\)]+)\\)");

}