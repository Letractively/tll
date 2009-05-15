package com.tll.client.mvc.view.intf;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.Style;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.data.rpc.IHasRpcHandlers;
import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.RpcEvent;
import com.tll.client.listing.IListingHandler;
import com.tll.client.listing.IListingOperator;
import com.tll.client.listing.ListingEvent;
import com.tll.client.listing.ListingFactory;
import com.tll.client.ui.edit.EditEvent;
import com.tll.client.ui.edit.EditPanel;
import com.tll.client.ui.edit.IEditHandler;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.msg.IMsgDisplay;
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
 * InterfaceStack - {@link StackPanel} enumerating the system defined interface
 * entities tailored for on demand loading of stack {@link Widget}s.
 * @author jpk
 */
class InterfaceStack extends StackPanel implements IHasRpcHandlers, IListingHandler<Model> {

	/**
	 * IFieldPanelResolver
	 * @author jpk
	 */
	public static interface IFieldPanelResolver {

		FieldPanel<?> resolveFieldPanel(IEntityType type);
	}

	/**
	 * Generates an HTML string from a given prop val model having a name and
	 * description property set.
	 * @param intf the interface model
	 * @return HTML string
	 */
	public static String getStackHtml(Model intf) {
		String type = intf.getEntityType().getPresentationName();
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
	 * InterfacePanel - {@link EditPanel} for an interface w/in the
	 * {@link InterfaceStack}.
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
						msgDisplay.add(new Msg(getModel().descriptor() + " updated.", MsgLevel.INFO), null);
						break;

					case ADD:
						InterfaceStack.this.setStackText(InterfaceStack.this.getWidgetIndex(InterfacePanel.this),
								getStackHtml(result.getEntity()), true);
						setModel(result.getEntity());
						showCancelButton(false);
						showDeleteButton(true);
						msgDisplay.add(new Msg(getModel().descriptor() + " added.", MsgLevel.INFO), null);
						break;

					case PURGE:
						msgDisplay.add(new Msg(getModel().descriptor() + " deleted.", MsgLevel.INFO), null);
						// remove interface
						removeInterface(InterfacePanel.this);
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
			super(msgDisplay, fldPnlResolver.resolveFieldPanel(intfRef.getEntityType()), showCancelBtn, showDeleteBtn);
			this.intfRef = intfRef;

			addEditHandler(this);
			rpc.setSource(rpcSource);
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

	final IMsgDisplay msgDisplay;

	final Widget rpcSource;

	final AuxDataRequest auxDataRequest;

	final IFieldPanelResolver fldPnlResolver;

	private final IListingOperator<Model> listHandler;

	/**
	 * Map of {@link InterfacePanel}s keyed by the stack index.
	 */
	private final List<InterfacePanel> list = new ArrayList<InterfacePanel>();

	private boolean initialized;

	/**
	 * Constructor
	 * @param msgDisplay
	 * @param rpcSource
	 * @param auxDataRequest
	 * @param fldPnlResolver
	 */
	public InterfaceStack(IMsgDisplay msgDisplay, Widget rpcSource, AuxDataRequest auxDataRequest,
			IFieldPanelResolver fldPnlResolver) {
		super();
		this.msgDisplay = msgDisplay;
		this.rpcSource = rpcSource;
		this.auxDataRequest = auxDataRequest;
		this.fldPnlResolver = fldPnlResolver;
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
		msgDisplay.clear();
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

}