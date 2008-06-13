/**
 * The Logic Lab
 */
package com.tll.model.schema;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.TestBase;
import com.tll.model.IEntity;
import com.tll.util.CommonUtil;

/**
 * SchemaInfoTest
 * @author jpk
 */
@Test(groups = "model.schema")
public class SchemaInfoTest extends TestBase {

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

		final Class<? extends IEntity>[] entityClasses = CommonUtil.getClasses("com.tll.model", IEntity.class, true, null);
		for(final Class<? extends IEntity> entityClass : entityClasses) {
			final Map<String, ISchemaProperty> fdMap = schemaInfo.getAllSchemaProperties(entityClass);
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

}
