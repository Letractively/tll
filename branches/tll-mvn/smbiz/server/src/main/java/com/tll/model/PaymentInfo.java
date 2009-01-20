package com.tll.model;

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
import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;
import com.tll.model.schema.Nested;

/**
 * PaymentInfo - Wraps {@link PaymentData} (for security). A simple flat-file
 * type holder for several types of payment methods.
 * @see PaymentData For the actual field list.
 * @see PaymentType For the list of app supported payment types.
 * @author jpk
 */
@Entity
@Table(name = "payment_info")
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Name", properties = { INamedEntity.NAME }))
public class PaymentInfo extends NamedEntity {

	private static final long serialVersionUID = -8237732782824087760L;
	public static final int MAXLEN_NAME = 64;

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
	@Nested
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
}
