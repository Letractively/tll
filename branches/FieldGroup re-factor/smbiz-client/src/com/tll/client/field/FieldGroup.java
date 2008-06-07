/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.model.IModelRefProperty;
import com.tll.client.model.IPropertyBinding;
import com.tll.client.model.IPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.util.StringUtil;
import com.tll.client.validate.CompositeValidator;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;
import com.tll.util.IDescriptorProvider;

/**
 * FieldGroup - A group of {@link IField}s which may in turn be nested
 * {@link FieldGroup}s. Thus a FieldGroup is a hierarchical collection of
 * {@link IField}s.
 * <p>
 * Non-FieldGroup children of {@link FieldGroup}s are expected to have a unique
 * property name that allows the field to be mapped to the underlying
 * {@link Model}.
 * <p>
 * A FieldGroup represents a grouping of {@link IField}s for UI purposes only
 * and as such
 * <em>may not necessarily represent model hierarchy boundaries!</em>
 * <p>
 * To fully support data transfer ("binding") between a FieldGroup instance and
 * a Model instance, the following conventions are established:
 * <ol>
 * <li>Non-FieldGroup {@link IField}s contained in a FieldGroup are expected
 * to have a standard OGNL property name to support binding to/from the
 * underlying Model.
 * <li>Newly created IFields that map to a non-existant related many Model are
 * expected to have a property name that indicates as much:
 * <code>indexableProp{index}.propertyName</code> as opposed to the standard:
 * <code>indexableProp[index].propertyName</code>. In other words, curly
 * braces (<code>{}</code>) are used instead of square braces (<code>[]</code>).
 * </ol>
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
		List<FieldGroup> glist = null;
		for(IField fld : group) {
			if(fld instanceof FieldGroup == false) {
				if(fld.getPropertyName().equals(propertyName)) {
					return fld;
				}
			}
			else {
				if(glist == null) glist = new ArrayList<FieldGroup>();
				glist.add((FieldGroup) fld);
			}
		}
		if(glist != null) {
			for(FieldGroup fg : glist) {
				findField(propertyName, fg);
			}
		}
		return null;
	}

	/**
	 * Recursively extracts all {@link IField}s whose property name starts with
	 * the given property path. The found fields are added to the given set.
	 * @param propPath The property path used to compare against all encountered
	 *        fields
	 * @param group The field group to search
	 * @param set The set of found fields
	 */
	private static void findFields(final String propPath, FieldGroup group, Set<IField> set) {
		List<FieldGroup> glist = new ArrayList<FieldGroup>();
		for(IField fld : group) {
			if(fld instanceof FieldGroup == false) {
				if(fld.getPropertyName().startsWith(propPath)) {
					set.add(fld);
				}
			}
			else {
				glist.add((FieldGroup) fld);
			}
		}
		if(glist.size() > 0) {
			for(FieldGroup fg : glist) {
				findFields(propPath, fg, set);
			}
		}
	}

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
		// fields groups shall NOT serve as model hierarchy boundaries!!!
		throw new UnsupportedOperationException();
	}

	public void setPropertyName(String propName) {
		throw new UnsupportedOperationException();
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
	 * Recursively searches for a field having the given property name.
	 * @param propertyName
	 * @return The found field or <code>null</code> if it doesn't exist.
	 */
	public IField getField(String propertyName) {
		return propertyName == null ? null : findField(propertyName, this);
	}

	/**
	 * Finds all fields whose property name begins with the given property path.
	 * @param propPath The property path
	 * @return Set of matching fields
	 */
	public Set<IField> getFields(String propPath) {
		Set<IField> set = new HashSet<IField>();
		findFields(propPath, this, set);
		return set;
	}

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
	 * Recursively pre-pends the given property path to all child fields' property
	 * names.
	 * @param propertyPath The property path to pre-pend
	 */
	private void prePendPropertyName(String propertyPath) {
		for(IField fld : this) {
			if(fld instanceof FieldGroup) {
				((FieldGroup) fld).prePendPropertyName(propertyPath);
			}
			else {
				fld.setPropertyName(PropertyPath.getPropertyPath(propertyPath, fld.getPropertyName()));
			}
		}
	}

	/**
	 * Adds a field directly under this field group pre-pending the given parent
	 * property path to the field's <em>existing</em> property name.
	 * @param parentPropPath Pre-pended to the field's property name before the
	 *        field is added. May be <code>null</code> in which case the field's
	 *        property name remains un-altered.
	 * @param field The field to add
	 */
	public void addField(String parentPropPath, IField field) {
		if(parentPropPath != null) {
			if(field instanceof FieldGroup) {
				((FieldGroup) field).prePendPropertyName(parentPropPath);
			}
			else {
				field.setPropertyName(PropertyPath.getPropertyPath(parentPropPath, field.getPropertyName()));
			}
		}
		fields.add(field);
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
	 * Recursively binds a FieldGroup to a Model.
	 * @param group The FieldGroup
	 * @param model The Model
	 */
	private static void bindModel(FieldGroup group, final Model model) {
		final PropertyPath propPath = new PropertyPath();
		for(IField fld : group) {
			if(fld instanceof FieldGroup) {
				bindModel((FieldGroup) fld, model);
			}
			else {
				propPath.parse(fld.getPropertyName());
				IPropertyValue pv = model.getValue(propPath);
				if(pv == null) {
					fld.clear();
				}
				else {
					fld.bindModel(pv);
				}
			}
		}
		group.setMarkedDeleted(false);
	}

	/**
	 * Updates the model by recursively traversing the given FieldGroup.
	 * @param group The FieldGroup
	 * @param model The Model to be updated
	 * @return <code>true</code> when at least one valid update was transferred
	 *         to the given model.
	 */
	private static boolean updateModel(FieldGroup group, final Model model) {
		boolean changed = false;

		// map of newly created indexed properties keyed by the indexable property
		// path
		// this construct serves to unify like new indexed properties which is a
		// pre-cursor
		// to determining the model relative index range of indexed properties that
		// need to be created!
		Map<PropertyPath, Set<IField>> unboundFields = null;

		model.setMarkedDeleted(group.markedDeleted);
		if(!group.markedDeleted) {
			IPropertyValue pv;
			final PropertyPath propPath = new PropertyPath();
			for(IField fld : group) {
				if(fld instanceof FieldGroup) {
					updateModel((FieldGroup) fld, model);
				}
				else {
					// non-group field
					try {
						propPath.parse(fld.getPropertyName());

						int n = propPath.nextUnboundNode(0);
						if(n >= 0) {
							// unbound property!
							PropertyPath unboundPath = propPath.parent(n);
							if(unboundFields == null) {
								unboundFields = new HashMap<PropertyPath, Set<IField>>();
							}
							Set<IField> set = unboundFields.get(unboundPath);
							if(set == null) {
								set = new HashSet<IField>();
								unboundFields.put(unboundPath, set);
							}
							set.add(fld);
						}
						else {
							// existing model property
							pv = model.getValue(propPath);
							if(pv != null) {
								if(fld.updateModel(pv)) {
									changed = true;
								}
							}
						}
					}
					catch(IllegalArgumentException e) {
						throw new IllegalStateException(e.getMessage());
					}
				}
			}

			// handle the unbound indexed props (newly created in the ui)
			if(unboundFields != null) {
				for(PropertyPath upp : unboundFields.keySet()) {
					// create the missing properties in the model
					if(upp.isIndexed()) {
						// unbound indexed property
						RelatedManyProperty rmp = (RelatedManyProperty) model.getBinding(upp.indexedParent());
						Model stub = AuxDataCache.instance().getEntityPrototype(rmp.getRelatedType());
						if(stub == null) {
							throw new IllegalStateException("Unable to acquire a fresh " + rmp.getRelatedType().getName());
						}
						// now we need to propagate the actual property path to the child
						// fields
						String upps = upp.toString();
						String app = rmp.add(stub); // actual property path
						String oldIndexToken = upps.substring(0, upps.lastIndexOf(PropertyPath.UNBOUND_LEFT_INDEX_CHAR));
						String newIndexToken = app.substring(0, app.lastIndexOf(PropertyPath.UNBOUND_LEFT_INDEX_CHAR));
						Set<IField> ufields = unboundFields.get(upp);
						for(IField fld : ufields) {
							fld.setPropertyName(StringUtil.replace(fld.getPropertyName(), oldIndexToken, newIndexToken));
							// do the model update
							pv = model.getValue(propPath);
							if(pv != null) {
								if(fld.updateModel(pv)) {
									changed = true;
								}
							}
						}
					}
					else {
						// unbound non-indexed property
						throw new UnsupportedOperationException("Only indexed properties may be unbound");
					}
				}
			}
		}
		return changed;
	}

	public void bindModel(IPropertyBinding binding) {
		if(binding instanceof IModelRefProperty == false) {
			throw new IllegalArgumentException("Only model refs are bindable to field groups.");
		}
		bindModel(this, ((IModelRefProperty) binding).getModel());
	}

	public boolean updateModel(IPropertyBinding binding) {
		if(binding instanceof IModelRefProperty == false) {
			throw new IllegalArgumentException("Only model refs are updatable by field groups.");
		}
		return updateModel(this, ((IModelRefProperty) binding).getModel());
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
