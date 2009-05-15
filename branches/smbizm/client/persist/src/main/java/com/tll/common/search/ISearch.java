/**
 * The Logic Lab
 * @author jpk Aug 30, 2007
 */
package com.tll.common.search;

import java.util.List;

import com.tll.IMarshalable;
import com.tll.common.model.IEntityType;
import com.tll.common.model.IEntityTypeProvider;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.IQueryParam;

/**
 * ISearch - Client side search criteria definition.
 * @author jpk
 */
public interface ISearch extends IMarshalable, IEntityTypeProvider {

	/**
	 * @return The type of search desired. This should correspond to a supported
	 *         server side criteria type.
	 */
	CriteriaType getCriteriaType();

	/**
	 * @return String that matches a server side EntityType enum element. May be
	 *         <code>null<code>.
	 */
	IEntityType getEntityType();

	/**
	 * Resets the state of the object.
	 */
	void clear();

	/**
	 * @return The name of the server-side named query definition. May be
	 *         <code>null</code>.
	 */
	String getNamedQuery();

	/**
	 * @return Possible query parameters when a named query is specified.
	 */
	List<IQueryParam> getQueryParams();
}
