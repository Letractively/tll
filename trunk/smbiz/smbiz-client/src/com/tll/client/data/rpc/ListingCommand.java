/**
 * The Logic Lab
 * @author jpk Aug 30, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.data.IListingCommand;
import com.tll.client.data.ListingOp;
import com.tll.client.data.ListingPayload;
import com.tll.client.data.PropKey;
import com.tll.client.event.IListingListener;
import com.tll.client.event.ISourcesListingEvents;
import com.tll.client.event.type.ListingEvent;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingOperator;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.search.ISearch;
import com.tll.client.ui.listing.AbstractListingWidget;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.SortColumn;
import com.tll.listhandler.Sorting;

/**
 * ListingCommand - Issues RPC listing commands to the server.
 * @author jpk
 */
public final class ListingCommand extends RpcCommand<ListingPayload> implements IListingOperator, ISourcesListingEvents {

	private static final IListingServiceAsync svc;
	static {
		svc = (IListingServiceAsync) GWT.create(IListingService.class);
		((ServiceDefTarget) svc).setServiceEntryPoint(App.getBaseUrl() + "rpc/listing");
	}

	/**
	 * The listing event listeners.
	 */
	private final ListingListenerCollection listeners = new ListingListenerCollection();

	/**
	 * The enqueued command to go to the server.
	 */
	private IListingCommand listingCommand;

	/**
	 * Has the listing been initially generated? This flag is necessary to discern
	 * between refresh and initial display server listing requests.
	 */
	private boolean listingGenerated;

	/**
	 * This listing this operator operates on.
	 */
	private final AbstractListingWidget listingWidget;

	/**
	 * The search criteria that dictates data retrieval from the server.
	 */
	private final ISearch criteria;

	private final String listingName;
	private final ListHandlerType listHandlerType;
	private final PropKey[] props;
	private final int pageSize;
	private final Sorting sorting;

	/**
	 * Constructor
	 * @param listingWidget
	 * @param listingConfig
	 * @param listHandlerType
	 * @param criteria
	 */
	public ListingCommand(AbstractListingWidget listingWidget, final IListingConfig listingConfig,
			ListHandlerType listHandlerType, ISearch criteria) {
		this(listingWidget, listingWidget, Integer.toString(criteria.hashCode()), listHandlerType, listingConfig
				.getPropKeys(), listingConfig.getPageSize(), (listingConfig.isSortable() ? listingConfig.getDefaultSorting()
				: null), criteria);
	}

	/**
	 * Constructor
	 * @param sourcingWidget
	 * @param listingWidget
	 * @param listingName
	 * @param listHandlerType
	 * @param props
	 * @param pageSize
	 * @param sorting
	 * @param criteria
	 */
	public ListingCommand(Widget sourcingWidget, AbstractListingWidget listingWidget, String listingName,
			ListHandlerType listHandlerType, PropKey[] props, int pageSize, Sorting sorting, ISearch criteria) {
		super(sourcingWidget);
		this.listingWidget = listingWidget;
		this.listingName = listingName;
		this.listHandlerType = listHandlerType;
		this.props = props;
		this.pageSize = pageSize;
		this.sorting = sorting;
		this.criteria = criteria;
		addListingListener(listingWidget);
	}

	public void addListingListener(IListingListener listener) {
		listeners.add(listener);
	}

	public void removeListingListener(IListingListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Generate or refresh the listing.
	 * @param criteria The search criteria.
	 * @param refresh Force a listing refresh?
	 */
	public void list(ISearch criteria, boolean refresh) {
		if(criteria == null) {
			throw new IllegalStateException("No criteria specified.");
		}
		ListingOp listingOp = (!listingGenerated || refresh) ? ListingOp.REFRESH : ListingOp.DISPLAY;
		com.tll.client.data.ListingCommand lc =
				new com.tll.client.data.ListingCommand(pageSize, listHandlerType, listingName, props, listingOp, criteria);
		lc.setSorting(sorting);
		this.listingCommand = lc;
	}

	/**
	 * Issue a sort command to the server.
	 * @param sortColumn sortColumn
	 */
	public void sort(SortColumn sortColumn) {
		ListingOp listingOp = ListingOp.SORT;
		com.tll.client.data.ListingCommand lc = new com.tll.client.data.ListingCommand(listingName, listingOp);
		lc.setSorting(new Sorting(sortColumn));
		this.listingCommand = lc;
	}

	/**
	 * Navigate the listing.
	 * @param navAction
	 * @param pageNum
	 */
	public void navigate(ListingOp navAction, Integer pageNum) {
		ListingOp listingOp = navAction;
		com.tll.client.data.ListingCommand lc = new com.tll.client.data.ListingCommand(listingName, listingOp);
		lc.setPageNumber(pageNum);
		this.listingCommand = lc;
	}

	public void refresh() {
		list(criteria, true);
		execute();
	}

	public void display() {
		list(criteria, false);
		execute();
	}

	public void insertRow(int rowIndex, Model rowData) {
		throw new UnsupportedOperationException("RPC listing operators do not support row insertion.");
	}

	public void updateRow(int rowIndex, Model rowData) {
		throw new UnsupportedOperationException("RPC listing operators do not support row updating.");
	}

	/**
	 * We rely on the model change event dispatching system to subsequently update
	 * the listing.
	 */
	public void deleteRow(int rowIndex) {
		RefKey rowRef = listingWidget.getRowRef(rowIndex);
		assert rowRef != null && rowRef.isSet();

		CrudCommand cc = new CrudCommand(listingWidget);
		cc.purge(rowRef);
		cc.execute();
	}

	/**
	 * Clear the listing.
	 */
	public void clear() {
		this.listingCommand = new com.tll.client.data.ListingCommand(listingName, ListingOp.CLEAR);
	}

	@Override
	protected void doExecute() {
		if(listingCommand == null) {
			throw new IllegalStateException("No listing command set!");
		}
		svc.process(listingCommand, getAsyncCallback());
	}

	@Override
	public void handleSuccess(ListingPayload result) {
		assert listingCommand != null;
		super.handleSuccess(result);
		if(!result.hasErrors()) {
			listingGenerated = true;
		}
		final ListingOp op = listingCommand.getListingOp();
		final Sorting sorting = listingCommand.getSorting();
		listingCommand = null; // reset

		listeners.fireListingEvent(new ListingEvent(sourcingWidget, !result.hasErrors(), op, result.getPage(), sorting));
	}

}
