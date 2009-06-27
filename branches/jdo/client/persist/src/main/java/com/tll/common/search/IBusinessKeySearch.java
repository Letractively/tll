/**
 * The Logic Lab
 * @author jpk
 * @since May 18, 2009
 */
package com.tll.common.search;

import com.tll.common.model.IEntityTypeProvider;
import com.tll.common.model.IPropertyValue;


/**
 * IBusinessKeySearch
 * @author jpk
 */
public interface IBusinessKeySearch extends ISearch, IEntityTypeProvider {

	/**
	 * @return The name of the server side business key
	 */
	String getBusinessKeyName();

	/**
	 * @return The property values that constitute the business key value.
	 */
	IPropertyValue[] getProperties();
}
