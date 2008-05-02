/**
 * The Logic Lab
 * @author jpk
 * Mar 30, 2008
 */
package com.tll.client.listing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tll.client.event.type.ListingEvent;
import com.tll.client.model.Model;
import com.tll.client.ui.listing.AbstractListingWidget;
import com.tll.listhandler.IPage;
import com.tll.listhandler.PageUtil;
import com.tll.listhandler.SortColumn;

/**
 * DataCollectionListingOperator - {@link IListingOperator} based on an existing
 * collection of listing elements.
 * @author jpk
 */
public class DataCollectionListingOperator extends AbstractListingOperator {

	private final int pageSize;

	private final List<Model> dataList;
	/**
	 * The size of the data list
	 */
	private int size = -1;
	/**
	 * The 0-based page number
	 */
	private int pageNum = 0;
	/**
	 * The calculated number of pages
	 */
	private int numPages = -1;

	/**
	 * Constructor
	 * @param listingWidget
	 * @param pageSize
	 * @param dataList
	 */
	public DataCollectionListingOperator(AbstractListingWidget listingWidget, int pageSize, List<Model> dataList) {
		super(listingWidget);
		this.pageSize = pageSize;
		this.dataList = dataList;
		this.size = dataList == null ? 0 : dataList.size();
		this.numPages = (pageSize > -1) ? PageUtil.calculateNumPages(pageSize, size) : (size > 0 ? 1 : 0);
	}

	/**
	 * Extracts a sub-array from the dataList
	 * @param startIndex 0-based inclusive
	 * @param endIndex 0-based EXCLUSIVE
	 * @return Array of data list elements
	 */
	private Model[] subArray(int startIndex, int endIndex) {
		Model[] array = new Model[endIndex - startIndex];
		for(int i = startIndex; i < endIndex; i++) {
			array[i] = dataList.get(i);
		}
		return array;
	}

	/**
	 * Generates an IPage for the desired page number
	 * @param page The desired page number
	 * @return Ad-hoc generated IPage instance
	 */
	private IPage<Model> generatePage(int page) {
		// calculate first and last page indexes
		final int startIndex = page * pageSize; // (0-based index)
		int indx = startIndex + pageSize; // 0-based exclusive
		if(indx > size) indx = size;
		final int endIndex = indx;
		final Model[] pageElements = subArray(startIndex, endIndex);

		return new IPage<Model>() {

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

			public List<Model> getPageElements() {
				return new ArrayList<Model>(Arrays.asList(pageElements));
			}

			public int getNumPages() {
				return numPages;
			}

			public int getNumPageElements() {
				return pageElements.length;
			}

			public int getFirstIndex() {
				return PageUtil.getPageIndexFromListIndex(startIndex, size, pageSize);
			}

			public int getLastIndex() {
				return PageUtil.getPageIndexFromListIndex(endIndex, size, pageSize);
			}
		};

	}

	public void navigate(ClientListingOp navAction, Integer page) {
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

		IPage<Model> mpage = generatePage(navPageNum);
		ListingEvent event = new ListingEvent(listingWidget, true, navAction, mpage, null);
		listingWidget.onListingEvent(event);
	}

	public void refresh() {
		if(size == 0) return;
		IPage<Model> mpage = generatePage(pageNum);

		ListingEvent event = new ListingEvent(listingWidget, true, ClientListingOp.REFRESH, mpage, null);
		listingWidget.onListingEvent(event);
	}

	public void display() {
		refresh();
	}

	public void sort(SortColumn sortColumn) {
		// TODO implement
		throw new UnsupportedOperationException("Sorting data collection listings is not currently implemented");
	}

	public void clear() {
		size = pageNum = numPages = -1;

	}

	public void insertRow(int rowIndex, Model rowData) {
		dataList.add(rowIndex, rowData);
		listingWidget.onListingEvent(new ListingEvent(listingWidget, true, ClientListingOp.INSERT_ROW, rowIndex, rowData));
	}

	public void updateRow(int rowIndex, Model rowData) {
		dataList.remove(rowIndex);
		dataList.add(rowIndex, rowData);
		listingWidget.onListingEvent(new ListingEvent(listingWidget, true, ClientListingOp.UPDATE_ROW, rowIndex, rowData));
	}

	public void deleteRow(int rowIndex) {
		dataList.remove(rowIndex);
		Model removed = dataList.remove(rowIndex);
		listingWidget.onListingEvent(new ListingEvent(listingWidget, true, ClientListingOp.DELETE_ROW, rowIndex, removed));

	}
}
