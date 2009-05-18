package com.tll.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * payment transaction entity
 * @author jpk
 */
@Entity
@Table(name = "payment_trans")
@BusinessObject(businessKeys = {
	@BusinessKeyDef(name = "Pay Trans Date, Payment Op and Pay Type",
			properties = { "payTransDate", "payOp", "payType" }),
			@BusinessKeyDef(name = "Refnum", properties = { "refNum" })
})
public class PaymentTrans extends TimeStampEntity {

	private static final long serialVersionUID = -7701606626029329438L;
	public static final int MAXLEN_AUTH_NUM = 32;
	public static final int MAXLEN_REF_NUM = 32;
	public static final int MAXLEN_RESPONSE = 32;
	public static final int MAXLEN_RESPONSE_MSG = 128;

	private Date payTransDate;

	private PaymentOp payOp;

	private PaymentType payType;

	private float amount = 0f;

	private PaymentProcessor paymentProcessor;

	private String authNum;

	private String refNum;

	private String response;

	private String responseMsg;

	private String notes;

	public Class<? extends IEntity> entityClass() {
		return PaymentTrans.class;
	}

	/**
	 * @return Returns the amount.
	 */
	@Column(precision = 7, scale = 2)
	// @Size(min = 0, max = 999999)
	public float getAmount() {
		return amount;
	}

	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * @return Returns the authNum.
	 */
	@Column(name = "auth_num")
	@Length(max = MAXLEN_AUTH_NUM)
	public String getAuthNum() {
		return authNum;
	}

	/**
	 * @param authNum The authNum to set.
	 */
	public void setAuthNum(String authNum) {
		this.authNum = authNum;
	}

	/**
	 * @return Returns the notes.
	 */
	@Column
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes The notes to set.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return Returns the paymentProcessor.
	 */
	@Column(name = "payment_processor")
	public PaymentProcessor getPaymentProcessor() {
		return paymentProcessor;
	}

	/**
	 * @param paymentProcessor The paymentProcessor to set.
	 */
	public void setPaymentProcessor(PaymentProcessor paymentProcessor) {
		this.paymentProcessor = paymentProcessor;
	}

	/**
	 * @return Returns the payOp.
	 */
	@Column(name = "pay_op")
	@NotNull
	public PaymentOp getPayOp() {
		return payOp;
	}

	/**
	 * @param payOp The payOp to set.
	 */
	public void setPayOp(PaymentOp payOp) {
		this.payOp = payOp;
	}

	/**
	 * @return Returns the payTransDate.
	 */
	@Column(name = "pay_trans_date")
	@Temporal(value = TemporalType.TIMESTAMP)
	@NotNull
	public Date getPayTransDate() {
		return payTransDate;
	}

	/**
	 * @param payTransDate The payTransDate to set.
	 */
	public void setPayTransDate(Date payTransDate) {
		this.payTransDate = payTransDate;
	}

	/**
	 * @return Returns the payType.
	 */
	@Column(name = "pay_type")
	@NotNull
	public PaymentType getPayType() {
		return payType;
	}

	/**
	 * @param payType The payType to set.
	 */
	public void setPayType(PaymentType payType) {
		this.payType = payType;
	}

	/**
	 * @return Returns the refNum.
	 */
	@Column(name = "ref_num", unique = true)
	@NotEmpty
	@Length(max = MAXLEN_REF_NUM)
	public String getRefNum() {
		return refNum;
	}

	/**
	 * @param refNum The refNum to set.
	 */
	public void setRefNum(String refNum) {
		this.refNum = refNum;
	}

	/**
	 * @return Returns the response.
	 */
	@Column
	@NotEmpty
	@Length(max = MAXLEN_RESPONSE)
	public String getResponse() {
		return response;
	}

	/**
	 * @param response The response to set.
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @return Returns the responseMsg.
	 */
	@Column(name = "response_msg")
	@NotEmpty
	@Length(max = MAXLEN_RESPONSE_MSG)
	public String getResponseMsg() {
		return responseMsg;
	}

	/**
	 * @param responseMsg The responseMsg to set.
	 */
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
}
