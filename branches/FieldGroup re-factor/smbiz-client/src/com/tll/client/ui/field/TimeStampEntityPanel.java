/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.ui.field;

import com.tll.client.util.GlobalFormat;

/**
 * TimeStampEntityPanel
 * @author jpk
 */
public abstract class TimeStampEntityPanel extends FieldGroupPanel {

	protected boolean bindTimestampFields = true;
	protected TextField dateCreated;
	protected TextField dateModified;

	/**
	 * Constructor
	 * @param propName
	 * @param displayName
	 */
	public TimeStampEntityPanel(String propName, String displayName) {
		super(propName, displayName);
	}

	public void setBindTimestampFields(boolean bindTimestampFields) {
		this.bindTimestampFields = bindTimestampFields;
	}

	@Override
	protected void configure() {
		if(bindTimestampFields) {
			dateCreated = fdate("dateCreated", "Created", GlobalFormat.DATE);
			dateModified = fdate("dateModified", "Modified", GlobalFormat.DATE);
			dateCreated.setReadOnly(true);
			dateModified.setReadOnly(true);
			// dateCreated.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			// dateModified.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

			fields.addField(dateCreated);
			fields.addField(dateModified);
		}
	}

}
