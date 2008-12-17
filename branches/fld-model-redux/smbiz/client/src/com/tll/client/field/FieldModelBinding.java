/**
 * The Logic Lab
 * @author jpk
 * Dec 14, 2008
 */
package com.tll.client.field;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.IPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.BooleanValidator;
import com.tll.client.validate.CharacterValidator;
import com.tll.client.validate.DateValidator;
import com.tll.client.validate.DecimalValidator;
import com.tll.client.validate.IntegerValidator;
import com.tll.model.EntityType;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;

/**
 * FieldModelBinding - A collection of field bindings.
 * @author jpk
 */
public class FieldModelBinding {

	/**
	 * ModelPropertyDefinition - Defines a model property at a given property path
	 * relative to a given model.
	 * @author jpk
	 */
	private static final class ModelPropertyDefinition {

		/**
		 * The property path identifying the target model property.
		 */
		String propertyPath;

		/**
		 * The property type of the targeted model property.
		 */
		PropertyType propType;

		/**
		 * Relevant only for relational model properties and identifies the ascribed
		 * model type for that property.
		 */
		EntityType entityType;

		/**
		 * Constructor
		 * @param propertyPath The property path that locates the model property
		 *        relative to a root model
		 * @param propType The property type
		 * @param entityType The model type when identifying relational model
		 *        properties
		 */
		public ModelPropertyDefinition(String propertyPath, PropertyType propType, EntityType entityType) {
			super();
			this.propertyPath = propertyPath;
			this.propType = propType;
			this.entityType = entityType;
		}
	}

	/**
	 * ModelAction
	 * @author jpk
	 */
	private enum ModelAction {
		CREATE,
		REMOVE;
	}

	/**
	 * ModelBindingAction
	 * @author jpk
	 */
	private static final class ModelBindingAction {

		ModelPropertyDefinition propDef;

		ModelAction action;

		/**
		 * Constructor
		 * @param propDef
		 * @param action
		 */
		public ModelBindingAction(ModelPropertyDefinition propDef, ModelAction action) {
			super();
			this.propDef = propDef;
			this.action = action;
		}

	}

	/**
	 * FieldBinding - A coupling between an {@link IField} and a model
	 * {@link IPropertyValue} to facilite bi-directional data transfer between the
	 * two.
	 * @author jpk
	 */
	private static final class FieldBinding {

		/**
		 * The bound field.
		 */
		private final IField field;

		/**
		 * The bound model property.
		 */
		private final IPropertyValue prop;

		/**
		 * Constructor
		 * @param field The required field to bind
		 * @param prop The required model property to bind
		 */
		public FieldBinding(IField field, IPropertyValue prop) {
			if(field == null) throw new IllegalArgumentException("A field must be specified.");
			if(field instanceof Widget == false) throw new IllegalArgumentException("The field must be a Widget.");
			if(prop == null) throw new IllegalArgumentException("A model property must be specified.");
			this.field = field;
			this.prop = prop;
			bind();
		}

		private GlobalFormat getFieldFormat() {
			return (field instanceof HasFormat) ? ((HasFormat) field).getFormat() : null;
		}

		/**
		 * One-time binding of an IField to a model {@link IPropertyValue}.
		 */
		private void bind() {
			boolean required;
			int maxlen;

			// process model prop metadata
			final PropertyMetadata metadata = prop.getMetadata();
			if(metadata != null) {
				required = metadata.isRequired() && !metadata.isManaged();
				maxlen = metadata.getMaxLen();

				// set the binding (type coercion) validator
				switch(metadata.getPropertyType()) {
					case BOOL:
						field.addValidator(BooleanValidator.INSTANCE);
						break;
					case CHAR:
						field.addValidator(CharacterValidator.INSTANCE);
						break;
					case DATE: {
						switch(getFieldFormat()) {
							case DATE:
								field.addValidator(DateValidator.DATE_VALIDATOR);
								break;
							case TIME:
								field.addValidator(DateValidator.TIME_VALIDATOR);
								break;
							default:
							case TIMESTAMP:
								field.addValidator(DateValidator.TIMESTAMP_VALIDATOR);
								break;
						}
						break;
					}
					case FLOAT:
					case DOUBLE: {
						switch(getFieldFormat()) {
							case CURRENCY:
								field.addValidator(DecimalValidator.CURRENCY_VALIDATOR);
								break;
							case PERCENT:
								field.addValidator(DecimalValidator.PERCENT_VALIDATOR);
								break;
							case DECIMAL:
								field.addValidator(DecimalValidator.DECIMAL_VALIDATOR);
								break;
						}
						break;
					}
					case INT:
					case LONG:
						field.addValidator(IntegerValidator.INSTANCE);
						break;

					case ENUM:
					case STRING:
						// no type coercion validator needed
						break;

					case STRING_MAP:
						// TODO impl
						throw new UnsupportedOperationException();

					default:
						throw new IllegalStateException("Unhandled model property type: " + metadata.getPropertyType().name());
				}
			}
			else {
				required = false;
				maxlen = -1;
			}

			field.setRequired(required);
			if(field instanceof HasMaxLength) {
				((HasMaxLength) field).setMaxLen(maxlen);
			}
		}

		/**
		 * Data transfer (model -> field).
		 */
		void push() {
			// string-ize model prop value
			final String sval = Fmt.format(prop.getValue(), getFieldFormat());
			// assign it to the field
			field.setResetValue(sval);
			field.setValue(sval);
		}

		/**
		 * Data transfer (field -> model).
		 */
		void pull() {
			prop.setValue(field.getValidatedValue());
		}
	}

	/**
	 * List of model binding actions that are processed when data is sent from the
	 * fields to the model.
	 */
	private List<ModelBindingAction> modelBindingActions;

	/**
	 * The set of individual field bindings.
	 */
	private final Set<FieldBinding> set = new HashSet<FieldBinding>();

	/**
	 * Constructor
	 */
	public FieldModelBinding() {
		super();
	}

	private void addModelBindingAction(ModelBindingAction a) {
		if(modelBindingActions == null) {
			modelBindingActions = new ArrayList<ModelBindingAction>();
		}
		modelBindingActions.add(a);
	}

	private void removeModelBindingAction(ModelBindingAction a) {
		if(modelBindingActions != null) {
			modelBindingActions.remove(a);
		}
	}

	public void addRelatedOneModel(String propertyPath, EntityType entityType) {
		addModelBindingAction(new ModelBindingAction(new ModelPropertyDefinition(propertyPath, PropertyType.RELATED_ONE,
				entityType), ModelAction.CREATE));
	}

	public void removeRelatedOneModel(String propertyPath, EntityType entityType) {
		addModelBindingAction(new ModelBindingAction(new ModelPropertyDefinition(propertyPath, PropertyType.RELATED_ONE,
				entityType), ModelAction.REMOVE));
	}

	public void addIndexedModel(String propertyPath, EntityType entityType) {
		addModelBindingAction(new ModelBindingAction(new ModelPropertyDefinition(propertyPath, PropertyType.INDEXED,
				entityType), ModelAction.CREATE));
	}

	public void removeIndexedModel(String propertyPath, EntityType entityType) {
		addModelBindingAction(new ModelBindingAction(new ModelPropertyDefinition(propertyPath, PropertyType.INDEXED,
				entityType), ModelAction.REMOVE));
	}

	/**
	 * Adds a field binding.
	 * @param model The {@link Model}
	 * @param modelPropertyPath The property path targeting the model property in
	 *        the given {@link Model}
	 */
	public void addBinding(IField field, Model model, String modelPropertyPath) {
		addBinding(field, model.getPropertyValue(modelPropertyPath));
	}

	/**
	 * Adds a field binding.
	 * @param field The resolved field to be bound
	 * @param prop The resolved model property
	 */
	private void addBinding(IField field, IPropertyValue prop) {
		set.add(new FieldBinding(field, prop));
	}

	public int size() {
		return set.size();
	}

	/**
	 * Data transfer (model -> field).
	 */
	public void setFieldValues() {
		for(FieldBinding b : set) {
			b.push();
		}
	}

	/**
	 * Data transfer (field -> model).
	 */
	public void setModelValues() {

		// handle model definitions

		for(FieldBinding b : set) {
			b.pull();
		}
	}

	public void clear() {
		set.clear();
	}
}
