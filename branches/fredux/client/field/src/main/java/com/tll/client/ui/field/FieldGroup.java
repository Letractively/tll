/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client.ui.field;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.CompositeValidationException;
import com.tll.client.validate.CompositeValidator;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;
import com.tll.common.model.PropertyPath;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * FieldGroup - A group of {@link IField}s which may in turn be nested
 * {@link FieldGroup}s. Thus a FieldGroup is a hierarchical collection of
 * {@link IField}s.
 * <p>
 * Non-FieldGroup children of {@link FieldGroup}s are:
 * <ol>
 * <li>Assumed to be {@link IFieldWidget} instances
 * <li>Expected to have a unique <em>property</em> name (in addition to a unique
 * name)
 * </ol>
 * Unique property names enable the resolution of {@link IFieldWidget}s.
 * <p>
 * A {@link FieldGroup} is a grouping of {@link IField}s for UI purposes
 * <em>does not necessarily represent model hierarchy boundaries</em>
 * <p>
 * <b>IMPT: </b> {@link FieldGroup}s do <em>NOT</em> (as yet) support handling
 * of circular references! As such, do not add a field to a field group that is
 * a child of another field group where both are children of a common ancestor
 * field group.
 * @author jpk
 */
public final class FieldGroup implements IField, Iterable<IField> {

	/**
	 * Recursively searches the given field group for a field whose name matches
	 * that given. The first found field is returned.
	 * @param name The name to search for. If <code>null</code> is specified,
	 *        <code>null</code> is returned.
	 * @param group The group to search in
	 * @return The found IField or <code>null</code> if no matching field found
	 */
	private static IField findByName(final String name, FieldGroup group) {
		if(name == null) return null;
		if(name.equals(group.name)) return group;

		// first go through the non-group child fields
		for(final IField fld : group) {
			if(fld instanceof FieldGroup == false) {
				if(name.equals(fld.getName())) {
					return fld;
				}
			}
		}

		IField rfld;
		for(final IField fld : group) {
			if(fld instanceof FieldGroup) {
				rfld = findByName(name, (FieldGroup) fld);
				if(rfld != null) return rfld;
			}
		}

		return null;
	}

	/**
	 * Recursively searches the given field group for a nested field whose
	 * property name matches that given. The first found field is returned.
	 * @param propertyName The property name to search for
	 * @param group The group to search in
	 * @return The found IField or <code>null</code> if no matching field found
	 */
	private static IFieldWidget<?> findByPropertyName(final String propertyName, FieldGroup group) {

		// first go through the non-group child fields
		for(final IField fld : group) {
			if(fld instanceof IFieldWidget) {
				if(((IFieldWidget<?>) fld).getPropertyName().equals(propertyName)) {
					return (IFieldWidget<?>) fld;
				}
			}
		}

		IFieldWidget<?> rfld;
		for(final IField fld : group) {
			if(fld instanceof FieldGroup) {
				rfld = findByPropertyName(propertyName, (FieldGroup) fld);
				if(rfld != null) return rfld;
			}
		}

		return null;
	}

	/**
	 * Recursively extracts all {@link IFieldWidget}s whose property name starts
	 * with the given property path. The found fields are added to the given set.
	 * @param propPath The property path that all matching fields' property name
	 *        must start with. If <code>null</code>, all encountered
	 *        {@link IFieldWidget}s are included
	 * @param group The field group to search
	 * @param set The set of found fields
	 */
	private static void findFieldWidgets(final String propPath, FieldGroup group, Set<IFieldWidget<?>> set) {
		Set<FieldGroup> gset = null;
		for(final IField fld : group) {
			if(fld instanceof IFieldWidget) {
				if(propPath == null || ((IFieldWidget<?>) fld).getPropertyName().startsWith(propPath)) {
					set.add((IFieldWidget<?>) fld);
				}
			}
			else {
				if(gset == null) gset = new HashSet<FieldGroup>();
				gset.add((FieldGroup) fld);
			}
		}
		if(gset != null) {
			for(final FieldGroup fg : gset) {
				findFieldWidgets(propPath, fg, set);
			}
		}
	}

	/**
	 * Recursively pre-pends the given parent property path to all applicable
	 * child fields' property names.
	 * @param parentPropPath The parent property path to pre-pend
	 */
	private static void setParentPropertyPath(IField field, String parentPropPath) {
		if(field instanceof FieldGroup) {
			for(final IField f : (FieldGroup) field) {
				setParentPropertyPath(f, parentPropPath);
			}
		}
		else {
			assert field instanceof IFieldWidget;
			((IFieldWidget<?>) field).setPropertyName(PropertyPath.getPropertyPath(parentPropPath, ((IFieldWidget<?>) field)
					.getPropertyName()));
		}
	}

	/**
	 * Verifies a field's worthiness to add to this group based on:
	 * <ol>
	 * <li>Name uniqueness for <em>all</em> existing fields in this group.
	 * <li>Property name uniqueness <em>if</em> the given field is an
	 * {@link IFieldWidget} instance.
	 * </ol>
	 * @param f the field to verify
	 * @throws IllegalArgumentException When the verification fails.
	 */
	private static void verifyAddField(final IField f, FieldGroup group) throws IllegalArgumentException {
		assert group != null;
		if(f == null) throw new IllegalArgumentException("No field specified.");
		final boolean isWidget = (f instanceof IFieldWidget);
		for(final IField ef : group) {
			if(f.getName().equals(ef.getName())) {
				throw new IllegalArgumentException("Field name: '" + f.getName() + "' already exists.");
			}
			if(isWidget && (ef instanceof IFieldWidget)) {
				if(((IFieldWidget<?>) f).getPropertyName().equals(((IFieldWidget<?>) ef).getPropertyName())) {
					throw new IllegalArgumentException("Field property name: '" + ((IFieldWidget<?>) f).getPropertyName()
							+ "' already exists.");
				}
			}
			if(ef instanceof FieldGroup) {
				verifyAddField(f, (FieldGroup) ef);
			}
		}
	}

	/**
	 * The optional name. This is only used for convenient identification
	 * purposes.
	 */
	private String name;

	/**
	 * The collection of child fields.
	 */
	private final Set<IField> fields = new HashSet<IField>();

	/**
	 * The field group validator(s).
	 */
	private CompositeValidator validator;

	/**
	 * The Widget that is used to convey validation feedback.
	 */
	private Widget feedbackWidget;
	
	private MsgPopupRegistry mregistry;

	/**
	 * Constructor
	 * @param name The required unique name for this field group.
	 */
	public FieldGroup(String name) {
		super();
		setName(name);
	}

	public Iterator<IField> iterator() {
		return fields.iterator();
	}

	@Override
	public void setMsgPopupRegistry(MsgPopupRegistry mregistry) {
		this.mregistry = mregistry;
		for(final IField field : fields) {
			field.setMsgPopupRegistry(mregistry);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name == null) throw new IllegalArgumentException();
		this.name = name;
	}

	/**
	 * @return The designated Widget to receive validation messages.
	 */
	public Widget getWidget() {
		return feedbackWidget;
	}

	/**
	 * Sets the Widget to for which validation messages are bound.
	 * @param feedbackWidget A Widget designated to be the validation feeback
	 *        hook.
	 */
	public void setWidget(Widget feedbackWidget) {
		this.feedbackWidget = feedbackWidget;
	}

	/**
	 * Recursively searches for a field having the given name.
	 * @param name
	 * @return The found field or <code>null</code> if no field exists with the
	 *         given name.
	 */
	public IField getFieldByName(String name) {
		return findByName(name, this);
	}

	/**
	 * Recursively searches for a field widget having the given property name.
	 * @param propertyName
	 * @return The found field or <code>null</code> if it doesn't exist.
	 */
	public IFieldWidget<?> getFieldWidget(String propertyName) {
		return propertyName == null ? null : findByPropertyName(propertyName, this);
	}

	/**
	 * Recursively searches for a field widget having the given name.
	 * @param name
	 * @return The found field widget or <code>null</code> if no field widget
	 *         exists with the given name.
	 */
	public IFieldWidget<?> getFieldWidgetByName(String name) {
		final IField f = findByName(name, this);
		return f instanceof IFieldWidget ? (IFieldWidget<?>) f : null;
	}

	/**
	 * Finds all {@link IFieldWidget}s whose property name begins with the given
	 * property path.
	 * @param propPath The property path. If <code>null</code>, all
	 *        {@link IFieldWidget}s are included.
	 * @return Set of matching fields never <code>null</code> but may be empty
	 *         (when no matches found).
	 */
	public Set<IFieldWidget<?>> getFieldWidgets(String propPath) {
		final Set<IFieldWidget<?>> set = new HashSet<IFieldWidget<?>>();
		findFieldWidgets(propPath, this, set);
		return set;
	}
	
	/**
	 * Adds a field to this field group.
	 * @param field The field to add
	 * @throws IllegalArgumentException When this field instance already exists or
	 *         another field exists with the same name.
	 */
	public void addField(IField field) throws IllegalArgumentException {
		verifyAddField(field, this);
		// NOTE: the field add op should go through based on the verify routine
		if(!fields.add(field)) throw new IllegalStateException();
	}

	/**
	 * Adds a field to this field group pre-pending the given parent property path
	 * to the field's <em>existing</em> property name.
	 * @param parentPropPath Pre-pended to the field's existing property path
	 *        before the field is added. May be <code>null</code> in which case
	 *        the field's property name remains un-altered.
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
			for(final IField fld : fields) {
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
			for(final IField fld : fields) {
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
			for(final IField fld : fields) {
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
			for(final IField fld : fields) {
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
		for(final IField fld : fields) {
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
			for(final IField fld : clc) {
				removeField(fld);
			}
		}
	}

	public void applyPropertyMetadata(IPropertyMetadataProvider provider) {
		for(final IField f : fields) {
			f.applyPropertyMetadata(provider);
		}
	}

	public boolean isRequired() {
		for(final IField field : fields) {
			if(field.isRequired()) return true;
		}
		return false;
	}

	public void setRequired(boolean required) {
		for(final IField field : fields) {
			field.setRequired(required);
		}
	}

	public boolean isReadOnly() {
		for(final IField field : fields) {
			if(!field.isReadOnly()) return false;
		}
		return true;
	}

	/**
	 * Iterates over the child fields, setting their readOnly property.
	 * @param readOnly true/false
	 */
	public void setReadOnly(boolean readOnly) {
		for(final IField field : fields) {
			field.setReadOnly(readOnly);
		}
	}

	public boolean isEnabled() {
		for(final IField field : fields) {
			if(!field.isEnabled()) return false;
		}
		return true;
	}

	public void setEnabled(boolean enabled) {
		for(final IField field : fields) {
			field.setEnabled(enabled);
		}
	}

	public boolean isVisible() {
		for(final IField field : fields) {
			if(field.isVisible()) return true;
		}
		return false;
	}

	public void setVisible(boolean visible) {
		for(final IField field : fields) {
			field.setVisible(visible);
		}
	}

	public void clearValue() {
		for(final IField f : fields) {
			f.clearValue();
		}
	}
	
	/**
	 * Removes all child fields from this group.
	 */
	public void clear() {
		fields.clear();
	}

	public void reset() {
		for(final IField f : fields) {
			f.reset();
		}
	}

	/**
	 * @return The number of child fields.
	 */
	public int size() {
		return fields.size();
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

	public void validate() throws CompositeValidationException {
		if(feedbackWidget == null || mregistry == null) {
			throw new IllegalStateException();
		}
		
		boolean hasErrors = false;
		final CompositeValidationException errors = new CompositeValidationException();
		
		for(final IField field : fields) {
			try {
				field.validate();
			}
			catch(final ValidationException e) {
				errors.add(e.getErrors(), field);
				hasErrors = true;
			}
		}
		
		if(validator != null) {
			try {
				validator.validate(null);
			}
			catch(final ValidationException e) {
				errors.add(e.getErrors(), this);
				//mregistry.addMsgs(e.getErrors(), feedbackWidget, true);
				hasErrors = true;
			}
		}
		
		if(hasErrors) {
			throw errors;
		}
	}

	@Override
	public int hashCode() {
		return 37 + name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final FieldGroup other = (FieldGroup) obj;
		assert name != null;
		if(!name.equals(other.name)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "FieldGroup[" + name + ']';
	}
}
