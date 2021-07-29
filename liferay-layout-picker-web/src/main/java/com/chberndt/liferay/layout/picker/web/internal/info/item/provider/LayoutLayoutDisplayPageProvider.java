package com.chberndt.liferay.layout.picker.web.internal.info.item.provider;

import com.liferay.info.item.InfoItemReference;
import com.liferay.layout.display.page.LayoutDisplayPageObjectProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(immediate = true, service = LayoutDisplayPageProvider.class)
public class LayoutLayoutDisplayPageProvider
	implements LayoutDisplayPageProvider<Layout> {

	@Override
	public String getClassName() {
		return Layout.class.getName();
	}

	@Override
	public LayoutDisplayPageObjectProvider<Layout>
		getLayoutDisplayPageObjectProvider(
			InfoItemReference infoItemReference) {

		try {
			Layout layout = _layoutLocalService.getLayout(
				infoItemReference.getClassPK());

			if (layout.isDraft()) {
				return null;
			}

			return new LayoutLayoutDisplayPageObjectProvider(layout);
		}
		catch (PortalException portalException) {
			throw new RuntimeException(portalException);
		}
	}

	@Override
	public LayoutDisplayPageObjectProvider<Layout>
		getLayoutDisplayPageObjectProvider(long groupId, String urlTitle) {

		return null;

		// TODO

		//		try {
		//			Layout layout = _layoutLocalService.getLayout(
		//				groupId, urlTitle);

		//
		//			return new LayoutLayoutDisplayPageObjectProvider(layout);
		//		}
		//		catch (PortalException portalException) {
		//			throw new RuntimeException(portalException);
		//		}

	}

	@Override
	public String getURLSeparator() {
		return "/l/";
	}

	@Reference
	private LayoutLocalService _layoutLocalService;

}