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
import com.tll.client.convert.ToStringConverter;
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
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param multiSelect
	 * @param options
	 */
	public SelectField(String name, String propName, String labelText, String helpText, boolean multiSelect,
			Collection<?> options) {
		super(name, propName, labelText, helpText);
		setComparator(SimpleComparator.INSTANCE);
		lb = new ListBox(multiSelect);
		lb.addClickListener(this);
		lb.addChangeListener(this);
		setOptions(options);
	}

	/**
	 * Sets the options.
	 * @param options The options to set
	 */
	public void setOptions(Collection<?> options) {
		if(this.options == null) this.options = new ArrayList<Object>();
		lb.clear();

		ArrayList<Object> newSelected = new ArrayList<Object>();

		for(Object item : options) {
			lb.addItem(ToStringConverter.INSTANCE.convert(getConverter() == null ? item : getConverter().convert(item)));
			if(selected != null && contains(selected, item)) {
				lb.setItemSelected(this.lb.getItemCount() - 1, true);
				newSelected.add(item);
			}
			this.options.add(item);
		}

		ArrayList<Object> old = selected;
		selected = newSelected;

		firePropertyChange(selected, old);
		fireChangeListeners();
	}

	private void firePropertyChange(ArrayList<Object> selected, ArrayList<Object> old) {
		if(changeSupport != null) {
			if(isMultipleSelect()) {
				changeSupport.firePropertyChange(PROPERTY_VALUE, old, selected);
			}
			else {
				Object prev = ((old == null) || (old.size() == 0)) ? null : old.get(0);
				Object curr = (selected == null || selected.size() == 0) ? null : selected.get(0);
				changeSupport.firePropertyChange(PROPERTY_VALUE, prev, curr);
			}
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

	public boolean isMultipleSelect() {
		return lb.isMultipleSelect();
	}

	public void setMultipleSelect(boolean multiple) {
		lb.setMultipleSelect(multiple);
		if(selected != null && selected.size() > 1) {
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
		lb.addItem(ToStringConverter.INSTANCE.convert(getConverter().convert(o)));
	}

	public void removeItem(final Object o) {
		int i = 0;
		for(Iterator<Object> it = this.options.iterator(); it.hasNext(); i++) {
			Object option = it.next();
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

			selected.add(value);
		}

		firePropertyChange(selected, old);

		fireChangeListeners();
	}

	public String getText() {
		if(isMultipleSelect()) {
			// comma delimit
			if(selected != null) {
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < selected.size(); i++) {
					sb.append(ToStringConverter.INSTANCE.convert(selected.get(i)));
					if(i < selected.size() - 1) {
						sb.append(',');
					}
				}
				return sb.toString();
			}
			return "";
		}
		final int si = getSelectedIndex();
		return si < 0 ? "" : lb.getItemText(si);
	}

	public void setText(String text) {
		setValue(text);
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

		ArrayList<Object> old = this.selected;
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
