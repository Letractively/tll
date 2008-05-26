/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.server.rpc.listing;

import javax.servlet.http.HttpServletRequest;

import com.tll.SystemError;
import com.tll.client.data.IListingCommand;
import com.tll.client.data.ListingOp;
import com.tll.client.data.ListingPayload;
import com.tll.client.data.Status;
import com.tll.client.data.rpc.IListingService;
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
import com.tll.listhandler.SearchResult;
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
public final class ListingService<E extends IEntity> extends RpcServlet implements IListingService {

	private static final long serialVersionUID = 7575667259462319956L;

	/**
	 * Process a listing related request
	 * @param listingCommand The listing command
	 * @return Payload contains the table page and status.
	 */
	@SuppressWarnings("unchecked")
	public ListingPayload process(final IListingCommand listingCommand) {
		final ListingPayload p = new ListingPayload();
		final Status status = p.getStatus();
		assert status != null;

		IListingHandler handler = null;

		if(listingCommand == null) {
			status.addMsg("No listing command specified.", MsgLevel.ERROR);
		}

		final String listingName = listingCommand == null ? null : listingCommand.getListingName();
		if(listingName == null) {
			status.addMsg("No listing name specified.", MsgLevel.ERROR);
		}

		final ListingOp listingOp = listingCommand == null ? null : listingCommand.getListingOp();
		if(listingOp == null) {
			status.addMsg("No listing op specified.", MsgLevel.ERROR);
		}

		Integer pageNum = listingCommand.getPageNumber();

		if(!status.hasErrors()) {

			final RequestContext requestContext = getRequestContext();
			final HttpServletRequest request = getRequestContext().getRequest();
			assert request != null;

			handler = ListingCache.getHandler(request, listingName);

			final ListingState state = ListingCache.getState(request, listingName);
			boolean pageNumWasSynced = false;

			try {
				// acquire the listing handler
				if(handler == null || listingOp == ListingOp.REFRESH) {

					if(log.isDebugEnabled()) log.debug("Generating listing handler for listing: '" + listingName + "'...");

					// get the client side criteria
					final ISearch search = listingCommand.getSearchCriteria();
					if(search == null) {
						throw new ListingException(listingName, "No search criteria specified.");
					}

					// resolve the entity class and corres. marshaling entity service
					final Class<E> entityClass = EntityUtil.entityClassFromType(search.getEntityType());
					final IMEntityServiceImpl<E, ISearch> mEntitySvc =
							(IMEntityServiceImpl<E, ISearch>) MEntityServiceImplFactory.instance(entityClass);

					// translate client side criteria to server side criteria
					final ICriteria<? extends E> criteria;
					try {
						criteria = mEntitySvc.translate(requestContext, EntityUtil.entityTypeFromClass(entityClass), search);
					}
					catch(final IllegalArgumentException iae) {
						throw new ListingException(listingName, "Unable to translate listing command search criteria: "
								+ listingCommand.descriptor(), iae);
					}

					// resolve the listing halder data provider
					final IListHandlerDataProvider<E> dataProvider =
							requestContext.getEntityServiceFactory().instanceByEntityType(entityClass);

					// resolve the list handler type
					final ListHandlerType lht = listingCommand.getListHandlerType();
					if(lht == null) {
						throw new ListingException(listingName, "No list handler type specified.");
					}
					IListHandler<SearchResult<E>> listHandler = null;
					try {
						listHandler =
								ListHandlerFactory.create(criteria, listingCommand.getSorting(), lht, listingCommand.getPageSize(),
										dataProvider);
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
							mEntitySvc.getMarshalingListHandler(requestContext, listingCommand);
					marshalingListHandler.setWrappedHandler(listHandler);

					// instantiate the handler
					handler =
							new ModelListingHandler(marshalingListHandler, listingName, listingCommand.isPageable(), listingCommand
									.getPageSize());

					// sync up with cached state (if present)
					if(listingOp != ListingOp.REFRESH && state != null) {
						if(log.isDebugEnabled())
							log.debug("Found cached state for listing '" + listingName + "': " + state.toString());

						// sorting
						if(!(listingOp == ListingOp.SORT) && state.getSorting() != null) {
							if(log.isDebugEnabled())
								log.debug("Setting listing sorting from cached state for '" + listingName + "'...");
							try {
								handler.sort(state.getSorting());
							}
							catch(final ListHandlerException lhe) {
								throw new ListingException(listingName, "An unexpected sorting error occurred: " + lhe.getMessage(),
										lhe);
							}
						}

						// page num
						if(state.getPageNumber() != null) {
							if(log.isDebugEnabled())
								log.debug("Setting page num from cached state for listing '" + listingName + "'...");
							// listingOp.setPageNumber(state.getPageNumber());
							pageNum = state.getPageNumber();
							pageNumWasSynced = true;
						}
					}

				}

				// do the listing op
				if(log.isDebugEnabled()) log.debug("Performing listing op for '" + listingName + "'...");
				try {
					switch(listingOp) {

						// refresh/display
						case REFRESH:
						case DISPLAY: {
							int cp;

							// first, try to get page num from the context
							if(pageNum != null) {
								cp = pageNum.intValue();
								if(log.isDebugEnabled()) log.debug("Retrieved page (" + cp + ") from listing op.");
								if(cp >= handler.getNumPages()) cp = handler.getNumPages() - 1;
							}
							else {
								cp = 0;
							}
							setCurrentPage(cp, pageNumWasSynced, handler, status);
							break;
						}

							// page nav
						case GOTO_PAGE:
							if(pageNum == null)
								throw new ListingException(listingName, "Unable to goto page: Page Number not specified.");
							setCurrentPage(pageNum.intValue(), pageNumWasSynced, handler, status);
							break;

						case FIRST_PAGE:
							handler.setCurrentPage(0, false);
							break;

						case LAST_PAGE:
							handler.setCurrentPage(handler.getNumPages() - 1, false);
							break;

						case PREVIOUS_PAGE: {
							int pn = handler.getPageNumber() - 1;
							if(pn < 0) pn = 0;
							handler.setCurrentPage(pn, false);
							break;
						}

						case NEXT_PAGE: {
							int pn = handler.getPageNumber() + 1;
							if(pn >= handler.getNumPages()) pn = handler.getNumPages() - 1;
							handler.setCurrentPage(pn, false);
							break;
						}

							// sort
						case SORT: {
							try {
								handler.sort(listingCommand.getSorting());
							}
							catch(final EmptyListException ele) {
								throw ele;
							}
							catch(final ListHandlerException lhe) {
								throw new ListingException(listingName, "Unable to sort listing: " + lhe.getMessage(), lhe);
							}
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
				if(!listingCommand.getRetainStateOnClear()) {
					ListingCache.clearState(request, listingName);
				}
			}
			else if(listingOp == ListingOp.CLEAR_ALL) {
				// clear all
				if(log.isDebugEnabled()) log.debug("Clearing ALL listings...");
				ListingCache.clearHandler(request, listingName);
				if(!listingCommand.getRetainStateOnClear()) {
					ListingCache.clearAll(request, listingCommand.getRetainStateOnClear());
				}
			}
			else {
				if(log.isDebugEnabled()) log.debug("[Re-]Caching listing handler '" + listingName + "'...");
				ListingCache.storeHandler(request, listingName, handler);
				if(handler != null) {
					if(log.isDebugEnabled()) log.debug("[Re-]Caching listing state '" + listingName + "'...");
					ListingCache.storeState(request, listingName, handler.getState());
				}
			}
		}

		// only generate the table page when it is needed at the client
		if(handler != null && ListingOp.CLEAR != listingOp && ListingOp.CLEAR_ALL != listingOp) {
			if(log.isDebugEnabled()) log.debug("Setting generated mpage for '" + listingName + "'...");
			p.setPage(handler.getPage());
		}

		return p;
	}

	/**
	 * Sets the current page adjusting the requested page number if necessary
	 * @param rpn The requested 0-based page number
	 * @param adjustPageNum Adjust the page number of the one given is out of
	 *        bounds?
	 * @param handler
	 * @param status
	 * @throws PageNumOutOfBoundsException
	 * @throws EmptyListException
	 * @throws ListingException
	 */
	private void setCurrentPage(final int rpn, final boolean adjustPageNum, final IListingHandler handler,
			final Status status) throws PageNumOutOfBoundsException, EmptyListException, ListingException {
		final int apn = handler.setCurrentPage(rpn, adjustPageNum);
		if(rpn != apn) {
			status.addMsg("The requested page number: " + rpn + " was adjusted to: " + apn, MsgLevel.INFO);
		}
	}
}
