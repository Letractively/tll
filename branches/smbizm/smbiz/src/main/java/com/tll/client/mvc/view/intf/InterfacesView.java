/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.mvc.view.intf;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.Style;
import com.tll.client.data.rpc.IHasRpcHandlers;
import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.RpcEvent;
import com.tll.client.listing.IListingHandler;
import com.tll.client.listing.IListingOperator;
import com.tll.client.listing.ListingEvent;
import com.tll.client.listing.ListingFactory;
import com.tll.client.model.IHasModelChangeHandlers;
import com.tll.client.model.IModelChangeHandler;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.model.ModelChangeManager;
import com.tll.client.mvc.view.AbstractModelAwareView;
import com.tll.client.mvc.view.StaticViewInitializer;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.RpcUiHandler;
import com.tll.client.ui.edit.EditEvent;
import com.tll.client.ui.edit.EditPanel;
import com.tll.client.ui.edit.IEditHandler;
import com.tll.client.ui.edit.EditEvent.EditOp;
import com.tll.client.ui.field.intf.AbstractInterfacePanel;
import com.tll.client.ui.field.intf.MultiOptionInterfacePanel;
import com.tll.client.ui.field.intf.SwitchInterfacePanel;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.search.InterfaceSearch;
import com.tll.criteria.CriteriaType;
import com.tll.dao.Sorting;
import com.tll.listhandler.ListHandlerType;

/**
 * InterfacesView - AbstractView for managing Interfaces and the sub-entities.
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public class InterfacesView extends AbstractModelAwareView<StaticViewInitializer> implements ClickHandler {

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
	 * InterfacesStack - Extended {@link StackPanel} tailored for on demand
	 * loading of stack {@link Widget}s.
	 * @author jpk
	 */
	private static final class InterfacesStack extends StackPanel implements IListingHandler<Model> {

		private static AbstractInterfacePanel resolveInterfacePanel(IEntityType intfType) {
			final SmbizEntityType set = IEntityType.Util.toEnum(SmbizEntityType.class, intfType);
			if(SmbizEntityType.INTERFACE_MULTI == set || SmbizEntityType.INTERFACE_SINGLE == set) {
				return new MultiOptionInterfacePanel();
			}
			else if(SmbizEntityType.INTERFACE_SWITCH == set) {
				return new SwitchInterfacePanel();
			}
			else {
				throw new IllegalArgumentException();
			}
		}

		/**
		 * InterfaceStack - Exteneded {@link EditPanel} to manage edit, rpc and
		 * model change events for the bound interface.
		 * @author jpk
		 */
		private final class InterfaceStack extends EditPanel implements IEditHandler, IHasRpcHandlers, IHasModelChangeHandlers, IModelChangeHandler {

			// private final int stackIndex;
			private final ModelKey intfRef;
			private Model model; // the interface model
			private final AuxDataRequest auxDataRequest = new AuxDataRequest();

			/**
			 * Constructor
			 * @param intfRef
			 */
			public InterfaceStack(/*int stackIndex, */ModelKey intfRef) {
				super(App.getGlobalMsgPanel(), resolveInterfacePanel(intfRef.getEntityType()), false, true);
				// this.stackIndex = stackIndex;
				this.intfRef = intfRef;

				addEditHandler(this);
				addRpcHandler(new RpcUiHandler(InterfacesStack.this));
				addModelChangeHandler(this);
				setVisible(false); // hide initially

				auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_SWITCH);
				auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_SINGLE);
				auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_MULTI);
				auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_OPTION);
				auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
			}

			@Override
			public HandlerRegistration addRpcHandler(IRpcHandler handler) {
				return addHandler(handler, RpcEvent.TYPE);
			}

			@Override
			public HandlerRegistration addModelChangeHandler(IModelChangeHandler handler) {
				return addHandler(handler, ModelChangeEvent.TYPE);
			}

			public void loadInterfaceIfNecessary() {
				if(model == null) {
					ModelChangeManager.loadModel(this, intfRef, null, auxDataRequest).execute();
				}
			}

			public void onEdit(EditEvent event) {
				Command cmd = null;
				if(event.getOp().isSave()) {
					cmd = ModelChangeManager.persistModel(this, model, null);
				}
				else if(event.getOp() == EditOp.DELETE) {
					cmd = ModelChangeManager.deleteModel(this, model.getKey(), null);
				}
				if(cmd != null) {
					cmd.execute();
				}
			}

			void handleModelChangeError(ModelChangeEvent event) {
				applyFieldErrors(event.getStatus().getMsgs(MsgAttr.FIELD.flag));
			}

			void handleModelChangeSuccess(ModelChangeEvent event) {
				switch(event.getChangeOp()) {
					case LOADED:
						model = event.getModel();
						// open er up
						// showStack(stackIndex);
						setVisible(true);
						// NOTE: we fall through
					case UPDATED:
						setModel(model);
						break;

					case DELETED:
						// refresh the interface listing
						refreshData();
						break;
				}
			}

			@Override
			public void onModelChangeEvent(ModelChangeEvent event) {
				if(event.getStatus() != null && event.getStatus().hasErrors()) {
					// has errors
					handleModelChangeError(event);
				}
				else {
					// no errors
					handleModelChangeSuccess(event);
				}
			}

		}// InterfaceStack

		private final IListingOperator<Model> listHandler;

		/**
		 * Map of {@link InterfaceStack}s keyed by the stack index.
		 */
		private final List<InterfaceStack> list = new ArrayList<InterfaceStack>();

		private boolean initialized;

		/**
		 * Constructor
		 */
		public InterfacesStack() {
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

		void refreshData() {
			initialized = false;
			listHandler.refresh();
		}

		void clearData() {
			listHandler.clear();
		}

		/**
		 * Generates an HTML string from a given prop val model having a name and
		 * description property set.
		 * @param model
		 * @return HTML string
		 */
		private String getStackHtml(Model model) {
			final String name = model.getName();
			final String desc = model.asString("description");
			String type = model.getEntityType().getPresentationName();
			final int i = type.indexOf('-');
			if(i > 0) type = type.substring(0, i);
			return "<p class=\"" + Style.FLOAT_LEFT + "\"><span class=\"" + Style.BOLD + "\">" + name + " </span> (" + type
			+ ") </p><p class=\"" + Style.SMALL_ITALIC + " " + Style.FLOAT_RIGHT + "\">" + desc + "</p>";
		}

		@Override
		public void showStack(int index) {
			if(initialized) {
				final InterfaceStack ir = list.get(index);
				ir.loadInterfaceIfNecessary();
			}
			super.showStack(index);
		}

		public void onListingEvent(ListingEvent<Model> event) {
			// reset stack panel
			clear();

			list.clear();
			final Model[] intfs = event.getPageElements();

			if(intfs == null || intfs.length < 1) return;

			for(int i = 0; i < intfs.length; i++) {
				final Model data = intfs[i];
				final ModelKey ref = data.getKey();
				assert ref != null && ref.isSet();
				final InterfaceStack pnl = new InterfaceStack(ref);
				list.add(pnl);
				add(pnl, getStackHtml(data), true);
			}
			initialized = true;
		}

		void handleModelChangeSuccess(ModelChangeEvent event) {
			final Widget ew = (Widget) event.getSource();
			for(final InterfaceStack pnl : list) {
				if(ew == pnl) {
					pnl.handleModelChangeSuccess(event);
					return;
				}
			}
		}

		void handleModelChangeError(ModelChangeEvent event) {
			final Widget ew = (Widget) event.getSource();
			for(final InterfaceStack pnl : list) {
				if(ew == pnl) {
					pnl.handleModelChangeError(event);
					return;
				}
			}
		}

	} // InterfacesStack

	private final InterfacesStack intfStack = new InterfacesStack();

	private final Button btnAddIntf = new Button("Add Interface");

	/**
	 * Constructor
	 */
	public InterfacesView() {
		super();
		btnAddIntf.addClickHandler(this);
		addWidget(btnAddIntf);
		addWidget(intfStack);
	}

	@Override
	protected Class getViewClass() {
		return klas;
	}

	public String getLongViewName() {
		return "Interfaces";
	}

	@Override
	protected String getViewStyle() {
		return "interfaces";
	}

	@Override
	public void doInitialization(StaticViewInitializer initializer) {
		// no-op
	}

	public void refresh() {
		intfStack.refreshData();
	}

	@Override
	protected void doDestroy() {
		intfStack.clearData();
	}

	public void onClick(ClickEvent event) {
		if(event.getSource() == btnAddIntf) {
			// TODO add an interface
		}
	}

	@Override
	protected boolean shouldHandleModelChangeEvent(ModelChangeEvent event) {
		final SmbizEntityType set = IEntityType.Util.toEnum(SmbizEntityType.class, event.getModelKey().getEntityType());
		return event.getSource() == this || (event.getModelKey() != null && set.isInterfaceType());
	}

	@Override
	protected void handleModelChangeError(ModelChangeEvent event) {
		intfStack.handleModelChangeError(event);
	}

	@Override
	protected void handleModelChangeSuccess(ModelChangeEvent event) {
		intfStack.handleModelChangeSuccess(event);
	}
}
