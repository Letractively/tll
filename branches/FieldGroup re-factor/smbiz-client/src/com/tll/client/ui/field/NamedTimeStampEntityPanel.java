/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.ui.field;

/**
 * NamedTimeStampEntityPanel
 * @author jpk
 */
public abstract class NamedTimeStampEntityPanel extends TimeStampEntityPanel {

	protected boolean bindNameField = true;
	protected TextField name;

	/**
	 * Constructor
	 * @param displayName
	 */
	public NamedTimeStampEntityPanel(String displayName) {
		super(displayName);
	}

	public void setBindNameField(boolean bindNameField) {
		this.bindNameField = bindNameField;
	}

	@Override
	protected void doInit() {
		super.doInit();

		if(bindNameField) {
			name = ftext("name", "Name", 30);
			fields.addField(name);
		}
	}

}
