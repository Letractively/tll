/**
 * The Logic Lab
 * @author jpk Jan 3, 2009
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.AbstractBindableWidget;
import com.tll.common.bind.IModel;

/**
 * IndexedFieldPanel - Caters to the display of field collections that are
 * indexable by adapting the {@link FieldPanel} with index related routines. All
 * indexing operations sync with the {@link FieldPanel}'s member ("underlying")
 * field group.
 * @param <W> the indexed field panel widget render type
 * @param <I> the index field panel type
 * @author jpk
 */
public abstract class IndexedFieldPanel<W extends Widget, I extends FieldPanel<?>> extends
		AbstractBindableWidget<Collection<IModel>> implements IFieldGroupProvider {

	/**
	 * The indexed property name used to bind this indexed widget to a parent
	 * binding action.
	 */
	private final String indexedPropertyName;

	/**
	 * The list of {@link IndexFieldPanel}s.
	 */
	protected final List<I> indexPanels = new ArrayList<I>();

	/**
	 * The model data collection.
	 */
	private Collection<IModel> value;
	
	/**
	 * The root field group. Child groups correspond to the index panels.
	 */
	private final FieldGroup group;
	
	private boolean drawn;

	/**
	 * Constructor
	 * @param name The name to ascribe to this field panel serving as this field
	 *        panel's field group name
	 * @param indexedPropertyName The name of the indexed property relative to the
	 *        parent field panel
	 */
	public IndexedFieldPanel(String name, String indexedPropertyName) {
		super();
		group = new FieldGroup(name);
		this.indexedPropertyName = indexedPropertyName;
	}

	/**
	 * Provides the ref to the parent field panel which is necessary for proper
	 * model/field binding.
	 * @return Ref to the parent field panel.
	 */
	public abstract FieldPanel<?> getParentFieldPanel();

	/**
	 * @return The property path that identifies this indexed property relative to
	 *         the parent field panel
	 */
	public final String getIndexedPropertyName() {
		return indexedPropertyName;
	}

	/**
	 * @return The number of {@link IndexFieldPanel}s.
	 */
	protected final int size() {
		return indexPanels.size();
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
	private I add(IModel model) throws IllegalArgumentException {
		Log.debug("IndexedFieldPanel.add() - START");
		final I ip = createIndexPanel();
		indexPanels.add(ip);

		ip.setModel(model);
		ip.getFieldGroup().setName(ip.getFieldGroup().descriptor() + " - " + size());
		group.addField(ip.getFieldGroup());
		ip.setErrorHandler(getParentFieldPanel().getErrorHandler());
		getParentFieldPanel().getAction().bind(ip);

		return ip;
	}

	/**
	 * Removes an indexed field group at the given index syncing with the
	 * underlying field group.
	 * @param index The index at which the field group is removed
	 * @return The removed field group
	 * @throws IndexOutOfBoundsException
	 */
	protected I remove(int index) throws IndexOutOfBoundsException {
		final I removed = indexPanels.remove(index);
		group.removeField(removed.getFieldGroup());
		getParentFieldPanel().getAction().unbind(removed);
		return removed;
	}

	/**
	 * Marks or un-marks a field panel at the given index as deleted.
	 * @param index
	 * @param deleted mark as deleted or un-deleted?
	 * @throws IndexOutOfBoundsException
	 */
	protected void markDeleted(int index, boolean deleted) throws IndexOutOfBoundsException {
		final I ip = indexPanels.get(index);
		ip.getModel().setMarkedDeleted(deleted);
		ip.getFieldGroup().setEnabled(!deleted);
	}

	/**
	 * Clears the field group list cleaning up bindings and listeners.
	 */
	protected void clear() {
		group.clear();
		indexPanels.clear();
	}

	@Override
	public Collection<IModel> getValue() {
		return value;
	}

	@Override
	public void setValue(Collection<IModel> value) {
		//final Collection<IBindable> old = this.value;
		if(this.value != null) clear();
		this.value = value;
		// we don't want auto-transfer of data!!
		//changeSupport.firePropertyChange(IBindableWidget.PROPERTY_VALUE, old, this.value);
		for(final IModel m : this.value) {
			add(m);
		}
	}

	@Override
	public FieldGroup getFieldGroup() {
		return group;
	}

	@Override
	protected void onAttach() {
		final FieldPanel<?> parent = getParentFieldPanel();
		if(parent == null) throw new IllegalStateException();
		parent.getAction().bind(this);
		super.onAttach();
	}
	
	/**
	 * Draws the contents in the ui.
	 */
	protected abstract void draw();

	@Override
	protected void onLoad() {
		super.onLoad();
		if(!drawn) {
			draw();
			drawn = true;
		}
	}

	@Override
	public String toString() {
		return "IndexedFieldPanel [ " + indexedPropertyName + " ]";
	}
}
