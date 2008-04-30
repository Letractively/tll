package com.tll.client.data;

import java.util.List;

import com.tll.client.IMarshalable;
import com.tll.client.model.Model;
import com.tll.listhandler.IPage;
import com.tll.listhandler.Sorting;

/**
 * PageData - {@link IPage} impl whose data elements are
 * {@link Model} instances.
 * @author jpk
 */
public final class PageData implements IPage<Model>, IMarshalable {

	private String listingName;
	private int pageSize;
	private List<Model> pageElements;
	private int firstIndex = -1;
	private int lastIndex = -1;
	private int totalSize = 0;
	private int pageNumber;
	private int numPages;
	private Sorting sorting;

	/**
	 * Constructor
	 */
	public PageData() {
		super();
	}

	public String descriptor() {
		return "Table Page";
	}

	public String getListingName() {
		return listingName;
	}

	public void setListingName(String listingName) {
		this.listingName = listingName;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<Model> getPageElements() {
		return pageElements;
	}

	public Model getPageElement(int index) {
		return pageElements.get(index);
	}

	public int getNumPageElements() {
		return pageElements == null ? 0 : pageElements.size();
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getNumPages() {
		return numPages;
	}

	public void setNumPages(int numPages) {
		this.numPages = numPages;
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public Sorting getSorting() {
		return sorting;
	}

	public void setSorting(Sorting sorting) {
		this.sorting = sorting;
	}

	public boolean isFirstPage() {
		return getPageNumber() == 0;
	}

	public boolean isLastPage() {
		return getPageNumber() == getNumPages() - 1;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb
				.append(", pageRows.size(): "
						+ (getPageElements() == null ? "NULL" : Integer.toString(getPageElements().size())));
		sb.append(", firstIndex: " + getFirstIndex());
		sb.append(", lastIndex: " + getLastIndex());
		sb.append(", totalSize: " + getTotalSize());
		sb.append(", pageNum: " + getPageNumber());
		sb.append(", numPages: " + getNumPages());
		return sb.toString();
	}

}
