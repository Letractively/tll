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
import com.tll.client.convert.IConverter;
import com.tll.client.convert.ToStringConverter;
import com.tll.client.ui.IWidgetRenderer;
import com.tll.client.ui.VerticalRenderer;
import com.tll.common.util.ObjectUtil;

/**
 * RadioGroupField
 * @author jpk
 */
public final class RadioGroupField extends AbstractDataField<String> {
	
	/**
	 * Impl
	 * @author jpk
	 */
	@SuppressWarnings("synthetic-access")
	final class Impl extends FocusPanel implements IEditable<String> {
		
		@Override
		public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
			return addHandler(handler, ValueChangeEvent.getType());
		}

		@Override
		public String getValue() {
			int i = 0;
			for(final RadioButton rb : radioButtons) {
				if(rb.getValue() == Boolean.TRUE) {
					return rb.getFormValue();
				}
				i++;
			}
			return null;
		}

		@Override
		public void setValue(String value, boolean fireEvents) {
			final String old = getValue();
			setValue(value);
			if(fireEvents && !ObjectUtil.equals(old, value)) {
				ValueChangeEvent.fire(this, value);
			}
		}

		@Override
		public void setValue(String value) {
			int i = 0;
			for(final RadioButton rb : radioButtons) {
				if(rb.getFormValue().equals(value)) {
					rb.setValue(Boolean.TRUE);
					return;
				}
				i++;
			}
		}
	}
	
	/**
	 * The default radio button renderer.
	 */
	private static final IWidgetRenderer DEFAULT_RENDERER = VerticalRenderer.INSTANCE;

	private final Impl fp = new Impl();

	/**
	 * List of radio buttons contained in {@link #rbPanel}. There is one for each
	 * option.
	 */
	private final List<RadioButton> radioButtons = new ArrayList<RadioButton>();
	
	/**
	 * The radio button renderer.
	 */
	private final IWidgetRenderer renderer;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param renderer the render to employ for rendering the radio buttons
	 * @param data
	 */
	RadioGroupField(String name, String propName, String labelText, String helpText, IWidgetRenderer renderer,
			Map<String, String> data) {
		super(name, propName, labelText, helpText);
		this.renderer = renderer == null ? DEFAULT_RENDERER : renderer;
		fp.addValueChangeHandler(this);
		setData(data);
	}
	
	@Override
	protected IConverter<String, Object> getConverter() {
		return ToStringConverter.INSTANCE;
	}

	private void render() {
		assert renderer != null;
		fp.clear();
		fp.add(renderer.render(radioButtons));
	}
	
	private RadioButton create(String name, String value) {
		final RadioButton rb = new RadioButton("rg_" + getDomId(), name);
		rb.setFormValue(value);
		rb.setStyleName(Styles.LABEL);
		rb.addClickHandler(new ClickHandler() {

			@SuppressWarnings("synthetic-access")
			@Override
			public void onClick(ClickEvent event) {
				assert event.getSource() instanceof RadioButton;
				final RadioButton rb = (RadioButton) event.getSource();
				final String val = rb.getFormValue();
				// fire a value change event..
				ValueChangeEvent.fire(fp, val);
			}
		});
		return rb;
	}

	/**
	 * Builds or re-builds the radio buttons firing change events if the current
	 * value becomes orphaned.
	 * @param data
	 */
	@Override
	public void setData(Map<String, String> data) {
		radioButtons.clear();
		if(data != null) {
			for(final String key : data.keySet()) {
				radioButtons.add(create(data.get(key), key));
			}
		}
		if(isAttached()) render();
	}
	
	@Override
	public void addDataItem(String name, String value) {
		radioButtons.add(create(name, value));
		if(isAttached()) render();
	}

	@Override
	public void removeDataItem(String value) {
		for(final RadioButton rb : radioButtons) {
			if(rb.getValue().equals(value)) {
				radioButtons.remove(rb);
				break;
			}
		}
		if(isAttached()) render();
	}

	@Override
	public IEditable<String> getEditable() {
		return fp;
	}

	public String getText() {
		return fp.getValue();
	}

	public void setText(String text) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		render();
	}
}
