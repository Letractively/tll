/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.field.IField;

/**
 * RadioGroupField
 * @author jpk
 */
public final class RadioGroupField extends AbstractDataMapField {

	private final FocusPanel fp = new FocusPanel();

	/**
	 * Panel that contains only the radio buttons. Is either vertical or
	 * horizontal.
	 */
	private final CellPanel rbPanel;

	/**
	 * List of radio buttons contained in {@link #rbPanel}.
	 */
	private final List<RadioButton> radioButtons = new ArrayList<RadioButton>();

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt
	 * @param lblMode
	 * @param dataMap
	 * @param renderHorizontal
	 */
	public RadioGroupField(String propName, String lblTxt, int lblMode, Map<String, String> dataMap,
			boolean renderHorizontal) {
		super(propName, lblTxt, lblMode, dataMap);
		if(renderHorizontal) {
			rbPanel = new HorizontalPanel();
		}
		else {
			rbPanel = new VerticalPanel();
		}
		fp.add(rbPanel);
		fp.addFocusListener(this);
	}

	public RadioButton[] getRadioButtons() {
		if(rbPanel.getWidgetCount() == 0) {
			if(dataMap != null) {
				for(String n : dataMap.keySet()) {
					RadioButton rb = new RadioButton("rg_" + domId, n);
					rb.setStyleName(IField.CSS_FIELD_LABEL);
					rb.addClickListener(this);
					rbPanel.add(rb);
					radioButtons.add(rb);
				}
			}
		}
		return radioButtons.toArray(new RadioButton[radioButtons.size()]);
	}

	@Override
	public HasFocus getEditable(String value) {
		getRadioButtons();
		if(value != null) {
			for(RadioButton rb : radioButtons) {
				String rbVal = dataMap.get(rb.getText());
				rb.setChecked(value != null && value.equals(rbVal));
			}
		}
		return fp;
	}

	@Override
	public String getEditableValue() {
		if(radioButtons != null && dataMap != null) {
			for(RadioButton rb : radioButtons) {
				if(rb.isChecked()) {
					return dataMap.get(rb.getText());
				}
			}
		}
		return null;
	}

	@Override
	public void onClick(Widget sender) {
		for(RadioButton rb : radioButtons) {
			if(sender == rb) {
				onChange(this);
				return;
			}
		}
		super.onClick(sender);
	}

	public IField copy() {
		return new RadioGroupField(propName, lblTxt, lblMode, dataMap, (rbPanel instanceof HorizontalPanel));
	}

}
