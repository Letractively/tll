/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.ui.field;

import com.tll.client.field.IField.LabelMode;

/**
 * NamedTimeStampEntityPanel
 * @author jpk
 */
public abstract class NamedTimeStampEntityPanel extends TimeStampEntityPanel {

	protected TextField name;

	/**
	 * Constructor
	 * @param propName
	 * @param displayName
	 */
	public NamedTimeStampEntityPanel(String propName, String displayName) {
		super(propName, displayName);
	}

	@Override
	protected void configure() {
		super.configure();

		name = ftext("name", "Name", LabelMode.ABOVE, 30);

		fields.addField(name);
	}

}
