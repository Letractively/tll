package com.tll.listhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.SystemError;
import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.model.IEntity;

/**
 * Abstract search supporting list handler class. All search supporting list
 * handler implementations should derive from this class.
 * @author jpk
 */
public abstract class SearchListHandler<E extends IEntity> extends AbstractListHandler<SearchResult<E>> {

	protected final Log LOG = LogFactory.getLog(this.getClass());

	/**
	 * The list handler service
	 */
	protected final IListHandlerDataProvider<E> dataProvider;

	/**
	 * The search criteria.
	 */
	private ICriteria<? extends E> criteria;

	/**
	 * The sorting directive.
	 */
	private Sorting sorting;

	/**
	 * Constructor
	 * @param dataProvider
	 */
	SearchListHandler(IListHandlerDataProvider<E> dataProvider) {
		super();
		if(dataProvider == null) {
			throw new IllegalArgumentException("A list handler service must be specified.");
		}
		this.dataProvider = dataProvider;
	}

	protected final Class<? extends E> getEntityClass() {
		return criteria == null ? null : criteria.getEntityClass();
	}

	public final boolean hasElements() {
		return (size() > 0);
	}

	public final void refresh() throws EmptyListException {
		if(criteria != null) {
			try {
				executeSearch(criteria, sorting);
			}
			catch(final NoMatchingResultsException nmre) {
				throw new EmptyListException(nmre.getMessage());
			}
			catch(final InvalidCriteriaException e) {
				throw new SystemError("Search list handler refresh failed: " + e.getMessage(), e);
			}
		}
	}

	public final Sorting getSorting() {
		return sorting;
	}

	public final void executeSearch(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException,
			NoMatchingResultsException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		if(sorting == null) {
			// NOTE: we always require sorting for search based list handling
			throw new InvalidCriteriaException("A Sort directive must be specified for search based list handlers.");
		}
		// we now commit to the given criteria and sorting irregardless of the
		// search outcome to maintain proper state
		this.criteria = criteria;
		this.sorting = sorting;
		doSearch(criteria, sorting);
	}

	/**
	 * Sub-classes are responsible for performing the actual search.
	 * @param criteria The criteria to refresh with. Guaranteed non-<code>null</code>.
	 * @param sorting The sort directive. Guaranteed non-<code>null</code>.
	 * @throws InvalidCriteriaException
	 * @throws NoMatchingResultsException
	 */
	protected abstract void doSearch(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException,
			NoMatchingResultsException;

	public final void sort(Sorting sorting) throws ListHandlerException {
		if(sorting == null || sorting.size() < 1) {
			throw new ListHandlerException("No sorting directives specified");
		}
		if(criteria == null) {
			throw new ListHandlerException("Unable to sort: No criteria present");
		}

		// only sort when at least 2 list elements and sorting directive argument
		// differs than what is held in the list handler's criteria.
		if(size() > 1 && !sorting.equals(sorting)) {

			try {
				try {
					doSearch(criteria, sorting);
					this.sorting = sorting;
				}
				catch(final NoMatchingResultsException e) {
					throw new EmptyListException("No list elements exist anymore.");
				}
			}
			catch(final InvalidCriteriaException e) {
				throw new ListHandlerException("Unexpected invalid criteria exception occurred: " + e.getMessage(), e);
			}
		}
	}
}
