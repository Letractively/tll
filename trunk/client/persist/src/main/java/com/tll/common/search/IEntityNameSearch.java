/**
 * The Logic Lab
 * @author jpk
 * @since May 18, 2009
 */
package com.tll.common.search;

import com.tll.common.model.IEntityType;

/**
 * IEntityNameSearch - A search by name definition.
 * @author jpk
 */
public interface IEntityNameSearch extends ISearch {

	/**
	 * @return the targeted entity type
	 */
	IEntityType getEntityType();

	/**
	 * @return The name by which to search
	 */
	String getName();
}
