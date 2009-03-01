/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ListBox;
import com.tll.client.convert.IConverter;

/**
 * SelectField
 * @author jpk
 */
public final class MultiSelectField extends AbstractDataField<Collection<String>> {

	/**
	 * Impl
	 * @author jpk
	 */
	final class Impl extends ListBox implements IEditable<Collection<String>>, ChangeHandler {

		/**
		 * Constructor
		 */
		public Impl() {
			super(true);
			addChangeHandler(this);
		}

		@Override
		public void onChange(ChangeEvent event) {
			ValueChangeEvent.fire(this, getValue());
		}

		@Override
		public Collection<String> getValue() {
			final ArrayList<String> sel = new ArrayList<String>(); 
			for(int i = 0; i < getItemCount(); i++) {
				if(isItemSelected(i)) {
					sel.add(getValue(i));
				}
			}
			return sel;
		}

		@Override
		public void setValue(Collection<String> value, boolean fireEvents) {
			setValue(value);
			if(fireEvents) {
				ValueChangeEvent.fire(this, getValue());
			}
		}

		@Override
		public void setValue(Collection<String> value) {
			setSelectedIndex(-1);
			for(int i = 0; i < getItemCount(); i++) {
				for(final String val : value) {
					if(val.equals(getValue(i))) {
						setItemSelected(i, true);
					}
				}
			}
		}

		@Override
		public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Collection<String>> handler) {
			return addHandler(handler, ValueChangeEvent.getType());
		}
	}
	
	/**
	 * Converter
	 * @author jpk
	 */
	static final class Converter implements IConverter<Collection<String>, Object> {

		@SuppressWarnings("unchecked")
		@Override
		public Collection<String> convert(Object in) throws IllegalArgumentException {
			try {
				return (Collection<String>) in;
			}
			catch(final ClassCastException e) {
				throw new IllegalArgumentException("The value must be a collection of strings.");
			}
		}
	}

	private static final Converter CONVERTER = new Converter();

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
	MultiSelectField(String name, String propName, String labelText, String helpText, Map<String, String> data) {
		super(name, propName, labelText, helpText);
		lb = new Impl();
		lb.addValueChangeHandler(this);
		setData(data);
	}

	@Override
	protected IConverter<Collection<String>, Object> getConverter() {
		return CONVERTER;
	}

	/**
	 * Sets the options.
	 * @param data The options to set
	 */
	@Override
	public void setData(Map<String, String> data) {
		lb.clear();
		for(final String val : data.keySet()) {
			final String key = data.get(val);
			lb.addItem(key, val);
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
	protected IEditable<Collection<String>> getEditable() {
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
		// comma delimit
		final StringBuilder sb = new StringBuilder();
		for(int i = 0; i < lb.getItemCount(); i++) {
			if(lb.isItemSelected(i)) {
				sb.append(',');
				sb.append(lb.getValue(i));
			}
		}
		return sb.substring(1);
	}

	public void setText(String text) {
		throw new UnsupportedOperationException();
	}

}
