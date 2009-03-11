/**
 * The Logic Lab
 * @author jpk Jan 3, 2009
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.tll.IProvider;
import com.tll.client.bind.FieldBindingAction;
import com.tll.client.convert.IConverter;
import com.tll.client.ui.BindableWidgetAdapter;
import com.tll.client.validate.ValidationException;
import com.tll.common.bind.IModel;
import com.tll.common.bind.IPropertyChangeListener;
import com.tll.common.model.PropertyPathException;

/**
 * IndexedFieldPanel - Caters to the display of indexed field collections.
 * <p>
 * The {@link IndexedFieldPanel} is savagely different from its brethren
 * {@link FieldPanel} in that the value type is a raw reference to a model
 * collection rather than a {@link FieldGroup}. Because of this, this field
 * panel type manages its own binding life-cycle which is triggered by the
 * {@link #setValue(Collection)} and {@link #getValue()} methods.
 * <p>
 * <b>IMPT:</b> This panel's field group is expected to be a child of the parent
 * field panel's field group.
 * @param <W> the indexed field panel widget render type
 * @param <I> the index field panel type
 * @author jpk
 */
public abstract class IndexedFieldPanel<W extends Widget, I extends FieldPanel<?>> extends FieldPanel<W> implements
		IIndexedFieldBoundWidget {

	/**
	 * Index - Wrapper class for each field panel at an index encapsulating the
	 * field panel and its own field binding action.
	 * @author jpk
	 * @param <I> the index field panel type
	 */
	static final class Index<I extends FieldPanel<?>> implements IProvider<I> {

		final I fieldPanel;
		final FieldBindingAction binding;

		/**
		 * Constructor
		 * @param fieldPanel
		 * @param binding
		 */
		public Index(I fieldPanel, FieldBindingAction binding) {
			super();
			this.fieldPanel = fieldPanel;
			this.binding = binding;
		}

		@Override
		public I get() {
			return fieldPanel;
		}

		private String getIndexTypeName() {
			return fieldPanel.getModel().getEntityType().getPresentationName();
		}
		
		public void setFieldGroupName(int index) {
			fieldPanel.getFieldGroup().setName(getIndexTypeName() + " - " + index);
		}
	}
	
	private final BindableWidgetAdapter<Collection<IModel>> adapter;

	/**
	 * The indexed property name identifying the related many model collection in
	 * the root model.
	 */
	private final String indexedPropertyName;

	/**
	 * The list of indexed {@link FieldPanel}s.
	 */
	private final List<Index<I>> indexPanels = new ArrayList<Index<I>>();

	/**
	 * Internally held ref to the related many model collection to know whether to
	 * re-bind or not. This avoids spurious re-binds when property change events
	 * fire!
	 */
	private transient Collection<IModel> value;

	/**
	 * Constructor
	 * @param fieldGroupName The presentation worthy field group for the managed
	 *        field group containing the indexable sub-field groups
	 * @param indexedPropertyName The name of the indexed property identifying the
	 *        related many model collection in the root model (the model held in
	 *        the parent field panel). This is needed to "subscribe" to the
	 *        parent's binding life-cycle so that this panel receives the raw
	 *        model collection during binding
	 */
	public IndexedFieldPanel(String fieldGroupName, String indexedPropertyName) {
		adapter = new BindableWidgetAdapter<Collection<IModel>>(this);
		setFieldGroup(new FieldGroup(fieldGroupName));
		this.indexedPropertyName = indexedPropertyName;
	}

	@Override
	public final String getIndexedPropertyName() {
		return indexedPropertyName;
	}

	@Override
	public final Collection<IModel> getValue() {
		if(value == null) {
			value = new ArrayList<IModel>(indexPanels.size());
			// update the model collection!
			for(final Index<I> index : indexPanels) {
				try {
					index.binding.execute();
				}
				catch(final ValidationException e) {
					throw new RuntimeException(e);
				}
				value.add(index.fieldPanel.getModel());
			}
		}
		return value;
	}

	@Override
	public final void setValue(Collection<IModel> value) {
		if(this.value != value) {
			this.value = null;
			clear();
			// we don't want auto-transfer of data!!
			//changeSupport.firePropertyChange(IBindableWidget.PROPERTY_VALUE, old, this.value);
			if(value != null) {
				for(final IModel m : value) {
					add(m, false);
				}
			}
		}
	}

	/**
	 * @return A prototype model used to bind to a newly added indexed field
	 *         panel.
	 */
	protected abstract IModel createPrototypeModel();

	/**
	 * Factory method to obtain a new index field panel instance.
	 * @return new field panel instance for employ at a particular index.
	 */
	protected abstract I createIndexPanel();

	/**
	 * Adds a new indexed field group from the UI.
	 */
	protected final void add() {
		add(createPrototypeModel(), true);
	}

	/**
	 * Adds the given index in the ui.
	 * @param index the index to add to the ui
	 * @param isUiAdd Are we adding via the UI or adding an existing index?
	 */
	protected abstract void addUi(I index, boolean isUiAdd);

	/**
	 * Adds an indexed field group populating the fields with data held in the
	 * given model.
	 * @param model The model to be converted to a field group
	 * @throws IllegalArgumentException When the field to add already exists in
	 *         the underlying field group or has the same name as an existing
	 *         field in the underlying group.
	 */
	private void add(IModel model, boolean isAddUi) throws IllegalArgumentException {
		Log.debug("IndexedFieldPanel.add() - START");
	
		final I ip = createIndexPanel();
		ip.setModel(model);
		
		// NOTE: we *don't* specify and error handler for this index field panel's binding actions
		// as this is handled by the parent binding action since 
		// its root field group is expected to contain this panel's field group as a child
		final Index<I> index = new Index<I>(ip, new FieldBindingAction());
		indexPanels.add(index);
	
		index.setFieldGroupName(size());
		getFieldGroup().addField(ip.getFieldGroup());
	
		index.binding.set(index.fieldPanel);
		index.binding.bind(index.fieldPanel);
	
		// propagate the error handler from parent field group to this index field group
		ip.getFieldGroup().setErrorHandler(getFieldGroup().getErrorHandler());
	
		// add in the ui
		addUi(ip, isAddUi);
	}

	/**
	 * Removes an indexed field group at the given index syncing with the
	 * underlying field group.
	 * @param index The index at which the field group is removed
	 * @throws IndexOutOfBoundsException
	 */
	protected final void remove(int index) throws IndexOutOfBoundsException {
		// remove from the ui first
		removeUi(index);
	
		final boolean rebuildNames = index < indexPanels.size() - 1;
		Index<I> remove = indexPanels.remove(index);
		assert remove != null;
	
		remove.binding.unbind(remove.fieldPanel);
		getFieldGroup().removeField(remove.fieldPanel.getFieldGroup());
		remove = null;
	
		// re-set remaining field group names if necessary
		if(rebuildNames) {
			for(int i = 0; i < indexPanels.size(); i++) {
				indexPanels.get(i).setFieldGroupName(i);
			}
		}
	}

	/**
	 * Removes the index panel from the ui at the given numeric index.
	 * @param index the numeric index at which to remove the index panel in the ui
	 */
	protected abstract void removeUi(int index) throws IndexOutOfBoundsException;

	/**
	 * Marks or un-marks a field panel at the given index as deleted.
	 * @param index
	 * @param deleted mark as deleted or un-deleted?
	 * @throws IndexOutOfBoundsException
	 */
	protected void markDeleted(int index, boolean deleted) throws IndexOutOfBoundsException {
		final Index<I> ip = indexPanels.get(index);
		ip.fieldPanel.getModel().setMarkedDeleted(deleted);
		ip.fieldPanel.getFieldGroup().setEnabled(!deleted);
	}

	/**
	 * Clears the field group list cleaning up bindings and listeners.
	 */
	public final void clear() {
		// remove in reverse to avoid spurious index group re-names
		for(int i = size() - 1; i >= 0; i--) {
			remove(i);
		}
		assert getFieldGroup().size() == 0;
		assert indexPanels.size() == 0;
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected IFieldRenderer<W> getRenderer() {
		// default is no renderer
		// TODO revert to renderer impl strategy
		return null;
	}

	/**
	 * @return A newly created iterator over the indexed field panels.
	 */
	protected final Iterator<? extends IProvider<I>> getIndexIterator() {
		return indexPanels.iterator();
	}

	/**
	 * @return The number of indexed elements.
	 */
	protected final int size() {
		return indexPanels.size();
	}

	@Override
	public void setValue(Collection<IModel> value, boolean fireEvents) {
		// default is to not support this method
		throw new UnsupportedOperationException();
	}

	@Override
	public final IConverter<Collection<IModel>, Object> getConverter() {
		return adapter.getConverter();
	}

	@Override
	public final void setConverter(IConverter<Collection<IModel>, Object> converter) {
		adapter.setConverter(converter);
	}

	@Override
	public final Object getProperty(String propPath) throws PropertyPathException {
		return adapter.getProperty(propPath);
	}

	@Override
	public final void setProperty(String propPath, Object value) throws IllegalArgumentException, PropertyPathException {
		adapter.setProperty(propPath, value);
	}

	@Override
	public final void addPropertyChangeListener(IPropertyChangeListener l) {
		adapter.addPropertyChangeListener(l);
	}

	@Override
	public final void addPropertyChangeListener(String propertyName, IPropertyChangeListener l) {
		adapter.addPropertyChangeListener(propertyName, l);
	}

	@Override
	public final void removePropertyChangeListener(IPropertyChangeListener l) {
		adapter.removePropertyChangeListener(l);
	}

	@Override
	public final void removePropertyChangeListener(String propertyName, IPropertyChangeListener l) {
		adapter.removePropertyChangeListener(propertyName, l);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Collection<IModel>> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public String toString() {
		return "IndexedFieldPanel [ " + indexedPropertyName + " ]";
	}
}
