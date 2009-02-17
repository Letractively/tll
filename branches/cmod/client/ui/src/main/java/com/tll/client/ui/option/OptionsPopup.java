/**
 * The Logic Lab
 * @author jpk Dec 11, 2007
 */
package com.tll.client.ui.option;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.tll.client.ui.TimedPopup;

/**
 * OptionsPopup - A context menu popup widget that pops up at mouse click
 * locations.
 * @author jpk
 */
public class OptionsPopup extends TimedPopup implements MouseDownHandler, MouseOverHandler, MouseOutHandler,
		IOptionHandler, IHasOptionHandlers {

	protected final OptionsPanel optionsPanel = new OptionsPanel();

	/**
	 * Constructor
	 * @param duration The duration in mili-seconds
	 */
	public OptionsPopup(int duration) {
		super(true, false, duration);
		optionsPanel.addOptionHandler(this);
		addDomHandler(this, MouseDownEvent.getType());
		addDomHandler(this, MouseOverEvent.getType());
		addDomHandler(this, MouseOutEvent.getType());
		setWidget(optionsPanel);
	}

	public void setOptions(Option[] options) {
		optionsPanel.setOptions(options);
	}

	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		if(!event.isCanceled()) {
			switch(event.getTypeInt()) {
				case Event.ONKEYDOWN: {
					//Log.debug("OptionsPopup.onPreviewNativeEvent: " + event.toDebugString());
					switch(event.getNativeEvent().getKeyCode()) {
						case KeyCodes.KEY_UP:
						case KeyCodes.KEY_DOWN:
						case KeyCodes.KEY_ENTER:
							DomEvent.fireNativeEvent(event.getNativeEvent(), optionsPanel);
							//optionsPanel.fireEvent(KeyDownEvent); // TODO why does this not work ?
							event.cancel();
							break;
						case KeyCodes.KEY_ESCAPE:
							hide();
							event.cancel();
							break;
					}
				}
			}
		}
	}
	
	public void onMouseDown(MouseDownEvent event) {
		//Log.debug("OptionsPopup.onMouseDown: " + event.toDebugString());
		//final Element elm = event.getNativeEvent().getTarget();
		// need to UN-offset the positioning of the focus panel (the sender)
		setPopupPosition(event.getClientX() + 13, event.getClientY() - 5);
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		//Log.debug("OptionsPopup.onMouseOut: " + event.toDebugString());
		startTimer();
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		//Log.debug("OptionsPopup.onMouseOver: " + event.toDebugString());
		cancelTimer();
	}

	@Override
	public HandlerRegistration addOptionHandler(IOptionHandler handler) {
		return optionsPanel.addOptionHandler(handler);
	}

	@Override
	public void onOptionEvent(OptionEvent event) {
		//Log.debug("OptionsPopup.onOptionEvent: " + event.toDebugString());
		assert (event.getSource() == optionsPanel);
		switch(event.getOptionEventType()) {
			case CHANGED:
				cancelTimer();
				break;
			case SELECTED:
				hide();
				break;
		}
	}
}
