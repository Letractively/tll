/**
 * The Logic Lab
 * @author jpk
 * Jan 23, 2009
 */
package com.tll.model;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.model.key.BusinessKey;
import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * BusinessKeyFactoryTest
 * @author jpk
 */
@Test(groups = "model")
public class BusinessKeyFactoryTest {

	@BusinessObject(businessKeys = {
		@BusinessKeyDef(name = TestEntity.BK_NAME, properties = { "name" }),
		@BusinessKeyDef(name = TestEntity.BK_CODE, properties = { "code" }),
		@BusinessKeyDef(name = TestEntity.BK_AR, properties = {
			"authNum", "refNum" })
	})
	static class TestEntity extends EntityBase {
		private static final long serialVersionUID = 1L;
		
		public static final String BK_NAME = "Name";
		public static final String BK_CODE = "Code";
		public static final String BK_AR = "Auth Num & Ref Num";
		
		private String name;
		private int code;
		private String authNum;
		private String refNum;

		@Override
		public Class<? extends IEntity> entityClass() {
			return TestEntity.class;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getAuthNum() {
			return authNum;
		}

		public void setAuthNum(String authNum) {
			this.authNum = authNum;
		}

		public String getRefNum() {
			return refNum;
		}

		public void setRefNum(String refNum) {
			this.refNum = refNum;
		}

	}
	
	private TestEntity stubTestEntity() {
		TestEntity e = new TestEntity();
		e.setId(1);
		e.setName("name");
		e.setCode(1);
		e.setAuthNum("authNum");
		e.setRefNum("refNum");
		return e;
	}

	public void testBusinessKeyFactoryCreateFromClass() throws Exception {
		BusinessKey<TestEntity>[] bks = BusinessKeyFactory.create(TestEntity.class);
		assert bks != null && bks.length == 3 : "Incorrect number of created business keys.";
	}

	public void testBusinessKeyFactoryCreateFromInstance() throws Exception {
		TestEntity e = stubTestEntity();
		BusinessKey<TestEntity>[] bks = BusinessKeyFactory.create(e);
		assert bks != null && bks.length == 3 : "Incorrect number of created business keys.";
		for(BusinessKey<TestEntity> bk : bks) {
			if(TestEntity.BK_NAME.equals(bk.getBusinessKeyName())) {
				assert bk.getPropertyNames() != null && bk.getPropertyNames().length == 1;
			}
			else if(TestEntity.BK_CODE.equals(bk.getBusinessKeyName())) {
				assert bk.getPropertyNames() != null && bk.getPropertyNames().length == 1;
			}
			else if(TestEntity.BK_AR.equals(bk.getBusinessKeyName())) {
				assert bk.getPropertyNames() != null && bk.getPropertyNames().length == 2;
			}
			else {
				Assert.fail("Unknown business key name");
			}
			for(String pn : bk.getPropertyNames()) {
				assert bk.getPropertyValue(pn) != null;
			}
		}
	}

	public void testBusinessKeyFactoryIsBusinessKeyUnique() throws Exception {
		TestEntity[] arr = new TestEntity[] {
			stubTestEntity(), stubTestEntity()
		};
		List<TestEntity> list = Arrays.asList(arr);
		assert BusinessKeyFactory.isBusinessKeyUnique(list) == false : "Is business key unique check failed.";
	}
}