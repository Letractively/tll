/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.event;

import java.util.ArrayList;


/**
 * ISourcesFieldBindingEvents
 * @author jpk
 */
public interface ISourcesFieldBindingEvents {

	/**
	 * Adds a listener
	 * @param listener
	 */
	void addFieldBindingListener(IFieldBindingListener listener);

	/**
	 * Removes a listener
	 * @param listener
	 */
	void removeFieldBindingListener(IFieldBindingListener listener);

	/**
	 * FieldBindingListenerCollection
	 * @author jpk
	 */
	final class FieldBindingListenerCollection extends ArrayList<IFieldBindingListener> {

		public void fireBeforeBindEvent() {
			for(IFieldBindingListener listener : this) {
				listener.onBeforeBind();
			}
		}

		public void fireAfterBindEvent() {
			for(IFieldBindingListener listener : this) {
				listener.onAfterBind();
			}
		}

		public void fireBeforeUnbindEvent() {
			for(IFieldBindingListener listener : this) {
				listener.onBeforeUnbind();
			}
		}

		public void fireAfterUnbindEvent() {
			for(IFieldBindingListener listener : this) {
				listener.onAfterUnbind();
			}
		}
	}
}
