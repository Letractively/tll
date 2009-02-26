/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

/**
 * SuggestField
 * @author jpk
 */
public final class SuggestField extends AbstractDataField<String> {

	/**
	 * Impl
	 * @author jpk
	 */
	static final class Impl extends SuggestBox implements IEditable<String> {

		/**
		 * Constructor
		 */
		public Impl() {
			super(new MultiWordSuggestOracle());
		}

		MultiWordSuggestOracle getOracle() {
			return (MultiWordSuggestOracle) getSuggestOracle();
		}
	}

	private final Impl sb;
	
	private Map<String, String> data;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param data
	 */
	SuggestField(String name, String propName, String labelText, String helpText, Map<String, String> data) {
		super(name, propName, labelText, helpText);
		sb = new Impl();
		addValueChangeHandler(this);
		setData(data);
	}
	
	private void buildOracle() {
		final MultiWordSuggestOracle oracle = sb.getOracle();
		oracle.clear();
		if(data != null) {
			for(final String s : data.keySet()) {
				oracle.add(s);
			}
		}
	}

	@Override
	public void setData(Map<String, String> data) {
		this.data = data;
		buildOracle();
	}

	@Override
	public void addDataItem(String name, String value) {
		if(data.put(value, name) == null) {
			sb.getOracle().add(name);
		}
	}

	@Override
	public void removeDataItem(String value) {
		if(data.remove(value) != null) {
			buildOracle();
		}
	}

	public String getText() {
		return sb.getText();
	}

	public void setText(String text) {
		sb.setText(text);
	}

	@Override
	protected IEditable<String> getEditable() {
		return sb;
	}
}
