/**
 * The Logic Lab
 * @author jpk Aug 31, 2007
 */
package com.tll.server.rpc.listing;

import com.tll.common.model.Model;
import com.tll.dao.SearchResult;
import com.tll.listhandler.DecoratedListHandler;
import com.tll.model.IEntity;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;

/**
 * MarshalingListHandler - {@link IMarshalingListHandler} implementation
 * @author jpk
 * @param <E>
 */
public class MarshalingListHandler<E extends IEntity> extends DecoratedListHandler<SearchResult<E>, Model> implements IMarshalingListHandler<E> {

	protected final Marshaler marshaler;
	protected final MarshalOptions marshalOptions;

	/**
	 * Constructor
	 * @param marshaler
	 * @param marshalOptions
	 */
	public MarshalingListHandler(Marshaler marshaler, MarshalOptions marshalOptions) {
		super();
		this.marshaler = marshaler;
		this.marshalOptions = marshalOptions;
	}

	/**
	 * Transforms the given {@link Model}.
	 * <p>
	 * Default impl is NO transform and return to given prop val group.
	 * @param model The {@link Model} to transform
	 * @return The transformed {@link Model}
	 * @throws IllegalStateException When a transform related error occurrs.
	 */
	protected Model transform(Model model) {
		return model;
	}

	public final Model getDecoratedElement(SearchResult<E> element) {
		return transform(marshaler.marshalSearchResult(element, marshalOptions));
	}
}
