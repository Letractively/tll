/**
 * The Logic Lab
 * @author jpk
 * Jul 4, 2008
 */
package com.tll.client.field;

/**
 * IFieldBindingListener - Definition to embellish field binding before and
 * after the core binding operation.
 * @author jpk
 */
// TODO move to event package!
public interface IFieldBindingListener {

	/**
	 * Called just before field binding.
	 */
	void onBeforeBind();

	/**
	 * Called just after a field binding.
	 */
	void onAfterBind();

	/**
	 * Called just before field un-binding.
	 */
	void onBeforeUnbind();

	/**
	 * Called just after field un-binding.
	 */
	void onAfterUnbind();
}
