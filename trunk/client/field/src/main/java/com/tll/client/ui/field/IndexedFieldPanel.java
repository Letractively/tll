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
import com.tll.client.convert.IConverter;
import com.tll.client.ui.BindableWidgetAdapter;
import com.tll.client.validate.ErrorClassifier;
import com.tll.common.bind.IPropertyChangeListener;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.util.PropertyPath;

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
public abstract class IndexedFieldPanel<W extends Widget, I extends FieldPanel<?>> extends FieldPanel<W> implements IIndexedFieldBoundWidget {

	/**
	 * Index - Wrapper class for each field panel at an index encapsulating the
	 * field panel and its own field binding action.
	 * @author jpk
	 */
	private final class Index implements IProvider<I> {

		int index = -1;
		final I fieldPanel;

		/**
		 * Constructor
		 * @param index the property index
		 * @param fieldPanel
		 */
		public Index(int index, I fieldPanel) {
			super();
			this.fieldPanel = fieldPanel;
			setIndex(index, true);
		}

		@Override
		public I get() {
			return fieldPanel;
		}

		/**
		 * Updates the property index.
		 * @param index
		 */
		void setIndex(int index) {
			setIndex(index, false);
		}

		private void setIndex(int index, boolean add) {
			final String tgt = PropertyPath.index(getIndexedPropertyName(), index);
			if(add) {
				fieldPanel.getFieldGroup().setParentPropertyPath(tgt);
			}
			else {
				final String existing = PropertyPath.index(getIndexedPropertyName(), this.index);
				fieldPanel.getFieldGroup().replaceParentPropertyPath(existing, tgt);
			}
			final String name = fieldPanel.getModel().getEntityType().descriptor();
			fieldPanel.getFieldGroup().setName(name + " - " + (index + 1));
			this.index = index;
		}
	}

	private final BindableWidgetAdapter<Collection<Model>> adapter;

	/**
	 * The indexed property name identifying the related many model collection in
	 * the root model.
	 */
	private final String indexedPropertyName;

	/**
	 * The list of indexed {@link FieldPanel}s.
	 */
	private final List<Index> indexPanels = new ArrayList<Index>();

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
		adapter = new BindableWidgetAdapter<Collection<Model>>(this);
		setFieldGroup(new FieldGroup(fieldGroupName));
		this.indexedPropertyName = indexedPropertyName;
	}

	@Override
	public final String getIndexedPropertyName() {
		return indexedPropertyName;
	}

	@Override
	public final Collection<Model> getValue() {
		final ArrayList<Model> value = new ArrayList<Model>(indexPanels.size());
		// update the model collection!
		for(final Index index : indexPanels) {
			try {
				index.fieldPanel.updateModel();
				value.add(index.fieldPanel.getModel());
			}
			catch(final Exception e) {
				Log.debug("Unable to get index model collection (" + e.getMessage() + ")", e);
				if(e instanceof RuntimeException) {
					throw (RuntimeException) e;
				}
				throw new RuntimeException(e);
			}
		}
		return value;
	}

	@Override
	public void setValue(Collection<Model> value) {
		Log.debug("Setting value: " + this);
		clearIndexed();
		if(value != null) {
			for(final Model m : value) {
				add(m, false);
			}
		}
	}

	@Override
	public void setValue(Collection<Model> value, boolean fireEvents) {
		// default is to not support this method
		throw new UnsupportedOperationException();
	}

	/**
	 * @return A prototype model used to bind to a newly added indexed field
	 *         panel.
	 */
	protected abstract Model createPrototypeModel();

	/**
	 * Factory method to obtain a new index field panel instance.
	 * <p>
	 * NOTE: no wiring of this created field panel should be performed as this is
	 * managed internally.
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
	 * @param model The index model
	 * @param isUiAdd Are we adding via the UI or is this from initial loading?
	 * @throws IllegalArgumentException When the field to add already exists in
	 *         the underlying field group or has the same name as an existing
	 *         field in the underlying group.
	 */
	private void add(Model model, boolean isUiAdd) throws IllegalArgumentException {
		Log.debug("IndexedFieldPanel.add() - START");

		final I ip = createIndexPanel();
		//ip.setErrorHandler(getErrorHandler());
		ip.setModelChangeTracker(getModelChangeTracker());
		ip.setModel(model); // this generates the field group and binds!

		// calculate the property index
		final int pindex = size();

		final Index index = new Index(pindex, ip);
		if(!indexPanels.add(index)) {
			throw new IllegalStateException("Unable to add index: " + ip);
		}

		getFieldGroup().addField(ip.getFieldGroup());

		// propagate the error handler
		ip.getFieldGroup().setErrorHandler(getFieldGroup().getErrorHandler());

		// add in the ui
		addUi(ip, isUiAdd);
	}

	/**
	 * Removes an indexed field group at the given index syncing with the
	 * underlying field group.
	 * @param index The index at which the field group is removed
	 * @param isUiRemove Are we removing via the UI?
	 * @throws IndexOutOfBoundsException
	 */
	protected final void remove(int index, boolean isUiRemove) throws IndexOutOfBoundsException {
		// remove from the ui first
		removeUi(index, isUiRemove);

		final boolean rebuildIndexes = index < indexPanels.size() - 1;
		Index remove = indexPanels.remove(index);
		assert remove != null;

		remove.fieldPanel.unbind();
		if(!getFieldGroup().removeField(remove.fieldPanel.getFieldGroup(), true)) {
			throw new IllegalStateException("Unable to remove index field group: " + remove.fieldPanel.getFieldGroup());
		}
		remove = null;

		// re-build indexes only if necessary
		if(rebuildIndexes) {
			for(int i = index; i < indexPanels.size(); i++) {
				final Index idx = indexPanels.get(i);
				// remove and re-add index field group to maintain hash code integrity
				// since the name field has changed
				getFieldGroup().removeField(idx.fieldPanel.getFieldGroup(), false);
				indexPanels.get(i).setIndex(i);
				getFieldGroup().addField(idx.fieldPanel.getFieldGroup());
			}
		}
	}

	/**
	 * Removes the index panel from the ui at the given numeric index.
	 * @param index the numeric index at which to remove the index panel in the ui
	 * @param isUiRemove Are we removing via the UI?
	 * @throws IndexOutOfBoundsException
	 */
	protected abstract void removeUi(int index, boolean isUiRemove) throws IndexOutOfBoundsException;

	/**
	 * Marks or un-marks a field panel at the given index as deleted.
	 * @param index
	 * @param deleted mark as deleted or un-deleted?
	 * @throws IndexOutOfBoundsException
	 */
	protected void markDeleted(int index, boolean deleted) throws IndexOutOfBoundsException {
		final Index ip = indexPanels.get(index);
		ip.fieldPanel.getModel().setMarkedDeleted(deleted);
		ip.fieldPanel.enable(!deleted);

		// add mark deletion change to model change tracker if present
		if(getModelChangeTracker() != null) {
			final String path = PropertyPath.index(indexedPropertyName, index);
			if(deleted)
				getModelChangeTracker().addChange(path);
			else
				getModelChangeTracker().removeChange(path);
		}
	}

	public final void clearIndexed() {
		// remove in reverse to avoid spurious index group re-names
		for(int i = size() - 1; i >= 0; i--) {
			remove(i, false);
		}
		assert getFieldGroup().size() == 0;
		assert indexPanels.size() == 0;
	}

	@Override
	public final void reset() {
		// handle indexed field panels
		final ArrayList<Index> tormv = new ArrayList<Index>();
		final ArrayList<Index> toundel = new ArrayList<Index>();
		for(final Index i : indexPanels) {
			i.fieldPanel.getFieldGroup().getErrorHandler().clear(ErrorClassifier.CLIENT);
			final Model im = i.fieldPanel.getModel();
			if(im.isNew()) {
				tormv.add(i);
			}
			else if(im.isMarkedDeleted()) {
				toundel.add(i);
			}
		}
		for(final Index i : tormv) {
			remove(i.index, true);
		}
		for(final Index i : toundel) {
			markDeleted(i.index, false);
		}
	}

	@Override
	protected final FieldGroup generateFieldGroup() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected final IFieldRenderer<W> getRenderer() {
		return null; // indexed field panels don't employ IFieldRenderers
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
	public final IConverter<Collection<Model>, Object> getConverter() {
		return adapter.getConverter();
	}

	@Override
	public final void setConverter(IConverter<Collection<Model>, Object> converter) {
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
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Collection<Model>> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public String toString() {
		return "IndexedFieldPanel [ " + indexedPropertyName + " ]";
	}
}
