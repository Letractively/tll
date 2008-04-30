/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.field.IField;
import com.tll.client.util.GlobalFormat;

/**
 * TimeStampEntityPanel
 * @author jpk
 */
public abstract class TimeStampEntityPanel extends FieldGroupPanel {

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

	@Override
	protected void configure() {
		dateCreated = fdate("dateCreated", "Created", IField.LBL_ABOVE, GlobalFormat.DATE);
		dateModified = fdate("dateModified", "Modified", IField.LBL_ABOVE, GlobalFormat.DATE);
		dateCreated.setReadOnly(true);
		dateModified.setReadOnly(true);
		dateCreated.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		dateModified.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		fields.addField(dateCreated);
		fields.addField(dateModified);
	}

}
