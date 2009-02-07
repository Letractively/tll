/**
 * The Logic Lab
 * @author jpk Dec 23, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * NoEntityExistsPanel - A placeholder panel to indicate there is currently no
 * valid mapping to the backing entity. Clients shall listen to click events to
 * be notified the user wishes to create the entity. The click event sender is
 * an instance of this class.
 * @author jpk
 */
public class NoEntityExistsPanel extends VerticalPanel implements SourcesClickEvents {

	private final Object refToken;
	private final Label lblDesc;
	private final Button btnCreate;
	private ClickListenerCollection clickListeners;

	/**
	 * Constructor
	 * @param refToken For clients in helping resolve this placeholder.
	 * @param description
	 * @param createBtnText
	 */
	public NoEntityExistsPanel(Object refToken, String description, String createBtnText) {
		super();
		this.refToken = refToken;
		lblDesc = new Label(description);
		lblDesc.getElement().getStyle().setProperty("fontStyle", "italic");
		btnCreate = new Button(createBtnText);
		add(lblDesc);
		add(btnCreate);
		setWidth("100%");
		setHeight("100%");
		setCellHorizontalAlignment(lblDesc, ALIGN_CENTER);
		setCellHorizontalAlignment(btnCreate, ALIGN_CENTER);
		setCellVerticalAlignment(lblDesc, ALIGN_BOTTOM);
		setCellVerticalAlignment(btnCreate, ALIGN_TOP);
		sinkEvents(Event.ONCLICK);
	}

	public Object getRefToken() {
		return refToken;
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
				Element elm = event.getTarget();
				if(elm == btnCreate.getElement()) {
					// if(DOM.compare(elm, btnCreate.getElement())) {
					// user wants to create the entity
					clickListeners.fireClick(this);
				}
				break;
		}
	}
}
