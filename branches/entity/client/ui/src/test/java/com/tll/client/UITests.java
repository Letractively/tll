package com.tll.client;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.BusyPanel;
import com.tll.client.ui.Dialog;
import com.tll.client.ui.GlassPanel;
import com.tll.client.ui.IWidgetRef;
import com.tll.client.ui.Position;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.client.ui.msg.IMsgOperator;
import com.tll.client.ui.msg.MsgLevelImageBundle;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.ui.msg.Msgs;
import com.tll.client.ui.option.IOptionHandler;
import com.tll.client.ui.option.Option;
import com.tll.client.ui.option.OptionEvent;
import com.tll.client.ui.option.OptionsPanel;
import com.tll.client.ui.option.OptionsPopup;
import com.tll.client.ui.toolbar.Toolbar;
import com.tll.client.ui.toolbar.ToolbarImageBundle;
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
			new MsgsText(),
			new MsgPopupRegistryTest(),
			new GlobalMsgPanelTest(),
			new BusyPanelTest(),
			new DialogTest(),
			new OptionsPanelTest(),
			new OptionsPopupTest(),
			new OptionsPopupTest2(),
			new PushButtonStyleTest(),
			new ToolbarStyleTest(),
		};
	}

	static final class MsgsText extends DefaultUITestCase {

		static final Msg m1 = new Msg("This is message 1", MsgLevel.INFO);
		static final Msg m2 = new Msg("This is message 2", MsgLevel.WARN);
		static final Msg m3 = new Msg("This is message 3", MsgLevel.ERROR);
		static final ArrayList<Msg> mlist;

		static {
			mlist = new ArrayList<Msg>();
			mlist.add(m1);
			mlist.add(m2);
			mlist.add(m3);
		}

		FlowPanel pnl;
		Label lblA, lblB;

		/**
		 * Constructor
		 */
		public MsgsText() {
			super("Msgs", "Tests the static methods in the Msgs class");
		}

		@Override
		protected void init() {
			pnl = new FlowPanel();
			RootPanel.get().getElement().getStyle().setProperty("backgroundColor", "silver");
			lblA = new Label("Label A");
			lblB = new Label("Label B");
			pnl.add(lblA);
			pnl.add(lblB);
		}

		@Override
		protected void teardown() {
			pnl = null;
		}

		@Override
		protected Widget getContext() {
			return pnl;
		}

		@Override
		protected Button[] getTestActions() {
			return new Button[] {
				new Button("Post single on Label A", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						Msgs.post(m1, lblA);
					}
				}), new Button("Post multiple on Label A", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						Msgs.post(mlist, lblA);
					}
				}), new Button("Post timed single on Label A", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						Msgs.post(m1, lblA, Position.BOTTOM, 1000, true);
					}
				}), };
		}
	}

	/**
	 * MsgPopupRegistryTest
	 * @author jpk
	 */
	static final class MsgPopupRegistryTest extends UITestCase {

		static final Msg msgInfo = new Msg("An info message.", MsgLevel.INFO);
		static final Msg msgWarn = new Msg("A warn message.", MsgLevel.WARN);
		static final Msg msgError = new Msg("An error message.", MsgLevel.ERROR);
		static final Msg msgFatal = new Msg("A fatal message.", MsgLevel.FATAL);

		static final Msg cmsgInfo = new Msg("A classified info message.", MsgLevel.INFO);
		static final Msg cmsgWarn = new Msg("A classified warn message.", MsgLevel.WARN);
		static final Msg cmsgError = new Msg("A classified error message.", MsgLevel.ERROR);
		static final Msg cmsgFatal = new Msg("A classified fatal message.", MsgLevel.FATAL);

		static final Msg[] allMsgs = new Msg[] {
			msgInfo, msgWarn, msgError, msgFatal };

		static final Msg[] allCMsgs = new Msg[] {
			cmsgInfo, cmsgWarn, cmsgError, cmsgFatal };

		static final int classifier = 1;

		MsgPopupRegistry registry;
		HorizontalPanel layout;
		VerticalPanel buttonPanel;
		FlowPanel context, nestedContext;
		Label refWidget;
		boolean bTglShowImg = true;

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
			buttonPanel.add(new Button("Show All Msgs", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.getOperator(context, true).showMsgs(true);
				}
			}));
			buttonPanel.add(new Button("Hide All Msgs", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.getOperator(context, true).showMsgs(false);
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
					registry.getOperator(refWidget, false).addMsgs(Arrays.asList(allMsgs), null);
				}
			}));
			buttonPanel.add(new Button("Add classified Messages", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.getOperator(refWidget, false).addMsgs(Arrays.asList(allCMsgs), Integer.valueOf(classifier));
				}
			}));
			buttonPanel.add(new Button("Remove classified Messages", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.getOperator(refWidget, false).removeMsgs(classifier);
				}
			}));

			// show/hide msg level images
			final Button btnLevel = new Button("Show Msg Level Imgs");
			btnLevel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					final IMsgOperator op = registry.getOperator(context, true);
					op.showMsgs(Position.BOTTOM, -1, bTglShowImg);
					btnLevel.setText(bTglShowImg ? "Hide Msg Level Imgs" : "Show Msg Level Imgs");
					bTglShowImg = !bTglShowImg;
				}
			});
			buttonPanel.add(btnLevel);

			buttonPanel.add(new Button("Test cloaking", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					registry.clear();
					final IMsgOperator operator = registry.getOperator(refWidget, false);
					operator.addMsg(msgWarn, null);
					nestedContext.setVisible(false);
					operator.showMsgs(true);
					Window
					.alert("No message popup should appear even though showMsgs() was called because the nestedContext's visibliity was just set to false.");
					nestedContext.setVisible(true);
					operator.showMsgs(true);
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
	} // MsgPopupRegistryTest

	/**
	 * BusyPanelTest - Tests the {@link BusyPanel}.
	 * @author jpk
	 */
	static final class BusyPanelTest extends UITestCase {

		HorizontalPanel layout;
		VerticalPanel buttonPanel;
		FlowPanel context;
		AbsolutePanel localOverlay;
		GlassPanel busyPanel;

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
			busyPanel = new BusyPanel(true);
		}

		private void stubTestButtons() {
			buttonPanel = new VerticalPanel();

			buttonPanel.add(new Button("Test Local", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if(localOverlay != null) {
						RootPanel.get().remove(localOverlay);
					}
					localOverlay = BusyPanel.createOverlay(context);
					localOverlay.add(busyPanel, 0, 0);
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
			// realize scrolling for testing purposes
			layout.setWidth("1000px");
			layout.setHeight("1000px");
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

	/**
	 * GlobalMsgPanelTest
	 * @author jpk
	 */
	static final class GlobalMsgPanelTest extends DefaultUITestCase {

		static final int classifier = 1;

		static IWidgetRef createRef(final Widget w, final String descriptor) {
			return new IWidgetRef() {

				@Override
				public Widget getWidget() {
					return w;
				}

				@Override
				public String descriptor() {
					return descriptor;
				}
			};
		}

		/**
		 * Factory method for creating test {@link Msg}s.
		 * @param level the msg level
		 * @param cfr optional classifier id
		 * @param num the number of messages to create
		 * @return newly created list of {@link Msg}s.
		 */
		static ArrayList<Msg> stubMsgs(MsgLevel level, Integer cfr, int num) {
			final ArrayList<Msg> list = new ArrayList<Msg>(num);
			for(int i = 0; i < num; i++) {
				String s;
				if(cfr == null) {
					s = "This is " + level.getName() + " message #" + Integer.toString(i + 1);
				}
				else {
					s =
						"This is classified " + level.getName() + " message #" + Integer.toString(i + 1) + " (cid: " + cfr
						+ ")";
				}
				list.add(new Msg(s, level));
			}
			return list;
		}

		GlobalMsgPanel gmp;
		HorizontalPanel refWidgetPanel;
		IWidgetRef tb1, tb2, lbl;

		/**
		 * Constructor
		 */
		public GlobalMsgPanelTest() {
			super("GlobalMsgPanel", "Verifies the GlobalMsgPanel layout and its operations.");
		}

		@Override
		protected Widget getContext() {
			if(gmp == null) {
				gmp = new GlobalMsgPanel();
			}
			return gmp;
		}

		@Override
		protected void init() {
			refWidgetPanel = new HorizontalPanel();
			refWidgetPanel.getElement().getStyle().setProperty("margin", "1em");
			refWidgetPanel.setSpacing(5);

			TextBox tb = new TextBox();
			tb.setValue("Text Box 1");
			tb1 = createRef(tb, "Text Box 1");
			refWidgetPanel.add(tb1.getWidget());

			tb = new TextBox();
			tb.setValue("Text Box 2");
			tb2 = createRef(tb, "Text Box 2");
			refWidgetPanel.add(tb2.getWidget());

			lbl = createRef(new Label("Num Msgs"), "Num Msgs");
			refWidgetPanel.add(lbl.getWidget());

			RootPanel.get().add(refWidgetPanel);
		}

		@Override
		protected void teardown() {
			refWidgetPanel.removeFromParent();
			refWidgetPanel = null;
		}

		@Override
		protected Button[] getTestActions() {
			return new Button[] {
				new Button("Clear all Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.clear();
					}
				}),
				new Button("Remove Fatal Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.remove(MsgLevel.FATAL);
					}
				}),
				new Button("Remove Error Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.remove(MsgLevel.ERROR);
					}
				}),
				new Button("Remove Warn Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.remove(MsgLevel.WARN);
					}
				}),
				new Button("Remove Info Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.remove(MsgLevel.INFO);
					}
				}),
				new Button("Remove Text Box 1 Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.remove(tb1, null);
					}
				}),
				new Button("Remove Text Box 2 Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.remove(tb2, null);
					}
				}),
				new Button("Remove Label Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.remove(lbl, null);
					}
				}),
				new Button("Remove Un-sourced Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.removeUnsourced(null);
					}
				}),
				new Button("Remove Classified Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.remove(classifier);
					}
				}),
				new Button("Add Fatal Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.add(tb1, stubMsgs(MsgLevel.FATAL, null, 1), null);
						gmp.add(tb2, stubMsgs(MsgLevel.FATAL, null, 1), null);
						gmp.add(lbl, stubMsgs(MsgLevel.FATAL, null, 1), null);
					}
				}),
				new Button("Add Error Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.add(tb1, stubMsgs(MsgLevel.ERROR, null, 1), null);
						gmp.add(tb2, stubMsgs(MsgLevel.ERROR, null, 1), null);
						gmp.add(lbl, stubMsgs(MsgLevel.ERROR, null, 1), null);
					}
				}),
				new Button("Add Warn Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.add(tb1, stubMsgs(MsgLevel.WARN, null, 1), null);
						gmp.add(tb2, stubMsgs(MsgLevel.WARN, null, 1), null);
						gmp.add(lbl, stubMsgs(MsgLevel.WARN, null, 1), null);
					}
				}),
				new Button("Add Info Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.add(tb1, stubMsgs(MsgLevel.INFO, null, 1), null);
						gmp.add(tb2, stubMsgs(MsgLevel.INFO, null, 1), null);
						gmp.add(lbl, stubMsgs(MsgLevel.INFO, null, 1), null);
					}
				}),
				new Button("Add Un-sourced Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						gmp.add(stubMsgs(MsgLevel.INFO, null, 1), null);
						gmp.add(stubMsgs(MsgLevel.WARN, null, 1), null);
						gmp.add(stubMsgs(MsgLevel.ERROR, null, 1), null);
						gmp.add(stubMsgs(MsgLevel.FATAL, null, 1), null);
					}
				}),
				new Button("Add Classified Messages", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						final Integer igr = Integer.valueOf(classifier);
						gmp.add(stubMsgs(MsgLevel.INFO, igr, 1), igr);
						gmp.add(stubMsgs(MsgLevel.WARN, igr, 1), igr);
						gmp.add(stubMsgs(MsgLevel.ERROR, igr, 1), igr);
						gmp.add(stubMsgs(MsgLevel.FATAL, igr, 1), igr);
					}
				}),
				new Button("Show Size", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						final StringBuilder sb = new StringBuilder();
						sb.append("Total size: " + gmp.size() + "\r\n");
						sb.append(MsgLevel.INFO + " size: " + gmp.size(MsgLevel.INFO) + "\r\n");
						sb.append(MsgLevel.WARN + " size: " + gmp.size(MsgLevel.WARN) + "\r\n");
						sb.append(MsgLevel.ERROR + " size: " + gmp.size(MsgLevel.ERROR) + "\r\n");
						sb.append(MsgLevel.FATAL + " size: " + gmp.size(MsgLevel.FATAL) + "\r\n");
						Window.alert(sb.toString());
					}
				}),
			};
		}
	} // GlobalMsgPanelTest

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
		static class OptionEventIndicator extends Composite implements IOptionHandler {

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
	 * OptionsPopupTest
	 * @author jpk
	 */
	static final class OptionsPopupTest extends UITestCase {

		FocusPanel contextArea;
		OptionsPopup popup;

		@Override
		public String getDescription() {
			return "Tests the OptionsPopup panel with a hide duration.";
		}

		@Override
		public String getName() {
			return "OptionsPopup - hide duration";
		}

		@Override
		public void load() {
			popup = new OptionsPopup(2000);
			popup.setOptions(new Option[] {
				new Option("Option 1"), new Option("Option 2"), new Option("Option 3"), new Option("Option 4"),
				new Option("Option 5") });

			contextArea = new FocusPanel();
			contextArea.setSize("200px", "200px");
			contextArea.getElement().getStyle().setProperty("margin", "1em");
			contextArea.getElement().getStyle().setProperty("border", "1px solid gray");

			// IMPT: this enables the popup to be positioned at the mouse click
			// location!
			contextArea.addMouseDownHandler(popup);

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
	 * OptionsPopupTest
	 * @author jpk
	 */
	static final class OptionsPopupTest2 extends UITestCase {

		FocusPanel contextArea;
		OptionsPopup popup;

		@Override
		public String getDescription() {
			return "Tests the OptionsPopup panel with NO hide duration.";
		}

		@Override
		public String getName() {
			return "OptionsPopup - Indefinite";
		}

		@Override
		public void load() {
			popup = new OptionsPopup();
			popup.setOptions(new Option[] {
				new Option("Option 1"), new Option("Option 2"), new Option("Option 3"), new Option("Option 4"),
				new Option("Option 5") });

			contextArea = new FocusPanel();
			contextArea.setSize("200px", "200px");
			contextArea.getElement().getStyle().setProperty("margin", "1em");
			contextArea.getElement().getStyle().setProperty("border", "1px solid gray");

			// IMPT: this enables the popup to be positioned at the mouse click
			// location!
			contextArea.addMouseDownHandler(popup);

			RootPanel.get().add(contextArea);
		}

		@Override
		public void unload() {
			RootPanel.get().remove(contextArea);
			contextArea = null;
			popup = null;
		}
	} // OptionsPopupTest2

	/**
	 * PushButtonStyleTest
	 * @author jpk
	 */
	static final class PushButtonStyleTest extends UITestCase {

		FlowPanel contextArea;
		PushButton b1, b2, b3;

		@Override
		public String getDescription() {
			return "Tests the PushButton styling.";
		}

		@Override
		public String getName() {
			return "PushButton CSS Styling Test";
		}

		@Override
		public void load() {
			contextArea = new FlowPanel();
			contextArea.setSize("200px", "200px");
			contextArea.getElement().getStyle().setProperty("margin", "1em");
			contextArea.getElement().getStyle().setProperty("border", "1px solid gray");
			contextArea.getElement().getStyle().setProperty("padding", "1em");
			RootPanel.get().add(contextArea);

			b1 = new PushButton("Push Button 1");
			b1.getElement().getStyle().setProperty("margin", "1em");
			b2 = new PushButton(AbstractImagePrototype.create(MsgLevelImageBundle.INSTANCE.error()).createImage());
			b2.getElement().getStyle().setProperty("margin", "1em");
			b3 = new PushButton(AbstractImagePrototype.create(MsgLevelImageBundle.INSTANCE.warn()).createImage());
			b3.getElement().getStyle().setProperty("margin", "1em");
			contextArea.add(b1);
			contextArea.add(b2);
			contextArea.add(b3);
		}

		@Override
		public void unload() {
			RootPanel.get().remove(contextArea);
			contextArea = null;
			b1 = b2 = b3 = null;
		}
	} // PushButtonStyleTest

	/**
	 * ToolbarStyleTest
	 * @author jpk
	 */
	static final class ToolbarStyleTest extends UITestCase {

		FlowPanel contextArea;
		Toolbar tb;

		@Override
		public String getDescription() {
			return "Tests the Toolbar styling.";
		}

		@Override
		public String getName() {
			return "Toolbar Styling Test";
		}

		@Override
		public void load() {
			contextArea = new FlowPanel();
			contextArea.getElement().getStyle().setProperty("margin", "1em");
			contextArea.getElement().getStyle().setProperty("border", "1px solid gray");
			contextArea.getElement().getStyle().setProperty("padding", "3em");
			RootPanel.get().add(contextArea);

			tb = new Toolbar();
			contextArea.add(tb);

			// add contents to the the toolbar
			final PushButton pb = new PushButton(AbstractImagePrototype.create(MsgLevelImageBundle.INSTANCE.info()).createImage());
			pb.setEnabled(false);
			tb.addButton(pb, "Info");
			tb.add(AbstractImagePrototype.create(ToolbarImageBundle.INSTANCE.split()).createImage());
			tb.addButton(new PushButton(AbstractImagePrototype.create(MsgLevelImageBundle.INSTANCE.error()).createImage()), "Error");
			tb.add(AbstractImagePrototype.create(ToolbarImageBundle.INSTANCE.split()).createImage());
			tb.addButton(new PushButton(AbstractImagePrototype.create(MsgLevelImageBundle.INSTANCE.warn()).createImage()), "Warn");
			tb.addButton(new PushButton(AbstractImagePrototype.create(MsgLevelImageBundle.INSTANCE.warn()).createImage()), "Warn");
			tb.addButton(new PushButton(AbstractImagePrototype.create(MsgLevelImageBundle.INSTANCE.warn()).createImage()), "Warn");
			tb.add(AbstractImagePrototype.create(ToolbarImageBundle.INSTANCE.split()).createImage());
			final Label lbl = new Label("This is a label");
			lbl.setWidth("100%");
			tb.add(lbl);

			tb.add(new PushButton(AbstractImagePrototype.create(MsgLevelImageBundle.INSTANCE.fatal()).createImage()));
		}

		@Override
		public void unload() {
			RootPanel.get().remove(contextArea);
			contextArea = null;
			tb = null;
		}
	} // ToolbarStyleTest
}
