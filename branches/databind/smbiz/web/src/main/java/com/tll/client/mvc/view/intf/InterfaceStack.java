package com.tll.client.mvc.view.intf;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.Style;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.data.rpc.IRpcCommand;
import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.RpcEvent;
import com.tll.client.listing.IListingHandler;
import com.tll.client.listing.IListingOperator;
import com.tll.client.listing.ListingEvent;
import com.tll.client.listing.rpc.RemoteListingOperator;
import com.tll.client.model.IModelChangeHandler;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.ui.RpcUiHandler;
import com.tll.client.ui.edit.EditEvent;
import com.tll.client.ui.edit.EditPanel;
import com.tll.client.ui.edit.IEditHandler;
import com.tll.client.ui.field.AbstractBindableFieldPanel;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.client.validate.ErrorHandlerBuilder;
import com.tll.client.validate.ErrorHandlerDelegate;
import com.tll.common.data.ModelDataRequest;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.NamedQuerySearch;
import com.tll.dao.Sorting;
import com.tll.listhandler.ListHandlerType;

/**
 * InterfaceStack - {@link StackPanel} enumerating the system defined interface
 * entities tailored for on demand loading of stack {@link Widget}s.
 * @author jpk
 */
class InterfaceStack extends Composite implements IRpcHandler, IListingHandler<Model> {

	/**
	 * IFieldPanelResolver
	 * @author jpk
	 */
	public static interface IFieldPanelResolver {

		AbstractBindableFieldPanel<?> resolveFieldPanel(ModelKey intfRef);
	}

	/**
	 * IFieldPanelDataLoader
	 * @author jpk
	 */
	public static interface IFieldPanelDataLoader {

		IRpcCommand load(ModelKey intfKey, ModelDataRequest auxDataRequest);
	}

	/**
	 * Generates an HTML string from a given prop val model having a name and
	 * description property set.
	 * @param intf the interface model
	 * @return HTML string
	 */
	static String getStackHtml(Model intf) {
		String type = intf.getEntityType().descriptor();
		final int i = type.indexOf('-');
		if(i > 0) type = type.substring(0, i);
		if(intf.isNew()) {
			return "<p class=\"" + Style.FLOAT_LEFT + "\"><span class=\"" + Style.BOLD + "\">-New " + type + "-</span></p>";
		}
		final String name = intf.getName();
		final String desc = intf.asString("description");
		return "<p class=\"" + Style.FLOAT_LEFT + "\"><span class=\"" + Style.BOLD + "\">" + name + " </span> (" + type
		+ ") </p><p class=\"" + Style.SMALL_ITALIC + " " + Style.FLOAT_RIGHT + "\">" + desc + "</p>";
	}

	/**
	 * InterfacesPanel - Extended {@link StackPanel} to accommodate on demand
	 * loading of the child panels.
	 * @author jpk
	 */
	final class InterfacesPanel extends StackPanel {

		@Override
		public void showStack(int index) {
			if(initialized) {
				final InterfacePanel ir = list.get(index);
				ir.loadIfNecessary();
			}
			gmp.clear();
			super.showStack(index);
		}

	} // InterfacesPanel

	/**
	 * InterfacePanel - {@link EditPanel} for an interface w/in the
	 * {@link InterfaceStack}.
	 * @author jpk
	 */
	final class InterfacePanel extends EditPanel implements IEditHandler, IRpcHandler, IModelChangeHandler {

		private final ModelKey intfRef;

		/**
		 * Constructor
		 * @param intfRef
		 * @param showCancelBtn
		 * @param showDeleteBtn
		 */
		public InterfacePanel(ModelKey intfRef, boolean showCancelBtn, boolean showDeleteBtn) {
			super(fldPnlResolver.resolveFieldPanel(intfRef), showCancelBtn, showDeleteBtn, true);
			this.intfRef = intfRef;
			setErrorHandler(errorHandler, false);
			addEditHandler(this);
			setVisible(false); // hide initially
			addHandler(this, ModelChangeEvent.TYPE);
			addHandler(this, RpcEvent.TYPE);
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

		public void loadIfNecessary() {
			if(fieldPanel.getModel() == null) {
				final IRpcCommand cmd = loader.load(intfRef, auxDataRequest);
				cmd.setSource(this);
				DeferredCommand.addCommand(cmd);
			}
		}

		@Override
		public void onEdit(EditEvent event) {
			switch(event.getOp()) {
			case ADD:
				crud.add(fieldPanel.getModel());
				break;
			case UPDATE:
				crud.update(fieldPanel.getChangedModel());
				break;
			case DELETE:
				crud.purge(fieldPanel.getModel().getKey());
				break;
			case CANCEL:
				// remove the pending add from the stack
				removeInterface(this);
				return;
			}
			crud.setSource(this);
			DeferredCommand.addCommand(crud);
		}

		@Override
		public void onRpcEvent(RpcEvent event) {
			rpcHandler.onRpcEvent(event);
		}

		@Override
		public void onModelChangeEvent(ModelChangeEvent event) {
			// TODO fix - we don't carry Status in ModelChangeEvent anymore
			/*
			if(event.getStatus().hasErrors()) {
				applyFieldErrors(event.getStatus().getMsgs(MsgAttr.FIELD.flag), ErrorClassifier.SERVER, true);
				return;
			}
			*/
			switch(event.getChangeOp()) {
			case LOADED:
				setModel(event.getModel());
				setVisible(true);
				break;

			case UPDATED:
				setModel(event.getModel());
				msgDisplay.add(new Msg(fieldPanel.getModel().descriptor() + " updated.", MsgLevel.INFO), null);
				break;

			case ADDED:
				InterfaceStack.this.stackPanel.setStackText(InterfaceStack.this.stackPanel.getWidgetIndex(InterfacePanel.this),
						getStackHtml(event.getModel()), true);
				setModel(event.getModel());
				showCancelButton(false);
				showDeleteButton(true);
				msgDisplay.add(new Msg(fieldPanel.getModel().descriptor() + " added.", MsgLevel.INFO), null);
				break;

			case DELETED:
				msgDisplay.add(new Msg(fieldPanel.getModel().descriptor() + " deleted.", MsgLevel.INFO), null);
				// remove interface
				removeInterface(InterfacePanel.this);
				break;
			}
		}

	}// InterfacePanel

	final FlowPanel flowPanel;

	final InterfacesPanel stackPanel;

	final GlobalMsgPanel gmp;

	final ErrorHandlerDelegate errorHandler;

	final IRpcHandler rpcHandler;

	final ModelDataRequest auxDataRequest;

	final IFieldPanelResolver fldPnlResolver;

	final boolean deletable;

	final IListingOperator<Model> listHandler;

	final List<InterfacePanel> list = new ArrayList<InterfacePanel>();

	final IFieldPanelDataLoader loader;

	final CrudCommand crud;

	boolean initialized;

	/**
	 * Constructor
	 * @param criteria The listing criteria by which interfaces are retrieved
	 * @param auxDataRequest The needed aux data for displaying a stack panel
	 * @param fldPnlResolver Resolves the appropriate {@link AbstractBindableFieldPanel} when it
	 *        needs to be loaded
	 * @param loader Responsible for loading the model data that backs the field
	 *        panel generated by the field panel resolver
	 * @param deletable Is the interface data deletable?
	 */
	public InterfaceStack(NamedQuerySearch criteria, ModelDataRequest auxDataRequest,
			IFieldPanelResolver fldPnlResolver, IFieldPanelDataLoader loader, boolean deletable) {
		super();
		this.flowPanel = new FlowPanel();
		this.stackPanel = new InterfacesPanel();
		this.gmp = new GlobalMsgPanel();
		this.errorHandler = ErrorHandlerBuilder.build(true, true, gmp);
		this.rpcHandler = new RpcUiHandler(this);
		this.auxDataRequest = auxDataRequest;
		this.fldPnlResolver = fldPnlResolver;
		this.deletable = deletable;
		final String listingId = SmbizEntityType.INTERFACE.name();
		final Sorting defaultSorting = new Sorting("name");
		final String[] propKeys = {
			"name", "code", "description"
		};
		listHandler =
			RemoteListingOperator.create(listingId, ListHandlerType.IN_MEMORY, criteria, propKeys, -1, defaultSorting);
		listHandler.setSourcingWidget(this);
		this.loader = loader;
		crud = new CrudCommand();
		addHandler(this, ListingEvent.TYPE);
		addHandler(this, RpcEvent.TYPE);

		flowPanel.add(gmp);
		flowPanel.add(stackPanel);
		initWidget(flowPanel);
	}

	@Override
	public void onRpcEvent(RpcEvent event) {
		rpcHandler.onRpcEvent(event);
	}

	void refreshData() {
		initialized = false;
		listHandler.refresh();
	}

	void clearData() {
		listHandler.clear();
	}

	public void showInterface(int index) {
		stackPanel.showStack(index);
	}

	/**
	 * Adds a single instance of a presribed type of interface data
	 * @param intf the interface data
	 * @param useKeyOnly
	 */
	public void addInterface(Model intf, boolean useKeyOnly) {
		assert intf != null;
		final InterfacePanel pnl = useKeyOnly ? new InterfacePanel(intf.getKey(), false, deletable) : new InterfacePanel(intf);
		list.add(pnl);
		stackPanel.add(pnl, getStackHtml(intf), true);
	}

	public void removeInterface(InterfacePanel pnl) {
		if(stackPanel.remove(pnl)) {
			list.remove(pnl);
		}
	}

	public int getNumInterfaces() {
		return stackPanel.getWidgetCount();
	}

	@Override
	public void onListingEvent(ListingEvent<Model> event) {
		// reset stack panel
		stackPanel.clear();
		list.clear();

		final Model[] intfs = event.getPageElements();
		if(intfs != null) {
			for(int i = 0; i < intfs.length; i++) {
				addInterface(intfs[i], true);
			}
			initialized = true;
			if(stackPanel.getWidgetCount() > 0) stackPanel.showStack(0);
		}
	}

}