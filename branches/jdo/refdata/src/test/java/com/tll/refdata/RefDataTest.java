/**
 * The Logic Lab
 * @author jpk
 * Jan 23, 2009
 */
package com.tll.refdata;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;


/**
 * RefDataTest
 * @author jpk
 */
@Test(groups = "refdata")
public class RefDataTest {
	
	public void testBasicLoad() throws Exception {
		RefData refdata = new RefData();

		Map<RefDataType, Map<String, String>> map = refdata.getAllRefData();
		assert map != null && map.size() == 1 : "Null or Incorrect ref data map size.";
	}

	@SuppressWarnings("unchecked")
	public void testRefresh() throws Exception {
		RefData refdata = new RefData();

		Map<String, String> rmap = refdata.getRefData(RefDataType.US_STATES);
		final int size = rmap == null ? 0 : rmap.size();
		assert size > 10 : "Null or Incorrect ref data map size.";

		// mutate the refdata file
		File f = new File("./target/test-classes/refdata-usps-state-abbrs.txt");
		List<String> lines = FileUtils.readLines(f);
		assert lines != null && lines.size() > 10;
		assert f.delete() == true;
		assert f.createNewFile() == true;
		FileUtils.writeLines(f, lines.subList(0, 10));
		
		rmap = refdata.getRefData(RefDataType.US_STATES);
		assert rmap != null && rmap.size() == 10 : "Incorrect refresh map size.";
		
		// restore test file
		FileUtils.writeLines(f, lines.subList(10, lines.size()));
	}

}
