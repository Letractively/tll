/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.IBindable;
import com.tll.client.bind.IPropertyChangeListener;
import com.tll.client.bind.PropertyChangeSupport;
import com.tll.client.model.MalformedPropPathException;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.PropertyPathException;
import com.tll.client.msg.Msg;
import com.tll.client.validate.CompositeValidator;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;

/**
 * FieldGroup - A group of {@link IField}s which may in turn be nested
 * {@link FieldGroup}s. Thus a FieldGroup is a hierarchical collection of
 * {@link IField}s.
 * <p>
 * Non-FieldGroup children of {@link FieldGroup}s are expected to have a unique
 * property name thus allowing any contained field to be retrieved via a
 * property path.
 * <p>
 * A FieldGroup represents a grouping of {@link IField}s for UI purposes and as
 * such <em>does not necessarily represent model hierarchy boundaries</em>
 * <p>
 * <b>IMPT: </b> {@link FieldGroup}s do <em>NOT</em> (as yet) support handling
 * of circular references! As such, do not add a field that is already added to
 * another iterable field construct that are both reachable under a commot field
 * iterable construct.
 * @author jpk
 */
public final class FieldGroup implements IField<Object>, Iterable<IField<?>> {

	/**
	 * Recursively searches for a single field whose property name matches the
	 * given property name
	 * @param propertyName The property name to search for
	 * @param group The group to search in
	 * @return The found IField or <code>null</code> if no matching field found
	 */
	private static IField<?> findField(final String propertyName, Iterable<IField<?>> group) {

		// first go through the non-group child fields
		for(IField<?> fld : group) {
			if(fld instanceof Iterable == false) {
				if(fld.getPropertyName().equals(propertyName)) {
					return fld;
				}
			}
		}

		IField<?> rfld;
		for(IField<?> fld : group) {
			if(fld instanceof Iterable) {
				rfld = findField(propertyName, (FieldGroup) fld);
				if(rfld != null) return rfld;
			}
		}

		return null;
	}

	/**
	 * Recursively extracts all {@link IField}s whose property name starts with
	 * the given property path. The found fields are added to the given set.
	 * @param propPath The property path that all matching fields' property name
	 *        must start with.
	 * @param itr The iterable collection of fields
	 * @param set The set of found fields
	 */
	private static void findFields(final String propPath, Iterable<IField<?>> itr, Set<IField<?>> set) {
		List<Iterable<IField<?>>> glist = null;
		for(IField<?> fld : itr) {
			if(fld instanceof Iterable == false) {
				if(fld.getPropertyName().startsWith(propPath)) {
					set.add(fld);
				}
			}
			else {
				if(glist == null) glist = new ArrayList<Iterable<IField<?>>>();
				glist.add((FieldGroup) fld);
			}
		}
		if(glist != null) {
			for(Iterable<IField<?>> fg : glist) {
				findFields(propPath, fg, set);
			}
		}
	}

	/**
	 * Recursively sets the given parent property path to all child fields'
	 * property names.
	 * @param parentPropPath The parent property path to set
	 */
	private static void setParentPropertyPath(IField<?> field, String parentPropPath) {
		if(field instanceof Iterable) {
			for(IField<?> f : (FieldGroup) field) {
				setParentPropertyPath(f, parentPropPath);
			}
		}
		else {
			final PropertyPath p = new PropertyPath(field.getPropertyName());
			p.setParentPropertyPath(parentPropPath);
			field.setPropertyName(p.toString());
		}
	}

	/**
	 * The collection of child fields.
	 */
	private final Set<IField<?>> fields = new HashSet<IField<?>>();

	/**
	 * The field group validator(s).
	 */
	private CompositeValidator validator;

	/**
	 * The Widget that is used to convey validation feedback.
	 */
	private Widget feedbackWidget;

	/**
	 * The {@link PropertyChangeSupport} aggregate which is shared by all child
	 * {@link IField}s.
	 */
	private PropertyChangeSupport changeSupport;

	/**
	 * Constructor
	 */
	public FieldGroup() {
		super();
	}

	public String getName() {
		throw new UnsupportedOperationException();
	}

	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	public String descriptor() {
		throw new UnsupportedOperationException();
	}

	public String getPropertyName() {
		// fields groups shall NOT serve as model hierarchy boundaries!!!
		throw new UnsupportedOperationException();
	}

	public void setPropertyName(String propName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return The designated Widget to receive validation messages.
	 */
	public Widget getFeedbackWidget() {
		return feedbackWidget;
	}

	/**
	 * Sets the Widget to for which validation messages are bound.
	 * @param feedbackWidget A Widget designated to be the validation feeback
	 *        hook.
	 */
	public void setFeedbackWidget(Widget feedbackWidget) {
		this.feedbackWidget = feedbackWidget;
	}

	public Iterator<IField<?>> iterator() {
		return fields.iterator();
	}

	/**
	 * Recursively searches for a field having the given property name. <br>
	 * @param propertyName
	 * @return The found field or <code>null</code> if it doesn't exist.
	 */
	public IField<?> getField(String propertyName) {
		return propertyName == null ? null : findField(propertyName, this);
	}

	/**
	 * Finds all fields whose property name begins with the given property path.
	 * @param propPath The property path
	 * @return Set of matching fields never <code>null</code> but may be empty
	 *         (when no matches found).
	 */
	public Set<IField<?>> getFields(String propPath) {
		Set<IField<?>> set = new HashSet<IField<?>>();
		findFields(propPath, this, set);
		return set;
	}

	/**
	 * Adds a field to this field group.
	 * @param field The field to add
	 * @throws IllegalArgumentException When this field instance already exists or
	 *         another field exists with the same property name.
	 */
	public void addField(IField<?> field) throws IllegalArgumentException {
		if(!fields.add(field)) {
			throw new IllegalArgumentException(
					"This field instance was already added or a field having the same property name already exists.");
		}
	}

	/**
	 * Adds a field to this field group pre-pending the given parent property path
	 * to the field's <em>existing</em> property name.
	 * @param parentPropPath Pre-pended to the field's "root" (non-path) property
	 *        name before the field is added. May be <code>null</code> in which
	 *        case the field's property name remains un-altered. If the field has
	 *        an parent property path is is <em>replaced</em>.
	 * @param field The field to add
	 */
	public void addField(String parentPropPath, IField<?> field) {
		setParentPropertyPath(field, parentPropPath);
		addField(field);
	}

	/**
	 * Adds multiple fields to this group.
	 * @param fields The fields to add
	 */
	public void addFields(Iterable<IField<?>> fields) {
		if(fields != null) {
			for(IField<?> fld : fields) {
				addField(fld);
			}
		}
	}

	/**
	 * Adds an array of fields to this group.
	 * @param fields The array of fields to add
	 */
	public void addFields(IField<?>[] fields) {
		if(fields != null) {
			for(IField<?> fld : fields) {
				addField(fld);
			}
		}
	}

	/**
	 * Adds multiple fields to this group.
	 * @param parentPropPath Pre-pended to the each field's property name before
	 *        the fields are added. May be <code>null</code> in which case the
	 *        fields' property names remain un-altered.
	 * @param fields The fields to add
	 */
	public void addFields(String parentPropPath, Iterable<IField<?>> fields) {
		if(fields != null) {
			for(IField<?> fld : fields) {
				addField(parentPropPath, fld);
			}
		}
	}

	/**
	 * Adds multiple fields to this group.
	 * @param parentPropPath Pre-pended to the each field's property name before
	 *        the fields are added. May be <code>null</code> in which case the
	 *        fields' property names remain un-altered.
	 * @param fields The fields to add
	 */
	public void addFields(String parentPropPath, IField<?>[] fields) {
		if(fields != null) {
			for(IField<?> fld : fields) {
				addField(parentPropPath, fld);
			}
		}
	}

	/**
	 * Removes a field by reference searching recursively. If the given field is
	 * <code>null</code> or is <em>this</em> field group, no field is removed and
	 * <code>false</code> is returned.
	 * @param field The field to remove.
	 * @return <code>true</code> if the field was removed, <code>false</code> if
	 *         not.
	 */
	public boolean removeField(IField<?> field) {
		if(field == null || field == this) return false;
		for(IField<?> fld : fields) {
			if(fld == field) {
				return fields.remove(field);
			}
			else if(fld instanceof FieldGroup) {
				((FieldGroup) fld).removeField(field);
			}
		}
		return false;
	}

	/**
	 * Removes a collection of fields from this group.
	 * @param clc The collection of fields to remove.
	 */
	public void removeFields(Iterable<IField<?>> clc) {
		if(clc != null) {
			for(IField<?> fld : clc) {
				removeField(fld);
			}
		}
	}

	public boolean isRequired() {
		for(IField<?> field : fields) {
			if(field.isRequired()) return true;
		}
		return false;
	}

	public void setRequired(boolean required) {
		for(IField<?> field : fields) {
			field.setRequired(required);
		}
	}

	public boolean isReadOnly() {
		for(IField<?> field : fields) {
			if(!field.isReadOnly()) return false;
		}
		return true;
	}

	/**
	 * Iterates over the child fields, setting their readOnly property.
	 */
	public void setReadOnly(boolean readOnly) {
		for(IField<?> field : fields) {
			field.setReadOnly(readOnly);
		}
	}

	public boolean isEnabled() {
		for(IField<?> field : fields) {
			if(!field.isEnabled()) return false;
		}
		return true;
	}

	public void setEnabled(boolean enabled) {
		for(IField<?> field : fields) {
			field.setEnabled(enabled);
		}
	}

	public boolean isVisible() {
		for(IField<?> field : fields) {
			if(field.isVisible()) return true;
		}
		return false;
	}

	public void setVisible(boolean visible) {
		for(IField<?> field : fields) {
			field.setVisible(visible);
		}
	}

	public void clear() {
		for(IField<?> f : fields) {
			f.clear();
		}
	}

	public Object getValue() {
		throw new UnsupportedOperationException();
	}

	public void setValue(Object value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return The number of child fields.
	 */
	public int size() {
		return fields.size();
	}

	public void addChangeListener(ChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void removeChangeListener(ChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void addValidator(IValidator validator) {
		if(validator != null) {
			if(this.validator == null) {
				this.validator = new CompositeValidator();
			}
			this.validator.add(validator);
		}
	}

	public void removeValidator(IValidator validator) {
		if(validator != null && this.validator != null) {
			this.validator.remove(validator);
		}
	}

	public Object validate(Object value) throws ValidationException {
		if(value != this) throw new IllegalArgumentException();
		Map<Widget, List<Msg>> errors = new HashMap<Widget, List<Msg>>();
		for(IField<?> field : fields) {
			try {
				field.validate(((IBindable) field).getProperty(field.getPropertyName()));
			}
			catch(PropertyPathException e) {
				// won't happen
				throw new IllegalStateException();
			}
			catch(ValidationException e) {
				errors.put(field.getWidget(), e.getErrors());
			}
		}
		if(validator != null) {
			try {
				value = validator.validate(value);
			}
			catch(ValidationException e) {
				errors.put(feedbackWidget, e.getErrors());
			}
		}
		if(errors.size() > 0) {
			throw new ValidationException(errors);
		}
		return value;
	}

	public FieldLabel getFieldLabel() {
		throw new UnsupportedOperationException();
	}

	public Widget getWidget() {
		throw new UnsupportedOperationException();
	}

	public void setFieldContainer(Widget fieldContainer) {
		throw new UnsupportedOperationException();
	}

	public void setFieldLabelContainer(Widget fieldLabelContainer) {
		throw new UnsupportedOperationException();
	}

	public String getText() {
		throw new UnsupportedOperationException();
	}

	public void setText(String text) {
		throw new UnsupportedOperationException();
	}

	public Object getProperty(String propPath) throws PropertyPathException {
		return getField(propPath).getProperty(propPath);
	}

	public void setProperty(String propPath, Object value) throws PropertyPathException, Exception {
		IField<?> f = getField(propPath);
		if(f == null) {
			throw new MalformedPropPathException("Unable to find field with name: " + propPath);
		}
		f.setProperty(propPath, value);
	}

	public void setPropertyChangeSupport(PropertyChangeSupport changeSupport) {
		if(changeSupport != null && (this.changeSupport != null && this.changeSupport.hasAnyListeners())) {
			throw new IllegalStateException("Field group already references a property change support reference");
		}
		this.changeSupport = changeSupport;
		for(IField<?> f : fields) {
			f.setPropertyChangeSupport(changeSupport);
		}
	}

	private void ensureChangeSupportAggregated() throws IllegalStateException {
		if(changeSupport == null) {
			throw new IllegalStateException("A root field group must first be declared");
		}
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		ensureChangeSupportAggregated();
		changeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		ensureChangeSupportAggregated();
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public IPropertyChangeListener[] getPropertyChangeListeners() {
		ensureChangeSupportAggregated();
		return changeSupport.getPropertyChangeListeners();
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		ensureChangeSupportAggregated();
		changeSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		ensureChangeSupportAggregated();
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	@Override
	public String toString() {
		return "FieldGroup";
	}
}