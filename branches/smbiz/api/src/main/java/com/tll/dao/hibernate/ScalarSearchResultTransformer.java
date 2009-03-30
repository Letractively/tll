/**
 * The Logic Lab
 * @author jpk
 * Mar 10, 2008
 */
package com.tll.dao.hibernate;

import com.tll.model.IEntity;
import com.tll.model.Scalar;

/**
 * ScalarSearchResultTransformer
 * @author jpk
 */
final class ScalarSearchResultTransformer extends SearchResultTransformer {

	private static final long serialVersionUID = -5328234435526988142L;

	private final Class<? extends IEntity> refType;

	/**
	 * Constructor
	 * @param refType
	 */
	public ScalarSearchResultTransformer(Class<? extends IEntity> refType) {
		super();
		this.refType = refType;
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		return new Scalar(refType, tuple, aliases);
	}
}
