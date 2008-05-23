package com.tll.client.ui.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.field.impl.FieldLabelImpl;
import com.tll.client.util.StringUtil;

/**
 * FieldLabel - the field label of an {@link AbstractField} impl.
 * @author jpk
 */
public final class FieldLabel extends Widget implements HasText, SourcesClickEvents {

	public static final String CSS_FIELD_LABEL = "lbl";

	public static final String CSS_FIELD_REQUIRED_TOKEN = "rqd";

	private static final String requiredToken = "<sup class=\"" + CSS_FIELD_REQUIRED_TOKEN + "\">*</sup>";

	private static final FieldLabelImpl impl = (FieldLabelImpl) GWT.create(FieldLabelImpl.class);

	private boolean required;
	private String text;
	private ClickListenerCollection clickListeners;

	/**
	 * Constructor
	 * @param text The label text
	 * @param fldId The DOM element id of the associated form field. May be
	 *        <code>null</code>.
	 * @param required Is the associated field required?
	 */
	public FieldLabel(String text, String fldId, boolean required) {
		setElement(DOM.createLabel());
		this.required = required;
		setStyleName(CSS_FIELD_LABEL);
		setText(text);
		if(fldId != null) {
			setFor(fldId);
		}
	}

	/**
	 * Constructor
	 * @param text
	 */
	public FieldLabel(String text) {
		this(text, null, false);
	}

	public void addClickListener(ClickListener listener) {
		if(clickListeners == null) {
			clickListeners = new ClickListenerCollection();
		}
		clickListeners.add(listener);
	}

	public void removeClickListener(ClickListener listener) {
		if(clickListeners != null) {
			clickListeners.remove(listener);
		}
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch(event.getTypeInt()) {
			case Event.ONCLICK:
				if(clickListeners != null) clickListeners.fireClick(this);
				break;
		}
	}

	/**
	 * Sets the for attrubute.
	 * @param fldId The DOM element id of the associated form field.
	 */
	public void setFor(String fldId) {
		sinkEvents(Event.ONCLICK);
		impl.setFor(getElement(), fldId);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if(StringUtil.isEmpty(text)) text = "";
		getElement().setInnerHTML(required ? text + requiredToken : text);
		this.text = text;
	}

	public void setRequired(boolean required) {
		if(this.required == required) return;
		getElement().setInnerHTML(required ? text + requiredToken : text);
		this.required = required;
	}
}