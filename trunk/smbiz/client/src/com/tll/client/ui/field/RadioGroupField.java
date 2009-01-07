/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.convert.IConverter;
import com.tll.client.convert.ToStringConverter;
import com.tll.client.util.ObjectUtil;

/**
 * RadioGroupField
 * @author jpk
 */
public final class RadioGroupField extends AbstractField<String> {

	private final FocusPanel fp = new FocusPanel();

	/**
	 * Panel that contains only the radio buttons. Is either vertical or
	 * horizontal.
	 */
	private final CellPanel rbPanel;

	/**
	 * The options.
	 */
	private List<?> options;

	/**
	 * List of radio buttons contained in {@link #rbPanel}. There is one for each
	 * option.
	 */
	private final List<RadioButton> radioButtons = new ArrayList<RadioButton>();

	private String old;

	/**
	 * Constructor
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param options
	 * @param renderHorizontal
	 */
	public RadioGroupField(String propName, String labelText, String helpText, Collection<?> options,
			boolean renderHorizontal) {
		super(propName, labelText, helpText);
		setConverter(ToStringConverter.INSTANCE);
		setOptions(options);
		if(renderHorizontal) {
			rbPanel = new HorizontalPanel();
		}
		else {
			rbPanel = new VerticalPanel();
		}
		fp.add(rbPanel);
		// fp.addFocusListener(this);
		addChangeListener(this);
	}

	/**
	 * Builds or re-builds the radio buttons firing change events if the current
	 * value becomes orphaned.
	 * @param options
	 */
	public void setOptions(Collection<?> options) {
		if(options == null || options.size() < 1) {
			throw new IllegalArgumentException("No options specified.");
		}
		boolean valueBound = false;
		old = getValue();
		rbPanel.clear();
		radioButtons.clear();
		IConverter<String, Object> converter = getConverter();
		for(Object n : options) {
			Object co = converter.convert(n);
			String sval = co == null ? null : co.toString();
			RadioButton rb = new RadioButton("rg_" + getDomId(), sval);
			rb.setStyleName(IField.STYLE_FIELD_LABEL);
			rb.addClickListener(this);
			rbPanel.add(rb);
			radioButtons.add(rb);
			assert co != null;
			if(co == old || co.equals(old)) {
				valueBound = true;
			}
		}
		if(!valueBound && old != null) {
			if(changeSupport != null) changeSupport.firePropertyChange(PROPERTY_VALUE, old, null);
			fireChangeListeners();
		}
	}

	@Override
	public HasFocus getEditable() {
		return fp;
	}

	public String getValue() {
		int i = 0;
		for(RadioButton rb : radioButtons) {
			if(rb.isChecked()) {
				return getConverter().convert(options.get(i));
			}
			i++;
		}
		return null;
	}

	public void setValue(Object value) {
		setText(getConverter().convert(value));
	}

	public String getText() {
		return getValue();
	}

	public void setText(String text) {
		final String old = getValue();
		int i = 0;
		for(Object o : options) {
			if(o.equals(text)) {
				radioButtons.get(i).setChecked(true);
			}
			i++;
		}
		if(changeSupport != null && !ObjectUtil.equals(old, text)) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, text);
		}
	}

	@Override
	public void onClick(Widget sender) {
		super.onClick(sender);
		String cv = getValue();
		if(changeSupport != null) changeSupport.firePropertyChange(PROPERTY_VALUE, old, cv);
		old = cv;
		fireChangeListeners();
	}
}
