/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.PropertyPath;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;
import com.tll.util.IDescriptorProvider;

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
 * @author jpk
 */
public final class FieldGroup implements IField, Iterable<IField>, IDescriptorProvider {

	/**
	 * Recursively searches for a single field whose property name matches the
	 * given property name
	 * @param propertyName The property name to search for
	 * @param group The group to search in
	 * @return The found IField or <code>null</code> if no matching field found
	 */
	private static IField findField(final String propertyName, FieldGroup group) {

		// first go through the non-group child fields
		for(IField fld : group) {
			if(fld instanceof FieldGroup == false) {
				if(fld.getPropertyName().equals(propertyName)) {
					return fld;
				}
			}
		}

		IField rfld;
		for(IField fld : group) {
			if(fld instanceof FieldGroup) {
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
	 * @param group The field group to search
	 * @param set The set of found fields
	 */
	private static void findFields(final String propPath, FieldGroup group, Set<IField> set) {
		List<FieldGroup> glist = null;
		for(IField fld : group) {
			if(fld instanceof FieldGroup == false) {
				if(fld.getPropertyName().startsWith(propPath)) {
					set.add(fld);
				}
			}
			else {
				if(glist == null) glist = new ArrayList<FieldGroup>();
				glist.add((FieldGroup) fld);
			}
		}
		if(glist != null) {
			for(FieldGroup fg : glist) {
				findFields(propPath, fg, set);
			}
		}
	}

	/**
	 * Recursively sets the given parent property path to all child fields'
	 * property names.
	 * @param parentPropPath The parent property path to set
	 */
	private static void setParentPropertyPath(IField field, String parentPropPath) {
		if(field instanceof FieldGroup) {
			for(IField f : (FieldGroup) field) {
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
	private final Set<IField> fields;

	/**
	 * The field group validator(s)
	 */
	private IValidator validator;

	/**
	 * A presentation worthy display name. Mainly used in providing validation
	 * feedback to the UI.
	 */
	private final String displayName;

	/**
	 * The Widget that is used to convey validation feedback.
	 */
	private Widget feedbackWidget;

	/**
	 * Constructor - Simple field group w/ no display name.
	 * @param fields The fields that will make up this group
	 * @param displayName The UI display name used for presenting validation
	 *        feedback to the UI.
	 * @param feedbackWidget The feedback Widget
	 */
	public FieldGroup(Set<IField> fields, String displayName, Widget feedbackWidget) {
		super();
		if(fields == null) throw new IllegalArgumentException("A set of fields must be specified.");
		this.fields = fields;
		this.displayName = displayName;
		this.feedbackWidget = feedbackWidget;
	}

	/**
	 * Constructor
	 * @param displayName The UI display name used for presenting validation
	 *        feedback to the UI.
	 * @param feedbackWidget The feedback Widget
	 */
	public FieldGroup(String displayName, Widget feedbackWidget) {
		this(new HashSet<IField>(), displayName, feedbackWidget);
	}

	public String descriptor() {
		return displayName;
	}

	public void addFocusListener(FocusListener listener) {
		throw new UnsupportedOperationException();
	}

	public void removeFocusListener(FocusListener listener) {
		throw new UnsupportedOperationException();
	}

	public boolean isRequired() {
		throw new UnsupportedOperationException();
	}

	public void setRequired(boolean required) {
		for(IField field : fields) {
			field.setRequired(required);
		}
	}

	public String getPropertyName() {
		// fields groups shall NOT serve as model hierarchy boundaries!!!
		throw new UnsupportedOperationException();
	}

	public void setPropertyName(String propName) {
		throw new UnsupportedOperationException();
	}

	public boolean isReadOnly() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Iterates over the child fields, setting their readOnly property.
	 */
	public void setReadOnly(boolean readOnly) {
		for(IField field : fields) {
			field.setReadOnly(readOnly);
		}
	}

	public boolean isEnabled() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Iterates over the child fields, setting their enabled property.
	 */
	public void setEnabled(boolean enabled) {
		for(IField field : fields) {
			field.setEnabled(enabled);
		}
	}

	public boolean isVisible() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Iterates over the child fields, setting their visible property.
	 */
	public void setVisible(boolean visible) {
		for(IField field : fields) {
			field.setVisible(visible);
		}
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

	public Iterator<IField> iterator() {
		return fields.iterator();
	}

	/**
	 * Recursively searches for a field having the given property name. <br>
	 * @param propertyName
	 * @return The found field or <code>null</code> if it doesn't exist.
	 */
	public IField getField(String propertyName) {
		return propertyName == null ? null : findField(propertyName, this);
	}

	/**
	 * Finds all fields whose property name begins with the given property path.
	 * @param propPath The property path
	 * @return Set of matching fields never <code>null</code> but may be empty
	 *         (when no matches found).
	 */
	public Set<IField> getFields(String propPath) {
		Set<IField> set = new HashSet<IField>();
		findFields(propPath, this, set);
		return set;
	}

	/**
	 * Adds a field to this field group.
	 * @param field
	 * @see #addField(String, IField)
	 */
	public void addField(IField field) {
		fields.add(field);
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
	public void addField(String parentPropPath, IField field) {
		setParentPropertyPath(field, parentPropPath);
		addField(field);
	}

	/**
	 * Adds multiple fields to this group.
	 * @param fields The fields to add
	 */
	public void addFields(Iterable<IField> fields) {
		if(fields != null) {
			for(IField fld : fields) {
				addField(fld);
			}
		}
	}

	/**
	 * Adds an array of fields to this group.
	 * @param fields The array of fields to add
	 */
	public void addFields(IField[] fields) {
		if(fields != null) {
			for(IField fld : fields) {
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
	public void addFields(String parentPropPath, Iterable<IField> fields) {
		if(fields != null) {
			for(IField fld : fields) {
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
	public void addFields(String parentPropPath, IField[] fields) {
		if(fields != null) {
			for(IField fld : fields) {
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
	public boolean removeField(IField field) {
		if(field == null || field == this) return false;
		for(IField fld : fields) {
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
	public void removeFields(Iterable<IField> clc) {
		if(clc != null) {
			for(IField fld : clc) {
				removeField(fld);
			}
		}
	}

	public void reset() {
		MsgManager.instance().clear(feedbackWidget, false);
		for(IField field : fields) {
			field.reset();
		}
	}

	/**
	 * @return The number of child fields.
	 */
	public int size() {
		return fields.size();
	}

	@Override
	public String toString() {
		return descriptor() + " (FieldGroup)";
	}

	public void addChangeListener(ChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void removeChangeListener(ChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void validate() throws ValidationException {
		boolean valid = true;
		for(IField field : fields) {
			try {
				field.validate();
			}
			catch(ValidationException e) {
				valid = false;
			}
		}
		if(validator != null) {
			try {
				validator.validate(null);
			}
			catch(ValidationException e) {
				valid = false;
				// handle UI msg if we have a feedback widget specified
				if(feedbackWidget != null) {
					MsgManager.instance().post(false, e.getValidationMessages(), Position.BOTTOM, feedbackWidget, -1, false)
							.show();
				}
			}
		}
		if(!valid) {
			throw new ValidationException(descriptor() + " has errors.");
		}
	}

	public Object validate(Object value) {
		throw new UnsupportedOperationException();
	}

	public Object getValidatedValue() {
		throw new UnsupportedOperationException();
	}

	public void markInvalid(boolean invalid, List<Msg> msgs) {
		if(invalid) {
			if(msgs == null) return;
			List<Msg> unboundFieldMessages = new ArrayList<Msg>();
			for(Msg fm : msgs) {
				IField fld = getField(fm.getRefToken());
				if(fld != null) {
					List<Msg> l = new ArrayList<Msg>();
					l.add(fm);
					fld.markInvalid(true, l);
				}
				else {
					// field not found
					unboundFieldMessages.add(fm);
				}
			}
			if(unboundFieldMessages.size() > 0) {
				MsgManager.instance().post(true, unboundFieldMessages, Position.BOTTOM, feedbackWidget, -1, true).show();
			}
		}
		else {
			if(feedbackWidget != null) MsgManager.instance().clear(feedbackWidget, false);
		}
	}
}
