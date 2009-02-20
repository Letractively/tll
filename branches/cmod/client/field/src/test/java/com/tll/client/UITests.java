package com.tll.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.tll.client.ui.Dialog;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/Style of
 * compiled GWT code.
 */
public final class UITests extends AbstractUITest {

	@Override
	protected String getTestSubjectName() {
		return "client-field module";
	}

	@Override
	protected UITestCase[] getTestCases() {
		return new UITestCase[] {};
	}

	/**
	 * DialogTest
	 * @author jpk
	 */
	static final class DialogTest extends UITestCase {

		VerticalPanel vp;
		Dialog dlg;
		Button btnShow, btnShowOverlay, btnHide;

		@Override
		public String getName() {
			return "Dialog";
		}

		@Override
		public String getDescription() {
			return "Tests the Dialog widget.";
		}

		@Override
		public void unload() {
			if(vp != null) {
				vp.removeFromParent();
				if(dlg != null) {
					dlg.hide();
					dlg = null;
				}
			}
		}

		void buildDialog(boolean showOverlay) {
			btnHide = new Button("Hide", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					dlg.hide();
					dlg = null;
				}
			});

			final SimplePanel p = new SimplePanel();
			p.setWidth("300px");
			p.setHeight("300px");
			p.add(btnHide);

			dlg = new Dialog(btnShow, showOverlay);
			dlg.setText("A Dialog");
			dlg.setWidget(p);
		}

		@Override
		public void load() {
			vp = new VerticalPanel();
			RootPanel.get().add(vp);

			btnShow = new Button("Show", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					buildDialog(false);
					dlg.center();
				}
			});
			vp.add(btnShow);

			btnShowOverlay = new Button("Show with Overlay", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					buildDialog(true);
					dlg.center();
				}
			});
			vp.add(btnShowOverlay);
		}

	} // DialogTest
}
