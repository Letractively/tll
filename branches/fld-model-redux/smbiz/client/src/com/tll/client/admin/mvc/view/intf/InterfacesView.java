/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.mvc.view.intf;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.CSS;
import com.tll.client.admin.ui.field.intf.AbstractInterfacePanel;
import com.tll.client.admin.ui.field.intf.MultiOptionInterfacePanel;
import com.tll.client.admin.ui.field.intf.SwitchInterfacePanel;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.rpc.ListingCommand;
import com.tll.client.event.IEditListener;
import com.tll.client.event.IListingListener;
import com.tll.client.event.type.EditEvent;
import com.tll.client.event.type.ListingEvent;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.event.type.ShowViewRequest;
import com.tll.client.event.type.StaticViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.event.type.EditEvent.EditOp;
import com.tll.client.listing.ListingFactory;
import com.tll.client.model.Model;
import com.tll.client.model.ModelChangeManager;
import com.tll.client.model.RefKey;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.search.impl.InterfaceSearch;
import com.tll.client.ui.field.EditPanel;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.SelectNamedQuery;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.Sorting;
import com.tll.model.EntityType;

/**
 * InterfacesView - AbstractView for managing Interfaces and the sub-entities.
 * @author jpk
 */
public class InterfacesView extends AbstractView implements ClickListener {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private Class() {
			super("interfacesView");
		}

		@Override
		public IView newView() {
			return new InterfacesView();
		}

	}

	/**
	 * InterfacesStack - Extended {@link StackPanel} tailored for on demand
	 * loading of stack {@link Widget}s.
	 * @author jpk
	 */
	private static final class InterfacesStack extends StackPanel implements IListingListener<Model> {

		/**
		 * InterfaceStack - Binding between a stack index and an {@link EditPanel}
		 * assigned to manage the edit for the asociated interface.
		 * @author jpk
		 */
		private final class InterfaceStack implements IEditListener {

			// private final int stackIndex;
			private final RefKey intfRef;
			private Model model; // the interface model
			private final EditPanel editPanel;
			private final AuxDataRequest auxDataRequest = new AuxDataRequest();

			/**
			 * Constructor
			 * @param intfRef
			 */
			public InterfaceStack(/*int stackIndex, */RefKey intfRef) {
				super();
				// this.stackIndex = stackIndex;
				this.intfRef = intfRef;

				editPanel = new EditPanel(resolveInterfacePanel(intfRef.getType()), false, true);
				editPanel.addEditListener(this);
				editPanel.setVisible(false); // hide initially

				auxDataRequest.requestEntityPrototype(EntityType.INTERFACE_SWITCH);
				auxDataRequest.requestEntityPrototype(EntityType.INTERFACE_SINGLE);
				auxDataRequest.requestEntityPrototype(EntityType.INTERFACE_MULTI);
				auxDataRequest.requestEntityPrototype(EntityType.INTERFACE_OPTION);
				auxDataRequest.requestEntityPrototype(EntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
			}

			private AbstractInterfacePanel resolveInterfacePanel(EntityType intfType) {
				switch(intfType) {
					case INTERFACE_MULTI:
					case INTERFACE_SINGLE:
						return new MultiOptionInterfacePanel();
					case INTERFACE_SWITCH:
						return new SwitchInterfacePanel();
					default:
						throw new IllegalArgumentException();
				}
			}

			public void loadInterfaceIfNecessary() {
				if(model == null) {
					ModelChangeManager.instance().loadModel(editPanel, intfRef, null, auxDataRequest);
				}
			}

			public void onEditEvent(EditEvent event) {
				if(event.getOp().isSave()) {
					if(editPanel.updateModel()) {
						ModelChangeManager.instance().persistModel(editPanel, model, null);
					}
				}
				else if(event.getOp() == EditOp.DELETE) {
					ModelChangeManager.instance().deleteModel(editPanel, model.getRefKey(), null);
				}
			}

			void handleModelChangeError(ModelChangeEvent event) {
				editPanel.applyErrorMsgs(event.getStatus().getFieldMsgs());
			}

			void handleModelChangeSuccess(ModelChangeEvent event) {
				switch(event.getChangeOp()) {
					case LOADED:
						model = event.getModel();
						// open er up
						// showStack(stackIndex);
						editPanel.setVisible(true);
						// NOTE: we fall through
					case UPDATED:
						editPanel.bindModel(model);
						break;

					case DELETED:
						// refresh the interface listing
						refreshData();
						break;
				}
			}

		}// InterfaceStack

		private final ListingCommand<InterfaceSearch> listingCommand;

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
			String listingName = EntityType.INTERFACE.name();
			InterfaceSearch criteria = new InterfaceSearch(CriteriaType.SCALAR_NAMED_QUERY);
			criteria.setNamedQuery(SelectNamedQuery.INTERFACE_SUMMARY_LISTING);
			Sorting defaultSorting = new Sorting("name", "intf");
			listingCommand =
					ListingFactory.createListingCommand(this, listingName, ListHandlerType.COLLECTION, criteria, null, -1,
							defaultSorting);
			listingCommand.addListingListener(this);
		}

		void refreshData() {
			initialized = false;
			listingCommand.refresh();
		}

		void clearData() {
			listingCommand.clear();
		}

		/**
		 * Generates an HTML string from a given prop val model having a name and
		 * description property set.
		 * @param model
		 * @return HTML string
		 */
		private String getStackHtml(Model model) {
			String name = model.getName();
			String desc = model.asString("description");
			String type = model.getEntityType().getName();
			int i = type.indexOf('-');
			if(i > 0) type = type.substring(0, i);
			return "<p class=\"" + CSS.FLOAT_LEFT + "\"><span class=\"" + CSS.BOLD + "\">" + name + " </span> (" + type
					+ ") </p><p class=\"" + CSS.SMALL_ITALIC + " " + CSS.FLOAT_RIGHT + "\">" + desc + "</p>";
		}

		@Override
		public void showStack(int index) {
			if(initialized) {
				InterfaceStack ir = list.get(index);
				ir.loadInterfaceIfNecessary();
			}
			super.showStack(index);
		}

		public void onListingEvent(ListingEvent<Model> event) {
			// reset stack panel
			clear();

			list.clear();
			Model[] intfs = event.getPageElements();

			if(intfs == null || intfs.length < 1) return;

			for(int i = 0; i < intfs.length; i++) {
				Model data = intfs[i];
				RefKey ref = data.getRefKey();
				assert ref != null && ref.isSet();
				InterfaceStack ir = new InterfaceStack(ref);
				list.add(ir);
				add(ir.editPanel, getStackHtml(data), true);
			}
			initialized = true;
		}

		void handleModelChangeSuccess(ModelChangeEvent event) {
			final Widget ew = event.getWidget();
			for(InterfaceStack iv : list) {
				if(ew == iv.editPanel) {
					iv.handleModelChangeSuccess(event);
					return;
				}
			}
		}

		void handleModelChangeError(ModelChangeEvent event) {
			final Widget ew = event.getWidget();
			for(InterfaceStack iv : list) {
				if(ew == iv.editPanel) {
					iv.handleModelChangeError(event);
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
		btnAddIntf.addClickListener(this);
		addWidget(btnAddIntf);
		addWidget(intfStack);
	}

	@Override
	protected ViewClass getViewClass() {
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
	public void doInitialization(ViewRequestEvent viewRequest) {
		// no-op
	}

	public void refresh() {
		intfStack.refreshData();
	}

	@Override
	protected void doDestroy() {
		intfStack.clearData();
	}

	@Override
	public ShowViewRequest newViewRequest() {
		return new StaticViewRequest(this, klas);
	}

	public void onClick(Widget sender) {
		if(sender == btnAddIntf) {
			// TODO add an interface
		}
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
