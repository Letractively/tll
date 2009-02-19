package com.tll.client;

import java.util.Arrays;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
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
import com.tll.client.ui.BusyPanel;
import com.tll.client.ui.Dialog;
import com.tll.client.ui.msg.IMsgOperator;
import com.tll.client.ui.msg.MsgPopupRegistry;
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
			new BusyPanelTest(), new DialogTest(), new OptionsPanelTest(), new OptionsPopupTest(), new MsgPopupRegistryTest() };
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
		public String getName() {
			return "OptionsPanel";
		}

		@Override
		public String getDescription() {
			return "Tests the OptionsPanel functionality and event handling.";
		}

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
			return "Tests the OptionsPopup panel.";
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

	/**
	 * MsgPopupRegistryTest
	 * @author jpk
	 */
	static final class MsgPopupRegistryTest extends UITestCase {

		static final Msg msgInfo = new Msg("An info message.", MsgLevel.INFO);
		static final Msg msgWarn = new Msg("A warn message.", MsgLevel.WARN);
		static final Msg msgError = new Msg("An error message.", MsgLevel.ERROR);
		static final Msg msgFatal = new Msg("A fatal message.", MsgLevel.FATAL);

		static final Msg[] allMsgs = new Msg[] {
			msgInfo, msgWarn, msgError, msgFatal };

		MsgPopupRegistry registry;
		HorizontalPanel layout;
		VerticalPanel buttonPanel;
		FlowPanel context, nestedContext;
		Label refWidget;

		@Override
		public String getName() {
			return "MsgPopupRegistry";
		}

		@Override
		public String getDescription() {
			return "Tests MsgPopupRegistry routines.";
		}

		private void stubContext() {
			context = new FlowPanel();
			context.setSize("200px", "200px");
			context.getElement().getStyle().setProperty("border", "1px solid gray");
			context.getElement().getStyle().setProperty("padding", "10px");

			nestedContext = new FlowPanel();
			nestedContext.setSize("100px", "100px");
			nestedContext.getElement().getStyle().setProperty("border", "1px solid gray");
			nestedContext.getElement().getStyle().setProperty("padding", "5px");
			context.add(nestedContext);

			refWidget = new Label("Ref Widget");
			refWidget.getElement().getStyle().setProperty("border", "1px solid gray");
			refWidget.getElement().getStyle().setProperty("margin", "5px");
			nestedContext.add(refWidget);
		}

		private void stubTestButtons() {
			buttonPanel = new VerticalPanel();

			buttonPanel.add(new Button("Show All Messages", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.getOperator(context, true).showMsgs();
				}
			}));
			buttonPanel.add(new Button("Hide All Messages", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.getOperator(context, true).hideMsgs();
				}
			}));
			buttonPanel.add(new Button("Clear All Messages", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.getOperator(context, true).clearMsgs();
				}
			}));
			buttonPanel.add(new Button("Clear Ref Widget Messages", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.getOperator(refWidget, false).clearMsgs();
				}
			}));
			buttonPanel.add(new Button("Add Ref Widget Messages", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.addMsgs(Arrays.asList(allMsgs), refWidget, false);
				}
			}));
			buttonPanel.add(new Button("Set Ref Widget Messages", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.addMsgs(Arrays.asList(allMsgs), refWidget, true);
				}
			}));
			buttonPanel.add(new Button("Show Message Level Images", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.getOperator(context, true).setShowMsgLevelImages(true);
				}
			}));
			buttonPanel.add(new Button("Hide Message Level Images", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.getOperator(context, true).setShowMsgLevelImages(false);
				}
			}));
			buttonPanel.add(new Button("Test cloaking", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.clear();
					registry.addMsg(msgWarn, refWidget, false);
					final IMsgOperator operator = registry.getOperator(refWidget, false);
					nestedContext.setVisible(false);
					operator.showMsgs();
					Window
							.alert("No message popup should appear even though showMsgs() was called because the nestedContext's visibliity was just set to false.");

					nestedContext.setVisible(true);
					operator.showMsgs();
					Window.alert("Now it should be showing because the nestedContext's visibliity was just set to true.");
				}
			}));
			buttonPanel.add(new Button("Test scroll handling", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Window.alert("TODO: implement");
				}
			}));
			buttonPanel.add(new Button("Test drag handling", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Window.alert("TODO: implement");
				}
			}));
		}

		@Override
		public void load() {
			registry = new MsgPopupRegistry();
			stubContext();
			stubTestButtons();
			layout = new HorizontalPanel();
			layout.add(buttonPanel);
			layout.add(context);
			layout.getElement().getStyle().setProperty("margin", "1em");
			layout.setSpacing(5);
			RootPanel.get().add(layout);
		}

		@Override
		public void unload() {
			layout.removeFromParent();
			context = null;
			buttonPanel = null;
			layout = null;
			registry.clear();
			registry = null;
		}
	} // MsgManagerTest

	/**
	 * BusyPanelTest - Tests the {@link BusyPanel}.
	 * @author jpk
	 */
	static final class BusyPanelTest extends UITestCase {

		HorizontalPanel layout;
		VerticalPanel buttonPanel;
		FlowPanel context;
		BusyPanel busyPanel = new BusyPanel(true, null, 20);
		
		@Override
		public String getName() {
			return "BusyPanel";
		}

		@Override
		public String getDescription() {
			return "Tests the BusyPanel globally and locally.";
		}

		private void stubContext() {
			context = new FlowPanel();
			context.setSize("200px", "200px");
			context.getElement().getStyle().setProperty("border", "1px solid gray");
			context.getElement().getStyle().setProperty("padding", "10px");

		}

		private void stubTestButtons() {
			buttonPanel = new VerticalPanel();

			buttonPanel.add(new Button("Test Local", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					BusyPanel.getAbsoluteOverlay(context).add(busyPanel, 0, 0);
				}
			}));
			buttonPanel.add(new Button("Test Global", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					RootPanel.get().add(busyPanel, 0, 0);
				}
			}));
		}

		@Override
		public void load() {
			stubContext();
			stubTestButtons();
			layout = new HorizontalPanel();
			layout.add(buttonPanel);
			layout.add(context);
			layout.getElement().getStyle().setProperty("margin", "1em");
			layout.setSpacing(5);
			RootPanel.get().add(layout);
		}

		@Override
		public void unload() {
			busyPanel.removeFromParent();
			busyPanel = null;
			layout.removeFromParent();
			context = null;
			buttonPanel = null;
			layout = null;
		}
		
	} // BusyPanelTest
}
