/**
 * The Logic Lab
 * @author jpk
 * Jul 4, 2008
 */
package com.tll.client.ui.listing;

import com.tll.client.field.IField;
import com.tll.client.listing.IListingConfig;

/**
 * FieldListing - Listing Widget dedicated to IField type data.
 * @author jpk
 */
public class FieldListing extends DataListingWidget<IField> {

	/**
	 * Constructor
	 * @param config
	 */
	public FieldListing(IListingConfig<IField> config) {
		super(config);
	}

}
