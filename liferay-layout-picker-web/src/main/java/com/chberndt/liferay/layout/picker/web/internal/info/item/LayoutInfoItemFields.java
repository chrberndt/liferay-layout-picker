package com.chberndt.liferay.layout.picker.web.internal.info.item;

import com.liferay.info.field.InfoField;
import com.liferay.info.field.type.TextInfoFieldType;
import com.liferay.info.localized.InfoLocalizedValue;

/**
 * @author Christian Berndt
 */
public interface LayoutInfoItemFields {

	public static final InfoField<TextInfoFieldType> friendlyUrlInfoField =
		InfoField.builder(
		).infoFieldType(
			TextInfoFieldType.INSTANCE
		).name(
			"friendly-url"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				LayoutInfoItemFields.class, "friendly-url")
		).build();

}