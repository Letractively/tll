/**
 * The Logic Lab
 * @author jpk
 * Jan 3, 2009
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.Binding;
import com.tll.client.ui.AbstractBoundWidget;
import com.tll.client.ui.IBoundWidget;
import com.tll.common.bind.IBindable;
import com.tll.common.model.NullNodeInPropPathException;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.UnsetPropertyException;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * IndexedFieldPanel - Caters to the display of field collections that are
 * indexable by adapting the {@link FieldPanel} with index related routines. All
 * indexing operations sync with the {@link FieldPanel}'s member ("underlying")
 * field group.
 * @param <I> the index field panel type
 * @param <M> the model type
 * @author jpk
 */
public abstract class IndexedFieldPanel<I extends FieldPanel<? extends Widget, M>, M extends IBindable> extends
		AbstractBoundWidget<Collection<M>, Collection<M>, M> implements IFieldGroupProvider/*, Iterable<I>*/{

	/**
	 * Index
	 * @author jpk
	 * @param <I> The index type
	 */
	protected static final class Index<I> {

		I fp;
		boolean markedDeleted;

		/**
		 * Constructor
		 * @param fp
		 */
		public Index(I fp) {
			super();
			this.fp = fp;
		}
	} // Index

	/*
	private final class IndexIterator implements Iterator<I> {

		public IndexIterator() {
			super();
			this.size = indexPanels == null ? 0 : indexPanels.size();
		}

		private int index = -1;
		private final int size;

		public boolean hasNext() {
			return index < (size - 1);
		}

		public I next() {
			if(index >= size) {
				throw new NoSuchElementException();
			}
			return indexPanels.get(++index).fp;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	} // IndexIterator
	*/

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
	private Collection<M> value;

	/**
	 * Holds bindings that bind {@link #value} to {@link #list}. Each sub-binding
	 * under this binding corresponds to a the field group of the same index in
	 * the {@link #list}.
	 */
	private final Binding binding = new Binding();

	private boolean drawn;

	/**
	 * Constructor
	 * @param name The name to ascribe to this field panel which serves the name
	 *        of the underlying top field group as well
	 */
	public IndexedFieldPanel(String name) {
		super();
		this.topGroup = new FieldGroup(name);
	}

	/*
	public Iterator<I> iterator() {
		return new IndexIterator();
	}
	*/

	/**
	 * @return The top-most aggregate {@link FieldGroup} containing the index
	 *         {@link FieldGroup}s.
	 */
	public final FieldGroup getFieldGroup() {
		return topGroup;
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
	protected abstract M createPrototypeModel();

	/**
	 * Factory method to obtain a new index field panel instance.
	 * @param indexModel Provided to set field panel specific properties and,
	 *        consequently, may be ignored if there are no such properties.
	 * @return new field panel instance for employ at a particular index.
	 */
	protected abstract I createIndexPanel(M indexModel);

	/**
	 * Draws the composite wrapped widget.
	 */
	protected abstract void draw();

	/**
	 * Adds a new indexed field group
	 */
	protected I add() {
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
	private I add(M model) throws IllegalArgumentException {
		Log.debug("IndexedFieldPanel.add() - START");
		final I ip = createIndexPanel(model);

		ip.setModel(model);

		// apply property metadata
		if(model instanceof IPropertyMetadataProvider) {
			ip.getFieldGroup().applyPropertyMetadata((IPropertyMetadataProvider) model);
		}

		indexPanels.add(new Index<I>(ip));

		ip.getFieldGroup().setName(topGroup.getName() + '[' + (size() - 1) + ']');
		topGroup.addField(ip.getFieldGroup());
		final Binding indexBinding = new Binding();
		binding.getChildren().add(indexBinding);
		bind(ip.getFieldGroup(), model, indexBinding);

		return ip;
	}

	/**
	 * Updates an <em>existing</em> indexed field group with data provided by the
	 * given model syncing with the underlying field group.
	 * @param index The index at which the field group is replaced
	 * @param model The model from which data is retrieved to update the field
	 *        group
	 * @throws IndexOutOfBoundsException When the given index is out of bounds
	 */
	protected void update(int index, M model) throws IndexOutOfBoundsException {
		assert model != null;
		unbindAtIndex(index);
		final FieldGroup fg = indexPanels.get(index).fp.getFieldGroup();
		bind(fg, model, binding.getChildren().get(index));
	}

	/**
	 * Removes an indexed field group at the given index syncing with the
	 * underlying field group.
	 * @param index The index at which the field group is removed
	 * @return The removed field group
	 * @throws IndexOutOfBoundsException
	 */
	protected I remove(int index) throws IndexOutOfBoundsException {
		final Index<I> removed = indexPanels.remove(index);
		topGroup.removeField(removed.fp.getFieldGroup());
		unbindAtIndex(index);
		return removed.fp;
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
		ip.fp.getFieldGroup().setEnabled(!deleted);
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
			for(final M m : value) {
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
	private void bind(FieldGroup fg, M model, Binding indexBinding) {
		for(final IField<?, ?> f : fg) {
			if(f instanceof FieldGroup == false) {
				final String propName = f.getPropertyName();
				try {

					// bind
					// TODO specify a MsgPopupRegistry in the FieldValidationFeedback constructor below 
					if(indexBinding != null) {
						binding.getChildren().add(
								new Binding(model, propName, null, null, f, IBoundWidget.PROPERTY_VALUE, f,
										new FieldValidationFeedback(null)));
					}

					// set field value
					f.setProperty(IBoundWidget.PROPERTY_VALUE, model.getProperty(propName));
				}
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
			}
			else {
				// drill down
				bind((FieldGroup) f, model, indexBinding);
			}
		}
	}

	/**
	 * Clears the field group list cleaning up bindings and listeners.
	 */
	public void clear() {
		Log.debug("IndexedFieldPanel.clearing " + toString() + "..");
		binding.unbind();
		binding.getChildren().clear();
		indexPanels.clear();
	}

	public final Collection<M> getValue() {
		return value;
	}

	public final void setValue(Collection<M> value) {
		final Collection<M> old = this.value;
		this.value = value;
		if(changeSupport != null) changeSupport.firePropertyChange("value", old, this.value);

		if(isAttached()) {
			regenerate();
		}
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
		setValue((Collection<M>) value);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if(!drawn) {
			regenerate();
			Log.debug("IndexedFieldPanel.onAttach() drawing..");
			draw();
			drawn = true;
		}
	}

	@Override
	public String toString() {
		return "IndexedFieldPanel [ " + (topGroup.getName() == null ? "<noname>" : topGroup.getName()) + " ]";
	}
}
