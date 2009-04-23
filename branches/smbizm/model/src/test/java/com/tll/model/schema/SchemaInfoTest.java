/**
 * The Logic Lab
 */
package com.tll.model.schema;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;
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

	static class AllTypesEntity {

		private TestEnum enm;
		private String string;
		private int integer;
		private double dbl;
		private float flot;
		private char character;
		private long lng;
		private Date date;

		public TestEnum getEnm() {
			return enm;
		}

		public void setEnm(TestEnum enm) {
			this.enm = enm;
		}

		public String getString() {
			return string;
		}

		public void setString(String string) {
			this.string = string;
		}

		public int getInteger() {
			return integer;
		}

		public void setInteger(int integer) {
			this.integer = integer;
		}

		public double getDbl() {
			return dbl;
		}

		public void setDbl(double dbl) {
			this.dbl = dbl;
		}

		public float getFlot() {
			return flot;
		}

		public void setFlot(float flot) {
			this.flot = flot;
		}

		public char getCharacter() {
			return character;
		}

		public void setCharacter(char character) {
			this.character = character;
		}

		public long getLng() {
			return lng;
		}

		public void setLng(long lng) {
			this.lng = lng;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
	}

	/**
	 * TestEntity
	 * @author jpk
	 */
	static class TestEntity extends NamedTimeStampEntity {

		private static final long serialVersionUID = -8237732782824087760L;
		public static final int MAXLEN_NAME = 64;

		private AllTypesEntity relatedOne;

		private Set<TestEntity> relatedMany = new LinkedHashSet<TestEntity>();

		private transient AllTypesEntity nested;

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
		public AllTypesEntity getNested() {
			return nested;
		}

		public void setNested(AllTypesEntity testData) {
			this.nested = testData;
		}

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "related_one")
		public AllTypesEntity getRelatedOne() {
			return relatedOne;
		}

		public void setRelatedOne(AllTypesEntity relatedOne) {
			this.relatedOne = relatedOne;
		}

		@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
		@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
		@AtLeastOne(type = "relatedMany")
		@BusinessKeyUniqueness(type = "relatedMany")
		@Valid
		public Set<TestEntity> getRelatedMany() {
			return relatedMany;
		}

		public void setRelatedMany(Set<TestEntity> related) {
			this.relatedMany = related;
		}

		@Override
		public String typeName() {
			return "Test Entity";
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
