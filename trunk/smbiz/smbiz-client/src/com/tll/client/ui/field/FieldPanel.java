/**
 * The Logic Lab
 * @author jpk Jan 6, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.field.IField;
import com.tll.client.ui.CSS;

/**
 * FieldPanel - Convenience class extending {@link FlowPanel} for the sake of
 * auto-setting field related CSS styles.
 * @author jpk
 */
public final class FieldPanel extends FlowPanel {

	/**
	 * Constructor
	 * @param fieldStyle The CSS class to add to this panel. Use those declared in
	 *        {@link IField}. May not be <code>null</code>.
	 */
	public FieldPanel(String fieldStyle) {
		super();
		assert fieldStyle != null;
		addStyleName(fieldStyle);
		if(IField.CSS_FIELD_ROW.equals(fieldStyle)) {
			addStyleName(CSS.FLOAT_CLEARFIX);
		}
	}

}
