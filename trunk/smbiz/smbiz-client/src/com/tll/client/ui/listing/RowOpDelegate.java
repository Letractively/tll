/**
 * The Logic Lab
 * @author jpk
 * May 8, 2008
 */
package com.tll.client.ui.listing;

import java.util.ArrayList;
import java.util.List;

import com.tll.client.App;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.model.RefKey;
import com.tll.client.ui.Option;

/**
 * RowOpDelegate - An adapter for {@link IRowOptionsDelegate} simplifying the
 * implementation.
 * @author jpk
 */
public abstract class RowOpDelegate implements IRowOptionsDelegate {

	private static final String EDIT_OPTION_PREFIX = "Edit ";
	private static final String DELETE_OPTION_PREFIX = "Delete ";

	/**
	 * Generates an edit option with the given name.
	 * <p>
	 * FORMAT: "Edit {subjectName}"
	 * @param subjectName
	 * @return New Option instance
	 */
	private static Option editOption(String subjectName) {
		return new Option(EDIT_OPTION_PREFIX + subjectName, App.imgs().pencil().createImage());
	}

	/**
	 * Generates a delete option with the given name.
	 * <p>
	 * FORMAT: "Delete {subjectName}"
	 * @param subjectName
	 * @return New Option instance
	 */
	private static Option deleteOption(String subjectName) {
		return new Option(DELETE_OPTION_PREFIX + subjectName, App.imgs().trash().createImage());
	}

	/**
	 * Does the option text indicate edit?
	 * @param optionText
	 * @return true/false
	 */
	private static boolean isEditOption(String optionText) {
		return optionText == null ? false : optionText.startsWith(EDIT_OPTION_PREFIX);
	}

	/**
	 * Does the option text indicate delete?
	 * @param optionText
	 * @return true/false
	 */
	private static boolean isDeleteOption(String optionText) {
		return optionText == null ? false : optionText.startsWith(DELETE_OPTION_PREFIX);
	}

	private Option[] staticOptions;

	protected abstract String getListingElementName();

	/**
	 * @return <code>true</code> when the row options are constant for all
	 *         elidgible.
	 */
	protected boolean isStaticOptions() {
		return true;
	}

	protected boolean isRowEditable(int rowIndex, RefKey rowRef) {
		return true;
	}

	protected boolean isRowDeletable(int rowIndex, RefKey rowRef) {
		return true;
	}

	protected Option[] getCustomRowOps(int rowIndex, RefKey rowRef) {
		return null;
	}

	/**
	 * Performs row editing.
	 * @param rowIndex The row index of the targeted row
	 * @param rowRef The ref of the row to edit
	 */
	protected void doEditRow(int rowIndex, RefKey rowRef) {
		// no-op
	}

	/**
	 * Performs row deletion.
	 * @param rowIndex The row index of the targeted row
	 * @param rowRef The ref of the row to delete
	 */
	protected void doDeleteRow(int rowIndex, RefKey rowRef) {
		// no-op
	}

	/**
	 * Performs custom row ops other that edit and delete.
	 * @param optionText The option text of the selection option
	 * @param rowIndex The row index of the targeted row
	 * @param rowRef The ref of the targeted row
	 */
	protected void handleRowOp(String optionText, int rowIndex, RefKey rowRef) {
		// no-op
	}

	public final Option[] getOptions(int rowIndex, RefKey rowRef) {
		if(isStaticOptions() && staticOptions != null) {
			return staticOptions;
		}
		List<Option> options = new ArrayList<Option>();
		if(isRowEditable(rowIndex, rowRef)) options.add(editOption(getListingElementName()));
		if(isRowDeletable(rowIndex, rowRef)) options.add(deleteOption(getListingElementName()));
		Option[] customOps = getCustomRowOps(rowIndex, rowRef);
		if(customOps != null) {
			for(Option o : customOps) {
				options.add(o);
			}
		}
		Option[] arr = options.toArray(new Option[options.size()]);
		if(isStaticOptions()) {
			staticOptions = arr;
		}
		return arr;
	}

	public final void handleOptionSelection(String optionText, int rowIndex, RefKey rowRef) {
		if(isEditOption(optionText)) {
			doEditRow(rowIndex, rowRef);
		}
		else if(isDeleteOption(optionText)) {
			doDeleteRow(rowIndex, rowRef);
		}
		else {
			handleRowOp(optionText, rowIndex, rowRef);
		}
	}
}