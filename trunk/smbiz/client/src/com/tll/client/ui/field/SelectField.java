/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.convert.ToStringConverter;
import com.tll.client.util.SimpleComparator;

/**
 * SelectField - Single select list box.
 * @author jpk
 * @param <I> The option "item" (element) type
 */
public final class SelectField<I> extends AbstractField<I> {

	/**
	 * The list box widget.
	 */
	private final ListBox lb;

	/**
	 * The list options.
	 */
	private Collection<I> options;

	/**
	 * The selected option.
	 */
	private I selected;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param options
	 */
	public SelectField(String name, String propName, String labelText, String helpText, Collection<I> options) {
		super(name, propName, labelText, helpText);
		setComparator(SimpleComparator.INSTANCE);
		lb = new ListBox();
		lb.addClickListener(this);
		lb.addChangeListener(this);
		setOptions(options);
	}

	/**
	 * Sets the options.
	 * @param options The options to set
	 */
	public void setOptions(Collection<I> options) {
		if(this.options == null) this.options = new ArrayList<I>();
		lb.clear();

		I newSelected = null;

		for(I item : options) {
			lb.addItem(ToStringConverter.INSTANCE.convert(getConverter() == null ? item : getConverter().convert(item)));
			if(selected != null && selected.equals(item)) {
				lb.setItemSelected(lb.getItemCount() - 1, true);
				newSelected = item;
			}
			this.options.add(item);
		}

		I old = selected;
		selected = newSelected;

		firePropertyChange(selected, old);
		fireChangeListeners();
	}

	private void firePropertyChange(I selected, I old) {
		if(changeSupport != null) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, selected);
		}
	}

	@Override
	protected HasFocus getEditable() {
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

	public void addItem(final I item) {
		options.add(item);
		lb.addItem(ToStringConverter.INSTANCE.convert(getConverter() == null ? item : getConverter().convert(item)));
	}

	public void removeItem(final Object o) {
		int i = 0;
		for(Iterator<I> it = this.options.iterator(); it.hasNext(); i++) {
			I option = it.next();
			if(getComparator().compare(option, o) == 0) {
				options.remove(option);
				removeItem(i);
			}
		}
	}

	public void removeItem(final int index) {
		lb.removeItem(index);
		update();
	}

	public int getSelectedIndex() {
		return lb.getSelectedIndex();
	}

	public I getValue() {
		return selected;
	}

	public void setValue(Object value) {
		if(options == null) throw new IllegalStateException("No options specified.");
		int i = 0;
		I old = selected;
		selected = null;

		for(Iterator<I> it = options.iterator(); it.hasNext(); i++) {
			I item = it.next();
			if(this.getComparator().compare(value, item) == 0) {
				lb.setItemSelected(i, true);
				selected = item;
				break;
			}
		}

		firePropertyChange(selected, old);
		fireChangeListeners();
	}

	public String getText() {
		final int si = getSelectedIndex();
		return si < 0 ? "" : lb.getItemText(si);
	}

	public void setText(String text) {
		setValue(text);
	}

	private void update() {
		I selected = null;
		Iterator<I> it = options.iterator();
		for(int i = 0; (i < lb.getItemCount()) && it.hasNext(); i++) {
			I item = it.next();
			if(lb.isItemSelected(i)) {
				selected = item;
				break;
			}
		}

		I old = this.selected;
		this.selected = selected;

		firePropertyChange(selected, old);
		fireChangeListeners();
	}

	@Override
	public void onChange(Widget sender) {
		super.onChange(sender);
		update();
	}

	@Override
	public void onClick(Widget sender) {
		super.onClick(sender);
		update();
	}
}
