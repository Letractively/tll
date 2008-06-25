package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.NamedEntity;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.BusinessKeyDefinition;
import com.tll.model.key.IBusinessKeyDefinition;

/**
 * PaymentInfo - Wraps {@link PaymentData} (for security). A simple flat-file
 * type holder for several types of payment methods.
 * @see PaymentData For the actual field list.
 * @see PaymentType For the list of app supported payment types.
 * @author jpk
 */
@Entity
@Table(name = "payment_info")
public class PaymentInfo extends NamedEntity {

	private static final long serialVersionUID = -8237732782824087760L;
	public static final int MAXLEN_NAME = 64;

	private static final IBusinessKeyDefinition bk =
			new BusinessKeyDefinition(PaymentInfo.class, "Name", new String[] { INamedEntity.NAME });

	private transient PaymentData paymentData;

	public Class<? extends IEntity> entityClass() {
		return PaymentInfo.class;
	}

	@Column
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	@Column(name = "data")
	@Type(type = "encobj")
	@NotNull
	@Valid
	public PaymentData getPaymentData() {
		return paymentData;
	}

	public void setPaymentData(PaymentData paymentData) {
		this.paymentData = paymentData;
	}

	@Transient
	public void clearPaymentData() {
		this.paymentData = null;
	}

	@Override
	@Transient
	public BusinessKey[] getBusinessKeys() {
		return new BusinessKey[] { new BusinessKey(bk, new Object[] { getName() }) };
	}
}
