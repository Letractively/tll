package com.tll.client.ui.listing;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.tll.IMarshalable;
import com.tll.client.listing.IListingConfig;

/**
 * @author jpk
 */
public class ListingWidget<R extends IMarshalable> extends CellTable<R> {

	public static interface TllResources extends com.google.gwt.user.cellview.client.CellTable.Resources {
    
	}
  
	private final List<SortableHeader> allHeaders = new ArrayList<SortableHeader>();

	/**
	 * Constructor
	 * @param config
	 */
	public ListingWidget(IListingConfig<R> config) {
		super();
	}
	
	private void addColumn(com.tll.client.listing.Column tllCol) {
    // Create the column.
    final TextColumn<R> column = new TextColumn<R>() {

			@Override
			public String getValue(R object) {
				return null;
			}
    	
    };
    final SortableHeader header = new SortableHeader(text);
    allHeaders.add(header);

    // Hook up sorting.
    header.setUpdater(new ValueUpdater<String>() {
      public void update(String value) {
        header.setSorted(true);
        header.toggleReverseSort();

        for (SortableHeader otherHeader : allHeaders) {
          if (otherHeader != header) {
            otherHeader.setSorted(false);
            otherHeader.setReverseSort(true);
          }
        }

        sortExpenses(items.getList(), header.getReverseSort() ? descComparator
            : ascComparator);
        table.redrawHeaders();
      }
    });
    table.addColumn(column, header);
    return column;
	}
}
