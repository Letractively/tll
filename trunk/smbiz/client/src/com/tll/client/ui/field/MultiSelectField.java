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
 * @param <I> The option "item" (element) type
 */
public final class MultiSelectField<I> extends AbstractField<Collection<I>> {

	/**
	 * The list box widget.
	 */
	private final ListBox lb;

	/**
	 * The list options.
	 */
	private Collection<I> options;

	/**
	 * The selected list options.
	 */
	private ArrayList<I> selected;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param options
	 */
	public MultiSelectField(String name, String propName, String labelText, String helpText, Collection<I> options) {
		super(name, propName, labelText, helpText);
		setComparator(SimpleComparator.INSTANCE);
		lb = new ListBox(true);
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

		ArrayList<I> newSelected = new ArrayList<I>();

		for(I item : options) {
			lb.addItem(ToStringConverter.INSTANCE.convert(getConverter() == null ? item : getConverter().convert(item)));
			if(selected != null && contains(selected, item)) {
				lb.setItemSelected(this.lb.getItemCount() - 1, true);
				newSelected.add(item);
			}
			this.options.add(item);
		}

		ArrayList<I> old = selected;
		selected = newSelected;

		firePropertyChange(selected, old);
		fireChangeListeners();
	}

	private void firePropertyChange(ArrayList<I> selected, ArrayList<I> old) {
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

	public Collection<I> getValue() {
		return selected;
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		if(options == null) throw new IllegalStateException("No options specified.");
		int i = 0;
		ArrayList<I> old = selected;
		selected = new ArrayList<I>();

		if(value instanceof Collection) {
			Collection<I> c = (Collection<I>) value;

			for(Iterator<I> it = options.iterator(); it.hasNext(); i++) {
				I item = it.next();

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
			for(Iterator<I> it = options.iterator(); it.hasNext(); i++) {
				Object item = it.next();

				if(this.getComparator().compare(value, item) == 0) {
					lb.setItemSelected(i, true);
				}
				else {
					lb.setItemSelected(i, false);
				}
			}

			selected.add((I) value);
		}

		firePropertyChange(selected, old);

		fireChangeListeners();
	}

	public String getText() {
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

	public void setText(String text) {
		setValue(text);
	}

	private boolean contains(final Collection<I> c, final Object o) {
		final Comparator<Object> cmp = getComparator();
		for(I next : c) {
			if(cmp.compare(o, next) == 0) {
				return true;
			}
		}
		return false;
	}

	private void update() {
		ArrayList<I> selected = new ArrayList<I>();
		Iterator<I> it = options.iterator();
		for(int i = 0; (i < lb.getItemCount()) && it.hasNext(); i++) {
			I item = it.next();
			if(lb.isItemSelected(i)) {
				selected.add(item);
			}
		}

		ArrayList<I> old = this.selected;
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
