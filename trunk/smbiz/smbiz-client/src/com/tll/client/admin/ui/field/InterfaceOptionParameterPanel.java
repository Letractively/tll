package com.tll.client.admin.ui.field;

import com.tll.client.field.IField;
import com.tll.client.ui.field.FieldPanel;

/**
 * ParameterPanel - Interface option parameter definition panel.
 * @author jpk
 */
final class InterfaceOptionParameterPanel extends InterfaceRelatedPanel {

	/**
	 * Constructor
	 */
	public InterfaceOptionParameterPanel() {
		super(null, null);
	}

	@Override
	protected void configure() {
		super.configure();

		FieldPanel frow;

		// first row
		frow = new FieldPanel(IField.CSS_FIELD_ROW);
		add(frow);
		frow.add(name);
		frow.add(code);
		frow.add(description);
	}
}