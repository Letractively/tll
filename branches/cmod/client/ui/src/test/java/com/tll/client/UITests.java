package com.tll.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.Dialog;
import com.tll.client.ui.MsgPanel;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.ui.option.IOptionHandler;
import com.tll.client.ui.option.Option;
import com.tll.client.ui.option.OptionEvent;
import com.tll.client.ui.option.OptionsPanel;
import com.tll.client.ui.option.OptionsPopup;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/Style of
 * compiled GWT code.
 */
public final class UITests extends AbstractUITest {

	@Override
	protected String getTestSubjectName() {
		return "client-ui module";
	}

	@Override
	protected UITestCase[] getTestCases() {
		return new UITestCase[] {
			new MsgPanelTest(), new DialogTest(), new OptionsPanelTest(), new OptionsPopupTest() };
	}

	/**
	 * MsgPanelTest - Tests {@link MsgPanel} layout.
	 * @author jpk
	 */
	static final class MsgPanelTest extends UITestCase {

		@Override
		public void unload() {
			MsgManager.instance().clear();
		}

		@Override
		public void load() {
			final List<Msg> msgs = new ArrayList<Msg>();
			msgs.add(new Msg("This is an error message.", MsgLevel.ERROR));
			msgs.add(new Msg("This is another error message.", MsgLevel.ERROR));
			msgs.add(new Msg("This is a warn message.", MsgLevel.WARN));
			msgs.add(new Msg("This is an info message.", MsgLevel.INFO));
			msgs.add(new Msg("This is another info message.", MsgLevel.INFO));
			msgs.add(new Msg("This is yet another info message.", MsgLevel.INFO));
			MsgManager.instance().post(false, msgs, Position.CENTER, RootPanel.get(), -1, true).show();
		}

		@Override
		public String getDescription() {
			return "Tests the layout/CSS of MsgPanels";
		}

		@Override
		public String getName() {
			return "MsgPanel";
		}
	} // MsgPanelTest

	/**
	 * DialogTest
	 * @author jpk
	 */
	static final class DialogTest extends UITestCase {

		VerticalPanel vp;
		Dialog dlg;
		Button btnShow, btnShowOverlay, btnHide;

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

		@Override
		public String getDescription() {
			return "Tests the Dialog widget";
		}

		@Override
		public String getName() {
			return "Dialog";
		}
	} // DialogTest

	/**
	 * OptionsPanelTest
	 * @author jpk
	 */
	static final class OptionsPanelTest extends UITestCase {

		/**
		 * OptionEventIndicator - Indicates Option events visually as they occur.
		 */
		class OptionEventIndicator extends Composite implements IOptionHandler {

			FlowPanel fp = new FlowPanel();
			VerticalPanel vp = new VerticalPanel();
			ScrollPanel sp = new ScrollPanel(vp);
			Button btnClear = new Button("Clear");

			/**
			 * Constructor
			 */
			public OptionEventIndicator() {
				super();

				sp.setWidth("200px");
				sp.setHeight("200px");

				btnClear.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						vp.clear();
					}
				});

				fp.add(sp);
				fp.add(btnClear);

				initWidget(fp);
			}

			@Override
			public void onOptionEvent(OptionEvent event) {
				vp.insert(new Label(event.toDebugString()), 0);
			}

		} // OptionEventIndicator

		SimplePanel optionPnlWrapper;
		OptionsPanel op;
		HorizontalPanel hp;
		OptionEventIndicator indicator;

		@Override
		public void unload() {
			RootPanel.get().remove(hp);
		}

		@Override
		public void load() {
			optionPnlWrapper = new SimplePanel();

			op = new OptionsPanel();
			op.setOptions(new Option[] {
				new Option("Option 1"), new Option("Option 2"), new Option("Option 3"), new Option("Option 4"),
				new Option("Option 5"), new Option("Option 6"), new Option("Option 7"), new Option("Option 8"),
				new Option("Option 9"), new Option("Option 10"), new Option("Option 11"), new Option("Option 12") });

			optionPnlWrapper.setWidth("200px");
			optionPnlWrapper.setHeight("200px");
			optionPnlWrapper.setWidget(op);

			hp = new HorizontalPanel();
			hp.setSpacing(10);
			hp.getElement().getStyle().setProperty("padding", "1em");
			hp.add(optionPnlWrapper);

			indicator = new OptionEventIndicator();
			op.addOptionHandler(indicator);
			hp.add(indicator);

			RootPanel.get().add(hp);
		}

		@Override
		public String getDescription() {
			return "Tests the OptionsPanel functionality and event handling.";
		}

		@Override
		public String getName() {
			return "OptionsPanel";
		}
	}

	/**
	 * OptionsPanelTest
	 * @author jpk
	 */
	static final class OptionsPopupTest extends UITestCase {

		FocusPanel contextArea = new FocusPanel();
		OptionsPopup popup;

		@Override
		public String getDescription() {
			return "Tests the OptionsPopup panel";
		}

		@Override
		public String getName() {
			return "OptionsPopup";
		}

		@Override
		public void load() {
			popup = new OptionsPopup(2000);
			popup.setOptions(new Option[] {
				new Option("Option 1"), new Option("Option 2"), new Option("Option 3"), new Option("Option 4"),
				new Option("Option 5") });

			contextArea.setSize("200px", "200px");
			contextArea.getElement().getStyle().setProperty("margin", "1em");
			contextArea.getElement().getStyle().setProperty("border", "1px solid gray");

			// IMPT: this enables the popup to be positioned at the mouse click location!
			contextArea.addMouseDownHandler(popup);

			contextArea.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if(!popup.isShowing()) {
						popup.show();
					}
				}
			});

			RootPanel.get().add(contextArea);
		}

		@Override
		public void unload() {
			RootPanel.get().remove(contextArea);
			contextArea = null;
			popup = null;
		}
	} // OptionsPopupTest
}
