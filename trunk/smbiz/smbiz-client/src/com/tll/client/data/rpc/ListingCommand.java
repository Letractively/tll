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
	 * IListingDefinition - Listing definition that encapsulates necessary config
	 * related properties for the server to generate a listing.
	 * @author jpk
	 */
	public interface IListingDefinition {

		public static final ListHandlerType DEFAULT_LIST_HANDLER_TYPE = ListHandlerType.PAGE;

		/**
		 * This name <em>must</em> be unique accross all defined
		 * IListingDefinition s in the app.
		 * @return The listing name.
		 */
		String getListingName();

		/**
		 * @return The server side list handler type
		 */
		ListHandlerType getListHandlerType();

		/**
		 * @return The data set
		 */
		PropKey[] getPropKeys();

		/**
		 * @return The page size or <code>-1</code> for <em>NO</em> paging.
		 */
		int getPageSize();

		/**
		 * @return <code>true</code> if the listing is sortable.
		 */
		boolean isSortable();

		/**
		 * @return The default (initial) Sorting directive.
		 */
		Sorting getDefaultSorting();
	}

	/**
	 * The necessary listing definition for server side list handling
	 * configuration
	 */
	private final IListingDefinition listingDef;

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
	 * Constructor
	 * @param sourcingWidget
	 * @param listingDef
	 */
	public ListingCommand(Widget sourcingWidget, IListingDefinition listingDef) {
		super(sourcingWidget);
		if(listingDef == null) {
			throw new IllegalArgumentException("No listing definition specified.");
		}
		this.listingDef = listingDef;
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
				new com.tll.client.data.ListingCommand(listingDef.getPageSize(), listingDef.getListHandlerType(), listingDef
						.getListingName(), listingDef.getPropKeys(), listingOp, criteria);
		if(listingDef.isSortable()) {
			lc.setSorting(listingDef.getDefaultSorting());
		}
		this.listingCommand = lc;
	}

	/**
	 * Issue a sort command to the server.
	 * @param sortColumn sortColumn
	 */
	public void sort(SortColumn sortColumn) {
		ListingOp listingOp = ListingOp.SORT;
		com.tll.client.data.ListingCommand lc =
				new com.tll.client.data.ListingCommand(listingDef.getListingName(), listingOp);
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
		com.tll.client.data.ListingCommand lc =
				new com.tll.client.data.ListingCommand(listingDef.getListingName(), listingOp);
		lc.setPageNumber(pageNum);
		this.listingCommand = lc;
	}

	/**
	 * Clear the listing.
	 */
	public void clear() {
		this.listingCommand = new com.tll.client.data.ListingCommand(listingDef.getListingName(), ListingOp.CLEAR);
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
