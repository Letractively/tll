package com.tll.listhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tll.criteria.Criteria;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;

/**
 * Abstract search supporting list handler class. All search supporting list
 * handler implementations should derive from this class.
 * @param <E> entity type
 * @author jpk
 */
public abstract class SearchListHandler<E> extends AbstractListHandler<SearchResult> {

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	/**
	 * The list handler data provider.
	 */
	protected final IListingDataProvider<E> dataProvider;

	/**
	 * The search criteria.
	 */
	protected Criteria<E> criteria;

	/**
	 * Constructor
	 * @param dataProvider The data provider used to fetch the list elements with
	 *        the given criteria.
	 * @param criteria The criteria used to generate the underlying list
	 * @param sorting The required sorting directive.
	 * @throws IllegalArgumentException When one or more required args are not
	 *         specifeid
	 */
	public SearchListHandler(IListingDataProvider<E> dataProvider, Criteria<E> criteria, Sorting sorting)
	throws IllegalArgumentException {
		super(sorting);
		if(dataProvider == null) {
			throw new IllegalArgumentException("A data provider must be specified.");
		}
		if(criteria == null) {
			throw new IllegalArgumentException("No criteria specified.");
		}
		if(sorting == null) {
			throw new IllegalArgumentException("No sorting directive specified.");
		}
		this.dataProvider = dataProvider;
		this.criteria = criteria;
	}
}
