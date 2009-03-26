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
import com.tll.util.ObjectUtil;

/**
 * SelectField - Single select list box.
 * @param <V> the value type
 * @author jpk
 */
public final class SelectField<V> extends AbstractDataField<V, V> {

	/**
	 * Impl
	 * @author jpk
	 */
	final class Impl extends ListBox implements IEditable<V>, ChangeHandler {

		/**
		 * Constructor
		 */
		public Impl() {
			super(false);
			addStyleName(Styles.TBOX);
			addChangeHandler(this);
		}

		@Override
		public void onChange(ChangeEvent event) {
			ValueChangeEvent.fire(this, getValue());
		}

		@Override
		public V getValue() {
			final int i = getSelectedIndex();
			return i == -1 ? null : getDataValue(getValue(i));
		}

		@Override
		public void setValue(V value, boolean fireEvents) {
			final V old = fireEvents ? getValue() : null;
			setValue(value);
			if(fireEvents) {
				final V nval = getValue();
				if(!ObjectUtil.equals(old, nval)) {
					ValueChangeEvent.fire(this, nval);
				}
			}
		}

		@Override
		public void setValue(V value) {
			setSelectedIndex(-1);
			if(value != null) {
				for(int i = 0; i < getItemCount(); i++) {
					if(value.equals(getDataValue(getValue(i)))) {
						setSelectedIndex(i);
						return;
					}
				}
			}
		}

		@Override
		public HandlerRegistration addValueChangeHandler(ValueChangeHandler<V> handler) {
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
	SelectField(String name, String propName, String labelText, String helpText, Map<V, String> data) {
		super(name, propName, labelText, helpText);
		lb = new Impl();
		lb.addValueChangeHandler(this);
		lb.addBlurHandler(this);
		setData(data);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		lb.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	@Override
	public void setData(Map<V, String> data) {
		super.setData(data);
		final V oldval = lb.getValue();
		lb.clear();
		for(final V val : data.keySet()) {
			lb.addItem(data.get(val));
			if(ObjectUtil.equals(val, oldval)) {
				lb.setItemSelected(lb.getItemCount() - 1, true);
			}
		}
	}

	@Override
	public void addDataItem(String name, V value) {
		super.addDataItem(name, value);
		lb.addItem(name);
	}

	@Override
	public void removeDataItem(V value) {
		super.removeDataItem(value);
		for(int i = 0; i < lb.getItemCount(); i++) {
			if(lb.getValue(i).equals(value)) {
				lb.removeItem(i);
				return;
			}
		}
	}

	@Override
	public IEditable<V> getEditable() {
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