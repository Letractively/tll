/**
 * The Logic Lab
 * @author jpk Aug 30, 2007
 */
package com.tll.client.search;

import java.util.Set;

import com.tll.IMarshalable;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.SelectNamedQueries;
import com.tll.model.EntityType;

/**
 * ISearch - Client side search criteria definition.
 * @author jpk
 */
public interface ISearch extends IMarshalable {

	/**
	 * @return The type of search desired. This should correspond to a supported
	 *         server side criteria type.
	 */
	CriteriaType getCriteriaType();

	/**
	 * @return String that matches a server side EntityType enum element. May be
	 *         <code>null<code>.
	 */
	EntityType getEntityType();

	/**
	 * Return all elements of the ordained type?
	 * @return boolean
	 */
	// boolean isRetrieveAll();
	/**
	 * Resets the state of the object.
	 */
	void clear();

	/**
	 * @return The named query definition. May be <code>null</code>.
	 */
	SelectNamedQueries getNamedQuery();

	/**
	 * @return Possible query parameters when a named query is specified.
	 */
	Set<IQueryParam> getQueryParams();
}
