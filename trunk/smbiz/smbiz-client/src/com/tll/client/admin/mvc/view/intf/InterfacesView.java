/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.mvc.view.intf;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.ui.field.InterfacePanel;
import com.tll.client.data.PropKey;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.data.rpc.ListingCommand;
import com.tll.client.data.rpc.ListingCommand.IListingDefinition;
import com.tll.client.event.ICrudListener;
import com.tll.client.event.IListingListener;
import com.tll.client.event.type.CrudEvent;
import com.tll.client.event.type.ListingEvent;
import com.tll.client.event.type.ShowViewRequest;
import com.tll.client.event.type.StaticViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.search.impl.InterfaceSearch;
import com.tll.client.ui.CSS;
import com.tll.client.ui.field.EditPanel;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.SelectNamedQuery;
import com.tll.listhandler.IPage;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.Sorting;

/**
 * InterfacesView - AbstractView for managing Interfaces and the sub-entities.
 * @author jpk
 */
public class InterfacesView extends AbstractView {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private Class() {
			super("interfacesView");
		}

		@Override
		public AbstractView newView() {
			return new InterfacesView();
		}

	}

	private static final InterfaceSearch criteria = new InterfaceSearch(CriteriaType.SCALAR_NAMED_QUERY);

	private static final IListingDefinition listingDef;

	static {
		criteria.setNamedQuery(SelectNamedQuery.INTERFACE_SUMMARY_LISTING);

		listingDef = new IListingDefinition() {

			public String getListingName() {
				return criteria.getNamedQuery().getQueryName();
			}

			public boolean isSortable() {
				return false;
			}

			public PropKey[] getPropKeys() {
				return null;
			}

			public int getPageSize() {
				return 25;
			}

			public ListHandlerType getListHandlerType() {
				return ListHandlerType.COLLECTION;
			}

			public Sorting getDefaultSorting() {
				return null;
			}

		};
	}

	/**
	 * InterfacesStack - Extended {@link StackPanel} tailored for on demand
	 * loading of stack {@link Widget}s.
	 * @author jpk
	 */
	private static final class InterfacesStack extends StackPanel implements IListingListener {

		/**
		 * InterfaceStack - Binding between a stack index and an {@link EditPanel}
		 * assigned to manage the edit for the asociated interface.
		 * @author jpk
		 */
		private final class InterfaceStack {

			private final int stackIndex;
			private final RefKey intfRef;
			private boolean loaded;
			private final EditPanel editPanel;

			/**
			 * Constructor
			 * @param stackIndex
			 * @param intfRef
			 */
			public InterfaceStack(int stackIndex, RefKey intfRef) {
				super();
				this.stackIndex = stackIndex;
				this.intfRef = intfRef;

				InterfacePanel pnlIntf = new InterfacePanel(null);
				editPanel = new EditPanel(pnlIntf, null, false);
			}

			public void loadInterfaceIfNecessary() {
				if(!loaded) {
					// fetch the interface from the server
					CrudCommand cmd = new CrudCommand(InterfacesStack.this);
					cmd.addCrudListener(new ICrudListener() {

						public void onCrudEvent(CrudEvent event) {
							assert event.getSource() == InterfacesStack.this;
							if(!event.getPayload().hasErrors()) {
								loaded = true;
								editPanel.setEntity(event.getPayload().getEntity());
								editPanel.refresh();
								// open er up
								showStack(stackIndex);
							}
						}

					});
					cmd.load(intfRef);
					cmd.execute();
				}
			}

		}// InterfaceStack

		private final ListingCommand listingCommand;

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
			listingCommand = new ListingCommand(this, listingDef);
			listingCommand.addListingListener(this);
		}

		void refreshData() {
			listingCommand.list(criteria, true);
			listingCommand.execute();
		}

		void clearData() {
			listingCommand.clear();
			listingCommand.execute();
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
			// String modified = PV.f(model, "dateModified");
			return "<p class=\"" + CSS.FLOAT_LEFT + " " + CSS.BOLD + "\">" + name + "</p><p class=\"" + CSS.SMALL_ITALIC
					+ " " + CSS.FLOAT_RIGHT + "\">" + desc + "</p>";
		}

		/**
		 * Populates the stack panel with the returned listing results.
		 * @param page The data elements
		 */
		private void setData(IPage<Model> page) {
			// reset stack panel
			clear();

			list.clear();
			if(page == null || page.getNumPageElements() < 1) return;

			List<Model> dataList = page.getPageElements();
			for(int i = 0; i < dataList.size(); i++) {
				Model data = dataList.get(i);
				RefKey ref = data.getRefKey();
				assert ref != null && ref.isSet();
				InterfaceStack ir = new InterfaceStack(i, ref);
				list.add(ir);
				add(ir.editPanel, getStackHtml(data), true);
			}
			showStack(-1); // hide all of them upon load
			initialized = true;
		}

		@Override
		public void showStack(int index) {
			if(initialized) {
				InterfaceStack ir = list.get(index);
				ir.loadInterfaceIfNecessary();
			}
			super.showStack(index);
		}

		public void onListingEvent(ListingEvent event) {
			setData(event.getPage());
		}
	}

	private final InterfacesStack intfStack = new InterfacesStack();

	/**
	 * Constructor
	 */
	public InterfacesView() {
		super();
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

	@Override
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

}
