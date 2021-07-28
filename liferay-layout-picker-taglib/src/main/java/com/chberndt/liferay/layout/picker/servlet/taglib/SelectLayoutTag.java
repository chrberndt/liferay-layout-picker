package com.chberndt.liferay.layout.picker.servlet.taglib;

import com.chberndt.liferay.layout.picker.servlet.ServletContextUtil;
import com.liferay.exportimport.kernel.staging.StagingUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.util.IncludeTag;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Christian Berndt
 *
 * Based on com.liferay.layout.taglib.servlet.taglib.react.SelectLayoutTag
 */
public class SelectLayoutTag extends IncludeTag {

	public String getComponentId() {
		return _componentId;
	}

	public boolean getFollowURLOnTitleClick() {
		return _followURLOnTitleClick;
	}

	public String getItemSelectorSaveEvent() {
		return _itemSelectorSaveEvent;
	}

	public String getNamespace() {
		return _namespace;
	}

	public String getPathThemeImages() {
		return _pathThemeImages;
	}

	public String getViewType() {
		return _viewType;
	}

	public boolean isCheckDisplayPage() {
		return _checkDisplayPage;
	}

	public boolean isEnableCurrentPage() {
		return _enableCurrentPage;
	}

	public boolean isMultiSelection() {
		return _multiSelection;
	}

	public boolean isPrivateLayout() {
		return _privateLayout;
	}

	public boolean isShowHiddenLayouts() {
		return _showHiddenLayouts;
	}

	public void setCheckDisplayPage(boolean checkDisplayPage) {
		_checkDisplayPage = checkDisplayPage;
	}

	public void setComponentId(String componentId) {
		_componentId = componentId;
	}

	public void setEnableCurrentPage(boolean enableCurrentPage) {
		_enableCurrentPage = enableCurrentPage;
	}

	public void setFollowURLOnTitleClick(boolean followURLOnTitleClick) {
		_followURLOnTitleClick = followURLOnTitleClick;
	}

	public void setItemSelectorSaveEvent(String itemSelectorSaveEvent) {
		_itemSelectorSaveEvent = itemSelectorSaveEvent;
	}

	public void setMultiSelection(boolean multiSelection) {
		_multiSelection = multiSelection;
	}

	public void setNamespace(String namespace) {
		_namespace = namespace;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		setServletContext(ServletContextUtil.getServletContext());
	}

	public void setPathThemeImages(String pathThemeImages) {
		_pathThemeImages = pathThemeImages;
	}

	public void setPrivateLayout(boolean privateLayout) {
		_privateLayout = privateLayout;
	}

	public void setShowHiddenLayouts(boolean showHiddenLayouts) {
		_showHiddenLayouts = showHiddenLayouts;
	}

	public void setViewType(String viewType) {
		_viewType = viewType;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_checkDisplayPage = false;
		_componentId = null;
		_enableCurrentPage = false;
		_followURLOnTitleClick = false;
		_itemSelectorSaveEvent = null;
		_multiSelection = false;
		_namespace = null;
		_pathThemeImages = null;
		_privateLayout = false;
		_showHiddenLayouts = false;
		_viewType = null;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest httpServletRequest) {
		try {
			httpServletRequest.setAttribute(
				"layout-picker:select-layout:data", _getData());
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	private Map<String, Object> _getData() throws Exception {
		return HashMapBuilder.<String, Object>put(
			"followURLOnTitleClick", _followURLOnTitleClick
		).put(
			"itemSelectorSaveEvent", _itemSelectorSaveEvent
		).put(
			"multiSelection", _multiSelection
		).put(
			"namespace", _namespace
		).put(
			"nodes", _getLayoutsJSONArray()
		).put(
			"pathThemeImages", _pathThemeImages
		).put(
			"viewType", _viewType
		).build();
	}

	private String _getLayoutBreadcrumb(Layout layout) throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Locale locale = themeDisplay.getLocale();

		List<Layout> ancestors = layout.getAncestors();

		StringBundler sb = new StringBundler((4 * ancestors.size()) + 5);

		if (layout.isPrivateLayout()) {
			sb.append(LanguageUtil.get(request, "private-pages"));
		}
		else {
			sb.append(LanguageUtil.get(request, "public-pages"));
		}

		sb.append(StringPool.SPACE);
		sb.append(StringPool.GREATER_THAN);
		sb.append(StringPool.SPACE);

		Collections.reverse(ancestors);

		for (Layout ancestor : ancestors) {
			sb.append(HtmlUtil.escape(ancestor.getName(locale)));
			sb.append(StringPool.SPACE);
			sb.append(StringPool.GREATER_THAN);
			sb.append(StringPool.SPACE);
		}

		sb.append(HtmlUtil.escape(layout.getName(locale)));

		return sb.toString();
	}

	private JSONArray _getLayoutsJSONArray() throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Group group = themeDisplay.getScopeGroup();

		if ((_privateLayout && !group.hasPrivateLayouts()) ||
			(!_privateLayout && !group.hasPublicLayouts())) {

			return JSONFactoryUtil.createJSONArray();
		}

		String layoutUuid = ParamUtil.getString(request, "layoutUuid");

		return JSONUtil.put(
			JSONUtil.put(
				"children",
				_getLayoutsJSONArray(
					themeDisplay.getScopeGroupId(), _privateLayout, 0,
					layoutUuid)
			).put(
				"disabled", true
			).put(
				"expanded", true
			).put(
				"icon", "home"
			).put(
				"id", "0"
			).put(
				"name", themeDisplay.getScopeGroupName()
			));
	}

	private JSONArray _getLayoutsJSONArray(
			long groupId, boolean privateLayout, long parentLayoutId,
			String selectedLayoutUuid)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
			groupId, privateLayout, parentLayoutId);

		for (Layout layout : layouts) {
			if ((layout.isHidden() && !_showHiddenLayouts) ||
				_isContentLayoutDraft(layout) ||
				StagingUtil.isIncomplete(layout)) {

				continue;
			}

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			JSONArray childrenJSONArray = _getLayoutsJSONArray(
				groupId, privateLayout, layout.getLayoutId(),
				selectedLayoutUuid);

			if (childrenJSONArray.length() > 0) {
				jsonObject.put("children", childrenJSONArray);
			}

			if ((_checkDisplayPage && !layout.isContentDisplayPage()) ||
				(_enableCurrentPage && (layout.getPlid() == _getSelPlid()))) {

				jsonObject.put("disabled", true);
			}

			// Add custom attribute plid
			jsonObject.put(
				"groupId", layout.getGroupId()
			).put(
				"icon", "page"
			).put(
				"id", layout.getUuid()
			).put(
				"layoutId", layout.getLayoutId()
			).put(
				"name", layout.getName(themeDisplay.getLocale())
			).put(
				"plid", layout.getPlid()
			).put(
				"privateLayout", layout.isPrivateLayout()
			).put(
				"url", PortalUtil.getLayoutRelativeURL(layout, themeDisplay)
			);

			if (Objects.equals(layout.getUuid(), selectedLayoutUuid)) {
				jsonObject.put("selected", true);
			}

			// Use Layout.name without public / private page prefix
			jsonObject.put("value", layout.getName(themeDisplay.getLocale()));
			// jsonObject.put("value", _getLayoutBreadcrumb(layout));

			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

	private long _getSelPlid() {
		return ParamUtil.getLong(
			request, "selPlid", LayoutConstants.DEFAULT_PLID);
	}

	private boolean _isContentLayoutDraft(Layout layout) {
		if (!layout.isTypeContent()) {
			return false;
		}

		Layout draftLayout = layout.fetchDraftLayout();

		if (draftLayout != null) {
			boolean published = GetterUtil.getBoolean(
				draftLayout.getTypeSettingsProperty("published"));

			return !published;
		}

		if (layout.isApproved() && !layout.isHidden() && !layout.isSystem()) {
			return false;
		}

		return true;
	}

	private static final String _PAGE = "/page.jsp";

	private static final Log _log = LogFactoryUtil.getLog(
		SelectLayoutTag.class);

	private boolean _checkDisplayPage;
	private String _componentId;
	private boolean _enableCurrentPage;
	private boolean _followURLOnTitleClick;
	private String _itemSelectorSaveEvent;
	private boolean _multiSelection;
	private String _namespace;
	private String _pathThemeImages;
	private boolean _privateLayout;
	private boolean _showHiddenLayouts;
	private String _viewType;

}