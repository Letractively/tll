/**
 * The Logic Lab
 * @author jpk
 * Jun 15, 2008
 */
package com.tll.client.data;

import com.tll.client.IMarshalable;
import com.tll.criteria.Criteria;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.Sorting;
import com.tll.model.IEntity;

/**
 * RemoteListingDefinition - Definition for server side listings. A unique
 * listing name must be bound to these types.
 * @author jpk
 */
public final class RemoteListingDefinition<E extends IEntity> implements IMarshalable {

	private ListHandlerType listHandlerType;
	private Criteria<E> criteria;
	private String[] propKeys;
	private int pageSize;
	private Sorting initialSorting;

	/**
	 * Constructor
	 */
	public RemoteListingDefinition() {
		super();
	}

	/**
	 * Constructor
	 * @param listHandlerType The required list handler type
	 * @param criteria The required search criteria
	 * @param propKeys The optional property keys filter array
	 * @param pageSize The required page size. <code>-1</code> means no paging.
	 * @param initialSorting The required default sorting directive
	 */
	public RemoteListingDefinition(ListHandlerType listHandlerType, Criteria<E> criteria, String[] propKeys,
			int pageSize, Sorting initialSorting) {
		super();
		this.listHandlerType = listHandlerType;
		this.criteria = criteria;
		this.propKeys = propKeys;
		this.pageSize = pageSize;
		this.initialSorting = initialSorting;
	}

	/**
	 * @return The list handling type. Controls how paging is performed on the
	 *         server.
	 */
	public ListHandlerType getListHandlerType() {
		return listHandlerType;
	}

	/**
	 * The max number of elements to show per page.
	 * @return int
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * The properties to include in the listing data. If <code>null</code> all
	 * properties will be included in the listing data.
	 * @return the prop keys array.
	 */
	public String[] getPropKeys() {
		return propKeys;
	}

	/**
	 * @return The search criteria for the listing.
	 */
	public Criteria<E> getCriteria() {
		return criteria;
	}

	/**
	 * @return The default sorting directive.
	 */
	public Sorting getInitialSorting() {
		return initialSorting;
	}
}