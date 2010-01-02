/**
 * The Logic Lab
 * @author jpk
 * @since May 18, 2009
 */
package com.tll.common.search;

import java.util.Collection;
import java.util.HashSet;

import com.tll.common.model.IEntityType;
import com.tll.common.model.IEntityTypeProvider;
import com.tll.common.model.IPropertyValue;
import com.tll.util.StringUtil;


/**
 * BusinessKeySearch
 * @author jpk
 */
public class BusinessKeySearch extends SearchBase implements IEntityTypeProvider {

	private IEntityType entityType;

	private String bkName;

	private Collection<IPropertyValue> props;


	/**
	 * Constructor
	 */
	public BusinessKeySearch() {
		super();
	}

	/**
	 * Constructor
	 * @param entityType
	 * @param bkName
	 */
	public BusinessKeySearch(IEntityType entityType, String bkName) {
		super();
		this.entityType = entityType;
		this.bkName = bkName;
	}

	public void addProperty(IPropertyValue pv) {
		if(props == null) props = new HashSet<IPropertyValue>();
		props.add(pv);
	}

	public void setProperties(Collection<IPropertyValue> props) {
		this.props = props;
	}

	public String getBusinessKeyName() {
		return bkName;
	}

	public IPropertyValue[] getProperties() {
		return props.toArray(new IPropertyValue[props.size()]);
	}

	@Override
	public boolean isSet() {
		return entityType != null && !StringUtil.isEmpty(bkName) && props.size() > 0;
	}

	@Override
	public IEntityType getEntityType() {
		return entityType;
	}

}
