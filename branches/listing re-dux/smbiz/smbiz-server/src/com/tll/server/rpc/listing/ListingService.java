/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.server.rpc.listing;

import javax.servlet.http.HttpServletRequest;

import com.tll.SystemError;
import com.tll.client.data.ListingOp;
import com.tll.client.data.ListingPayload;
import com.tll.client.data.ListingRequest;
import com.tll.client.data.Status;
import com.tll.client.data.rpc.IListingService;
import com.tll.client.model.Model;
import com.tll.client.msg.Msg.MsgAttr;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.listhandler.EmptyListException;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.IListHandlerDataProvider;
import com.tll.listhandler.ListHandlerException;
import com.tll.listhandler.ListHandlerFactory;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.Page;
import com.tll.listhandler.SearchResult;
import com.tll.listhandler.Sorting;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.server.RequestContext;
import com.tll.server.ServletUtil;
import com.tll.server.rpc.RpcServlet;
import com.tll.server.rpc.entity.IMEntityServiceImpl;
import com.tll.server.rpc.entity.MEntityServiceImplFactory;

/**
 * ListingService - Handles client listing requests.
 * @author jpk
 */
public final class ListingService<E extends IEntity, S extends ISearch> extends RpcServlet implements IListingService<S> {

	private static final long serialVersionUID = 7575667259462319956L;

	/**
	 * Process a listing related request
	 * @param listingRequest The listing command
	 * @return Payload contains the table page and status.
	 */
	@SuppressWarnings("unchecked")
	public ListingPayload process(final ListingRequest<S> listingRequest) {
		final ListingPayload p = new ListingPayload();
		final Status status = p.getStatus();
		assert status != null;

		IListingHandler<Model> handler = null;

		if(listingRequest == null) {
			status.addMsg("No listing command specified.", MsgLevel.ERROR);
		}

		final String listingName = listingRequest == null ? null : listingRequest.getListingName();
		if(listingName == null) {
			status.addMsg("No listing name specified.", MsgLevel.ERROR);
		}

		final ListingOp listingOp = listingRequest == null ? null : listingRequest.getListingOp();
		if(listingOp == null) {
			status.addMsg("No listing op specified.", MsgLevel.ERROR);
		}

		if(!status.hasErrors()) {

			final RequestContext requestContext = getRequestContext();
			final HttpServletRequest request = getRequestContext().getRequest();
			assert request != null;

			Integer offset = listingRequest.getOffset();
			Sorting sorting = listingRequest.getSorting();

			handler = ListingCache.getHandler(request, listingName);

			// get listing state (if cached)
			final ListingState state = ListingCache.getState(request, listingName);

			if(state != null) {
				if(log.isDebugEnabled())
					log.debug("Found cached state for listing '" + listingName + "': " + state.toString());
			}

			if(offset == null) {
				offset = state.getOffset();
				if(log.isDebugEnabled()) log.debug("Setting offset (" + state + ") from cache for listing:" + listingName);
			}

			if(sorting == null && state != null) {
				sorting = state.getSorting();
				if(log.isDebugEnabled())
					log.debug("Setting sorting (" + sorting.toString() + ") from cache for listing:" + listingName);
			}

			try {
				// acquire the listing handler
				if(handler == null || listingOp == ListingOp.REFRESH) {

					if(log.isDebugEnabled()) log.debug("Generating listing handler for listing: '" + listingName + "'...");

					// get the client side criteria
					final S search = listingRequest.getSearchCriteria();
					if(search == null) {
						throw new ListingException(listingName, "No search criteria specified.");
					}

					// resolve the entity class and corres. marshaling entity service
					final Class<E> entityClass = EntityUtil.entityClassFromType(search.getEntityType());
					final IMEntityServiceImpl<E, S> mEntitySvc =
							(IMEntityServiceImpl<E, S>) MEntityServiceImplFactory.instance(entityClass);

					// translate client side criteria to server side criteria
					final ICriteria<? extends E> criteria;
					try {
						criteria = mEntitySvc.translate(requestContext, EntityUtil.entityTypeFromClass(entityClass), search);
					}
					catch(final IllegalArgumentException iae) {
						throw new ListingException(listingName, "Unable to translate listing command search criteria: "
								+ listingRequest.descriptor(), iae);
					}

					// resolve the listing halder data provider
					final IListHandlerDataProvider<E> dataProvider =
							requestContext.getEntityServiceFactory().instanceByEntityType(entityClass);

					// resolve the list handler type
					final ListHandlerType lht = listingRequest.getListHandlerType();
					if(lht == null) {
						throw new ListingException(listingName, "No list handler type specified.");
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
						status.addMsg(e.getMessage(), MsgLevel.WARN);
					}
					catch(final ListHandlerException e) {
						// shouldn't happen
						throw new SystemError("Unable to instantiate the list handler: " + e.getMessage(), e);
					}

					// transform to marshaling list handler
					final IMarshalingListHandler<E> marshalingListHandler =
							mEntitySvc.getMarshalingListHandler(requestContext, listingRequest);
					marshalingListHandler.setWrappedHandler(listHandler);

					// instantiate the handler
					handler = new ListingHandler(marshalingListHandler, listingName, listingRequest.getPageSize());
				}

				// do the listing op
				if(log.isDebugEnabled()) log.debug("Performing listing op for '" + listingName + "'...");
				try {
					switch(listingOp) {

						// refresh/fetch
						case REFRESH:
						case FETCH: {
							handler.query(offset.intValue(), sorting, listingOp.isForce());
							break;
						}

							// clear
						case CLEAR:
						case CLEAR_ALL:
							// no-op - handled in the cache update phase
							break;

						// unhandled op
						default:
							throw new ListingException(listingName, "Unhandled listing op: " + listingOp.getName());

					}// switch

					status.addMsg(listingOp.getName() + " for '" + listingName + "' successful.", MsgLevel.INFO,
							MsgAttr.NODISPLAY.flag);
				}
				catch(final EmptyListException e) {
					throw new ListingException(listingName, "No matching rows exist.", e);
				}
				catch(final ListingException e) {
					throw new ListingException(listingName, "An unexpected error occurred performing listing operation: "
							+ e.getMessage(), e);
				}
			}
			catch(final ListingException e) {
				ServletUtil.handleException(requestContext, p, e, null, false);
			}
			catch(final RuntimeException re) {
				ServletUtil.handleException(requestContext, p, re, null, true);
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
			}
			else if(listingOp == ListingOp.CLEAR_ALL) {
				// clear all
				if(log.isDebugEnabled()) log.debug("Clearing ALL listings...");
				ListingCache.clearHandler(request, listingName);
				if(!listingRequest.getRetainStateOnClear()) {
					ListingCache.clearAll(request, listingRequest.getRetainStateOnClear());
				}
			}
			else {
				// cache listing handler
				if(handler == null) {
					if(log.isDebugEnabled()) log.debug("Clearing listing '" + listingName + "'...");
					ListingCache.clearHandler(request, listingName);
				}
				else {
					if(log.isDebugEnabled()) log.debug("[Re-]Caching listing '" + listingName + "'...");
					ListingCache.storeHandler(request, listingName, handler);
				}
				// cache listing state if handler is non-null
				if(handler != null) {
					if(log.isDebugEnabled()) log.debug("[Re-]Caching listing state '" + listingName + "'...");
					ListingCache.storeState(request, listingName, new ListingState(handler.getOffset(), handler.getSorting()));
				}
			}
		}// !status.hasErrors()

		// only generate the table page when it is needed at the client
		if(handler != null && !listingOp.isClear()) {
			if(log.isDebugEnabled()) log.debug("Sending page for '" + listingName + "'...");
			p.setPage(new Page<Model>(handler.getPageSize(), handler.size(), handler.getElements(), handler.getOffset()));
		}

		return p;
	}
}
