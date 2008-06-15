/*
 * Created on Oct 27, 2004
 */
package com.tll.listhandler;

import java.util.List;

import com.tll.client.IMarshalable;

/**
 * {@link com.tll.listhandler.IPage} implementation.
 * @see IPage
 * @author jpk
 */
public class Page<T> implements IPage<T>, IMarshalable {

	private int pageSize;
	private int totalSize = 0;
	private List<T> pageElements;
	private int offset = -1;
	/**
	 * Calculated property based on the page size and total size.
	 */
	private int numPages = 0;

	/**
	 * Constructor
	 */
	public Page() {
		super();
	}

	/**
	 * Constructor
	 * @param pageSize
	 */
	public Page(int pageSize, int totalSize) {
		super();
		this.pageSize = pageSize;
		this.totalSize = totalSize;
		calculateNumPages();
	}

	/**
	 * Constructor
	 * @param pageSize
	 * @param totalSize
	 * @param pageElements
	 * @param offset
	 */
	public Page(int pageSize, int totalSize, List<T> pageElements, int offset) {
		this(pageSize, totalSize);
		this.pageElements = pageElements;
		this.offset = offset;
	}

	public void setPage(List<T> pageElements, int pageNumber) {
		this.pageElements = pageElements;
		this.offset = pageNumber;
	}

	public void clear() {
		this.pageElements = null;
		this.offset = -1;
		this.numPages = 0;
	}

	public final int getPageSize() {
		return pageSize;
	}

	public final int getNumPages() {
		return numPages;
	}

	public final int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
		calculateNumPages();
	}

	private void calculateNumPages() {
		this.numPages = PageUtil.calculateNumPages(getPageSize(), getTotalSize());
	}

	public final List<T> getPageElements() {
		return pageElements;
	}

	public void setPageElements(List<T> pageElements) {
		this.pageElements = pageElements;
	}

	public int getNumPageElements() {
		return pageElements == null ? 0 : pageElements.size();
	}

	public final int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPageNumber() {
		return PageUtil.getPageNumberFromListIndex(offset, totalSize, pageSize);
	}

	public final int getFirstIndex() {
		return pageSize * offset;
	}

	public final int getLastIndex() {
		return getFirstIndex() + getNumPageElements() - 1;
	}

	public final boolean isFirstPage() {
		return offset == 0;
	}

	public final boolean isLastPage() {
		return offset == (numPages - 1);
	}

}
