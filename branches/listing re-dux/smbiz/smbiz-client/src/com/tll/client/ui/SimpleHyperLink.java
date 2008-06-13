/**
 * The Logic Lab
 * @author jpk Nov 21, 2007
 */
package com.tll.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

/**
 * SimpleHyperLink - Copied from gwtTk library.
 * @author jpk
 */
public class SimpleHyperLink extends Widget implements HasText, HasHTML, SourcesClickEvents {

	private ClickListenerCollection m_listeners;

	public SimpleHyperLink() {
		this(null, null);
	}

	public SimpleHyperLink(String text) {
		this(text, null);
	}

	/**
	 * Constructs a new SimpleHyperLink
	 * @param text a String or null
	 * @param clickListener a ClickListener or null
	 */
	public SimpleHyperLink(String text, ClickListener clickListener) {
		setElement(DOM.createAnchor());

		// prevents text selection by double-click
		getElement().setPropertyString("href", "#");

		sinkEvents(Event.ONCLICK);

		setStyleName("tk-SimpleHyperLink");

		if(text != null) {
			setText(text);
		}

		if(clickListener != null) {
			addClickListener(clickListener);
		}
	}

	@Override
	public void onBrowserEvent(Event event) {
		if(event.getTypeInt() == Event.ONCLICK) {
			if(m_listeners != null) {
				m_listeners.fireClick(this);
			}
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

	// SourcesClickEvents methods
	public void addClickListener(ClickListener listener) {
		if(m_listeners == null) {
			m_listeners = new ClickListenerCollection();
		}
		m_listeners.add(listener);
	}

	public void removeClickListener(ClickListener listener) {
		if(m_listeners != null) {
			m_listeners.remove(listener);
		}
	}
}
