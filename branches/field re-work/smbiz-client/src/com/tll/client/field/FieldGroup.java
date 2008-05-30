/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client.field;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.IModelRefProperty;
import com.tll.client.model.IPropertyBinding;
import com.tll.client.model.MalformedPropPathException;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.validate.CompositeValidator;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;
import com.tll.util.IDescriptorProvider;

/**
 * FieldGroup - A group of {@link IField}s which may in turn be nested
 * {@link FieldGroup}s. This construct assists in the management of property
 * paths so any given field group object graph should be structured such that
 * property path integrity is maintained.
 * <p>
 * <strong>NOTE: </strong>Property path conventions as they related to
 * {@link FieldGroup}s:
 * <ol>
 * <li>Each nested field group represents a single name in a property path.
 * E.g.: For property path 'account.addresses[3].firstName', the corres. field
 * group hierarchy is: fg(account)-nestedfg(addresses[3])-nestedfg(firstName)
 * </ol>
 * @author jpk
 */
// TODO create a field "set" construct to handle redundant (non-property path
// dependent) field aggregations for validation purposes.
public final class FieldGroup implements IField, Iterable<IField>, IDescriptorProvider {

	/**
	 * FieldBinding - Encapsulates parent/child relationship for a given parent
	 * FieldGroup and child IField.
	 * @author jpk
	 */
	private static class FieldBinding {

		public IField field;
		public FieldGroup parent;

		public FieldBinding(IField field, FieldGroup parent) {
			super();
			this.field = field;
			this.parent = parent;
		}
	}

	/**
	 * @param propPath
	 * @param group
	 * @param parentPropPath
	 * @return
	 * @throws MalformedPropPathException
	 */
	private static FieldBinding getBindingByPropertyPath(final String propPath, FieldGroup group, String parentPropPath)
			throws MalformedPropPathException {
		return getBindingByPropertyPath(new PropertyPath(propPath), 0, group);
	}

	/**
	 * Recursive method seeking the field bound to the given property path.
	 * @param propPath The property path object
	 * @param pindex The property path index
	 * @param group The current {@link FieldGroup}
	 * @return The {@link FieldBinding} (never <code>null</code>).
	 * @throws MalformedPropPathException
	 */
	private static FieldBinding getBindingByPropertyPath(final PropertyPath propPath, int pindex, FieldGroup group)
			throws MalformedPropPathException {
		String name = propPath.propNameAt(pindex);
		boolean atEnd = propPath.atEnd(pindex);
		for(IField fld : group) {
			if(name.equals(fld.getPropertyName())) {
				if(atEnd) {
					// found it
					return new FieldBinding(fld, group);
				}
				if(fld instanceof FieldGroup == false) {
					throw new MalformedPropPathException(propPath.toString());
				}
				return getBindingByPropertyPath(propPath, pindex + 1, (FieldGroup) fld);
			}
		}
		return new FieldBinding(null, group);
	}

	/**
	 * Needed to ensure unique pending property names and to hide this aspect from
	 * clients.
	 */
	private static int pendingPropertyCounter = 0;

	private static String pendingTokenPrefix = "{pending";

	/**
	 * Provides a "pending" property name. Pending implies that the property does
	 * <em>NOT</em> yet exist and the semantics of a pending property name
	 * serves to indicate this fact.
	 * @return A unique property name indicating "pending".
	 */
	public static String getPendingPropertyName() {
		return pendingTokenPrefix + pendingPropertyCounter++ + '}';
	}

	/**
	 * Is the given property name a pending property? Used in tandem with
	 * {@link #getPendingPropertyName()}. This scheme is used to indicate model
	 * add operations.
	 * @param propName The property name to check
	 * @return true/false
	 * @see #getPendingPropertyName()
	 */
	private static boolean isPendingProperty(String propName) {
		return propName != null && propName.startsWith(pendingTokenPrefix);
	}

	/**
	 * The property name representing a property path (for the case of field
	 * groups). May be <code>null</code>.
	 */
	private String propName;

	/**
	 * A presentation worthy display name.
	 */
	private final String displayName;

	private boolean required = false;
	private boolean readOnly = false;
	private boolean enabled = true;
	private boolean visible = true;

	/**
	 * Used to indicate this field group is scheduled for deletion when populating
	 * the field data back to the associated property value group.
	 */
	private boolean markedDeleted = false;

	/**
	 * The collection of child fields.
	 */
	private final Set<IField> fields = new HashSet<IField>();

	private CompositeValidator validators;

	/**
	 * The Widget to which this field group is bound. May be <code>null</code>.
	 */
	private Widget refWidget;

	/**
	 * Constructor
	 * @param displayName
	 * @param refWidget
	 */
	public FieldGroup(String displayName, Widget refWidget) {
		super();
		this.displayName = displayName;
		this.refWidget = refWidget;
	}

	/**
	 * Constructor
	 * @param propName
	 * @param displayName
	 * @param refWidget The associated UI {@link Widget}. May be
	 *        <code>null</code>. Used for validation feedback.
	 */
	public FieldGroup(String propName, String displayName, Widget refWidget) {
		this(displayName, refWidget);
		this.propName = propName;
	}

	public String descriptor() {
		return displayName;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		if(this.required == required) return;
		for(IField field : fields) {
			field.setRequired(required);
		}
		this.required = required;
	}

	public String getPropertyName() {
		return propName;
	}

	public void setPropertyName(String propName) {
		this.propName = propName;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		if(readOnly != this.readOnly) {
			for(IField field : fields) {
				field.setReadOnly(readOnly);
			}
			this.readOnly = readOnly;
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if(enabled != this.enabled) {
			for(IField field : fields) {
				field.setEnabled(enabled);
			}
			this.enabled = enabled;
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		if(visible != this.visible) {
			for(IField field : fields) {
				field.setVisible(visible);
			}
			this.visible = visible;
		}
	}

	public boolean isMarkedDeleted() {
		return markedDeleted;
	}

	public void setMarkedDeleted(boolean markedDeleted) {
		this.markedDeleted = markedDeleted;
		setEnabled(!markedDeleted);
	}

	public boolean isPending() {
		return isPendingProperty(propName);
	}

	public Widget getRefWidget() {
		return refWidget;
	}

	public void setRefWidget(Widget refWidget) {
		this.refWidget = refWidget;
	}

	public Iterator<IField> iterator() {
		return fields.iterator();
	}

	/**
	 * Finds a field having the given property path. Searches recursively.
	 * @param propPath
	 * @return The found field or <code>null</code> if it doesn't exist.
	 * @throws IllegalArgumentException When the property path doesn't map to the
	 *         field group properly or when the property path is inherently
	 *         mal-formed.
	 */
	public IField getField(String propPath) {
		if(propPath == null) return null;
		try {
			FieldBinding binding = getBindingByPropertyPath(propPath, this, getPropertyName());
			return binding.field;
		}
		catch(MalformedPropPathException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Sets a field given a property path
	 * @param propPath
	 * @param field
	 * @throws IllegalArgumentException When the property path doesn't map to the
	 *         field group properly or when the property path is inherently
	 *         mal-formed.
	 */
	/*
	public void setField(String propPath, IField field) throws IllegalArgumentException {
		assert field != null;
		try {
			FieldBinding binding =
					getBindingByPropertyPath(PropertyPathHelper.getPropertyPath(propPath, field.getPropertyName()), this,
							getPropertyName());
			if(binding.field == null) {
				binding.parent.addField(field);
			}
			else {
				Set<IField> set = binding.parent.fields;
				set.remove(binding.field);
				set.add(field);
			}
		}
		catch(MalformedPropPathException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	*/

	/**
	 * Recursively searches for the field of the given property path and if found
	 * is removed from its parent group.
	 * @param propPath The property path of the field to be removed.
	 * @return The removed field or <code>null</code> if not found.
	 */
	/*
	public IField removeField(String propPath) {
		if(propPath == null) return null;
		FieldBinding binding;
		try {
			binding = getBindingByPropertyPath(propPath, this, getPropertyName());
		}
		catch(MalformedPropPathException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		if(binding.field == null) return null;
		assert binding.parent != null;
		binding.parent.fields.remove(binding.field);
		return binding.field;
	}
	*/

	/**
	 * Removes a field by reference searching recursively. If the given field is
	 * <code>null</code> or is <em>this</em> field group, no field is removed
	 * and <code>false</code> is returned.
	 * @param field The field to remove.
	 * @return <code>true</code> if the field was removed, <code>false</code>
	 *         if not.
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
	 * Adds a field directly under this field group.
	 * @param field The field to add
	 */
	public void addField(IField field) {
		assert field != null;
		fields.add(field);
	}

	public void reset() {
		MsgManager.instance.clear(refWidget, true);
		for(IField field : fields) {
			field.reset();
		}
		markedDeleted = false;
	}

	public void clear() {
		MsgManager.instance.clear(refWidget, true);
		for(IField field : fields) {
			field.clear();
		}
	}

	public int size() {
		return fields.size();
	}

	/**
	 * For the case of a {@link FieldGroup}, we provide a comma delimited list of
	 * field values for all child {@link IField}s.
	 */
	public String getValue() {
		StringBuffer sb = new StringBuffer();
		for(IField field : fields) {
			sb.append(',');
			sb.append(field.getValue());
		}
		return sb.length() > 1 ? sb.substring(1) : sb.toString();
	}

	/**
	 * Setting a value on a {@link FieldGroup}, means we recursively set the same
	 * value to all child fields.
	 */
	public void setValue(String value) {
		for(IField field : fields) {
			field.setValue(value);
		}
	}

	/**
	 * Binds the model to this field group.
	 * <p>
	 * <strong>NOTE: </strong>The property name hierarchy of the model
	 * <em>must</em> to be consistent with this field group's property name
	 * hierarchy.
	 */
	public void bindModel(Model model) {
		for(IField fld : this) {
			if(fld instanceof FieldGroup) {
				final IPropertyBinding binding = model.getPropertyBinding(fld.getPropertyName());
				if(binding != null) {
					if(!binding.getType().isModelRef()) {
						throw new IllegalArgumentException("Can't bind model: Field group '" + fld.getPropertyName()
								+ "' does not map to a model ref property");
					}
					final Model submodel = ((IModelRefProperty) binding).getModel();
					if(submodel != null) {
						fld.bindModel(submodel);
					}
				}
			}
			else {
				fld.bindModel(model);
			}
		}
		setMarkedDeleted(false);
	}

	public boolean updateModel(Model model) {
		model.setMarkedDeleted(markedDeleted);
		if(markedDeleted) {
			// since we are marked as deleted, change is true
			return true;
		}
		boolean changed = false;
		for(IField fld : this) {
			if(fld instanceof FieldGroup) {
				final IPropertyBinding binding = model.getPropertyBinding(fld.getPropertyName());
				if(binding != null) {
					if(!binding.getType().isModelRef()) {
						throw new IllegalArgumentException("Can't update model: Field group '" + fld.getPropertyName()
								+ "' does not map to a model ref property");
					}
					final Model submodel = ((IModelRefProperty) binding).getModel();
					if(submodel != null) {
						if(fld.updateModel(submodel)) {
							changed = true;
						}
					}
				}
				else if(((FieldGroup) fld).isPending()) {
					// we have a pending addition
					changed = true;
				}
			}
			else {
				if(fld.updateModel(model)) {
					changed = true;
				}
			}
		}
		return changed;
	}

	public IValidator getValidators() {
		return validators;
	}

	public void addValidator(IValidator validator) {
		if(validators == null) {
			validators = new CompositeValidator();
		}
		validators.add(validator);
	}

	/**
	 * Validates the current state of the field group.
	 * @throws ValidationException When at least one validation error exists.
	 */
	public void validate() throws ValidationException {
		boolean valid = true;
		for(IField field : this) {
			if(field instanceof FieldGroup) {
				((FieldGroup) field).validate();
			}
			else {
				IValidator validator = field.getValidators();
				if(validator != null) {
					try {
						validator.validate(field.getValue());
					}
					catch(ValidationException e) {
						field.handleValidationFeedback(e);
						valid = false;
					}
				}
			}
		}
		if(validators != null) {
			try {
				validators.validate(null);
			}
			catch(ValidationException e) {
				valid = false;
			}
		}
		if(!valid) {
			throw new ValidationException(descriptor() + " has errors.");
		}
	}

	public void handleValidationFeedback(IValidationFeedback feedback) {
		final List<Msg> msgs = feedback.getValidationMessages();
		if(msgs == null) return;
		List<Msg> unboundFieldMessages = new ArrayList<Msg>();
		for(Msg fm : msgs) {
			IField fld = getField(fm.getRefToken());
			if(fld != null) {
				fld.handleValidationFeedback(new ValidationException(fm));
			}
			else {
				// field not found
				unboundFieldMessages.add(fm);
			}
		}
		if(unboundFieldMessages.size() > 0) {
			MsgManager.instance.post(true, unboundFieldMessages, Position.BOTTOM, refWidget, -1, true).show();
		}

	}

	@Override
	public String toString() {
		return getPropertyName() + " (FieldGroup)";
	}
}
