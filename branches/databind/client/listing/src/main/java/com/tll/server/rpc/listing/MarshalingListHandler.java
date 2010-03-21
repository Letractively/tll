/**
 * The Logic Lab
 * @author jpk Aug 31, 2007
 */
package com.tll.server.rpc.listing;

import java.util.HashSet;

import com.tll.common.model.CopyCriteria;
import com.tll.common.model.IModelProperty;
import com.tll.common.model.Model;
import com.tll.common.model.NullNodeInPropPathException;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.UnsetPropertyException;
import com.tll.dao.SearchResult;
import com.tll.listhandler.DecoratedListHandler;
import com.tll.listhandler.IListHandler;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;

/**
 * Transforms {@link SearchResult}s to {@link Model}s for use in client side
 * list handling.
 * @author jpk
 */
public final class MarshalingListHandler extends DecoratedListHandler<SearchResult, Model> {

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
	public MarshalingListHandler(IListHandler<SearchResult> listHandler, Marshaler marshaler,
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
		final int numProps = propKeys.length;
		final HashSet<IModelProperty> whitelist = new HashSet<IModelProperty>(numProps);
		for(int i = 0; i < numProps; i++) {
			IModelProperty mp;
			try {
				mp = model.getModelProperty(propKeys[i]);
			}
			catch(final NullNodeInPropPathException e) {
				mp = null;
			}
			catch(final UnsetPropertyException e) {
				mp = null;
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException(e);
			}
			if(mp != null) whitelist.add(mp);
		}
		final Model subset = model.copy(CopyCriteria.subset(whitelist));
		return subset;
	}

	public final Model getDecoratedElement(SearchResult element) {
		return transform(marshaler.marshalSearchResult(element, marshalOptions));
	}
}
