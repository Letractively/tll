/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * FieldWidget - Enables arbitrary {@link Widget}s to display as a UI field.
 * @author jpk
 */
public class FieldWidget extends FieldAdapter {

	/**
	 * Constructor
	 * @param lblTxt
	 * @param lblMode
	 * @param w The widget encased in the field's {@link Panel}.
	 */
	public FieldWidget(String lblTxt, int lblMode, Widget w) {
		super(lblTxt, lblMode);
		assert w != null;
		fp.add(w);
	}

}
