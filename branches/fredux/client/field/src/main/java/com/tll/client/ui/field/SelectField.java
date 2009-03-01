/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ListBox;
import com.tll.common.util.ObjectUtil;

/**
 * SelectField - Single select list box.
 * @author jpk
 */
public final class SelectField extends AbstractDataField<String> {

	/**
	 * Impl
	 * @author jpk
	 */
	final class Impl extends ListBox implements IEditable<String>, ChangeHandler {

		/**
		 * Constructor
		 */
		public Impl() {
			super(false);
			addChangeHandler(this);
		}

		@Override
		public void onChange(ChangeEvent event) {
			ValueChangeEvent.fire(this, getValue());
		}

		@Override
		public String getValue() {
			final int i = getSelectedIndex();
			return i == -1 ? null : getValue(i);
		}

		@Override
		public void setValue(String value, boolean fireEvents) {
			final String old = getValue();
			setValue(value);
			final String nval = getValue();
			if(fireEvents && !ObjectUtil.equals(old, nval)) {
				ValueChangeEvent.fire(this, nval);
			}
		}

		@Override
		public void setValue(String value) {
			setSelectedIndex(-1);
			for(int i = 0; i < getItemCount(); i++) {
				if(value.equals(getValue(i))) {
					setSelectedIndex(i);
					return;
				}
			}
		}

		@Override
		public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
			return addHandler(handler, ValueChangeEvent.getType());
		}
	}
	
	/**
	 * The list box widget.
	 */
	private final Impl lb;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param data
	 */
	SelectField(String name, String propName, String labelText, String helpText, Map<String, String> data) {
		super(name, propName, labelText, helpText);
		lb = new Impl();
		lb.addValueChangeHandler(this);
		setData(data);
	}

	@Override
	public void setData(Map<String, String> data) {
		final String oldval = lb.getValue();
		lb.clear();
		for(final String val : data.keySet()) {
			final String key = data.get(val);
			lb.addItem(key, val);
			if(val.equals(oldval)) {
				lb.setItemSelected(lb.getItemCount() - 1, true);
			}
		}
	}

	@Override
	public void addDataItem(String name, String value) {
		lb.addItem(name, value);
	}

	@Override
	public void removeDataItem(String value) {
		for(int i = 0; i < lb.getItemCount(); i++) {
			if(lb.getValue(i).equals(value)) {
				lb.removeItem(i);
				return;
			}
		}
	}

	@Override
	protected IEditable<String> getEditable() {
		return lb;
	}

	public int getItemCount() {
		return lb.getItemCount();
	}

	public boolean isItemSelected(int index) {
		return lb.isItemSelected(index);
	}

	public String getItemText(int index) {
		return lb.getItemText(index);
	}

	public void setItemText(int index, String text) {
		lb.setItemText(index, text);
	}

	public int getVisibleItemCount() {
		return lb.getVisibleItemCount();
	}

	public void setVisibleItemCount(final int visibleItems) {
		lb.setVisibleItemCount(visibleItems);
	}

	public int getSelectedIndex() {
		return lb.getSelectedIndex();
	}

	public String getText() {
		return lb.getSelectedIndex() == -1 ? null : lb.getItemText(lb.getSelectedIndex());
	}

	public void setText(String text) {
		throw new UnsupportedOperationException();
	}
}
