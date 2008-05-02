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
import com.tll.client.listing.ClientListingOp;
import com.tll.client.search.ISearch;
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

		public static final int DEFAULT_LIST_HANDLER_TYPE = IListingCommand.LIST_HANDLER_TYPE_PAGE;

		/**
		 * This name <em>must</em> be unique accross all defined
		 * IListingDefinition s in the app.
		 * @return The listing name.
		 */
		String getListingName();

		/**
		 * @return The page size or <code>-1</code> for <em>NO</em> paging.
		 */
		int getPageSize();

		/**
		 * @return The server side list handler type
		 */
		int getListHandlerType();

		/**
		 * @return The data set
		 */
		PropKey[] getPropKeys();

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
		ListingOp listingOp = new ListingOp();
		listingOp.setOp((!listingGenerated || refresh) ? ListingOp.REFRESH : ListingOp.DISPLAY);
		if(listingDef.isSortable()) {
			listingOp.setSorting(listingDef.getDefaultSorting());
		}
		IListingCommand lc =
				new com.tll.client.data.ListingCommand(listingDef.getPageSize(), listingDef.getListHandlerType(), listingDef
						.getListingName(), listingDef.getPropKeys(), listingOp, criteria);
		this.listingCommand = lc;
	}

	/**
	 * Issue a sort command to the server.
	 * @param sortColumn sortColumn
	 */
	public void sort(SortColumn sortColumn) {
		ListingOp listingOp = new ListingOp();
		listingOp.setOp(ListingOp.SORT);
		listingOp.setSorting(new Sorting(sortColumn));
		IListingCommand lc = new com.tll.client.data.ListingCommand(listingDef.getListingName(), listingOp);
		this.listingCommand = lc;
	}

	/**
	 * Navigate the listing.
	 * @param navAction
	 * @param pageNum
	 */
	public void navigate(int navAction, Integer pageNum) {
		ListingOp listingOp = new ListingOp();
		listingOp.setOp(navAction);
		listingOp.setPageNumber(pageNum);
		IListingCommand lc = new com.tll.client.data.ListingCommand(listingDef.getListingName(), listingOp);
		this.listingCommand = lc;
	}

	/**
	 * Clear the listing.
	 */
	public void clear() {
		this.listingCommand =
				new com.tll.client.data.ListingCommand(listingDef.getListingName(), new ListingOp(ListingOp.CLEAR));
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
		final int op = listingCommand.getListingOp().getOp();
		final Sorting sorting = listingCommand.getListingOp().getSorting();
		listingCommand = null; // reset

		// TODO temp
		ClientListingOp clo;
		switch(op) {
			case ListingOp.REFRESH:
			case ListingOp.DISPLAY:
				clo = ClientListingOp.REFRESH;
				break;
			case ListingOp.GOTO_PAGE:
				clo = ClientListingOp.GOTO_PAGE;
				break;
			case ListingOp.FIRST_PAGE:
				clo = ClientListingOp.FIRST_PAGE;
				break;
			case ListingOp.LAST_PAGE:
				clo = ClientListingOp.LAST_PAGE;
				break;
			case ListingOp.PREVIOUS_PAGE:
				clo = ClientListingOp.PREVIOUS_PAGE;
				break;
			case ListingOp.NEXT_PAGE:
				clo = ClientListingOp.NEXT_PAGE;
				break;
			case ListingOp.SORT:
				clo = ClientListingOp.SORT;
				break;
			case ListingOp.CLEAR:
			case ListingOp.CLEAR_ALL:
				clo = ClientListingOp.CLEAR;
				break;
			default:
				throw new IllegalStateException("TODO");
		}

		listeners.fireListingEvent(new ListingEvent(sourcingWidget, !result.hasErrors(), clo, result.getPage(), sorting));
	}
}
