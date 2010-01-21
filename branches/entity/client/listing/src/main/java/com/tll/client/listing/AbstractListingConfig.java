/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.client.listing;

import com.tll.dao.Sorting;


/**
 * AbstractListingConfig - Base class for {@link IListingConfig} impls.
 * @param <R> The row data type
 * @author jpk
 */
public abstract class AbstractListingConfig<R> implements IListingConfig<R> {

	private final String caption, listingElementName;
	private final String[] modelProps;
	private final Column[] cols;
	private final Sorting defaultSorting;
	private final int pageSize;

	/**
	 * Constructor
	 * @param caption
	 * @param listingElementName
	 * @param modelProps
	 * @param cols
	 * @param defaultSorting
	 */
	public AbstractListingConfig(String caption, String listingElementName, String[] modelProps, Column[] cols,
			Sorting defaultSorting) {
		this(caption, listingElementName, modelProps, cols, defaultSorting, DEFAULT_PAGE_SIZE);
	}

	/**
	 * Constructor
	 * @param caption
	 * @param listingElementName
	 * @param modelProps
	 * @param cols
	 * @param defaultSorting
	 * @param pageSize
	 */
	public AbstractListingConfig(String caption, String listingElementName, String[] modelProps, Column[] cols,
			Sorting defaultSorting, int pageSize) {
		super();
		this.caption = caption;
		this.listingElementName = listingElementName;
		this.modelProps = modelProps;
		this.cols = cols;
		this.defaultSorting = defaultSorting;
		this.pageSize = pageSize;
	}

	@Override
	public final String getListingId() {
		return Integer.toString(getClass().getName().hashCode());
	}

	@Override
	public final String getListingElementName() {
		return listingElementName;
	}

	@Override
	public final String getCaption() {
		return caption;
	}

	@Override
	public final Column[] getColumns() {
		return cols;
	}

	@Override
	public final String[] getModelProperties() {
		return modelProps;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public final Sorting getDefaultSorting() {
		return defaultSorting;
	}

	@Override
	public boolean isSortable() {
		return defaultSorting != null; // default
	}

	@Override
	public boolean isIgnoreCaseWhenSorting() {
		return false; // default
	}

	@Override
	public boolean isShowNavBar() {
		return true; // default
	}

	@Override
	public boolean isShowRefreshBtn() {
		return true; // default
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITableCellRenderer<R> getCellRenderer() {
		return (ITableCellRenderer<R>) DefaultCellRenderer.get();
	}
}
