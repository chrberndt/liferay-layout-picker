<%@ include file="/init.jsp" %>

<%
LayoutItemSelectorViewDisplayContext layoutItemSelectorViewDisplayContext = (LayoutItemSelectorViewDisplayContext)request.getAttribute(LayoutItemSelectorWebKeys.LAYOUT_ITEM_SELECTOR_VIEW_DISPLAY_CONTEXT);
Layout.class.getName();
PortalUtil.getClassNameId(Layout.class);
%>

<div class="container-fluid container-fluid-max-xl item-selector lfr-item-viewer">
	<c:if test="<%= layoutItemSelectorViewDisplayContext.isShowBreadcrumb() %>">
		<liferay-site-navigation:breadcrumb
			breadcrumbEntries="<%= layoutItemSelectorViewDisplayContext.getPortletBreadcrumbEntries() %>"
		/>
	</c:if>

	<layout-picker:select-layout
		checkDisplayPage="<%= false %>"
		enableCurrentPage="<%= true %>"
		followURLOnTitleClick="<%= false %>"
		itemSelectorSaveEvent="<%= HtmlUtil.escapeJS(layoutItemSelectorViewDisplayContext.getItemSelectedEventName()) %>"
		multiSelection="<%= false %>"
		namespace="<%= liferayPortletResponse.getNamespace() %>"
		pathThemeImages="<%= themeDisplay.getPathThemeImages() %>"
		privateLayout="<%= false %>"
		showHiddenLayouts="<%= false %>"
	/>
</div>

<aui:script>
	Liferay.on('<%= HtmlUtil.escapeJS(layoutItemSelectorViewDisplayContext.getItemSelectedEventName()) %>', function (event) {

		// Convert data attribute value to format expected by event handler
		// ' _com_liferay_layout_content_page_editor_web_internal_portlet_ContentPageEditorPortlet_selectInfoItem'

		var data = event.data;

		// console.log('data = ' + JSON.stringify(data));

		var valObj = {};
		valObj.classPK = data.plid;
		valObj.className = '<%= Layout.class.getName() %>';
		valObj.classNameId = '<%= PortalUtil.getClassNameId(Layout.class) %>';
		valObj.title = data.name;
		valObj.titleMap = data.nameMap;

		event.data.value = JSON.stringify(valObj);

	});
</aui:script>