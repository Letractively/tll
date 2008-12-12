/**
 * The Logic Lab
 * @author jkirton
 * Jul 8, 2008
 */
package com.tll.client.field;

/**
 * IFieldProvider - Definition for the ability to provide a set of
 * {@link IField}s.
 * @author jpk
 */
public interface IFieldProvider {

	/**
	 * @return An array of fields.
	 */
	IField[] getFields();
}
