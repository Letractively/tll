/**
 * The Logic Lab
 * @author jpk Aug 30, 2007
 */
package com.tll.client.search;

import java.util.Map;

import com.tll.client.IMarshalable;

/**
 * ISearch - Client side search criteria definition.
 * @author jpk
 */
public interface ISearch extends IMarshalable {

	/**
	 * Search type entity.
	 */
	public static final int TYPE_ENTTY = 0;

	/**
	 * Search type entity query.
	 */
	public static final int TYPE_ENTTY_QUERY = 1;

	/**
	 * Search type scalar query.
	 */
	public static final int TYPE_SCALER_QUERY = 2;

	/**
	 * @return The type of search desired. This should correspond to a supported
	 *         server side criteria type.
	 */
	int getSearchType();

	/**
	 * @return The name of the query to invoke. May be <code>null</code>.
	 */
	String getQueryName();

	/**
	 * @return Possible query parameters when a named query is specified.
	 */
	Map<String, String> getQueryParams();

	/**
	 * @return String that matches a server side EntityType enum element. May be
	 *         <code>null<code>.
	 */
	String getEntityType();

	/**
	 * Return all elements of the ordained type?
	 * @return boolean
	 */
	boolean isRetrieveAll();

	/**
	 * Resets the state of the object.
	 */
	void clear();
}
