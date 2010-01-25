/**
 * The Logic Lab
 */
package com.tll.model.validate;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.testng.annotations.Test;

import com.tll.model.IEntity;
import com.tll.model.NamedTimeStampEntity;

/**
 * AbstractValidatorTest
 * @author jpk
 */
@Test(groups = "model.validate")
public class ValidatorsTest {

	private static final Log logger = LogFactory.getLog(ValidatorsTest.class);

	/**
	 * Constructor
	 */
	public ValidatorsTest() {
		super();
	}

	/**
	 * TestEntity
	 * @author jpk
	 */
	@PhoneNumbers(value = @PhoneNumber(phonePropertyName = "phoneNumber"))
	@PostalCode()
	public static final class TestEntity extends NamedTimeStampEntity {

		private static final long serialVersionUID = 1L;

		private String name;

		private String phoneNumber;

		private String ssn;

		private String postalCode;

		protected Set<TestEntity> relatedMany = new LinkedHashSet<TestEntity>();

		@Override
		public Class<? extends IEntity> entityClass() {
			return TestEntity.class;
		}

		@NotEmpty
		@Override
		public String getName() {
			return name;
		}

		@NotEmpty
		@Length(max = 32, message = "Invalid phone number")
		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		@SSN
		public String getSsn() {
			return ssn;
		}

		public void setSsn(String ssn) {
			this.ssn = ssn;
		}

		public String getPostalCode() {
			return postalCode;
		}

		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}

		@AtLeastOne(type = "test entity")
		@BusinessKeyUniqueness(type = "test entity")
		@Valid
		public Set<TestEntity> getRelatedMany() {
			return relatedMany;
		}

		public void setRelatedMany(Set<TestEntity> relatedMany) {
			this.relatedMany = relatedMany;
		}

		@Override
		public String typeDesc() {
			return "Test Entity";
		}
	}

	TestEntity getTestEntity() {
		final TestEntity e = new TestEntity();
		e.setId(Long.valueOf(1));
		e.setName("name");
		e.setPhoneNumber("x");
		e.setPostalCode("y");
		e.setSsn("t");
		return e;
	}

	/**
	 * Tests entity validation.
	 * @throws Exception
	 */
	public final void testEntityValidation() throws Exception {
		final TestEntity e = getTestEntity();
		final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		final Validator validator = factory.getValidator();
		assert validator != null;
		final Set<ConstraintViolation<TestEntity>> invalids = validator.validate(e);
		for(final ConstraintViolation<TestEntity> em : invalids) {
			logger.debug("prop: " + em.getPropertyPath() + ", msg: " + em.getMessage());
		}
	}

}
