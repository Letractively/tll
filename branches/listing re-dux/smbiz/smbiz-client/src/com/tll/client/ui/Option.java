package com.tll.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * Option - A single option in an {@link OptionsPanel}.
 * @author jpk
 */
public final class Option extends Label {

	private static final String STYLE_OPTION = "option";

	private final String text;

	/**
	 * Constructor
	 * @param text The option text
	 * @param img The optional image the is placed before the option text.
	 */
	public Option(String text, Image img) {
		super();
		setStyleName(STYLE_OPTION);
		this.text = text;
		if(img != null) {
			getElement().appendChild((new ImageContainer(img)).getElement());
		}
		Element txt = DOM.createSpan();
		txt.setInnerText(text);
		getElement().appendChild(txt);
	}

	@Override
	public String getText() {
		return text;
	}
}