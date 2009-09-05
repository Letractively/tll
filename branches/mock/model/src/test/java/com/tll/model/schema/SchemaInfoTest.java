/**
 * The Logic Lab
 */
package com.tll.model.schema;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.model.EntityBase;
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

	static class AllTypesData implements Serializable {

		private static final long serialVersionUID = 1L;

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

	static class TestEntityA extends EntityBase {

		private static final long serialVersionUID = 3324870910518294253L;

		private String aProp;

		@Override
		public Class<? extends IEntity> entityClass() {
			return TestEntityA.class;
		}

		public String getAProp() {
			return aProp;
		}

		public void setAProp(String prop) {
			aProp = prop;
		}

	}

	static class TestEntityB extends EntityBase {

		private static final long serialVersionUID = -5868841032847881791L;

		private TestEntityA entityA;

		@Override
		public Class<? extends IEntity> entityClass() {
			return TestEntityB.class;
		}

		public TestEntityA getEntityA() {
			return entityA;
		}

		public void setEntityA(TestEntityA entityA) {
			this.entityA = entityA;
		}
	}

	/**
	 * TestEntity
	 * @author jpk
	 */
	static class TestEntity extends NamedTimeStampEntity {

		private static final long serialVersionUID = -8237732782824087760L;
		public static final int MAXLEN_NAME = 64;

		private TestEnum enm;
		private String string;
		private int integer;
		private double dbl;
		private float flot;
		private char character;
		private long lng;
		private Date date;
		private TestEntityB relatedOne;
		private Set<TestEntity> relatedMany = new LinkedHashSet<TestEntity>();
		@Nested
		private transient AllTypesData nested;
		private Map<String, String> smap;

		public Class<? extends IEntity> entityClass() {
			return TestEntity.class;
		}

		@Override
		public String typeName() {
			return "Test Entity";
		}

		@Override
		@NotEmpty
		@Length(max = MAXLEN_NAME)
		public String getName() {
			return name;
		}

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

		@NotNull
		public AllTypesData getNested() {
			return nested;
		}

		public void setNested(AllTypesData testData) {
			this.nested = testData;
		}

		public TestEntityB getRelatedOne() {
			return relatedOne;
		}

		public void setRelatedOne(TestEntityB relatedOne) {
			this.relatedOne = relatedOne;
		}

		@AtLeastOne(type = "relatedMany")
		@BusinessKeyUniqueness(type = "relatedMany")
		@Valid
		public Set<TestEntity> getRelatedMany() {
			return relatedMany;
		}

		public void setRelatedMany(Set<TestEntity> related) {
			this.relatedMany = related;
		}

		public Map<String, String> getSmap() {
			return smap;
		}

		public void setSmap(Map<String, String> smap) {
			this.smap = smap;
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
		final ISchemaInfo si = new SchemaInfo();
		Assert.assertNotNull(si);
		ISchemaProperty sp;

		sp = si.getSchemaProperty(TestEntity.class, "enm");
		assert sp.getPropertyType() == PropertyType.ENUM;

		sp = si.getSchemaProperty(TestEntity.class, "string");
		assert sp.getPropertyType() == PropertyType.STRING;

		sp = si.getSchemaProperty(TestEntity.class, "integer");
		assert sp.getPropertyType() == PropertyType.INT;

		sp = si.getSchemaProperty(TestEntity.class, "dbl");
		assert sp.getPropertyType() == PropertyType.DOUBLE;

		sp = si.getSchemaProperty(TestEntity.class, "flot");
		assert sp.getPropertyType() == PropertyType.FLOAT;

		sp = si.getSchemaProperty(TestEntity.class, "character");
		assert sp.getPropertyType() == PropertyType.CHAR;

		sp = si.getSchemaProperty(TestEntity.class, "lng");
		assert sp.getPropertyType() == PropertyType.LONG;

		sp = si.getSchemaProperty(TestEntity.class, "date");
		assert sp.getPropertyType() == PropertyType.DATE;

		sp = si.getSchemaProperty(TestEntity.class, "relatedOne");
		assert sp.getPropertyType() == PropertyType.RELATED_ONE;

		sp = si.getSchemaProperty(TestEntity.class, "relatedMany");
		assert sp.getPropertyType() == PropertyType.RELATED_MANY;

		sp = si.getSchemaProperty(TestEntity.class, "nested");
		assert sp.getPropertyType() == PropertyType.NESTED;

		sp = si.getSchemaProperty(TestEntity.class, "relatedOne.entityA.aProp");
		assert sp.getPropertyType() == PropertyType.STRING;

		sp = si.getSchemaProperty(TestEntity.class, "smap");
		assert sp.getPropertyType() == PropertyType.STRING_MAP;
	}
}
