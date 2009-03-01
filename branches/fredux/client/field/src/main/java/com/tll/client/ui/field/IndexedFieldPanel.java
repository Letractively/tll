/**
 * The Logic Lab
 * @author jpk Jan 3, 2009
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.tll.client.bind.Binding;
import com.tll.client.ui.AbstractBindableWidget;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.common.bind.IBindable;
import com.tll.common.model.PropertyPathException;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * IndexedFieldPanel - Caters to the display of field collections that are
 * indexable by adapting the {@link FieldPanel} with index related routines. All
 * indexing operations sync with the {@link FieldPanel}'s member ("underlying")
 * field group.
 * @param <I> the index field panel type
 * @author jpk
 */
public abstract class IndexedFieldPanel<I extends FieldPanel<?>> extends AbstractBindableWidget<Collection<IBindable>>
		implements IFieldGroupProvider {

	/**
	 * Index - Encapsulates a model and field panel for an indexed position.
	 * @param <I> The index type
	 * @author jpk
	 */
	public static final class Index<I> {

		final IBindable model;
		final I fieldPanel;
		boolean markedDeleted;

		/**
		 * Constructor
		 * @param model
		 * @param fieldPanel
		 */
		public Index(IBindable model, I fieldPanel) {
			super();
			this.model = model;
			this.fieldPanel = fieldPanel;
		}

		public IBindable getModel() {
			return model;
		}

		public I getFieldPanel() {
			return fieldPanel;
		}

	} // Index

	/**
	 * The indexed property name used to bind this indexed widget to a parent
	 * binding action.
	 */
	private final String indexedPropertyName;

	/**
	 * The group that serving as a common parent to the index field groups.
	 */
	private final FieldGroup topGroup;

	/**
	 * The list of indexed {@link FieldPanel}s.
	 */
	protected final List<Index<I>> indexPanels = new ArrayList<Index<I>>();

	/**
	 * The model data collection.
	 */
	private Collection<IBindable> value;

	/**
	 * Holds bindings that bind {@link #value} to {@link #indexPanels}. Each
	 * sub-binding under this binding corresponds to the field group of the same
	 * index in the {@link #indexPanels}.
	 */
	private final Binding binding = new Binding();

	private boolean drawn;

	/**
	 * Constructor
	 * @param name The name to ascribe to this field panel which serves the name
	 *        of the underlying top field group as well
	 * @param indexedPropertyName The name of the indexed property relative to a
	 *        parent root model.
	 */
	public IndexedFieldPanel(String name, String indexedPropertyName) {
		super();
		this.topGroup = new FieldGroup(name);
		this.indexedPropertyName = indexedPropertyName;
	}

	/**
	 * @return The top-most aggregate {@link FieldGroup} containing the index
	 *         {@link FieldGroup}s.
	 */
	public final FieldGroup getFieldGroup() {
		return topGroup;
	}

	/**
	 * @return The property path that identifies this indexed property in a parent
	 *         root model
	 */
	public final String getIndexedPropertyName() {
		return indexedPropertyName;
	}

	/**
	 * @return The number of indexed {@link FieldPanel}s.
	 */
	protected final int size() {
		return indexPanels.size();
	}

	/**
	 * @return A prototype model used to bind a newly added indexed field panel.
	 *         This model will be bound to the added field panel.
	 */
	protected abstract IBindable createPrototypeModel();

	/**
	 * Factory method to obtain a new index field panel instance.
	 * @param indexModel Provided to set field panel specific properties and,
	 *        consequently, may be ignored if there are no such properties.
	 * @return new field panel instance for employ at a particular index.
	 */
	protected abstract I createIndexPanel(IBindable indexModel);

	/**
	 * Draws the composite wrapped widget.
	 */
	protected abstract void draw();

	/**
	 * Adds a new indexed field group
	 */
	protected Index<I> add() {
		return add(createPrototypeModel());
	}

	/**
	 * Adds an indexed field group populating the fields with data held in the
	 * given model.
	 * @param model The model to be converted to a field group
	 * @return The added field panel
	 * @throws IllegalArgumentException When the field to add already exists in
	 *         the underlying field group or has the same name as an existing
	 *         field in the underlying group.
	 */
	private Index<I> add(IBindable model) throws IllegalArgumentException {
		Log.debug("IndexedFieldPanel.add() - START");
		final I ip = createIndexPanel(model);

		// apply property metadata
		if(model instanceof IPropertyMetadataProvider) {
			ip.getFieldGroup().applyPropertyMetadata((IPropertyMetadataProvider) model);
		}

		final Index<I> index = new Index<I>(model, ip);
		indexPanels.add(index);

		ip.getFieldGroup().setName(topGroup.getName() + '[' + (size() - 1) + ']');
		topGroup.addField(ip.getFieldGroup());
		final Binding indexBinding = new Binding();
		binding.getChildren().add(indexBinding);
		bind(ip.getFieldGroup(), model, indexBinding);

		return index;
	}

	/**
	 * Updates an <em>existing</em> indexed field group with data provided by the
	 * given model syncing with the underlying field group.
	 * @param index The index at which the field group is replaced
	 * @param model The model from which data is retrieved to update the field
	 *        group
	 * @throws IndexOutOfBoundsException When the given index is out of bounds
	 */
	protected void update(int index, IBindable model) throws IndexOutOfBoundsException {
		assert model != null;
		unbindAtIndex(index);
		final FieldGroup fg = indexPanels.get(index).fieldPanel.getFieldGroup();
		bind(fg, model, binding.getChildren().get(index));
	}

	/**
	 * Removes an indexed field group at the given index syncing with the
	 * underlying field group.
	 * @param index The index at which the field group is removed
	 * @return The removed field group
	 * @throws IndexOutOfBoundsException
	 */
	protected Index<I> remove(int index) throws IndexOutOfBoundsException {
		final Index<I> removed = indexPanels.remove(index);
		topGroup.removeField(removed.fieldPanel.getFieldGroup());
		unbindAtIndex(index);
		return removed;
	}

	/**
	 * Marks or un-marks a field panel at the given index as deleted.
	 * @param index
	 * @param deleted mark as deleted or un-deleted?
	 * @throws IndexOutOfBoundsException
	 */
	protected void markDeleted(int index, boolean deleted) throws IndexOutOfBoundsException {
		final Index<I> ip = indexPanels.get(index);
		ip.markedDeleted = deleted;
		ip.fieldPanel.getFieldGroup().setEnabled(!deleted);
	}

	/**
	 * Unbinds and removes all child bindings at the given index.
	 * @param index The index at which to unbind
	 */
	private void unbindAtIndex(int index) {
		final Binding indexBinding = binding.getChildren().get(index);
		indexBinding.unbind();
		indexBinding.getChildren().clear();
	}

	/**
	 * Regenerates the indexed field group list from the member {@link #value}
	 * collection.
	 */
	private void regenerate() {
		Log.debug("IndexedFieldPanel.regenerate() - START");
		clear();
		if(value != null) {
			for(final IBindable m : value) {
				add(m);
			}
		}
		Log.debug("IndexedFieldPanel.regenerate() - END");
	}

	/**
	 * Creates bindings for the fields in the given field panel to properties on
	 * the given model then updates the field values.
	 * @param fg The indexed field group to be updated
	 * @param model The model
	 * @param indexBinding If specified, bindings for each field in the given
	 *        field group whose value is successfully set will be added under this
	 *        binding.
	 */
	private void bind(FieldGroup fg, final IBindable model, final Binding indexBinding) {
		assert indexBinding != null;
		fg.setMsgPopupRegistry(getMsgPopupRegistry());
		for(final IField f : fg) {
			if(f instanceof IFieldWidget) {
				final IFieldWidget<?> fw = (IFieldWidget<?>) f;
				final String propName = fw.getPropertyName();
				//try {
					// bind
					indexBinding.getChildren().add(
							new Binding(model, propName, null, null, fw, IBindableWidget.PROPERTY_VALUE, fw,
									new FieldValidationFeedback(getMsgPopupRegistry())));
					// set field value
					//fw.setProperty(IBindableWidget.PROPERTY_VALUE, model.getProperty(propName));
				//}
				/*
				catch(final UnsetPropertyException e) {
					// ok
				}
				catch(final NullNodeInPropPathException e) {
					// ok
				}
				catch(final PropertyPathException e) {
					// bad proeperty path/name
					throw new IllegalStateException(e);
				}
				catch(final Exception e) {
					// bad value
					throw new IllegalStateException(e);
				}
				*/
			}
			else {
				// drill down
				bind((FieldGroup) f, model, indexBinding);
			}
		}
		indexBinding.bind();
		indexBinding.setRight();
	}

	/**
	 * Clears the field group list cleaning up bindings and listeners.
	 */
	public void clear() {
		Log.debug("IndexedFieldPanel.clearing " + toString() + "..");
		binding.unbind();
		binding.getChildren().clear();
		topGroup.clear();
		indexPanels.clear();
	}

	public final Collection<IBindable> getValue() {
		return value;
	}

	public final void setValue(Collection<IBindable> value) {
		if(this.value != null && this.value == value) return;
		final Collection<IBindable> old = this.value;
		this.value = value;
		changeSupport.firePropertyChange(IBindableWidget.PROPERTY_VALUE, old, this.value);
		regenerate();
	}

	@Override
	public void setMsgPopupRegistry(MsgPopupRegistry mregistry) {
		super.setMsgPopupRegistry(mregistry);
		// propagate to the fields
		topGroup.setMsgPopupRegistry(mregistry);
	}

	@Override
	public void setValue(Collection<IBindable> value, boolean fireEvents) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Collection<IBindable>> handler) {
		throw new UnsupportedOperationException();
	}

	public final Object getProperty(String propPath) {
		return getValue();
	}

	/*
	 * NOTE: we ignore the given property path
	 */
	@SuppressWarnings("unchecked")
	public final void setProperty(String propPath, Object value) throws PropertyPathException, Exception {
		if(value instanceof Collection == false) {
			throw new Exception("The value must be a collection");
		}
		setValue((Collection<IBindable>) value);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if(!drawn) {
			Log.debug("IndexedFieldPanel.onAttach() drawing..");
			draw();
			drawn = true;
		}
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		clear();
	}

	@Override
	public String toString() {
		return "IndexedFieldPanel [ " + indexedPropertyName + " ]";
	}
}
