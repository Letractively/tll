package com.tll.listhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.criteria.ICriteria;
import com.tll.model.IEntity;

/**
 * Abstract search supporting list handler class. All search supporting list
 * handler implementations should derive from this class.
 * @author jpk
 */
public abstract class SearchListHandler<E extends IEntity> extends AbstractListHandler<SearchResult<E>> {

	protected final Log LOG = LogFactory.getLog(this.getClass());

	/**
	 * The list handler data provider.
	 */
	protected final IListHandlerDataProvider<E> dataProvider;

	/**
	 * The search criteria.
	 */
	protected ICriteria<? extends E> criteria;

	/**
	 * Constructor
	 * @param dataProvider The data provider used to fetch the list elements with
	 *        the given criteria.
	 * @param criteria The criteria used to generate the underlying list
	 * @param sorting The required sorting directive.
	 */
	public SearchListHandler(IListHandlerDataProvider<E> dataProvider, ICriteria<? extends E> criteria, Sorting sorting)
			throws IllegalArgumentException {
		super();
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
		this.sorting = sorting;
	}
}