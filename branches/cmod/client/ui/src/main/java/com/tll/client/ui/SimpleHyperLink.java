/**
 * The Logic Lab
 * @author jpk Nov 21, 2007
 */
package com.tll.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * SimpleHyperLink - Copied from gwtTk library.
 * @author jpk
 */
public class SimpleHyperLink extends Widget implements HasText, HasHTML, HasClickHandlers {

	//private ClickListenerCollection m_listeners;

	public SimpleHyperLink() {
		this(null, null);
	}

	public SimpleHyperLink(String text) {
		this(text, null);
	}

	/**
	 * Constructs a new SimpleHyperLink
	 * @param text a String or <code>null</code>
	 * @param clickHandler May be <code>null</code>
	 */
	public SimpleHyperLink(String text, ClickHandler clickHandler) {
		setElement(DOM.createAnchor());

		// prevents text selection by double-click
		getElement().setPropertyString("href", "#");

		sinkEvents(Event.ONCLICK);

		setStyleName("tk-SimpleHyperLink");

		if(text != null) {
			setText(text);
		}

		if(clickHandler != null) {
			addHandler(clickHandler, ClickEvent.getType());
		}
	}

	@Override
	public void onBrowserEvent(Event event) {
		if(event.getTypeInt() == Event.ONCLICK) {
			//fireEvent(event);
			// keep '#' out of the location bar
			DOM.eventPreventDefault(event);
		}
	}

	// HasText methods
	public void setText(String text) {
		getElement().setInnerText(text);
	}

	public String getText() {
		return getElement().getInnerText();
	}

	// HasHTML mehtods
	public String getHTML() {
		return getElement().getInnerHTML();
	}

	public void setHTML(String html) {
		getElement().setInnerHTML(html);
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return null;
	}
}
