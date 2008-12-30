/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.renderer.ToStringRenderer;
import com.tll.client.util.SimpleComparator;

/**
 * SelectField
 * @author jpk
 */
public final class SelectField extends AbstractField<Object> {

	/**
	 * The list box widget.
	 */
	private final ListBox lb;

	/**
	 * The list options.
	 */
	private Collection<Object> options;

	/**
	 * The selected list options.
	 */
	private ArrayList<Object> selected;

	/**
	 * Constructor
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param isMultipleSelect
	 */
	public SelectField(String propName, String labelText, String helpText, boolean isMultipleSelect) {
		super(propName, labelText, helpText);
		setComparator(SimpleComparator.INSTANCE);
		lb = new ListBox(isMultipleSelect);
		lb.addClickListener(this);
		lb.addChangeListener(this);
		// lb.addFocusListener(this);
		lb.addChangeListener(this);

	}

	/**
	 * Constructor
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param isMultipleSelect
	 * @param options
	 */
	public SelectField(String propName, String labelText, String helpText, boolean isMultipleSelect,
			Collection<? extends Object> options) {
		this(propName, labelText, helpText, isMultipleSelect);
		setOptions(options);
	}

	/**
	 * Sets the options.
	 * @param options The options to set
	 */
	public void setOptions(Collection<? extends Object> options) {
		if(this.options == null) this.options = new ArrayList<Object>();
		lb.clear();

		ArrayList<Object> newSelected = new ArrayList<Object>();

		for(Object item : options) {
			lb.addItem(ToStringRenderer.INSTANCE.render(getRenderer() == null ? item : getRenderer().render(item)));
			if(contains(selected, item)) {
				lb.setItemSelected(this.lb.getItemCount() - 1, true);
				newSelected.add(item);
			}
			this.options.add(item);
		}

		ArrayList<Object> old = selected;
		this.selected = newSelected;

		if(isMultipleSelect()) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, selected);
		}
		else {
			Object prev = ((old == null) || (old.size() == 0)) ? null : old.get(0);
			Object curr = (selected.size() == 0) ? null : selected.get(0);
			changeSupport.firePropertyChange(PROPERTY_VALUE, prev, curr);
		}

		fireWidgetChange();
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

	public boolean isMultipleSelect() {
		return lb.isMultipleSelect();
	}

	public void setMultipleSelect(boolean multiple) {
		lb.setMultipleSelect(multiple);
		if(selected.size() > 1) {
			Object o = selected.get(0);
			selected.clear();
			selected.add(o);
		}
	}

	public int getVisibleItemCount() {
		return lb.getVisibleItemCount();
	}

	public void setVisibleItemCount(final int visibleItems) {
		lb.setVisibleItemCount(visibleItems);
	}

	public void addItem(final Object o) {
		options.add(o);
		lb.addItem(ToStringRenderer.INSTANCE.render(getRenderer().render(o)));
	}

	public void removeItem(final Object o) {
		int i = 0;
		for(Iterator<Object> it = this.options.iterator(); it.hasNext(); i++) {
			Object option = it.next();
			if(getComparator().compare(option, o) == 0) {
				options.remove(option);
				lb.removeItem(i);
				update();
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

	public Object getValue() {
		return lb.isMultipleSelect() ? selected : (selected == null ? null
				: (selected.size() == 0 ? null : selected.get(0)));
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		if(options == null) throw new IllegalStateException("No options specified.");
		int i = 0;
		ArrayList<Object> old = selected;
		selected = new ArrayList<Object>();

		if(value instanceof Collection) {
			Collection<Object> c = (Collection<Object>) value;

			for(Iterator<Object> it = this.options.iterator(); it.hasNext(); i++) {
				Object item = it.next();

				if(contains(c, item)) {
					lb.setItemSelected(i, true);
					selected.add(item);
				}
				else {
					lb.setItemSelected(i, false);
				}
			}
		}
		else {
			for(Iterator<Object> it = options.iterator(); it.hasNext(); i++) {
				Object item = it.next();

				if(this.getComparator().compare(value, item) == 0) {
					lb.setItemSelected(i, true);
				}
				else {
					lb.setItemSelected(i, false);
				}
			}

			this.selected.add(value);
		}

		if(this.isMultipleSelect()) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, selected);
		}
		else {
			Object prev = ((old == null) || (old.size() == 0)) ? null : old.get(0);
			Object curr = (this.selected.size() == 0) ? null : this.selected.get(0);
			changeSupport.firePropertyChange(PROPERTY_VALUE, prev, curr);
		}

		fireWidgetChange();
	}

	public String getText() {
		final int si = getSelectedIndex();
		return si < 0 ? "" : lb.getItemText(si);
	}

	public void setText(String text) {
		throw new UnsupportedOperationException();
	}

	private boolean contains(final Collection<Object> c, final Object o) {
		final Comparator<Object> cmp = getComparator();
		for(Object next : c) {
			if(cmp.compare(o, next) == 0) {
				return true;
			}
		}
		return false;
	}

	private void update() {
		ArrayList<Object> selected = new ArrayList<Object>();
		Iterator<Object> it = options.iterator();
		for(int i = 0; (i < lb.getItemCount()) && it.hasNext(); i++) {
			Object item = it.next();
			if(lb.isItemSelected(i)) {
				selected.add(item);
			}
		}

		ArrayList<Object> old = selected;
		selected.clear();
		selected.addAll(selected);
		// this.selected = selected;

		if(isMultipleSelect()) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, selected);
		}
		else {
			Object prev = ((old.size() == 0)) ? null : old.get(0);
			Object curr = (selected.size() == 0) ? null : selected.get(0);
			changeSupport.firePropertyChange(PROPERTY_VALUE, prev, curr);
		}

		fireWidgetChange();
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

	/*
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) return true;
		if(obj == null || (obj instanceof SelectField == false)) return false;
		final SelectField other = (SelectField) obj;
		return ((this.options != other.options) && ((this.options == null) || !this.options.equals(other.options)));
	}

	@Override
	public int hashCode() {
		return lb.hashCode();
	}
	*/
}
