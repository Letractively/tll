/**
 * The Logic Lab
 * @author jpk Aug 31, 2007
 */
package com.tll.server.rpc.listing;

import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.dao.SearchResult;
import com.tll.listhandler.DecoratedListHandler;
import com.tll.listhandler.IListHandler;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;

/**
 * MarshalingListHandler - Transforms {@link SearchResult}s to {@link Model}s
 * for use in client side list handling.
 * @author jpk
 */
public final class MarshalingListHandler extends DecoratedListHandler<SearchResult<?>, Model> {

	private final Marshaler marshaler;
	private final MarshalOptions marshalOptions;
	private final String[] propKeys;

	/**
	 * Constructor
	 * @param listHandler
	 * @param marshaler
	 * @param marshalOptions
	 * @param propKeys
	 */
	public MarshalingListHandler(IListHandler<SearchResult<?>> listHandler, Marshaler marshaler,
			MarshalOptions marshalOptions, String[] propKeys) {
		super(listHandler);
		this.marshaler = marshaler;
		this.marshalOptions = marshalOptions;
		this.propKeys = propKeys;
	}

	/**
	 * Transforms the given {@link Model}.
	 * <p>
	 * Default impl is NO transform and return to given prop val group.
	 * @param model The {@link Model} to transform
	 * @return The transformed {@link Model}
	 * @throws IllegalStateException When a transform related error occurrs.
	 */
	private Model transform(Model model) {
		if(propKeys == null) {
			return model;
		}
		final int numCols = propKeys.length;
		final Model xgrp = new Model(model.getEntityType());
		for(int i = 0; i < numCols; i++) {
			try {
				xgrp.set(model.getModelProperty(propKeys[i]));
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException(e);
			}
		}
		// ensure we have the id and version set
		xgrp.setId(model.getId());
		xgrp.setVersion(model.getVersion());
		return xgrp;
	}

	public final Model getDecoratedElement(SearchResult<?> element) {
		return transform(marshaler.marshalSearchResult(element, marshalOptions));
	}
}
