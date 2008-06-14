/**
 * The Logic Lab
 * @author jpk
 * Mar 30, 2008
 */
package com.tll.client.listing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tll.client.data.ListingOp;
import com.tll.client.model.IData;
import com.tll.client.ui.listing.ListingWidget;
import com.tll.listhandler.IPage;
import com.tll.listhandler.PageUtil;
import com.tll.listhandler.SortColumn;
import com.tll.listhandler.Sorting;

/**
 * DataCollectionListingOperator - {@link IListingOperator} based on an existing
 * collection of listing elements.
 * @author jpk
 */
// TODO implemenet sorting!!!
public class DataListingOperator<R extends IData> extends AbstractListingOperator<R> {

	/**
	 * The data provider.
	 */
	private final IDataProvider<R> dataProvider;

	private final int pageSize;

	private int numPages = -1;

	/**
	 * The 0-based page number
	 */
	private int pageNum = 0;

	// TODO make private when sorting is implemented
	/*private*/final Sorting sorting;

	/**
	 * Constructor
	 * @param listingWidget
	 * @param pageSize
	 * @param dataProvider
	 * @param sorting
	 */
	public DataListingOperator(ListingWidget<R> listingWidget, int pageSize, IDataProvider<R> dataProvider,
			Sorting sorting) {
		super(listingWidget);
		this.pageSize = pageSize;
		this.dataProvider = dataProvider;
		this.sorting = sorting;
	}

	/**
	 * Extracts a sub-array from the dataProvider
	 * @param startIndex 0-based inclusive
	 * @param endIndex 0-based EXCLUSIVE
	 * @return Array of data list elements
	 */
	@SuppressWarnings("unchecked")
	private R[] subArray(int startIndex, int endIndex) {
		IData[] array = new IData[endIndex - startIndex];
		for(int i = startIndex; i < endIndex; i++) {
			array[i] = dataProvider.getData().get(i);
		}
		return (R[]) array;
	}

	/**
	 * Generates an IPage for the desired page number
	 * @param page The desired page number
	 * @return Ad-hoc generated IPage instance
	 */
	private IPage<R> generatePage(int page) {

		// calculate size and num pages
		final int size = dataProvider.getData() == null ? 0 : dataProvider.getData().size();
		numPages = (pageSize > -1) ? PageUtil.calculateNumPages(pageSize, size) : (size > 0 ? 1 : 0);

		// calculate first and last page indexes
		int start, end;
		if(pageSize == -1) {
			// no paging
			start = 0;
			end = size;
		}
		else {
			start = page * pageSize; // (0-based index)
			end = start + pageSize; // 0-based exclusive
			if(end > size) end = size;
		}

		final int startIndex = start;
		final int endIndex = end;
		final R[] pageElements = subArray(startIndex, endIndex);

		return new IPage<R>() {

			public boolean isLastPage() {
				return pageNum == numPages - 1;
			}

			public boolean isFirstPage() {
				return pageNum == 0;
			}

			public int getTotalSize() {
				return size;
			}

			public int getPageSize() {
				return pageSize;
			}

			public int getPageNumber() {
				return pageNum;
			}

			public List<R> getPageElements() {
				return new ArrayList<R>(Arrays.asList(pageElements));
			}

			public int getNumPages() {
				return numPages;
			}

			public int getNumPageElements() {
				return pageElements.length;
			}

			public int getFirstIndex() {
				return pageSize == -1 ? 0 : PageUtil.getPageIndexFromListIndex(startIndex, size, pageSize);
			}

			public int getLastIndex() {
				return pageSize == -1 ? size - 1 : PageUtil.getPageIndexFromListIndex(endIndex, size, pageSize);
			}
		};

	}

	public void navigate(ListingOp navAction, Integer page) {
		int navPageNum;
		switch(navAction) {
			case GOTO_PAGE:
				assert page != null;
				navPageNum = page.intValue();
				break;

			case FIRST_PAGE:
				navPageNum = 0;
				break;

			case LAST_PAGE:
				navPageNum = numPages - 1;
				break;

			case PREVIOUS_PAGE:
				navPageNum = pageNum == 0 ? 0 : pageNum - 1;
				break;

			case NEXT_PAGE:
				navPageNum = pageNum == (numPages - 1) ? numPages - 1 : pageNum + 1;
				break;

			default:
				throw new IllegalArgumentException("Unrecognized listing navigation action");
		}

		if(pageNum == navPageNum) return; // no-op
		pageNum = navPageNum;
		assert pageNum >= 0;

		IPage<R> mpage = generatePage(navPageNum);
		listingWidget.setPage(mpage, null);
	}

	public void refresh() {
		IPage<R> mpage = generatePage(pageNum);
		listingWidget.setPage(mpage, null);
	}

	public void display() {
		refresh();
	}

	public void sort(SortColumn sortColumn) {
		// TODO implement
		throw new UnsupportedOperationException("Sorting data collection listings is not currently implemented");
	}

	public void clear() {
		numPages = pageNum = -1;
	}
}
