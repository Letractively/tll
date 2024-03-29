/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.tll.client.ui.GridRenderer;
import com.tll.util.ObjectUtil;

/**
 * RadioGroupField
 * @param <V> the data element value type
 * @author jpk
 */
public final class RadioGroupField<V> extends AbstractDataField<V, V> {

	/**
	 * Impl
	 * @author jpk
	 */
	@SuppressWarnings("synthetic-access")
	final class Impl extends FocusPanel implements IEditable<V> {

		@Override
		public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<V> handler) {
			return addHandler(handler, ValueChangeEvent.getType());
		}

		@Override
		public V getValue() {
			for(final RadioButton rb : radioButtons) {
				if(rb.getValue() == Boolean.TRUE) {
					return getDataValue(rb.getFormValue());
				}
			}
			return null;
		}

		@Override
		public void setValue(final V value, final boolean fireEvents) {
			final V old = fireEvents ? getValue() : null;
			setValue(value);
			if(fireEvents) {
				final V nval = getValue();
				if(!ObjectUtil.equals(old, nval)) {
					ValueChangeEvent.fire(this, nval);
				}
			}
		}

		@Override
		public void setValue(final V value) {
			for(final RadioButton rb : radioButtons) {
				if(getDataValue(rb.getFormValue()).equals(value)) {
					rb.setValue(Boolean.TRUE);
					return;
				}
			}
		}
	}

	/**
	 * GridStyles
	 * @author jpk
	 */
	public static final class GridStyles {

		/**
		 * Style applied to the radio grid renderer.
		 */
		public static final String GRID = "fradioGrid";
	}

	/**
	 * The default radio button renderer.
	 */
	private static final GridRenderer DEFAULT_RENDERER = new GridRenderer(1, GridStyles.GRID);

	private final Impl fp = new Impl();

	/**
	 * List of radio buttons contained in {@link #fp}. There is one for each
	 * option.
	 */
	private final List<RadioButton> radioButtons = new ArrayList<RadioButton>();

	/**
	 * The radio button renderer.
	 */
	private final GridRenderer renderer;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param renderer the render to employ for rendering the radio buttons
	 * @param data
	 */
	RadioGroupField(final String name, final String propName, final String labelText, final String helpText,
			final GridRenderer renderer, final Map<V, String> data) {
		super(name, propName, labelText, helpText);
		this.renderer = renderer == null ? DEFAULT_RENDERER : renderer;
		fp.addValueChangeHandler(this);
		setData(data);
	}

	/**
	 * @return The list of managed radio buttons.
	 */
	public List<RadioButton> getRadioButtons() {
		return radioButtons;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		for(final RadioButton rb : radioButtons) {
			rb.setEnabled(enabled);
		}
		super.setEnabled(enabled);
	}

	private void render() {
		assert renderer != null;
		fp.clear();
		fp.add(renderer.render(radioButtons));
	}

	private RadioButton create(final String name) {
		final RadioButton rb = new RadioButton("rg_" + getDomId(), name);
		rb.setFormValue(name);
		rb.addStyleName(Styles.CBRB);
		rb.addClickHandler(new ClickHandler() {

			@SuppressWarnings("synthetic-access")
			@Override
			public void onClick(final ClickEvent event) {
				assert event.getSource() instanceof RadioButton;
				final RadioButton radio = (RadioButton) event.getSource();
				// fire a value change event..
				ValueChangeEvent.fire(fp, getDataValue(radio.getFormValue()));
			}
		});
		rb.addBlurHandler(this);
		return rb;
	}

	/**
	 * Builds or re-builds the radio buttons firing change events if the current
	 * value becomes orphaned.
	 * @param data
	 */
	@Override
	public void setData(final Map<V, String> data) {
		super.setData(data);
		radioButtons.clear();
		if(data != null) {
			for(final String name : data.values()) {
				radioButtons.add(create(name));
			}
		}
		if(isAttached()) render();
	}

	@Override
	public void addDataItem(final String name, final V value) {
		super.addDataItem(name, value);
		radioButtons.add(create(name));
		if(isAttached()) render();
	}

	@Override
	public void removeDataItem(final V value) {
		super.removeDataItem(value);
		for(final RadioButton rb : radioButtons) {
			if(rb.getValue().equals(value)) {
				radioButtons.remove(rb);
				break;
			}
		}
		if(isAttached()) render();
	}

	@Override
	public IEditable<V> getEditable() {
		return fp;
	}

	@Override
	public String doGetText() {
		return getToken(fp.getValue());
	}

	@Override
	public void setText(final String text) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		render();
	}
}
