/**
 * The Logic Lab
 * @author jpk
 * @since Mar 21, 2010
 */
package com.tll.server.rpc.listing;

import com.google.inject.Inject;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.search.IListingSearch;
import com.tll.dao.SearchResult;
import com.tll.listhandler.IListHandler;
import com.tll.server.marshal.IMarshalOptionsResolver;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;

/**
 * Lising processor that provides marshaled {@link Model} instances for row
 * data.
 * @author jpk
 */
public class MarshalListingProcessor extends AbstractListingProcessor<Model> {
	
	private final IMarshalOptionsResolver mor;
	
	private final Marshaler marshaler;
	
	/**
	 * Constructor
	 * @param marshalOptionsResolver
	 * @param marshaler
	 */
	@Inject
	public MarshalListingProcessor(IMarshalOptionsResolver marshalOptionsResolver, Marshaler marshaler) {
		super();
		this.mor = marshalOptionsResolver;
		this.marshaler = marshaler;
	}

	@Override
	protected ListingHandler<Model> getRowDataListHandler(IListHandler<SearchResult> searchResultListHandler,
			IEntityType entityType, String listingId, RemoteListingDefinition<? extends IListingSearch> listingDef) {
		MarshalOptions mo;
		try {
			mo = mor.resolve(entityType);
		}
		catch(final IllegalArgumentException e) {
			mo = MarshalOptions.NO_REFERENCES;
		}
		final MarshalingListHandler marshalingListHandler =
			new MarshalingListHandler(searchResultListHandler, marshaler, mo, listingDef.getPropKeys());

		return new ListingHandler<Model>(marshalingListHandler, listingId, listingDef.getPageSize());
	}

}
