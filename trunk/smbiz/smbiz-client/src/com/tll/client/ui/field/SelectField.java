/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.ListBox;

/**
 * SelectField
 * @author jpk
 */
public class SelectField extends AbstractDataMapField {

	protected ListBox lb;

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt
	 * @param dataMap
	 */
	public SelectField(String propName, String lblTxt, Map<String, String> dataMap) {
		super(propName, lblTxt, dataMap);
	}

	public ListBox getListBox() {
		if(lb == null) {
			lb = new ListBox(false);
			if(dataMap != null) {
				for(String n : dataMap.keySet()) {
					String v = dataMap.get(n);
					lb.addItem(n, v);
				}
			}
			lb.addFocusListener(this);
		}
		return lb;
	}

	@Override
	protected HasFocus getEditable(String value) {
		getListBox();
		if(value != null) {
			for(int i = 0; i < lb.getItemCount(); i++) {
				if(value.equals(lb.getValue(i))) {
					lb.setSelectedIndex(i);
					break;
				}
			}
		}
		return lb;
	}

	@Override
	protected String getEditableValue() {
		if(lb != null && lb.getSelectedIndex() >= 0) {
			return lb.getValue(lb.getSelectedIndex());
		}
		return null;
	}
}
