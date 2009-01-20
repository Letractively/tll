/**
 * The Logic Lab
 */
package com.tll.model.schema;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.model.IEntity;
import com.tll.model.NamedTimeStampEntity;
import com.tll.model.validate.AtLeastOne;
import com.tll.model.validate.BusinessKeyUniqueness;

/**
 * SchemaInfoTest
 * @author jpk
 */
@Test(groups = "model.schema")
public class SchemaInfoTest {

	enum TestEnum {
		A,
		B,
		C;
	}

	static class TestData {

		private TestEnum ccType;

		private String ccNum;

		private String ccCvv2;

		private int ccExpMonth;

		private int ccExpYear;

		private String ccName;

		private String ccAddress1;

		private String ccAddress2;

		private String ccCity;

		private String ccState;

		private String ccZip;

		private String ccCountry;

		public TestEnum getCcType() {
			return ccType;
		}

		public void setCcType(TestEnum ccType) {
			this.ccType = ccType;
		}

		public String getCcNum() {
			return ccNum;
		}

		public void setCcNum(String ccNum) {
			this.ccNum = ccNum;
		}

		public String getCcCvv2() {
			return ccCvv2;
		}

		public void setCcCvv2(String ccCvv2) {
			this.ccCvv2 = ccCvv2;
		}

		public int getCcExpMonth() {
			return ccExpMonth;
		}

		public void setCcExpMonth(int ccExpMonth) {
			this.ccExpMonth = ccExpMonth;
		}

		public int getCcExpYear() {
			return ccExpYear;
		}

		public void setCcExpYear(int ccExpYear) {
			this.ccExpYear = ccExpYear;
		}

		public String getCcName() {
			return ccName;
		}

		public void setCcName(String ccName) {
			this.ccName = ccName;
		}

		public String getCcAddress1() {
			return ccAddress1;
		}

		public void setCcAddress1(String ccAddress1) {
			this.ccAddress1 = ccAddress1;
		}

		public String getCcAddress2() {
			return ccAddress2;
		}

		public void setCcAddress2(String ccAddress2) {
			this.ccAddress2 = ccAddress2;
		}

		public String getCcCity() {
			return ccCity;
		}

		public void setCcCity(String ccCity) {
			this.ccCity = ccCity;
		}

		public String getCcState() {
			return ccState;
		}

		public void setCcState(String ccState) {
			this.ccState = ccState;
		}

		public String getCcZip() {
			return ccZip;
		}

		public void setCcZip(String ccZip) {
			this.ccZip = ccZip;
		}

		public String getCcCountry() {
			return ccCountry;
		}

		public void setCcCountry(String ccCountry) {
			this.ccCountry = ccCountry;
		}

	}

	/**
	 * TestEntity
	 * @author jpk
	 */
	static class TestEntity extends NamedTimeStampEntity {

		private static final long serialVersionUID = -8237732782824087760L;
		public static final int MAXLEN_NAME = 64;

		private transient TestData testData;

		private Set<TestEntity> related = new LinkedHashSet<TestEntity>();

		public Class<? extends IEntity> entityClass() {
			return TestEntity.class;
		}

		@Column
		@NotEmpty
		@Length(max = MAXLEN_NAME)
		public String getName() {
			return name;
		}

		@Column(name = "data")
		@NotNull
		@Nested
		public TestData getTestData() {
			return testData;
		}

		public void setTestData(TestData testData) {
			this.testData = testData;
		}

		@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
		@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
		@AtLeastOne(type = "related")
		@BusinessKeyUniqueness(type = "related")
		@Valid
		public Set<TestEntity> getRelated() {
			return related;
		}

		public void setRelated(Set<TestEntity> related) {
			this.related = related;
		}

	}

	/**
	 * Constructor
	 */
	public SchemaInfoTest() {
		super();
	}

	@Test
	public void test() throws Exception {
		final ISchemaInfo schemaInfo = new SchemaInfo();
		Assert.assertNotNull(schemaInfo);

		final Map<String, ISchemaProperty> fdMap = schemaInfo.getAllSchemaProperties(TestEntity.class);
		Assert.assertNotNull(fdMap);
		for(final String propName : fdMap.keySet()) {
			final ISchemaProperty sp = fdMap.get(propName);
			assert sp != null : "Got null schema property";
			assert sp.getPropertyType() != null;
			if(!sp.getPropertyType().isRelational()) {
				assert sp instanceof PropertyMetadata : "Wrong ISchemaProperty impl instance.  Expected FieldData type";
			}
			else {
				assert sp instanceof RelationInfo : "Wrong ISchemaProperty impl instance.  Expected RelationInfo type";
			}
		}
	}

}
