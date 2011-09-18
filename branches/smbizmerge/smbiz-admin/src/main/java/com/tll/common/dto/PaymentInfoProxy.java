/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.common.dto;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.tll.model.PaymentInfo;

/**
 * @author jpk
 */
@ProxyFor(PaymentInfo.class)
public interface PaymentInfoProxy extends EntityProxy {

	Long getId();

	PaymentDataProxy getPaymentData();

	void setPaymentData(PaymentDataProxy pd);
}
