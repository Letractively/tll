package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

/**
 * @author jpk
 */
@PersistenceCapable
//@PhoneNumbers(value = {
//	@PhoneNumber(phonePropertyName = "phone"), @PhoneNumber(phonePropertyName = "fax") })
//@PostalCode() temp disable
@Uniques(value = @Unique(name = "Address 1 and Postal Code", members = {
	"address1", "postalCode" }))
public class Address extends EntityBase {

	private static final long serialVersionUID = 69385466934038047L;

	public static final int MAXLEN_FIRST_NAME = 128;
	public static final int MAXLEN_LAST_NAME = 128;
	public static final int MAXLEN_COMPANY = 128;
	public static final int MAXLEN_ATTN = 64;
	public static final int MAXLEN_ADDRESS1 = 128;
	public static final int MAXLEN_ADDRESS2 = 128;
	public static final int MAXLEN_CITY = 128;
	public static final int MAXLEN_PROVINCE = 128;
	public static final int MAXLEN_COUNTRY = 128;
	public static final int MAXLEN_POSTAL_CODE = 15;
	public static final int MAXLEN_PHONE = 15;
	public static final int MAXLEN_FAX = 15;
	public static final int MAXLEN_EMAIL_ADDRESS = 128;

	@Persistent
	private String firstName;
	@Persistent
	private String lastName;
	@Persistent
	private Character mi;
	@Persistent
	private String company;
	@Persistent
	private String attn;
	@Persistent
	private String address1;
	@Persistent
	private String address2;
	@Persistent
	private String city;
	@Persistent
	private String province;
	@Persistent
	private String postalCode;
	@Persistent
	private String country;
	@Persistent
	private String phone;
	@Persistent
	private String fax;
	@Persistent
	private String emailAddress;

	public Class<? extends IEntity> entityClass() {
		return Address.class;
	}

	/**
	 * @return Returns the address1.
	 */
	@NotEmpty
	@Length(max = MAXLEN_ADDRESS1)
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 The address1 to set.
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return Returns the address2.
	 */
	@Length(max = MAXLEN_ADDRESS2)
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 The address2 to set.
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return Returns the attn.
	 */
	@Length(max = MAXLEN_ATTN)
	public String getAttn() {
		return attn;
	}

	/**
	 * @param attn The attn to set.
	 */
	public void setAttn(String attn) {
		this.attn = attn;
	}

	/**
	 * @return Returns the city.
	 */
	@NotEmpty
	@Length(max = MAXLEN_CITY)
	public String getCity() {
		return city;
	}

	/**
	 * @param city The city to set.
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return Returns the company.
	 */
	@Length(max = MAXLEN_COMPANY)
	public String getCompany() {
		return company;
	}

	/**
	 * @param company The company to set.
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return Returns the country.
	 */
	@NotEmpty
	@Length(max = MAXLEN_COUNTRY)
	public String getCountry() {
		return country;
	}

	/**
	 * @param country The country to set.
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return Returns the emailAddress.
	 */
	@NotEmpty
	// @Email
	@Length(max = MAXLEN_EMAIL_ADDRESS)
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress The emailAddress to set.
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return Returns the fax.
	 */
	@Length(max = MAXLEN_FAX)
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax The fax to set.
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return Returns the firstName.
	 */
	@Length(max = MAXLEN_FIRST_NAME)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Returns the lastName.
	 */
	@Length(max = MAXLEN_LAST_NAME)
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return Returns the mi.
	 */
	public Character getMi() {
		return mi;
	}

	/**
	 * @param mi The mi to set.
	 */
	public void setMi(Character mi) {
		this.mi = mi;
	}

	/**
	 * @return Returns the phone.
	 */
	@Length(max = MAXLEN_PHONE)
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone The phone to set.
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return Returns the province.
	 */
	@NotEmpty
	@Length(max = MAXLEN_PROVINCE)
	public String getProvince() {
		return province;
	}

	/**
	 * @param state The state to set.
	 */
	public void setProvince(String state) {
		this.province = state;
	}

	/**
	 * @return Returns the postalCode.
	 */
	@NotEmpty
	@Length(max = MAXLEN_POSTAL_CODE)
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param zip The zip to set.
	 */
	public void setPostalCode(String zip) {
		this.postalCode = zip;
	}
}
