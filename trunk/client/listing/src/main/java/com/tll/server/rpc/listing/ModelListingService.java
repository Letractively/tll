/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.server.rpc.listing;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.tll.common.data.rpc.IListingService;
import com.tll.common.data.rpc.ListingPayload;
import com.tll.common.data.rpc.ListingRequest;
import com.tll.common.model.Model;
import com.tll.server.rpc.RpcServlet;
import com.tll.util.StringUtil;

/**
 * Handles client listing requests.
 * @author jpk
 */
public final class ModelListingService extends RpcServlet implements IListingService<Model> {

	private static final long serialVersionUID = 7575667259462319956L;

	private final MarshalListingProcessor processor;

	/**
	 * Constructor
	 * @param processor
	 */
	@Inject
	public ModelListingService(MarshalListingProcessor processor) {
		super();
		this.processor = processor;
	}

	/**
	 * Process a listing related request
	 * @param listingRequest The listing command
	 * @return Payload contains the table page and status.
	 */
	public ListingPayload<Model> process(final ListingRequest listingRequest) {
		final HttpServletRequest request = getRequestContext().getRequest();
		final String sessionId = request.getSession(false) == null ? null : request.getSession(false).getId();
		if(StringUtil.isEmpty(sessionId)) throw new IllegalStateException("Unable to obtain a valid session id");
		final ListingContext listingContext = (ListingContext) getServletContext().getAttribute(ListingContext.KEY);
		if(listingContext == null) throw new IllegalStateException("Unable to obtain the listing context");
		return processor.process(sessionId, listingContext, listingRequest);
	}
}
