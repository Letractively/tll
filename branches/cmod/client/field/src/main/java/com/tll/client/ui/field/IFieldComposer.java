/**
 * The Logic Lab
 * @author jpk
 * May 24, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;

/**
 * IFieldComposer - Composes fields onto a given "canvas" ( {@link Panel} )
 * dictating their layout.
 * @author jpk
 */
public interface IFieldComposer {

	/**
	 * Sets the {@link Panel} onto which the fields are drawn. When called,
	 * implementations should clear out any previously set draw state clearing any
	 * internally managed {@link Panel}s.
	 * @param canvas The {@link Panel} onto which the fields are drawn
	 */
	void setCanvas(Panel canvas);
}
