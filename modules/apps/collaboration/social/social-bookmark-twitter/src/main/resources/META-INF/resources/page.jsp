<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String contentId = GetterUtil.getString((String)request.getAttribute("liferay-social-bookmarks:bookmark:contentId"));
SocialBookmark socialBookmark = (SocialBookmark)request.getAttribute("liferay-social-bookmarks:bookmark:socialBookmark");
String title = GetterUtil.getString((String)request.getAttribute("liferay-social-bookmarks:bookmark:title"));
String url = GetterUtil.getString((String)request.getAttribute("liferay-social-bookmarks:bookmark:url"));

Map<String, String> data = new HashMap<>();

data.put("contentid", contentId);
%>

<clay:link
	buttonStyle="secondary"
	data="<%= data %>"
	elementClasses="btn-outline-borderless btn-sm lfr-portal-tooltip"
	href="<%= socialBookmark.getPostURL(title, url) %>"
	icon="twitter"
	title="<%= socialBookmark.getName(locale) %>"
/>