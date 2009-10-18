/**
 * The Logic Lab
 * @author jpk
 * @since Oct 17, 2009
 */
package com.tll.client.ui.impl;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.tll.client.ui.GlassPanel;


/**
 * GlassPanelImplIE6
 * @author jpk
 */
public class GlassPanelImplIE6 extends GlassPanelImpl {
	private int lastDocumentClientWidth = -1;
	private int lastDocumentClientHeight = -1;

	@Override
	public void matchDocumentSize(GlassPanel glassPanel, boolean dueToResize) {
		final int clientWidth = Window.getClientWidth();
		final int clientHeight = Window.getClientHeight();

		// Workaround for issue 1934
		// IE fires Window onresize events when the size of the body changes
		if (!dueToResize || clientWidth != lastDocumentClientWidth
				|| clientHeight != lastDocumentClientHeight) {
			final int offsetWidth = RootPanel.get().getOffsetWidth();
			final int offsetHeight = RootPanel.get().getOffsetHeight();

			final int scrollWidth = getWindowScrollWidth();
			final int scrollHeight = getWindowScrollHeight();

			final int width = Math.max(clientWidth, Math.max(offsetWidth, scrollWidth));
			final int height = Math.max(clientHeight, Math.max(offsetHeight, scrollHeight));
			glassPanel.setPixelSize(width, height);

			lastDocumentClientWidth = clientWidth;
			lastDocumentClientHeight = clientHeight;
		}
	}

	@Override
	public final void matchParentSize(final GlassPanel glassPanel, final AbsolutePanel parent) {
		// less than perfect due to parent borders, but works around other buggy size issues
		glassPanel.setPixelSize(parent.getOffsetWidth(), parent.getOffsetHeight());
	}
}
