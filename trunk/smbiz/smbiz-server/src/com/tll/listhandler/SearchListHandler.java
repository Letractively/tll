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
	protected ICriteria<? extends E> criteria;

	/**
	 * Constructor
	 * @param dataProvider
	 */
	public SearchListHandler(IListHandlerDataProvider<E> dataProvider) {
		super();
		if(dataProvider == null) {
			throw new IllegalArgumentException("A list handler service must be specified.");
		}
		this.dataProvider = dataProvider;
	}

	public boolean isSortable() {
		return true;
	}

	public final boolean hasElements() {
		return (size() > 0);
	}

	public final void refresh() throws EmptyListException {
		if(this.criteria != null) {
			try {
				executeSearch(criteria);
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
		return criteria == null ? null : criteria.getSorting();
	}

	public final void executeSearch(ICriteria<? extends E> criteria) throws InvalidCriteriaException,
			NoMatchingResultsException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		// NOTE: don't enfore this as we may be pointing to a named query
		if(criteria.getSorting() == null) {
			// NOTE: we always require sorting for search based list handling
			throw new InvalidCriteriaException("No criteria sorting specified.");
		}
		refresh(criteria);
	}

	/**
	 * Refresh the list handler elements with the provided criteria. This allows
	 * for centralization of search execution and sorting.
	 * @param criteria The criteria to refresh with. Guaranteed non-<code>null</code>.
	 * @throws InvalidCriteriaException
	 * @throws NoMatchingResultsException
	 */
	protected abstract void refresh(ICriteria<? extends E> criteria) throws InvalidCriteriaException,
			NoMatchingResultsException;

	public final void sort(Sorting sorting) throws ListHandlerException {
		if(!isSortable()) {
			throw new ListHandlerException("This list handler is not sortable");
		}
		if(sorting == null || sorting.size() < 1) {
			throw new ListHandlerException("No sorting directives specified");
		}
		if(criteria == null) {
			throw new ListHandlerException("Unable to sort: No criteria present");
		}

		// only sort when at least 2 list elements and sorting directive argument
		// differs than what is held in the list handler's criteria.
		if(size() > 1 && !sorting.equals(criteria.getSorting())) {

			try {
				// copy the criteria and set the sorting on the copy as we are not yet
				// sure the sorting will be successful!
				final ICriteria<? extends E> ccln = criteria.copy();
				ccln.setSorting(sorting);
				try {
					refresh(ccln);
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
