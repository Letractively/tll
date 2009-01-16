/**
 * The Logic Lab
 * @author jpk
 * Mar 10, 2008
 */
package com.tll.dao.hibernate;


/**
 * EntitySearchResultTransformer
 * @author jpk
 */
final class EntitySearchResultTransformer extends SearchResultTransformer {

	private static final long serialVersionUID = 9007124080132928698L;

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		return tuple[tuple.length - 1];
	}
}
