package com.chberndt.liferay.layout.picker.web.internal.info.item.selector;

import com.chberndt.liferay.layout.picker.web.internal.info.item.LayoutItemSelectorWebKeys;

import com.liferay.info.item.selector.InfoItemSelectorView;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.ItemSelectorView;
import com.liferay.item.selector.ItemSelectorViewDescriptorRenderer;
import com.liferay.item.selector.criteria.InfoItemItemSelectorReturnType;
import com.liferay.item.selector.criteria.info.item.criterion.InfoItemItemSelectorCriterion;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutService;
import com.liferay.portal.kernel.util.Portal;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletURL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	property = "item.selector.view.order:Integer=1",
	service = ItemSelectorView.class
)
public class LayoutItemSelectorView
	implements InfoItemSelectorView,
			   ItemSelectorView<InfoItemItemSelectorCriterion> {

	@Override
	public String getClassName() {
		return Layout.class.getName();
	}

	@Override
	public Class<? extends InfoItemItemSelectorCriterion>
		getItemSelectorCriterionClass() {

		return InfoItemItemSelectorCriterion.class;
	}

	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public List<ItemSelectorReturnType> getSupportedItemSelectorReturnTypes() {
		return _supportedItemSelectorReturnTypes;
	}

	@Override
	public String getTitle(Locale locale) {
		return _language.get(locale, "layout");
	}

	public boolean isPrivateLayout() {

		// TODO

		return false;
	}

	@Override
	public void renderHTML(
			ServletRequest servletRequest, ServletResponse servletResponse,
			InfoItemItemSelectorCriterion itemSelectorCriterion,
			PortletURL portletURL, String itemSelectedEventName, boolean search)
		throws IOException, ServletException {

		LayoutItemSelectorViewDisplayContext
			layoutItemSelectorViewDisplayContext =
				new LayoutItemSelectorViewDisplayContext(
					(HttpServletRequest)servletRequest, portletURL,
					itemSelectedEventName, isPrivateLayout());

		servletRequest.setAttribute(
			LayoutItemSelectorWebKeys.LAYOUT_ITEM_SELECTOR_VIEW_DISPLAY_CONTEXT,
			layoutItemSelectorViewDisplayContext);

		ServletContext servletContext = getServletContext();

		RequestDispatcher requestDispatcher =
			servletContext.getRequestDispatcher("/layouts.jsp");

		requestDispatcher.include(servletRequest, servletResponse);
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.chberndt.liferay.layout.picker.web)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	private static final List<ItemSelectorReturnType>
		_supportedItemSelectorReturnTypes = Collections.singletonList(
			new InfoItemItemSelectorReturnType());

	@Reference
	private ItemSelectorViewDescriptorRenderer<InfoItemItemSelectorCriterion>
		_itemSelectorViewDescriptorRenderer;

	@Reference
	private Language _language;

	@Reference
	private LayoutService _layoutService;

	@Reference
	private Portal _portal;

	private ServletContext _servletContext;

}