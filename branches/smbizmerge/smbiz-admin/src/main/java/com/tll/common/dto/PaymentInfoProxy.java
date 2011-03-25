/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.common.dto;

import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.ProxyFor;
import com.tll.model.PaymentData;
import com.tll.model.PaymentInfo;

/**
 * @author jpk
 */
@ProxyFor(PaymentInfo.class)
public interface PaymentInfoProxy extends EntityProxy {

	Long getId();

	PaymentData getPaymentData();

	void setPaymentData(PaymentData pd);
}
