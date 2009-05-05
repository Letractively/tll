/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.server.rpc.listing;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tll.SystemError;
import com.tll.common.data.ListingOp;
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.Status;
import com.tll.common.data.ListingPayload.ListingStatus;
import com.tll.common.data.rpc.IListingService;
import com.tll.common.model.Model;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.listhandler.EmptyListException;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.IListHandlerDataProvider;
import com.tll.listhandler.ListHandlerException;
import com.tll.listhandler.ListHandlerFactory;
import com.tll.listhandler.ListHandlerType;
import com.tll.model.IEntity;
import com.tll.server.rpc.RpcServlet;
import com.tll.server.rpc.entity.EntityTypeUtil;
import com.tll.server.rpc.entity.MEntityContext;
import com.tll.server.rpc.entity.MEntityServiceDelegate;

/**
 * ListingService - Handles client listing requests.
 * @param <E> the entity type
 * @param <S> the search type
 * @author jpk
 */
public final class ListingService<E extends IEntity, S extends ISearch> extends RpcServlet implements
IListingService<S, Model> {

	private static final long serialVersionUID = 7575667259462319956L;

	/**
	 * Process a listing related request
	 * @param listingRequest The listing command
	 * @return Payload contains the table page and status.
	 */
	@SuppressWarnings("unchecked")
	public ListingPayload<Model> process(final ListingRequest<S> listingRequest) {
		final Status status = new Status();

		ListingHandler<Model> handler = null;

		if(listingRequest == null) {
			status.addMsg("No listing command specified.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
		}

		final String listingName = listingRequest == null ? null : listingRequest.getListingName();
		if(listingName == null) {
			status.addMsg("No listing name specified.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
		}

		final ListingOp listingOp = listingRequest == null ? null : listingRequest.getListingOp();
		if(listingOp == null) {
			status.addMsg("No listing op specified.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
		}

		ListingStatus listingStatus = null;

		if(!status.hasErrors() && listingRequest != null) {

			final HttpServletRequest request = getRequestContext().getRequest();
			final MEntityContext context =
				(MEntityContext) getServletContext().getAttribute(MEntityContext.KEY);
			final MEntityServiceDelegate delegate =
				(MEntityServiceDelegate) getServletContext().getAttribute(MEntityServiceDelegate.KEY);
			if(context == null || delegate == null) {
				throw new IllegalStateException("Unable to obtain the mentity context and/or the mentity service delegate");
			}

			Integer offset = listingRequest.getOffset();
			Sorting sorting = listingRequest.getSorting();

			// get listing state (if cached)
			final ListingState state = ListingCache.getState(request, listingName);
			if(state != null) {
				if(log.isDebugEnabled())
					log.debug("Found cached state for listing '" + listingName + "': " + state.toString());
				if(offset == null) {
					offset = state.getOffset();
					assert offset != null;
					if(log.isDebugEnabled()) log.debug("Setting offset (" + offset + ") from cache for listing:" + listingName);
				}
				if(sorting == null) {
					sorting = state.getSorting();
					assert sorting != null;
					if(log.isDebugEnabled())
						log.debug("Setting sorting (" + sorting.toString() + ") from cache for listing:" + listingName);
				}
			}

			handler = ListingCache.getHandler(request, listingName);
			listingStatus = (handler == null ? ListingStatus.NOT_CACHED : ListingStatus.CACHED);
			if(log.isDebugEnabled()) log.debug("Listing status: " + listingStatus);

			try {
				// acquire the listing handler
				if(handler == null || listingOp == ListingOp.REFRESH) {

					if(log.isDebugEnabled()) log.debug("Generating listing handler for listing: '" + listingName + "'...");

					final RemoteListingDefinition<S> listingDef = listingRequest.getListingDef();
					if(listingDef != null) {
						final S search = listingDef.getSearchCriteria();
						if(search == null) {
							throw new ListingException(listingName, "No search criteria specified.");
						}

						// resolve the entity class and corres. marshaling entity service
						final Class<E> entityClass = (Class<E>) EntityTypeUtil.getEntityClass(search.getEntityType());

						// translate client side criteria to server side criteria
						final ICriteria<E> criteria;
						try {
							criteria = (ICriteria<E>) delegate.translate(search);
						}
						catch(final IllegalArgumentException iae) {
							throw new ListingException(listingName, "Unable to translate listing command search criteria: "
									+ listingRequest.descriptor(), iae);
						}

						// resolve the listing handler data provider
						final IListHandlerDataProvider<E> dataProvider =
							context.getEntityServiceFactory().instanceByEntityType(entityClass);

						// resolve the list handler type
						final ListHandlerType lht = listingDef.getListHandlerType();
						if(lht == null) {
							throw new ListingException(listingName, "No list handler type specified.");
						}
						// resolve the sorting to use
						sorting = (sorting == null ? listingDef.getInitialSorting() : sorting);
						if(sorting == null) {
							throw new ListingException(listingName, "No sorting directive specified.");
						}
						IListHandler<SearchResult<E>> listHandler = null;
						try {
							listHandler = ListHandlerFactory.create(criteria, sorting, lht, dataProvider);
						}
						catch(final InvalidCriteriaException e) {
							throw new ListingException(listingName, "Invalid criteria: " + e.getMessage(), e);
						}
						catch(final EmptyListException e) {
							// we proceed to allow client to still show the listing
							status.addMsg(e.getMessage(), MsgLevel.WARN, MsgAttr.STATUS.flag);
						}
						catch(final ListHandlerException e) {
							// shouldn't happen
							throw new SystemError("Unable to instantiate the list handler: " + e.getMessage(), e);
						}

						// transform to marshaling list handler
						final MarshalingListHandler<E> marshalingListHandler =
							new MarshalingListHandler<E>(listHandler, context.getMarshaler(),
									delegate.getMarshalOptions(search
											.getEntityType()),
											listingDef.getPropKeys());

						// instantiate the handler
						handler = new ListingHandler<Model>(marshalingListHandler, listingName, listingDef.getPageSize());
					}
				}

				// do the query related listing op
				if(handler != null && listingOp != null && listingOp.isQuery()) {
					if(log.isDebugEnabled())
						log.debug("Performing : '" + listingOp.getName() + "' for '" + listingName + "'...");
					try {
						handler.query(offset.intValue(), sorting, (listingOp == ListingOp.REFRESH));
						status.addMsg(listingOp.getName() + " for '" + listingName + "' successful.", MsgLevel.INFO,
								MsgAttr.STATUS.flag);
					}
					catch(final EmptyListException e) {
						throw new ListingException(listingName, "No matching rows exist.", e);
					}
					catch(final ListingException e) {
						throw new ListingException(listingName, "An unexpected error occurred performing listing operation: "
								+ e.getMessage(), e);
					}
				}
			}
			catch(final ListingException e) {
				exceptionToStatus(e, status);
				context.getExceptionHandler().handleException(e);
			}
			catch(final RuntimeException re) {
				context.getExceptionHandler().handleException(re);
				throw re;
			}

			// do caching
			if(listingOp == ListingOp.CLEAR) {
				// clear
				if(log.isDebugEnabled()) log.debug("Clearing listing '" + listingName + "'...");
				ListingCache.clearHandler(request, listingName);
				if(!listingRequest.getRetainStateOnClear()) {
					ListingCache.clearState(request, listingName);
				}
				listingStatus = ListingStatus.NOT_CACHED;
			}
			else if(listingOp == ListingOp.CLEAR_ALL) {
				// clear all
				if(log.isDebugEnabled()) log.debug("Clearing ALL listings...");
				ListingCache.clearHandler(request, listingName);
				if(!listingRequest.getRetainStateOnClear()) {
					ListingCache.clearAll(request, listingRequest.getRetainStateOnClear());
				}
				listingStatus = ListingStatus.NOT_CACHED;
			}
			else if(handler != null && !status.hasErrors()) {
				// cache listing handler
				if(log.isDebugEnabled()) log.debug("[Re-]Caching listing '" + listingName + "'...");
				ListingCache.storeHandler(request, listingName, handler);
				// cache listing state
				if(log.isDebugEnabled()) log.debug("[Re-]Caching listing state '" + listingName + "'...");
				ListingCache.storeState(request, listingName, new ListingState(handler.getOffset(), handler.getSorting()));
				listingStatus = ListingStatus.CACHED;
			}
		} // !status.hasErrors()

		final ListingPayload<Model> p = new ListingPayload<Model>(listingName, listingStatus);

		// only provide page data when it is needed at the client and there are no
		// errors
		if(handler != null && !status.hasErrors() && (listingOp != null && !listingOp.isClear())) {
			if(log.isDebugEnabled()) log.debug("Sending page data for '" + listingName + "'...");
			final List<Model> list = handler.getElements();
			p.setPageData(handler.size(), list.toArray(new Model[list.size()]), handler.getOffset(), handler.getSorting());
		}

		return p;
	}
}
