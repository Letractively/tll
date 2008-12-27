/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.util.IRenderer;
import com.tll.client.util.SimpleComparator;
import com.tll.client.util.ToStringRenderer;

/**
 * SelectField
 * @author jpk
 */
public final class SelectField extends AbstractField<Object> {

	/**
	 * The list box widget.
	 */
	private ListBox lb;

	private ArrayList<Object> selected;

	private Collection<Object> options;

	private final Vector<Object> changeListeners = new Vector<Object>();

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt
	 */
	public SelectField(String propName, String lblTxt) {
		super(propName, lblTxt);
		setComparator(SimpleComparator.INSTANCE);
	}

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt
	 * @param options
	 */
	public SelectField(String propName, String lblTxt, Collection<Object> options) {
		this(propName, lblTxt);
		setOptions(options);
	}

	private ListBox getListBox() {
		if(lb == null) {
			lb.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					update();
				}
			});
			lb.addChangeListener(new ChangeListener() {

				public void onChange(Widget sender) {
					update();
				}
			});

			// lb.addFocusListener(this);
			lb.addChangeListener(this);
		}
		return lb;
	}

	@Override
	protected HasFocus getEditable(String value) {
		getListBox();
		if(value != null) {
			for(int i = 0; i < lb.getItemCount(); i++) {
				if(value.equals(lb.getValue(i))) {
					lb.setSelectedIndex(i);
					break;
				}
			}
		}
		return lb;
	}

	@Override
	protected String getEditableValue() {
		if(lb != null && lb.getSelectedIndex() >= 0) {
			return lb.getValue(lb.getSelectedIndex());
		}
		return null;
	}

	public void addChangeListener(ChangeListener listener) {
		getListBox().addChangeListener(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		getListBox().removeChangeListener(listener);
	}

	public int getItemCount() {
		int retValue;

		retValue = lb.getItemCount();

		return retValue;
	}

	public boolean isItemSelected(int index) {
		boolean retValue;

		retValue = lb.isItemSelected(index);

		return retValue;
	}

	public void setItemText(int index, String text) {
		lb.setItemText(index, text);
	}

	public String getItemText(int index) {
		String retValue;

		retValue = lb.getItemText(index);

		return retValue;
	}

	public void setMultipleSelect(boolean multiple) {
		lb.setMultipleSelect(multiple);
		if(selected.size() > 1) {
			Object o = selected.get(0);
			selected.clear();
			selected.add(o);
		}
	}

	public boolean isMultipleSelect() {
		return lb.isMultipleSelect();
	}

	public void setName(String name) {
		lb.setName(name);
	}

	public String getName() {
		String retValue;

		retValue = lb.getName();

		return retValue;
	}

	public void setOptions(Collection<Object> options) {
		if(this.options == null) this.options = new ArrayList<Object>();
		this.options = new ArrayList<Object>();
		lb.clear();

		ArrayList<Object> newSelected = new ArrayList<Object>();

		for(Object item : options) {
			lb.addItem(ToStringRenderer.INSTANCE.render(getRenderer().render(item)));

			if(contains(selected, item)) {
				lb.setItemSelected(this.lb.getItemCount() - 1, true);
				newSelected.add(item);
			}

			this.options.add(item);
		}

		ArrayList<Object> old = selected;
		this.selected = newSelected;

		if(isMultipleSelect()) {
			changeSupport.firePropertyChange("value", old, selected);
		}
		else {
			Object prev = ((old == null) || (old.size() == 0)) ? null : old.get(0);
			Object curr = (this.selected.size() == 0) ? null : this.selected.get(0);
			changeSupport.firePropertyChange("value", prev, curr);
		}

		fireChangeListeners();
	}

	public Collection<Object> getOptions() {
		return options;
	}

	@Override
	public void setRenderer(IRenderer<String, Object> renderer) {
		super.setRenderer(renderer);
		setOptions(options);
	}

	public int getSelectedIndex() {
		int retValue;
		retValue = lb.getSelectedIndex();
		return retValue;
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
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
			changeSupport.firePropertyChange("value", old, selected);
		}
		else {
			Object prev = ((old == null) || (old.size() == 0)) ? null : old.get(0);
			Object curr = (this.selected.size() == 0) ? null : this.selected.get(0);
			changeSupport.firePropertyChange("value", prev, curr);
		}

		fireChangeListeners();
	}

	public Object getValue() {
		final Object returnValue;
		if(lb.isMultipleSelect()) {
			returnValue = selected;
		}
		else if(selected.size() == 0) {
			returnValue = null;
		}
		else {
			returnValue = selected.get(0);
		}
		return returnValue;
	}

	public void setVisibleItemCount(final int visibleItems) {
		lb.setVisibleItemCount(visibleItems);
	}

	public int getVisibleItemCount() {
		return lb.getVisibleItemCount();
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

	private boolean contains(final Collection<Object> c, final Object o) {
		for(Iterator<Object> it = c.iterator(); it.hasNext();) {
			Object next = it.next();
			if(getComparator().compare(o, next) == 0) {
				return true;
			}
		}
		return false;
	}

	private void fireChangeListeners() {
		for(Iterator<Object> it = this.changeListeners.iterator(); it.hasNext();) {
			ChangeListener l = (ChangeListener) it.next();
			l.onChange(this);
		}
		if(getAction() != null) {
			this.getAction().execute(this);
		}
	}

	private void update() {
		ArrayList<Object> selected = new ArrayList<Object>();
		Iterator<Object> it = this.options.iterator();
		for(int i = 0; (i < lb.getItemCount()) && it.hasNext(); i++) {
			Object item = it.next();
			if(lb.isItemSelected(i)) {
				selected.add(item);
			}
		}

		ArrayList<Object> old = this.selected;
		this.selected.clear();
		this.selected.addAll(selected);
		// this.selected = selected;

		if(isMultipleSelect()) {
			changeSupport.firePropertyChange("value", old, selected);
		}
		else {
			Object prev = ((old == null) || (old.size() == 0)) ? null : old.get(0);
			Object curr = (this.selected.size() == 0) ? null : this.selected.get(0);
			changeSupport.firePropertyChange("value", prev, curr);
		}

		fireChangeListeners();
	}

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
}
