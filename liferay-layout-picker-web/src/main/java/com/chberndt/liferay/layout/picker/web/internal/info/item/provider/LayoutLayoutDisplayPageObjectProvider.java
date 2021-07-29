package com.chberndt.liferay.layout.picker.web.internal.info.item.provider;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetTagLocalServiceUtil;
import com.liferay.layout.display.page.LayoutDisplayPageObjectProvider;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Locale;

/**
 * @author Christian Berndt
 */
public class LayoutLayoutDisplayPageObjectProvider
	implements LayoutDisplayPageObjectProvider<Layout> {

	public LayoutLayoutDisplayPageObjectProvider(Layout layout)
		throws PortalException {

		_layout = layout;

		_assetEntry = _getAssetEntry(layout);
	}

	@Override
	public long getClassNameId() {
		return _assetEntry.getClassNameId();
	}

	@Override
	public long getClassPK() {
		return _layout.getPlid();
	}

	@Override
	public long getClassTypeId() {
		return _assetEntry.getClassTypeId();
	}

	@Override
	public String getDescription(Locale locale) {
		return _assetEntry.getDescription(locale);
	}

	@Override
	public Layout getDisplayObject() {
		return _layout;
	}

	@Override
	public long getGroupId() {
		return _layout.getGroupId();
	}

	@Override
	public String getKeywords(Locale locale) {
		String[] assetTagNames = AssetTagLocalServiceUtil.getTagNames(
			_assetEntry.getClassName(), _assetEntry.getClassPK());
		String[] assetCategoryNames =
			AssetCategoryLocalServiceUtil.getCategoryNames(
				_assetEntry.getClassName(), _assetEntry.getClassPK());

		String[] keywords =
			new String[assetTagNames.length + assetCategoryNames.length];

		ArrayUtil.combine(assetTagNames, assetCategoryNames, keywords);

		return StringUtil.merge(keywords);
	}

	@Override
	public String getTitle(Locale locale) {
		return _assetEntry.getTitle(locale);
	}

	@Override
	public String getURLTitle(Locale locale) {
		AssetRenderer<?> assetRenderer = _assetEntry.getAssetRenderer();

		return assetRenderer.getUrlTitle(locale);
	}

	private AssetEntry _getAssetEntry(Layout layout) throws PortalException {
		AssetRendererFactory<?> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.
				getAssetRendererFactoryByClassNameId(
					PortalUtil.getClassNameId(Layout.class));

		return assetRendererFactory.getAssetEntry(
			Layout.class.getName(), layout.getPlid());
	}

	private final AssetEntry _assetEntry;
	private final Layout _layout;

}