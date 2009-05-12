/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.mvc.view.intf;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.Style;
import com.tll.client.data.rpc.AuxDataCacheHelper;
import com.tll.client.data.rpc.AuxDataCommand;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.data.rpc.IHasRpcHandlers;
import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.RpcEvent;
import com.tll.client.listing.IListingHandler;
import com.tll.client.listing.IListingOperator;
import com.tll.client.listing.ListingEvent;
import com.tll.client.listing.ListingFactory;
import com.tll.client.model.ModelAssembler;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.mvc.view.AbstractRpcAndModelAwareView;
import com.tll.client.mvc.view.StaticViewInitializer;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.Dialog;
import com.tll.client.ui.GridRenderer;
import com.tll.client.ui.edit.EditEvent;
import com.tll.client.ui.edit.EditPanel;
import com.tll.client.ui.edit.IEditHandler;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanel;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.RadioGroupField;
import com.tll.client.ui.field.intf.MultiOptionInterfacePanel;
import com.tll.client.ui.field.intf.SwitchInterfacePanel;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.client.ui.view.ViewToolbar;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityPayload;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.InterfaceSearch;
import com.tll.criteria.CriteriaType;
import com.tll.dao.Sorting;
import com.tll.listhandler.ListHandlerType;

/**
 * InterfacesView - AbstractView for managing Interfaces and the sub-entities.
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public class InterfacesView extends AbstractRpcAndModelAwareView<StaticViewInitializer> implements ClickHandler {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		@Override
		public String getName() {
			return "interfacesView";
		}

		@Override
		public InterfacesView newView() {
			return new InterfacesView();
		}

	}

	/**
	 * IntfSlectDlg
	 * @author jpk
	 */
	final class IntfSlectDlg extends Dialog {

		/**
		 * InterfaceTypeSelectPanel
		 * @author jpk
		 */
		final class InterfaceTypeSelectPanel extends FlowFieldPanel {

			SmbizEntityType intfType;

			@Override
			protected FieldGroup generateFieldGroup() {
				final FieldGroup fg = new FieldGroup("Interface Type Selection");
				final LinkedHashMap<SmbizEntityType, String> data = new LinkedHashMap<SmbizEntityType, String>();
				data.put(SmbizEntityType.INTERFACE_SWITCH, "Switch");
				data.put(SmbizEntityType.INTERFACE_SINGLE, "Single");
				data.put(SmbizEntityType.INTERFACE_MULTI, "Multi");
				final RadioGroupField<SmbizEntityType> f =
					FieldFactory.fradiogroup("interfaceType", "interfaceType", null, "Interface Type", data, new GridRenderer(
							1, null));
				f.addValueChangeHandler(new ValueChangeHandler<SmbizEntityType>() {

					@Override
					public void onValueChange(ValueChangeEvent<SmbizEntityType> event) {
						intfType = event.getValue();
						assert intfType != null;
						final Model m = ModelAssembler.assemble(intfType);
						assert m != null;
						intfStack.addInterface(m, false);
						IntfSlectDlg.this.hide();
						intfStack.showStack(intfStack.getWidgetCount() - 1);
					}
				});
				fg.addField(f);
				return fg;
			}

			@Override
			protected IFieldRenderer<FlowPanel> getRenderer() {
				return new IFieldRenderer<FlowPanel>() {

					@Override
					public void render(FlowPanel widget, FieldGroup fg) {
						final HorizontalPanel hp = new HorizontalPanel();
						hp.setStyleName("addIntf");
						hp.add(fg.getFieldByName("interfaceType").getWidget());
						hp.add(new Button("Cancel", new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								IntfSlectDlg.this.hide();
							}
						}));
						widget.add(hp);
					}
				};
			}

		} // InterfaceTypeSelectPanel

		final InterfaceTypeSelectPanel fldPnl = new InterfaceTypeSelectPanel();

		/**
		 * Constructor
		 */
		public IntfSlectDlg() {
			super();
			setText("Select the interface type to add");
			add(new InterfaceTypeSelectPanel());
		}

	} // IntfSlectDlg

	/**
	 * InterfaceStack - {@link StackPanel} tailored for on demand loading of stack
	 * {@link Widget}s.
	 * @author jpk
	 */
	final class InterfaceStack extends StackPanel implements IHasRpcHandlers, IListingHandler<Model> {

		/**
		 * InterfacePanel - {@link EditPanel} for an interface w/in the {@link InterfaceStack}.
		 * @author jpk
		 */
		final class InterfacePanel extends EditPanel implements IEditHandler {

			final class Rpc extends CrudCommand {

				@Override
				protected void handleSuccess(EntityPayload result) {
					super.handleSuccess(result);
					if(result.hasErrors()) {
						applyFieldErrors(result.getStatus().getMsgs(MsgAttr.FIELD.flag));
						return;
					}
					switch(crudOp) {
						case LOAD:
							setModel(result.getEntity());
							setVisible(true);
							break;

						case UPDATE:
							setModel(result.getEntity());
							gmp.add(new Msg(getModel().descriptor() + " updated.", MsgLevel.INFO), null);
							break;

						case PURGE:
							gmp.add(new Msg(getModel().descriptor() + " deleted.", MsgLevel.INFO), null);
							// remove interface
							InterfaceStack.this.removeInterface(InterfacePanel.this);
							break;
					}
				}
			} // Rpc

			private final ModelKey intfRef;
			private final Rpc rpc = new Rpc();

			/**
			 * Constructor
			 * @param intfRef
			 * @param showCancelBtn
			 * @param showDeleteBtn
			 */
			public InterfacePanel(ModelKey intfRef, boolean showCancelBtn, boolean showDeleteBtn) {
				super(gmp, resolveInterfaceFieldPanel(intfRef.getEntityType()), showCancelBtn, showDeleteBtn);
				this.intfRef = intfRef;

				addEditHandler(this);
				rpc.setSource(InterfacesView.this);
				setVisible(false); // hide initially
			}

			/**
			 * Constructor
			 * @param intf
			 */
			public InterfacePanel(Model intf) {
				this(intf.getKey(), true, false);
				setModel(intf);
				setVisible(true);
			}

			public void loadInterfaceIfNecessary() {
				if(getModel() == null) {
					rpc.load(intfRef, auxDataRequest);
					DeferredCommand.addCommand(rpc);
				}
			}

			@Override
			public void onEdit(EditEvent event) {
				switch(event.getOp()) {
					case ADD:
						rpc.add(getModel());
						break;
					case UPDATE:
						rpc.update(getModel());
						break;
					case DELETE:
						rpc.purge(getModel().getKey());
						break;
					case CANCEL:
						// remove the pending add from the stack
						removeInterface(this);
						return;
				}
				DeferredCommand.addCommand(rpc);
			}

		}// InterfacePanel

		private final IListingOperator<Model> listHandler;

		/**
		 * Map of {@link InterfacePanel}s keyed by the stack index.
		 */
		private final List<InterfacePanel> list = new ArrayList<InterfacePanel>();

		private boolean initialized;

		/**
		 * Constructor
		 */
		public InterfaceStack() {
			super();
			final String listingName = SmbizEntityType.INTERFACE.name();
			final InterfaceSearch criteria = new InterfaceSearch(CriteriaType.SCALAR_NAMED_QUERY);
			criteria.setNamedQuery("interface.summaryList");
			final Sorting defaultSorting = new Sorting("name");
			listHandler =
				ListingFactory.createRemoteOperator(listingName, ListHandlerType.COLLECTION, criteria, null, -1,
						defaultSorting);
			listHandler.setSourcingWidget(this);
			addHandler(this, ListingEvent.TYPE);
		}

		@Override
		public HandlerRegistration addRpcHandler(IRpcHandler handler) {
			return addHandler(handler, RpcEvent.TYPE);
		}

		void refreshData() {
			initialized = false;
			listHandler.refresh();
		}

		void clearData() {
			listHandler.clear();
		}

		@Override
		public void showStack(int index) {
			if(initialized) {
				final InterfacePanel ir = list.get(index);
				ir.loadInterfaceIfNecessary();
			}
			gmp.clear();
			super.showStack(index);
		}

		void addInterface(Model intf, boolean useKeyOnly) {
			assert intf != null;
			final InterfacePanel pnl = useKeyOnly ? new InterfacePanel(intf.getKey(), false, true) : new InterfacePanel(intf);
			list.add(pnl);
			add(pnl, getStackHtml(intf), true);
		}

		void removeInterface(InterfacePanel pnl) {
			if(remove(pnl)) {
				list.remove(pnl);
			}
		}

		@Override
		public void onListingEvent(ListingEvent<Model> event) {
			// reset stack panel
			clear();
			list.clear();

			final Model[] intfs = event.getPageElements();
			if(intfs != null) {
				for(int i = 0; i < intfs.length; i++) {
					addInterface(intfs[i], true);
				}
				initialized = true;
				if(getWidgetCount() > 0) showStack(0);
			}
		}

	} // InterfaceStack

	private static FieldPanel<?> resolveInterfaceFieldPanel(IEntityType intfType) {
		final SmbizEntityType set = IEntityType.Util.toEnum(SmbizEntityType.class, intfType);
		if(SmbizEntityType.INTERFACE_MULTI == set || SmbizEntityType.INTERFACE_SINGLE == set) {
			return new MultiOptionInterfacePanel();
		}
		else if(SmbizEntityType.INTERFACE_SWITCH == set) {
			return new SwitchInterfacePanel();
		}
		throw new IllegalArgumentException("Unhandled interface type");
	}

	/**
	 * Generates an HTML string from a given prop val model having a name and
	 * description property set.
	 * @param model
	 * @return HTML string
	 */
	private static String getStackHtml(Model model) {
		if(model.isNew()) {
			return "<p class=\"" + Style.FLOAT_LEFT + "\"><span class=\"" + Style.BOLD + "\">-New Interface-</span></p>";
		}
		final String name = model.getName();
		final String desc = model.asString("description");
		String type = model.getEntityType().getPresentationName();
		final int i = type.indexOf('-');
		if(i > 0) type = type.substring(0, i);
		return "<p class=\"" + Style.FLOAT_LEFT + "\"><span class=\"" + Style.BOLD + "\">" + name + " </span> (" + type
		+ ") </p><p class=\"" + Style.SMALL_ITALIC + " " + Style.FLOAT_RIGHT + "\">" + desc + "</p>";
	}

	private static final AuxDataRequest auxDataRequest = new AuxDataRequest();

	static {
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_SWITCH);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_SINGLE);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_MULTI);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_OPTION);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
	}

	private final GlobalMsgPanel gmp = new GlobalMsgPanel();

	private final InterfaceStack intfStack = new InterfaceStack();

	private final PushButton btnAddIntf = new PushButton("Add Interface");

	private IntfSlectDlg dlg;

	/**
	 * Constructor
	 */
	public InterfacesView() {
		super();
		btnAddIntf.addClickHandler(this);
		addWidget(gmp);
		addWidget(intfStack);
	}

	@Override
	protected Class getViewClass() {
		return klas;
	}

	@Override
	public String getLongViewName() {
		return "Interfaces";
	}

	@Override
	protected String getViewStyle() {
		return "interfaces";
	}

	@Override
	protected void doInitialization(StaticViewInitializer initializer) {
		// no-op
	}

	@Override
	protected void decorateToolbar(ViewToolbar toolbar) {
		// add add interface button
		toolbar.addViewOpButton(this.btnAddIntf, "Add Interface");
	}

	@Override
	protected void doRefresh() {
		intfStack.refreshData();
	}

	@Override
	protected void doDestroy() {
		intfStack.clearData();
	}

	private void showAddIntfDlg() {
		// show the interface type selection dialog
		if(dlg == null) dlg = new IntfSlectDlg();
		dlg.center();
	}

	@Override
	public void onClick(ClickEvent event) {
		if(event.getSource() == btnAddIntf) {
			// ensure we have the aux data cached
			final AuxDataRequest adr = AuxDataCacheHelper.filterRequest(auxDataRequest);
			if(adr != null) {
				(new AuxDataCommand(adr) {

					@Override
					protected void handleSuccess(AuxDataPayload result) {
						super.handleSuccess(result);
						showAddIntfDlg();
					}

				}).execute();
			}
			else {
				showAddIntfDlg();
			}
		}
	}

	@Override
	protected boolean shouldHandleModelChangeEvent(ModelChangeEvent event) {
		// we manage our own and don't need to be notifed of external model changes
		return false;
	}
}
