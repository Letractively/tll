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
import com.tll.common.util.ObjectUtil;

/**
 * RadioGroupField
 * @param <B> The bound type
 * @author jpk
 */
public final class RadioGroupField<B> extends AbstractField<B, String> {

	private final FocusPanel fp = new FocusPanel();

	/**
	 * Panel that contains only the radio buttons. Is either vertical or
	 * horizontal.
	 */
	private final CellPanel rbPanel;

	/**
	 * The options.
	 */
	private List<B> options;

	/**
	 * List of radio buttons contained in {@link #rbPanel}. There is one for each
	 * option.
	 */
	private final List<RadioButton> radioButtons = new ArrayList<RadioButton>();

	private String old;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param options
	 * @param converter
	 * @param renderHorizontal
	 */
	RadioGroupField(String name, String propName, String labelText, String helpText, Collection<B> options,
			IConverter<String, B> converter, boolean renderHorizontal) {
		super(name, propName, labelText, helpText);
		setConverter(converter);
		if(renderHorizontal) {
			rbPanel = new HorizontalPanel();
		}
		else {
			rbPanel = new VerticalPanel();
		}
		fp.add(rbPanel);
		setOptions(options);
		// fp.addFocusListener(this);
		addChangeListener(this);
	}

	/**
	 * Builds or re-builds the radio buttons firing change events if the current
	 * value becomes orphaned.
	 * @param options
	 */
	public void setOptions(Collection<B> options) {
		if(options == null || options.size() < 1) {
			throw new IllegalArgumentException("No options specified.");
		}
		boolean valueBound = false;
		old = getValue();
		rbPanel.clear();
		radioButtons.clear();
		IConverter<String, B> converter = getConverter();
		for(B n : options) {
			String sval = converter.convert(n);
			RadioButton rb = new RadioButton("rg_" + getDomId(), sval);
			rb.setStyleName(Styles.LABEL);
			rb.addClickListener(this);
			rbPanel.add(rb);
			radioButtons.add(rb);
			if(sval == old || (sval != null && sval.equals(old))) {
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

	@Override
	protected void setNativeValue(String nativeValue) {
		final String old = getValue();
		int i = 0;
		for(Object o : options) {
			if(o.equals(nativeValue)) {
				radioButtons.get(i).setChecked(true);
			}
			i++;
		}
		if(!ObjectUtil.equals(old, nativeValue)) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, nativeValue);
		}
	}

	@Override
	protected void doSetValue(B value) {
		setNativeValue(getConverter().convert(value));
	}

	public String getText() {
		return getValue();
	}

	public void setText(String text) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onClick(Widget sender) {
		super.onClick(sender);
		String cv = getValue();
		changeSupport.firePropertyChange(PROPERTY_VALUE, old, cv);
		old = cv;
		fireChangeListeners();
	}
}
