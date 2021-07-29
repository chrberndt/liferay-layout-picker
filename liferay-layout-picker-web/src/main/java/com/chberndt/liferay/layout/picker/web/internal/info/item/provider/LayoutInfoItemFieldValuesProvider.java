package com.chberndt.liferay.layout.picker.web.internal.info.item.provider;

import com.chberndt.liferay.layout.picker.web.internal.info.item.LayoutInfoItemFields;

import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.info.item.InfoItemReference;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true, property = Constants.SERVICE_RANKING + ":Integer=10",
	service = InfoItemFieldValuesProvider.class
)
public class LayoutInfoItemFieldValuesProvider
	implements InfoItemFieldValuesProvider<Layout> {

	@Override
	public InfoItemFieldValues getInfoItemFieldValues(Layout layout) {
		return InfoItemFieldValues.builder(
		).infoFieldValues(
			_getLayoutInfoFieldValues(layout)
		).infoItemReference(
			new InfoItemReference(Layout.class.getName(), layout.getPlid())
		).build();
	}

	private List<InfoFieldValue<Object>> _getLayoutInfoFieldValues(
		Layout layout) {

		List<InfoFieldValue<Object>> layoutFieldValues = new ArrayList<>();

		ThemeDisplay themeDisplay = _getThemeDisplay();

		layoutFieldValues.add(
			new InfoFieldValue<>(
				LayoutInfoItemFields.friendlyUrlInfoField,
				layout.getFriendlyURL(themeDisplay.getLocale())));

		return layoutFieldValues;
	}

	private ThemeDisplay _getThemeDisplay() {
		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		if (serviceContext != null) {
			return serviceContext.getThemeDisplay();
		}

		return null;
	}

}