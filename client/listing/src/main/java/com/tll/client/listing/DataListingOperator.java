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
 * @param <R> list element type
 * @param <H> list <em>handler</em> type
 */
public class DataListingOperator<R, H extends IListHandler<R>> extends AbstractListingOperator<R> {

	/**
	 * The data provider.
	 */
	protected final H dataProvider;

	protected final int pageSize;

	/**
	 * The current chunk of listing data.
	 */
	protected transient List<R> current;

	/**
	 * Constructor
	 * @param target
	 * @param pageSize
	 * @param dataProvider
	 */
	public DataListingOperator(Widget target, int pageSize, H dataProvider) {
		super(target, dataProvider.getSorting());
		this.pageSize = pageSize;
		this.dataProvider = dataProvider;
		this.sorting = dataProvider.getSorting();
	}
	
	@Override
	protected void doFetch(int ofst, Sorting srtg) {
		try {
			current = dataProvider.getElements(ofst, pageSize, srtg);
			sorting = srtg;
			listSize = current.size();
			offset = ofst;
		}
		catch(final EmptyListException e) {
			if(current != null) current.clear();
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
		doFetch(0, defaultSorting);
		fireListingEvent(ListingOp.REFRESH, current);
	}
	
	@Override
	public void firstPage() {
		super.firstPage();
		fireListingEvent(ListingOp.FETCH, current);
	}

	@Override
	public void lastPage() {
		super.lastPage();
		fireListingEvent(ListingOp.FETCH, current);
	}

	@Override
	public void nextPage() {
		super.nextPage();
		fireListingEvent(ListingOp.FETCH, current);
	}

	@Override
	public void previousPage() {
		super.previousPage();
		fireListingEvent(ListingOp.FETCH, current);
	}

	@Override
	public void gotoPage(int pageNum) {
		super.gotoPage(pageNum);
		fireListingEvent(ListingOp.FETCH, current);
	}

	@Override
	public void sort(Sorting srtg) {
		super.sort(srtg);
		fireListingEvent(ListingOp.FETCH, current);
	}

	@Override
	public void clear() {
		offset = 0;
		sorting = null;
		if(current != null) {
			current.clear();
			current = null;
		}
		fireListingEvent(ListingOp.CLEAR, null);
	}
}
