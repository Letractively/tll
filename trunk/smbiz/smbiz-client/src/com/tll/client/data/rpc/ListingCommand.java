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
import com.tll.client.search.ISearch;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.SortColumn;
import com.tll.listhandler.Sorting;

/**
 * ListingCommand - Issues RPC listing commands to the server.
 * @author jpk
 */
public final class ListingCommand extends RpcCommand<ListingPayload> implements ISourcesListingEvents {

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
	 * The unique name that identifies the listing this command targets on the
	 * server.
	 */
	private final String listingName;

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
	 * Constructor
	 * @param sourcingWidget
	 * @param listingName
	 */
	public ListingCommand(Widget sourcingWidget, String listingName) {
		super(sourcingWidget);
		this.listingName = listingName;
	}

	/**
	 * @return The unique server-side name of targeted listing.
	 */
	public String getListingName() {
		return listingName;
	}

	public void addListingListener(IListingListener listener) {
		listeners.add(listener);
	}

	public void removeListingListener(IListingListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Generate or refresh the listing.
	 * @param listHandlerType
	 * @param pageSize
	 * @param props
	 * @param searchCriteria The search criteria
	 * @param sorting The sorting directive
	 * @param refresh Force a listing refresh if listing data is found cached on
	 *        the server?
	 */
	public void list(ListHandlerType listHandlerType, int pageSize, PropKey[] props, ISearch searchCriteria,
			Sorting sorting, boolean refresh) {
		if(searchCriteria == null) {
			throw new IllegalStateException("No criteria specified.");
		}
		ListingOp listingOp = (!listingGenerated || refresh) ? ListingOp.REFRESH : ListingOp.DISPLAY;
		com.tll.client.data.ListingCommand lc =
				new com.tll.client.data.ListingCommand(listingName, listHandlerType, props, pageSize, searchCriteria, listingOp);
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

	/**
	 * Clear the listing.
	 */
	public void clear() {
		listingCommand = new com.tll.client.data.ListingCommand(listingName, ListingOp.CLEAR);
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
		final ListingOp op = listingCommand.getListingOp();
		if(!result.hasErrors() && !op.isClear()) {
			listingGenerated = true;
		}
		final Sorting sorting = listingCommand.getSorting();
		listingCommand = null; // reset

		listeners.fireListingEvent(new ListingEvent(sourcingWidget, listingName, !result.hasErrors(), op, result.getPage(),
				sorting));
	}

}
