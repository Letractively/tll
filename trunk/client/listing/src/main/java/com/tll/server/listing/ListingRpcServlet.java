/**
 * The Logic Lab
 * @author jpk
 * @since Dec 29, 2010
 */
package com.tll.server.listing;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tll.IMarshalable;
import com.tll.common.data.rpc.IListingService;
import com.tll.common.data.rpc.ListingPayload;
import com.tll.common.data.rpc.ListingRequest;
import com.tll.server.rpc.RpcServlet;
import com.tll.util.StringUtil;

/**
 * @author jpk
 */
@Singleton
public class ListingRpcServlet extends RpcServlet implements IListingService {

	private static final long serialVersionUID = 1533613186004458894L;

	private final ListingContext context;
	private final ListingProcessor processor;

	/**
	 * Constructor
	 * @param context
	 * @param processor
	 */
	@Inject
	public ListingRpcServlet(ListingContext context, ListingProcessor processor) {
		super();
		this.context = context;
		this.processor = processor;
	}

	@Override
	public <S extends IMarshalable, R extends IMarshalable> ListingPayload<R> process(
			final ListingRequest<S> listingRequest) {
		final HttpServletRequest request = getRequestContext().getRequest();
		final String sessionId = request.getSession(false) == null ? null : request.getSession(false).getId();
		if(StringUtil.isEmpty(sessionId)) throw new IllegalStateException("Unable to obtain a valid session id");
		return processor.process(sessionId, context, listingRequest);
	}
}
