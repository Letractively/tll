/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.model.IModelProperty;
import com.tll.client.model.IModelRefProperty;
import com.tll.client.model.IPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.ui.TimedPositionedPopup.Position;
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
 * A FieldGroup represents a grouping of {@link IField}s for UI purposes and as
 * such <em>does not necessarily represent model hierarchy boundaries</em>
 * <p>
 * To fully support data transfer ("binding") between a FieldGroup instance and
 * a Model instance, the following conventions are established:
 * <ol>
 * <li>Non-FieldGroup {@link IField}s contained in a FieldGroup are expected to
 * have a standard OGNL property name to support binding to/from the underlying
 * Model.
 * <li>Newly created IFields that map to a non-existant related many Model are
 * expected to have a property name that indicates as much:
 * <code>indexableProp{index}.propertyName</code> as opposed to the standard:
 * <code>indexableProp[index].propertyName</code>. In other words, curly braces
 * (<code>{}</code>) are used instead of square braces (<code>[]</code>).
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
				final IField rfld = findField(propertyName, fg);
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
	 * The collection of child fields.
	 */
	private final Set<IField> fields;

	/**
	 * The collection of validators.
	 */
	private CompositeValidator validators;

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
	 * The optional binding listener.
	 */
	private final IFieldBindingListener bindingListener;

	/**
	 * Flag to indicate whether or not the contents of this group shall be
	 * transferred to the model or not.
	 */
	private boolean updateModel;

	/**
	 * Collection of property paths marked for delete. These deletions are
	 * conveyed to the model when the model is updated.
	 */
	private Set<String> pendingDeletes;

	/**
	 * Constructor - Simple field group w/ no display name nor
	 * {@link IFieldBindingListener}.
	 * @param fields The fields that will make up this group
	 * @param displayName The UI display name used for presenting validation
	 *        feedback to the UI.
	 * @param feedbackWidget The feedback Widget
	 */
	public FieldGroup(Set<IField> fields, String displayName, Widget feedbackWidget) {
		super();
		this.fields = fields;
		this.displayName = displayName;
		this.feedbackWidget = feedbackWidget;
		this.bindingListener = null;
	}

	/**
	 * Constructor
	 * @param displayName The UI display name used for presenting validation
	 *        feedback to the UI.
	 * @param feedbackWidget The feedback Widget
	 * @param bindingListener The optional binding listener.
	 */
	public FieldGroup(String displayName, Widget feedbackWidget, IFieldBindingListener bindingListener) {
		this.fields = new HashSet<IField>();
		this.displayName = displayName;
		this.feedbackWidget = feedbackWidget;
		this.bindingListener = bindingListener;
	}

	public String descriptor() {
		return displayName;
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
	 * Associates a property path to a delete request.
	 * @param propertyPath The property path to be marked for delete
	 */
	public void addPendingDelete(String propertyPath) {
		if(pendingDeletes == null) {
			pendingDeletes = new HashSet<String>();
		}
		pendingDeletes.add(propertyPath);
	}

	/**
	 * Un-schedules a pending delete for the given property path.
	 * @param propertyPath The property path to remove from the pending deletions.
	 * @see #addPendingDelete(String)
	 */
	public void removePendingDelete(String propertyPath) {
		if(pendingDeletes != null) {
			pendingDeletes.remove(propertyPath);
		}
	}

	/**
	 * Toggle-able flag used to set whether these fields are to be applied to the
	 * model with it is updated.
	 * @return <code>true</code> if these fields are to be applied to the model.
	 */
	public boolean isUpdateModel() {
		return updateModel;
	}

	/**
	 * Update the model with these fields?
	 * @param updateModel true/false
	 */
	public void setUpdateModel(boolean updateModel) {
		this.updateModel = updateModel;
	}

	/**
	 * Is the given property path scheduled for deletion?
	 * @param propertyPath The property path to check
	 * @return true/false
	 */
	public boolean isPendingDelete(String propertyPath) {
		return pendingDeletes != null && pendingDeletes.contains(propertyPath);
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
	public void removeFields(Collection<IField> clc) {
		if(clc != null) {
			for(IField fld : clc) {
				removeField(fld);
			}
		}
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

	public void markReset() {
		for(IField field : fields) {
			field.markReset();
		}
	}

	public void reset() {
		MsgManager.instance.clear(feedbackWidget, true);
		for(IField field : fields) {
			field.reset();
		}
	}

	public void clear() {
		MsgManager.instance.clear(feedbackWidget, true);
		for(IField field : fields) {
			field.clear();
		}
	}

	/**
	 * @return The number of child fields.
	 */
	public int size() {
		return fields.size();
	}

	/**
	 * For the case of a {@link FieldGroup}, we provide a comma delimited list of
	 * field values for all child {@link IField}s.
	 */
	public String getValue() {
		StringBuilder sb = new StringBuilder();
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

	private void onBeforeBind(Model model) {
		// reset pending deletes
		if(pendingDeletes != null) pendingDeletes.clear();
		// reset update model flag
		updateModel = true;
		// provide an opportunity for the owning panel to ready their field group
		// before actual binding
		if(bindingListener != null) bindingListener.onBeforeBind(model);
	}

	private void onAfterBind() {
		if(bindingListener != null) bindingListener.onAfterBind();
	}

	/**
	 * Recursively binds a FieldGroup to a Model.
	 * @param group The FieldGroup
	 * @param model The Model
	 * @param propertyPathOffset The node index at which the given FieldGroup
	 *        binds to the given Model.
	 */
	private static void bindModel(final int propertyPathOffset, FieldGroup group, final Model model) {
		group.onBeforeBind(model);
		final PropertyPath propPath = new PropertyPath();
		for(IField fld : group) {
			if(fld instanceof FieldGroup) {
				bindModel(propertyPathOffset, (FieldGroup) fld, model);
			}
			else {
				PropertyPath resolved = propPath;
				propPath.parse(fld.getPropertyName());
				if(propertyPathOffset > 0) {
					resolved = propPath.nested(propertyPathOffset);
				}
				IPropertyValue pv = model.getValue(resolved);
				if(pv == null) {
					fld.clear();
				}
				else {
					fld.bindModel(pv);
				}
			}
		}
		group.onAfterBind();
	}

	/**
	 * Updates the model by recursively traversing the given FieldGroup.
	 * @param group The FieldGroup
	 * @param model The Model to be updated
	 * @param unboundFieldsMap
	 * @param depth The recursion depth
	 * @return <code>true</code> when at least one valid update was transferred to
	 *         the given model.
	 */
	private static boolean updateModel(FieldGroup group, final Model model,
			final Map<PropertyPath, Set<IField>> unboundFields, int depth) {

		if(!group.updateModel) return false;

		boolean changed = false;

		// first apply scheduled deletions
		if(group.pendingDeletes != null) {
			final PropertyPath pp = new PropertyPath();
			for(String path : group.pendingDeletes) {
				pp.parse(path);
				IModelProperty prop = model.getBinding(pp);
				if(prop.getType().isModelRef()) {
					((IModelRefProperty) prop).getModel().setMarkedDeleted(true);
					changed = true;
				}
			}
		}

		IPropertyValue pv;
		final PropertyPath propPath = new PropertyPath();
		for(IField fld : group) {
			if(fld instanceof FieldGroup) {
				if(updateModel((FieldGroup) fld, model, unboundFields, depth + 1)) {
					changed = true;
				}
			}
			else {
				// non-group field
				try {
					propPath.parse(fld.getPropertyName());

					int n = propPath.nextIndexedNode(0, true);
					if(n >= 0) {
						// unbound property!
						PropertyPath unboundPath = n == 0 ? new PropertyPath(propPath.pathAt(0)) : propPath.ancestor(n);
						assert unboundPath != null && unboundPath.length() > 0;
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

		if(depth == 0) {
			// handle the unbound indexed props (newly created in the ui)
			if(unboundFields.size() > 0) {
				for(PropertyPath upp : unboundFields.keySet()) {
					// create the missing properties in the model
					if(upp.isIndexed()) {
						// unbound indexed property
						RelatedManyProperty rmp = (RelatedManyProperty) model.getBinding(upp.indexedParent());
						Model stub = AuxDataCache.instance().getEntityPrototype(rmp.getRelatedType());
						if(stub == null)
							throw new IllegalStateException("Unable to acquire a " + rmp.getRelatedType().getName()
									+ " model prototype");

						// now we need to propagate the actual property path to the child
						// fields

						// actual property path RELATIVE TO THE RELATED MANY PROPERTY
						// BINDING AND NOT THE ROOT MODEL
						// e.g. relatedMany[1] NOT root.relatedMany[1]
						final String app = rmp.add(stub);

						// the depth index of the unbound prop path
						final int depthIndex = upp.depth() - 1;
						assert depthIndex >= 0;

						Set<IField> ufields = unboundFields.get(upp);
						for(IField fld : ufields) {

							// replace the indexed property path node
							propPath.parse(fld.getPropertyName());
							assert propPath.depth() > depthIndex;
							propPath.replaceAt(depthIndex, app);

							fld.setPropertyName(propPath.toString());
							// do the model update
							pv = model.getValue(propPath);
							if(pv != null) fld.updateModel(pv);
						}
						changed = true;
					}
					else {
						// unbound non-indexed property
						throw new UnsupportedOperationException("Only indexed properties may be unbound");
					}
				}
			}

			if(!changed) {
				MsgManager.instance.post(true, new Msg("No edits detected.", MsgLevel.WARN), Position.CENTER,
						group.feedbackWidget, -1, true).show();
			}
		}

		return changed;
	}

	/**
	 * Binds a Model to this group.
	 * @param propPathOffset Property path node index representing the parent
	 *        property path offset that is applied to all non-group child IFields.
	 * @param modelref The model ref property
	 */
	public void bindModel(int propPathOffset, IModelRefProperty modelref) {
		bindModel(propPathOffset, this, modelref.getModel());
	}

	public void bindModel(IModelProperty prop) {
		if(prop instanceof IModelRefProperty == false) {
			throw new IllegalArgumentException("Only model refs are bindable to field groups.");
		}
		bindModel(0, (IModelRefProperty) prop);
	}

	public boolean updateModel(IModelProperty prop) {
		if(prop instanceof IModelRefProperty == false) {
			throw new IllegalArgumentException("Only model refs are updatable by field groups.");
		}
		return updateModel(this, ((IModelRefProperty) prop).getModel(), new HashMap<PropertyPath, Set<IField>>(), 0);
	}

	public void addValidator(IValidator validator) {
		if(validators == null) {
			validators = new CompositeValidator();
		}
		validators.add(validator);
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
			MsgManager.instance.post(true, unboundFieldMessages, Position.BOTTOM, feedbackWidget, -1, true).show();
		}
	}

	@Override
	public String toString() {
		return descriptor() + " (FieldGroup)";
	}
}
