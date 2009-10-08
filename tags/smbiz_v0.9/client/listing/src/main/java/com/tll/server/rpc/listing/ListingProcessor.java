/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.server.rpc.listing;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.common.data.ListingOp;
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.Status;
import com.tll.common.data.ListingPayload.ListingStatus;
import com.tll.common.model.Model;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.IListingSearch;
import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.listhandler.EmptyListException;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.IListingDataProvider;
import com.tll.listhandler.ListHandlerException;
import com.tll.listhandler.ListHandlerFactory;
import com.tll.listhandler.ListHandlerType;
import com.tll.model.IEntity;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.RpcServlet;

/**
 * ListingProcessor - Handles listing requests
 * @author jpk
 */
final class ListingProcessor {

	private final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Processes all listing requests.
	 * @param sessionId the unique user session id
	 * @param context the listing context
	 * @param request the listing request
	 * @return the resultant listing payload
	 */
	ListingPayload<Model> process(final String sessionId, final ListingContext context,
			final ListingRequest request) {

		if(context == null) throw new IllegalStateException("Null listing context");

		final Status status = new Status();

		if(request == null) {
			status.addMsg("No listing request specified.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
		}

		final String listingId = request == null ? null : request.getListingId();
		if(listingId == null) {
			status.addMsg("No listing name specified.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
		}

		final ListingOp listingOp = request == null ? null : request.getListingOp();
		if(listingOp == null) {
			status.addMsg("No listing op specified.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
		}

		ListingHandler<Model> handler = null;
		ListingStatus listingStatus = null;

		if(!status.hasErrors() && request != null) {
			if(sessionId == null) {
				status.addMsg("No session id specified.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
			}
			else {
				Integer offset = request.getOffset();
				Sorting sorting = request.getSorting();

				// get listing state (if cached)
				final ListingState state = ListingCache.getState(sessionId, listingId);
				if(state != null) {
					if(log.isDebugEnabled())
						log.debug("Found cached state for listing '" + listingId + "': " + state.toString());
					if(offset == null) {
						offset = state.getOffset();
						assert offset != null;
						if(log.isDebugEnabled())
							log.debug("Setting offset (" + offset + ") from cache for listing:" + listingId);
					}
					if(sorting == null) {
						sorting = state.getSorting();
						assert sorting != null;
						if(log.isDebugEnabled())
							log.debug("Setting sorting (" + sorting.toString() + ") from cache for listing:" + listingId);
					}
				}

				handler = ListingCache.getHandler(sessionId, listingId);
				listingStatus = (handler == null ? ListingStatus.NOT_CACHED : ListingStatus.CACHED);
				if(log.isDebugEnabled()) log.debug("Listing status: " + listingStatus);

				try {
					// acquire the listing handler
					if(handler == null || listingOp == ListingOp.REFRESH) {

						if(log.isDebugEnabled()) log.debug("Generating listing handler for listing: '" + listingId + "'...");

						final RemoteListingDefinition<? extends IListingSearch> listingDef = request.getListingDef();
						if(listingDef != null) {
							final IListingSearch search = listingDef.getSearchCriteria();
							if(search == null) {
								throw new ListingException(listingId, "No search criteria specified.");
							}

							// translate client side criteria to server side criteria
							final Criteria<? extends IEntity> criteria;
							try {
								// delegate
								criteria = context.getSearchTranslator().translateListingSearchCriteria(context, search);
							}
							catch(final IllegalArgumentException iae) {
								throw new ListingException(listingId, "Unable to translate listing search criteria: "
										+ request.descriptor(), iae);
							}

							// resolve the listing handler data provider
							final IListingDataProvider dataProvider = context.getListingDataProviderResolver().resolve(request);

							// resolve the list handler type
							final ListHandlerType lht = listingDef.getListHandlerType();
							if(lht == null) {
								throw new ListingException(listingId, "No list handler type specified.");
							}
							// resolve the sorting to use
							sorting = (sorting == null ? listingDef.getInitialSorting() : sorting);
							if(sorting == null) {
								throw new ListingException(listingId, "No sorting directive specified.");
							}
							IListHandler<SearchResult<?>> listHandler = null;
							try {
								listHandler = ListHandlerFactory.create(criteria, sorting, lht, dataProvider);
							}
							catch(final InvalidCriteriaException e) {
								throw new ListingException(listingId, "Invalid criteria: " + e.getMessage(), e);
							}
							catch(final EmptyListException e) {
								// we proceed to allow client to still show the listing
								status.addMsg(e.getMessage(), MsgLevel.WARN, MsgAttr.STATUS.flag);
							}
							catch(final ListHandlerException e) {
								// shouldn't happen
								throw new IllegalStateException("Unable to instantiate the list handler: " + e.getMessage(), e);
							}

							// transform to marshaling list handler
							MarshalOptions mo;
							try {
								mo = context.getMarshalOptionsResolver().resolve(search.getEntityType());
							}
							catch(final IllegalArgumentException e) {
								mo = MarshalOptions.NO_REFERENCES;
							}
							final MarshalingListHandler marshalingListHandler =
								new MarshalingListHandler(listHandler, context.getMarshaler(), mo, listingDef.getPropKeys());

							// instantiate the handler
							handler = new ListingHandler<Model>(marshalingListHandler, listingId, listingDef.getPageSize());
						}
					}

					// do the query related listing op
					if(handler != null && listingOp != null && listingOp.isQuery()) {
						if(log.isDebugEnabled())
							log.debug("Performing : '" + listingOp.getName() + "' for '" + listingId + "'...");
						try {
							handler.query(offset.intValue(), sorting, (listingOp == ListingOp.REFRESH));
							status.addMsg(listingOp.getName() + " for '" + listingId + "' successful.", MsgLevel.INFO,
									MsgAttr.STATUS.flag);
						}
						catch(final EmptyListException e) {
							// we proceed to allow client to still show the listing
							status.addMsg(e.getMessage(), MsgLevel.WARN, MsgAttr.STATUS.flag);
						}
						catch(final ListingException e) {
							throw new ListingException(listingId, "An unexpected error occurred performing listing operation: "
									+ e.getMessage(), e);
						}
					}
				}
				catch(final ListingException e) {
					RpcServlet.exceptionToStatus(e, status);
					context.getExceptionHandler().handleException(e);
				}
				catch(final RuntimeException re) {
					context.getExceptionHandler().handleException(re);
					throw re;
				}

				// do caching
				if(listingOp == ListingOp.CLEAR) {
					// clear
					if(log.isDebugEnabled()) log.debug("Clearing listing '" + listingId + "'...");
					ListingCache.clearHandler(sessionId, listingId);
					if(!request.getRetainStateOnClear()) {
						ListingCache.clearState(sessionId, listingId);
					}
					listingStatus = ListingStatus.NOT_CACHED;
					status.addMsg("Cleared listing data for " + listingId, MsgLevel.INFO, MsgAttr.STATUS.flag);
				}
				else if(listingOp == ListingOp.CLEAR_ALL) {
					// clear all
					if(log.isDebugEnabled()) log.debug("Clearing ALL listings...");
					ListingCache.clearHandler(sessionId, listingId);
					if(!request.getRetainStateOnClear()) {
						ListingCache.clearAll(request.getRetainStateOnClear());
					}
					listingStatus = ListingStatus.NOT_CACHED;
					status.addMsg("Cleared ALL listing data", MsgLevel.INFO, MsgAttr.STATUS.flag);
				}
				else if(handler != null && !status.hasErrors()) {
					// cache listing handler
					if(log.isDebugEnabled()) log.debug("[Re-]Caching listing '" + listingId + "'...");
					ListingCache.storeHandler(sessionId, listingId, handler);
					// cache listing state
					if(log.isDebugEnabled()) log.debug("[Re-]Caching listing state '" + listingId + "'...");
					ListingCache.storeState(sessionId, listingId, new ListingState(handler.getOffset(), handler.getSorting()));
					listingStatus = ListingStatus.CACHED;
				}
			}
		} // !status.hasErrors()

		final ListingPayload<Model> p = new ListingPayload<Model>(status, listingId, listingStatus);

		// only provide page data when it is needed at the client and there are no
		// errors
		if(handler != null && !status.hasErrors() && (listingOp != null && !listingOp.isClear())) {
			if(log.isDebugEnabled()) log.debug("Sending page data for '" + listingId + "'...");
			final List<Model> list = handler.getElements();
			final Model[] arr = list == null ? new Model[0] : list.toArray(new Model[list.size()]);
			p.setPageData(handler.size(), arr, handler.getOffset(), handler.getSorting());
		}

		return p;
	}
}
