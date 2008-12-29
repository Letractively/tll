/*
 * Binding.java
 *
 * Created on July 16, 2007, 12:49 PM
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.tll.client.bind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.tll.client.ui.IBoundWidget;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;

/**
 * Binding - This class represents a DataBinding between two objects. It also
 * supports Child bindings.
 * <p>
 * For more information, see <a
 * href="http://code.google.com/p/gwittir/wiki/Binding">Binding</a> in the Wiki.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public final class Binding {

	private BindingInstance left;
	private BindingInstance right;
	private List<Binding> children;

	/**
	 * TRUE = left; FALSE = right;
	 */
	private Boolean lastSet = null;
	private boolean bound = false;

	/**
	 * Creates an empty Binding object. This is mostly useful for top-of-tree
	 * parent Bindings.
	 */
	public Binding() {
		super();
	}

	/**
	 * Creates a new binding. This method is a shorthand for working with
	 * BoundWidgets. The bound widget provided will become the left-hand binding,
	 * and the "value" property of the bound widget will be bound to the property
	 * specified by modelProperty of the object on the IBoundWidget's "model"
	 * property.
	 * @param widget IBoundWidget containing the model.
	 * @param validator A validator for the BouldWidget's value property.
	 * @param feedback A feedback implementation for validation errors.
	 * @param modelProperty The property on the Widgets model object to bind to.
	 */
	public Binding(IBoundWidget<?, ?, ?> widget, IValidator validator, IValidationFeedback feedback, String modelProperty) {
		this(widget, "value", validator, feedback, (IBindable) widget.getModel(), "modelProperty", null, null);
	}

	/**
	 * Creates a new instance of Binding
	 * @param left The left hand object.
	 * @param leftProperty Property on the left object.
	 * @param right The right hand object
	 * @param rightProperty Property on the right object.
	 */
	public Binding(IBindable left, String leftProperty, IBindable right, String rightProperty) {
		this.left = createBindingInstance(left, leftProperty);
		this.right = createBindingInstance(right, rightProperty);

		this.left.listener = new DefaultPropertyChangeListener(this.left, this.right);
		this.right.listener = new DefaultPropertyChangeListener(this.right, this.left);
	}

	/**
	 * Creates a new Binding instance.
	 * @param left The left hand object.
	 * @param leftProperty The property of the left hand object.
	 * @param leftIValidator A validator for the left hand property.
	 * @param leftFeedback Feedback for the left hand validator.
	 * @param right The right hand object.
	 * @param rightProperty The property on the right hand object
	 * @param rightIValidator IValidator for the right hand property.
	 * @param rightFeedback Feedback for the right hand validator.
	 */
	public Binding(IBindable left, String leftProperty, IValidator leftIValidator, IValidationFeedback leftFeedback,
			IBindable right, String rightProperty, IValidator rightIValidator, IValidationFeedback rightFeedback) {
		this.left = createBindingInstance(left, leftProperty);
		this.left.validator = leftIValidator;
		this.left.feedback = leftFeedback;

		this.right = createBindingInstance(right, rightProperty);
		this.right.validator = rightIValidator;
		this.right.feedback = rightFeedback;

		this.left.listener = new DefaultPropertyChangeListener(this.left, this.right);
		this.right.listener = new DefaultPropertyChangeListener(this.right, this.left);
	}

	/**
	 * @param left
	 * @param leftProperty
	 * @param leftConverter
	 * @param right
	 * @param rightProperty
	 * @param rightConverter
	 */
	public Binding(IBindable left, String leftProperty, IConverter leftConverter, IBindable right, String rightProperty,
			IConverter rightConverter) {
		this.left = createBindingInstance(left, leftProperty);
		this.left.converter = leftConverter;
		this.right = createBindingInstance(right, rightProperty);
		this.right.converter = rightConverter;

		this.left.listener = new DefaultPropertyChangeListener(this.left, this.right);
		this.right.listener = new DefaultPropertyChangeListener(this.right, this.left);
	}

	/**
	 * Creates a Binding with two populated binding instances.
	 * @param left The left binding instance.
	 * @param right The right binding instance
	 */
	public Binding(BindingInstance left, BindingInstance right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Sets the right object's property to the current value of the left.
	 */
	public void setRight() {
		if((left != null) && (right != null)) {
			try {
				left.listener.propertyChange(new PropertyChangeEvent(left.object, left.property, null, left.object
						.getProperty(left.property)));
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			Binding child = children.get(i);
			child.setRight();
		}

		lastSet = Boolean.FALSE;
	}

	/**
	 * Sets the left hand property to the current value of the right.
	 */
	public void setLeft() {
		if((left != null) && (right != null)) {
			try {
				right.listener.propertyChange(new PropertyChangeEvent(right.object, right.property, null, right.object
						.getProperty(right.property)));
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			Binding child = children.get(i);
			child.setLeft();
		}

		lastSet = Boolean.TRUE;
	}

	/**
	 * Establishes a two-way binding between the objects.
	 */
	public void bind() {
		if((left != null) && (right != null)) {
			left.object.addPropertyChangeListener(left.property, left.listener);

			if(left.nestedListener != null) {
				left.nestedListener.setup();
			}

			right.object.addPropertyChangeListener(right.property, right.listener);

			if(right.nestedListener != null) {
				right.nestedListener.setup();
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			Binding child = children.get(i);
			child.bind();
		}

		bound = true;
	}

	/**
	 * Returns a list of child Bindings.
	 * @return List of child bindings.
	 */
	public List<Binding> getChildren() {
		return children = (children == null) ? new ArrayList<Binding>() : children;
	}

	public boolean validate() {
		boolean valid = true;

		if((left != null) && (right != null)) {
			if(left.validator != null) {
				try {
					left.validator.validate(left.object.getProperty(left.property));
				}
				catch(ValidationException ve) {
					valid = false;

					if(left.feedback != null) {
						left.feedback.handleException(left.object, ve);
					}

					if(left.listener instanceof DefaultPropertyChangeListener) {
						((DefaultPropertyChangeListener) left.listener).lastException = ve;
					}
				}
				catch(Exception e) {
					valid = false;
					// Binding.LOGGER.log(Level.WARN, null, e);
					GWT.log("Binding warning", e);
				}
			}

			if(right.validator != null) {
				try {
					right.validator.validate(right.object.getProperty(right.property));
				}
				catch(ValidationException ve) {
					valid = false;

					if(right.feedback != null) {
						right.feedback.handleException(right.object, ve);
					}

					if(right.listener instanceof DefaultPropertyChangeListener) {
						((DefaultPropertyChangeListener) right.listener).lastException = ve;
					}
				}
				catch(Exception e) {
					valid = false;
					// Binding.LOGGER.log(Level.WARN, null, e);
					GWT.log("Binding warning", e);
				}
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			Binding child = children.get(i);
			valid = valid & child.isValid();
		}

		return valid;
	}

	/**
	 * Performs a quick validation on the Binding to determine if it is valid.
	 * @return boolean indicating all values are valid.
	 */
	public boolean isValid() {
		try {
			if((left != null) && (right != null)) {
				if(left.validator != null) {
					left.validator.validate(left.object.getProperty(left.property));
				}

				if(right.validator != null) {
					right.validator.validate(right.object.getProperty(right.property));
				}
			}

			boolean valid = true;

			for(int i = 0; (children != null) && (i < children.size()); i++) {
				Binding child = children.get(i);
				valid = valid & child.isValid();
			}

			return valid;
		}
		catch(ValidationException ve) {
			// Binding.LOGGER.log(Level.INFO, "Invalid", ve);
			GWT.log("Binding validation info", ve);

			return false;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Breaks the two way binding and removes all listeners.
	 */
	public void unbind() {
		if((left != null) && (right != null)) {
			left.object.removePropertyChangeListener(left.property, left.listener);

			if(left.nestedListener != null) {
				left.nestedListener.cleanup();
			}

			right.object.removePropertyChangeListener(right.property, right.listener);

			if(right.nestedListener != null) {
				right.nestedListener.cleanup();
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			Binding child = children.get(i);
			child.unbind();
		}

		bound = false;
	}

	/**
	 * Returns the left hand BindingInstance.
	 * @return Returns the left hand BindingInstance.
	 */
	public BindingInstance getLeft() {
		return left;
	}

	/**
	 * Returns the right hand BindingInstance.
	 * @return Returns the left hand BindingInstance.
	 */
	public BindingInstance getRight() {
		return right;
	}

	@Override
	public boolean equals(final Object obj) {
		if(obj == null) {
			return false;
		}

		final Binding other = (Binding) obj;
		if(left != other.left && (left == null || !left.equals(other.left))) {
			return false;
		}
		if(right != other.right && (right == null || !right.equals(other.right))) {
			return false;
		}
		return true;
	}

	/**
	 * @return int based on hash of the two objects being bound.
	 */
	@Override
	public int hashCode() {
		return right.object.hashCode() ^ left.object.hashCode();
	}

	/**
	 * @return String value representing the binding.
	 */
	@Override
	public String toString() {
		return "Binding [ \n + " + right.object + " \n " + left.object + "\n ]";
	}

	@SuppressWarnings("unchecked")
	private IBindable getDiscriminatedObject(Object collectionOrArray, String discriminator) {
		int equalsIndex = discriminator.indexOf("=");

		if(collectionOrArray instanceof Collection && (equalsIndex == -1)) {
			return getBindableAtCollectionIndex((Collection<IBindable>) collectionOrArray, Integer.parseInt(discriminator));
		}
		else if(collectionOrArray instanceof Collection) {
			return getBindableHavingPropertyOfValue((Collection<IBindable>) collectionOrArray, discriminator.substring(0,
					equalsIndex), discriminator.substring(equalsIndex + 1));
		}
		else if(collectionOrArray instanceof Map) {
			return getBindableAtMapKey((Map) collectionOrArray, discriminator);
		}
		else if(equalsIndex == -1) {
			return ((IBindable[]) collectionOrArray)[Integer.parseInt(discriminator)];
		}
		else {
			return getBindableHavingPropertyOfValue((IBindable[]) collectionOrArray, discriminator.substring(0, equalsIndex),
					discriminator.substring(equalsIndex + 1));
		}
	}

	private IBindable getBindableHavingPropertyOfValue(IBindable[] array, String propertyName, String stringValue) {
		for(IBindable b : array) {
			try {
				if((b.getProperty(propertyName) + "").equals(stringValue)) {
					return b;
				}
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
		throw new RuntimeException("No Object matching " + propertyName + "=" + stringValue);
	}

	private IBindable getBindableHavingPropertyOfValue(Iterable<IBindable> itr, String propertyName, String stringValue) {
		for(IBindable b : itr) {
			try {
				if((b.getProperty(propertyName) + "").equals(stringValue)) {
					return b;
				}
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		throw new RuntimeException("No Object matching " + propertyName + "=" + stringValue);
	}

	private IBindable getBindableAtMapKey(Map<String, IBindable> map, String key) {
		IBindable result = null;

		for(Iterator<Entry<String, IBindable>> it = map.entrySet().iterator(); it.hasNext();) {
			Entry<String, IBindable> e = it.next();
			if(e.getKey().toString().equals(key)) {
				result = e.getValue();
				break;
			}
		}

		return result;
	}

	private IBindable getBindableAtCollectionIndex(Iterable<IBindable> collection, int index) {
		int i = 0;
		IBindable result = null;

		for(Iterator<IBindable> it = collection.iterator(); it.hasNext() && (i <= index); i++) {
			result = it.next();
			return result;
		}

		throw new IndexOutOfBoundsException("Binding discriminator too high: " + index);
	}

	private BindingInstance createBindingInstance(IBindable object, String propertyName) {
		int dotIndex = propertyName.indexOf(".");
		BindingInstance instance = new BindingInstance();
		NestedPropertyChangeListener rtpcl =
				(dotIndex == -1) ? null : new NestedPropertyChangeListener(instance, object, propertyName);
		ArrayList<IBindable> parents = new ArrayList<IBindable>();
		ArrayList<String> propertyNames = new ArrayList<String>();

		while(dotIndex != -1) {
			String pname = propertyName.substring(0, dotIndex);
			propertyName = propertyName.substring(dotIndex + 1);
			parents.add(object);

			try {
				String discriminator = null;
				int descIndex = pname.indexOf("[");

				if(descIndex != -1) {
					discriminator = pname.substring(descIndex + 1, pname.indexOf("]", descIndex));
					pname = pname.substring(0, descIndex);
				}

				propertyNames.add(pname);

				if(discriminator != null) {
					// TODO Need a loop here to handle multi-dimensional/collections of
					// collections
					object = getDiscriminatedObject(object.getProperty(pname), discriminator);
				}
				else {
					object = (IBindable) object.getProperty(pname);
				}
			}
			catch(ClassCastException cce) {
				throw new RuntimeException("Nonbindable sub property: " + object + " . " + pname, cce);
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}

			dotIndex = propertyName.indexOf(".");
		}

		if(rtpcl != null) {
			rtpcl.parents = new IBindable[parents.size()];
			parents.toArray(rtpcl.parents);
			rtpcl.propertyNames = new String[propertyNames.size()];
			propertyNames.toArray(rtpcl.propertyNames);
		}

		instance.object = object;
		instance.property = propertyName;
		instance.nestedListener = rtpcl;

		return instance;
	}

	/**
	 * A data class containing the relevant data for one half of a binding
	 * relationship.
	 */
	private static final class BindingInstance {

		/**
		 * The Object being bound.
		 */
		public IBindable object;

		/**
		 * A converter when needed.
		 */
		public IConverter converter;

		/**
		 * The full property path of the property being bound.
		 */
		public String property;

		private IPropertyChangeListener listener;

		/**
		 * A IValidationFeedback object when needed.
		 */
		public IValidationFeedback feedback;

		/**
		 * A IValidator object when needed.
		 */
		public IValidator validator;

		private NestedPropertyChangeListener nestedListener;

		/**
		 * Constructor
		 */
		private BindingInstance() {
			super();
		}
	}

	/**
	 * DefaultPropertyChangeListener
	 * @author jpk
	 */
	private static final class DefaultPropertyChangeListener implements IPropertyChangeListener {

		private final BindingInstance instance;
		private final BindingInstance target;
		private ValidationException lastException = null;

		/**
		 * Constructor
		 * @param instance
		 * @param target
		 */
		DefaultPropertyChangeListener(BindingInstance instance, BindingInstance target) {
			this.instance = instance;
			this.target = target;
		}

		public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
			Object value = propertyChangeEvent.getNewValue();

			if(instance.validator != null) {
				try {
					value = instance.validator.validate(value);
				}
				catch(ValidationException ve) {
					if(instance.feedback != null) {
						if(lastException != null) {
							instance.feedback.resolve(propertyChangeEvent.getSource());
						}

						instance.feedback.handleException(propertyChangeEvent.getSource(), ve);
						lastException = ve;
					}
					else {
						lastException = ve;
						throw new RuntimeException(ve);
					}
				}
			}

			if(instance.feedback != null) {
				instance.feedback.resolve(propertyChangeEvent.getSource());
			}

			lastException = null;

			if(instance.converter != null) {
				value = instance.converter.convert(value);
			}

			try {
				target.object.setProperty(target.property, value);
				// target.property.getMutatorMethod().invoke(target.object, args);
			}
			catch(Exception e) {
				throw new RuntimeException("Exception setting property: " + target.property, e);
			}
		}

		@Override
		public String toString() {
			return "[Listener on : " + instance.object + " ] ";
		}
	}

	/**
	 * NestedPropertyChangeListener
	 * @author jpk
	 */
	private final class NestedPropertyChangeListener implements IPropertyChangeListener {

		BindingInstance target;
		IBindable sourceObject;
		String propertyName;
		IBindable[] parents;
		String[] propertyNames;

		NestedPropertyChangeListener(BindingInstance target, IBindable sourceObject, String propertyName) {
			this.target = target;
			this.sourceObject = sourceObject;
			this.propertyName = propertyName;
		}

		public void propertyChange(PropertyChangeEvent evt) {
			if(bound) {
				unbind();
				bound = true;
			}

			BindingInstance newInstance = createBindingInstance(sourceObject, propertyName);
			target.object = newInstance.object;
			target.nestedListener = newInstance.nestedListener;

			if(lastSet == Boolean.TRUE) {
				setLeft();
			}
			else if(lastSet == Boolean.FALSE) {
				setRight();
			}

			if(bound) {
				bind();
			}
		}

		public void setup() {
			for(int i = 0; i < parents.length; i++) {
				parents[i].addPropertyChangeListener(propertyNames[i], this);
			}
		}

		public void cleanup() {
			for(int i = 0; i < parents.length; i++) {
				parents[i].removePropertyChangeListener(propertyNames[i], this);
			}
		}
	}
}
