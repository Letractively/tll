/**
 * The Logic Lab
 */
package com.tll.model.validate;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.Valid;
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

		@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
		@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
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
		public String typeName() {
			return "Test Entity";
		}
	}
	
	TestEntity getTestEntity() {
		TestEntity e = new TestEntity();
		e.setId(1);
		e.setName("name");
		e.setPhoneNumber("4154467890");
		e.setPostalCode("55667");
		e.setSsn("4445556666");
		return e;
	}

	/**
	 * Tests entity validation.
	 * @throws Exception
	 */
	@Test
	public final void testEntityValidation() throws Exception {
		final TestEntity e = getTestEntity();
		final IEntityValidator<TestEntity> validator = EntityValidatorFactory.instance(TestEntity.class);
		assert validator != null;
		try {
			validator.validate(e);
		}
		catch(final InvalidStateException ise) {
			for(final InvalidValue em : ise.getInvalidValues()) {
				logger.debug("prop: " + em.getPropertyPath());
				logger.debug("msg: " + em.getMessage());
			}
		}
	}

}
