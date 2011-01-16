package com.tll.client.ui.listing;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.tll.common.dto.test.AddressDto;

/**
 * @author jpk
 */
public class TestListingWidget extends CellTable<AddressDto> {
	
	public static interface TllResources extends com.google.gwt.user.cellview.client.CellTable.Resources {
		
	}
	
	/**
	 * Constructor
	 */
	public TestListingWidget() {

		// name
		addColumn(new TextColumn<AddressDto>() {
			
			@Override
			public String getValue(AddressDto object) {
				return object.getFirstName() + ' ' + object.getLastName();
			}
		});

		// address
		addColumn(new TextColumn<AddressDto>() {
			
			@Override
			public String getValue(AddressDto object) {
				return object.getAddress1();
			}
		});

		// city
		addColumn(new TextColumn<AddressDto>() {
			
			@Override
			public String getValue(AddressDto object) {
				return object.getCity();
			}
		});

		// state
		addColumn(new TextColumn<AddressDto>() {
			
			@Override
			public String getValue(AddressDto object) {
				return object.getState();
			}
		});
		
		
	}
}
