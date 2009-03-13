/**
 * The Logic Lab
 * @author jpk
 * Feb 25, 2008
 */
package com.tll.server.rpc.listing;

import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.model.IEntity;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;

/**
 * PropKeyListHandler
 * @author jpk
 * @param <E>
 */
public class PropKeyListHandler<E extends IEntity> extends MarshalingListHandler<E> {

	private final String[] propKeys;

	/**
	 * Constructor
	 * @param marshaler
	 * @param marshalOptions
	 * @param propKeys
	 */
	public PropKeyListHandler(Marshaler marshaler, MarshalOptions marshalOptions, String[] propKeys) {
		super(marshaler, marshalOptions);
		this.propKeys = propKeys;
	}

	/**
	 * Transform the property value group based on the defined property keys.
	 * @param model The model to transform
	 * @return {@link Model} containing only those properties declared in the held
	 *         property keys array.
	 * @throws IllegalStateException When a transform related error occurrs.
	 */
	@Override
	protected Model transform(Model model) {
		if(propKeys == null) {
			return model;
		}
		final int numCols = propKeys.length;
		final Model xgrp = new Model(model.getEntityType());
		for(int i = 0; i < numCols; i++) {
			try {
				xgrp.set(model.getModelProperty(propKeys[i]));
			}
			catch(PropertyPathException e) {
				throw new IllegalStateException(e);
			}
		}
		return xgrp;
	}
}