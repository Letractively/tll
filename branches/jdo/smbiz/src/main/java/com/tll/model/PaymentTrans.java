package com.tll.model;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

/**
 * payment transaction entity
 * @author jpk
 */
@PersistenceCapable
@Uniques(value = {
	@Unique(name = "Pay Trans Date, Payment Op and Pay Type",
			members = { "payTransDate", "payOp", "payType" }),
	@Unique(name = "Refnum", members = { "refNum" })
})
public class PaymentTrans extends TimeStampEntity {

	private static final long serialVersionUID = -7701606626029329438L;
	public static final int MAXLEN_AUTH_NUM = 32;
	public static final int MAXLEN_REF_NUM = 32;
	public static final int MAXLEN_RESPONSE = 32;
	public static final int MAXLEN_RESPONSE_MSG = 128;

	@Persistent
	private Date payTransDate;

	@Persistent
	private PaymentOp payOp;

	@Persistent
	private PaymentType payType;

	@Persistent
	private float amount = 0f;

	@Persistent
	private PaymentProcessor paymentProcessor;

	@Persistent
	private String authNum;

	@Persistent
	private String refNum;

	@Persistent
	private String response;

	@Persistent
	private String responseMsg;

	@Persistent
	private String notes;

	public Class<? extends IEntity> entityClass() {
		return PaymentTrans.class;
	}

	/**
	 * @return Returns the amount.
	 */
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
