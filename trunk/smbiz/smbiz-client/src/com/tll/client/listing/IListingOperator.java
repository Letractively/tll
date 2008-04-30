/**
 * The Logic Lab
 * @author jpk
 * Mar 30, 2008
 */
package com.tll.client.listing;

import com.tll.client.model.Model;
import com.tll.listhandler.SortDir;

/**
 * IListingOperator - Performs listing ops on a particular listing. This
 * interface enables a pluggable (generic) way to provide listing data.
 * @author jpk
 */
public interface IListingOperator {

	/**
	 * Acquires or re-acquires the listing data resetting the listing state then
	 * dispatches the a listing event to the listing Widget.
	 */
	void refresh();

	/**
	 * Dispatches a listing event containing cached listing data and state. If no
	 * listing data exists, the listing data is acquired as {@link #refresh()}
	 * would. A corresponding listing event is then dispatched to the listing
	 * Widget.
	 */
	void display();

	/**
	 * Sorts the listing. A listing event is then dispatched to the listing
	 * Widget.
	 * @param colName
	 * @param direction
	 */
	void sort(String colName, SortDir direction);

	/**
	 * Navigates the listing. A listing event is then dispatched to the listing
	 * Widget and any other listeners.
	 * @param navAction
	 * @param page The 0-based page number
	 */
	void navigate(ClientListingOp navAction, Integer page);

	/**
	 * Relinquishes any inter-request listing cache and state associated with the
	 * listing this operator operates on. A listing event is then dispatched to
	 * the listing Widget.
	 */
	void clear();

	/**
	 * Inserts a new row at the given index shifting the existing rows downward. A
	 * listing event is then dispatched to the listing Widget.
	 * @param beforeRow The index before which the new row will be inserted.
	 * @param rowData The row data
	 */
	void insertRow(int rowIndex, Model rowData);

	/**
	 * Updates an existing row's cell contents. A listing event is then dispatched
	 * to the listing Widget.
	 * @param rowIndex The row index of the row to update
	 * @param rowData The new row data to apply
	 */
	void updateRow(int rowIndex, Model rowData);

	/**
	 * Deletes a row at the given row index. A listing event is then dispatched to
	 * the listing Widget.
	 * @param rowIndex The index of the row to delete
	 */
	void deleteRow(int rowIndex);
}