/**
 * The Logic Lab
 * @author jpk
 * May 8, 2008
 */
package com.tll.client.listing;

import java.util.ArrayList;
import java.util.List;

import com.tll.client.App;
import com.tll.client.ui.Option;

/**
 * AbstractRowOptions - An adapter for {@link IRowOptionsDelegate} simplifying
 * the implementation.
 * @author jpk
 */
public abstract class AbstractRowOptions implements IRowOptionsDelegate {

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

	/**
	 * @return The listing element name.
	 */
	protected abstract String getListingElementName();

	/**
	 * @return <code>true</code> when the row options are constant for all
	 *         elidgible rows.
	 */
	protected boolean isStaticOptions() {
		return true;
	}

	protected boolean isRowEditable(int rowIndex) {
		return true;
	}

	protected boolean isRowDeletable(int rowIndex) {
		return true;
	}

	protected Option[] getCustomRowOps(int rowIndex) {
		return null;
	}

	/**
	 * This method is invoked whtn a row is targeted for editing.
	 * @param rowIndex The row index of the targeted row
	 * @param rowRef The ref of the row to edit
	 */
	protected void doEditRow(int rowIndex) {
		// no-op
	}

	/**
	 * This method is invoked when a row is targeted for deletion.
	 * @param rowIndex The row index of the targeted row
	 * @param rowRef The ref of the row to delete
	 */
	protected void doDeleteRow(int rowIndex) {
		// no-op
	}

	/**
	 * Performs custom row ops other that edit and delete.
	 * @param optionText The option text of the selection option
	 * @param rowIndex The row index of the targeted row
	 * @param rowRef The ref of the targeted row
	 */
	protected void handleRowOp(String optionText, int rowIndex) {
		// no-op
	}

	public final Option[] getOptions(int rowIndex) {
		if(isStaticOptions() && staticOptions != null) {
			return staticOptions;
		}
		List<Option> options = new ArrayList<Option>();
		if(isRowEditable(rowIndex)) options.add(editOption(getListingElementName()));
		if(isRowDeletable(rowIndex)) options.add(deleteOption(getListingElementName()));
		Option[] customOps = getCustomRowOps(rowIndex);
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

	public final void handleOptionSelection(String optionText, int rowIndex) {
		if(isEditOption(optionText)) {
			doEditRow(rowIndex);
		}
		else if(isDeleteOption(optionText)) {
			doDeleteRow(rowIndex);
		}
		else {
			handleRowOp(optionText, rowIndex);
		}
	}
}