/**
 * The Logic Lab
 * @author jpk
 * Mar 30, 2008
 */
package com.tll.client.listing;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.tll.common.data.ListingOp;
import com.tll.dao.Sorting;
import com.tll.listhandler.EmptyListException;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.ListHandlerException;

/**
 * DataListingOperator - {@link IListingOperator} based on an existing
 * collection of data elements.
 * @author jpk
 * @param <R>
 */
public class DataListingOperator<R> extends AbstractListingOperator<R> {

	/**
	 * The data provider.
	 */
	private final IListHandler<R> dataProvider;
	
	private final int pageSize;
	
	/**
	 * The current chunk of listing data.
	 */
	private transient List<R> current;

	/**
	 * Constructor
	 * @param sourcingWidget
	 * @param pageSize
	 * @param dataProvider
	 * @param sorting
	 */
	public DataListingOperator(Widget sourcingWidget, int pageSize, IListHandler<R> dataProvider, Sorting sorting) {
		super(sourcingWidget);
		this.pageSize = pageSize;
		this.dataProvider = dataProvider;
		this.sorting = sorting;
	}

	/**
	 * Assembles a listing event ready for firing.
	 * @param pageElements
	 * @param listingOp
	 * @return A new ListingEvent
	 */
	@SuppressWarnings("unchecked")
	private ListingEvent<R> assembleListingEvent(List<R> pageElements, ListingOp listingOp) {
		final int listSize = pageElements == null ? 0 : pageElements.size();
		final R[] array = pageElements == null ? null : (R[]) pageElements.toArray();
		return new ListingEvent<R>(true, null, ListingOp.REFRESH, listSize, array, offset, sorting,
				pageSize);
	}
	
	@Override
	protected void doFetch(int offset, Sorting sorting) {
		try {
			current = dataProvider.getElements(offset, pageSize, sorting);
		}
		catch(final EmptyListException e) {
			throw new IllegalStateException(e);
		}
		catch(final IndexOutOfBoundsException e) {
			throw new IllegalStateException(e);
		}
		catch(final ListHandlerException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	protected int getPageSize() {
		return pageSize;
	}

	@Override
	public void refresh() {
		// not refreshable since we have a non-changing collection of elements in memory only 
		// so just goto first page
		firstPage();
	}

	@Override
	public void display() {
		super.display();
		sourcingWidget.fireEvent(assembleListingEvent(current, ListingOp.FETCH));
	}

	@Override
	public void firstPage() {
		super.firstPage();
		sourcingWidget.fireEvent(assembleListingEvent(current, ListingOp.FETCH));
	}

	@Override
	public void lastPage() {
		super.lastPage();
		sourcingWidget.fireEvent(assembleListingEvent(current, ListingOp.FETCH));
	}

	@Override
	public void nextPage() {
		super.nextPage();
		sourcingWidget.fireEvent(assembleListingEvent(current, ListingOp.FETCH));
	}

	@Override
	public void previousPage() {
		super.previousPage();
		sourcingWidget.fireEvent(assembleListingEvent(current, ListingOp.FETCH));
	}

	@Override
	public void gotoPage(int pageNum) {
		super.gotoPage(pageNum);
		sourcingWidget.fireEvent(assembleListingEvent(current, ListingOp.FETCH));
	}

	@Override
	public void sort(Sorting sorting) {
		super.sort(sorting);
	}

	@Override
	public void clear() {
		offset = 0;
		sorting = null;
	}
}
