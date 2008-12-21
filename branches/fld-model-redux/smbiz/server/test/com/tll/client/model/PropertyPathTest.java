/**
 * The Logic Lab
 * @author jpk
 * Jun 4, 2008
 */
package com.tll.client.model;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * PropertyPathTest - Test the public methods for {@link PropertyPath}.
 * @author jpk
 */
@Test(groups = "client-model")
public class PropertyPathTest {

	public void test() throws Exception {
		final PropertyPath pp = new PropertyPath();

		String path;

		// test empty property path
		pp.parse(null);
		assert pp.toString() == null;
		assert pp.nameAt(0) == null;
		assert pp.indexAt(0) == -1;

		// test path at
		path = "pathA.pathB.pathC.pathD[3]";
		pp.parse(path);
		assert "pathA".equals(pp.pathAt(0));
		assert "pathB".equals(pp.pathAt(1));
		assert "pathC".equals(pp.pathAt(2));
		assert "pathD[3]".equals(pp.pathAt(3));

		// test single node non-indexed property path
		path = "path";
		pp.parse(path);
		assert path.equals(pp.toString());
		assert pp.depth() == 1;
		assert pp.indexAt(0) == -1;
		try {
			pp.nameAt(1);
			Assert.fail();
		}
		catch(ArrayIndexOutOfBoundsException e) {
			// expected
		}

		// test single node indexed property path
		path = "path[2]";
		pp.parse(path);
		assert path.equals(pp.toString());
		assert pp.depth() == 1;
		assert "path".equals(pp.nameAt(0));
		assert pp.indexAt(0) == 2;

		// test invalid single node indexed property path w/ non-numeric index
		path = "path[a]";
		pp.parse(path);
		assert path.equals(pp.toString());
		assert pp.depth() == 1;
		assert "path".equals(pp.nameAt(0));
		try {
			pp.indexAt(0);
		}
		catch(MalformedPropPathException e) {
			// expected
		}

		// test property path depth
		path = "pathA.pathB[3].pathC.pathD";
		pp.parse(path);
		assert path.equals(pp.toString());
		assert pp.depth() == 4;
		assert "pathA".equals(pp.nameAt(0));
		assert "pathB".equals(pp.nameAt(1));
		assert "pathC".equals(pp.nameAt(2));
		assert "pathD".equals(pp.nameAt(3));
		assert pp.indexAt(0) == -1;
		assert pp.indexAt(1) == 3;
		assert pp.indexAt(2) == -1;
		assert pp.indexAt(3) == -1;

		// test ancestor method
		assert "pathA.pathB[3].pathC.pathD".equals(pp.ancestor(0).toString());
		assert "pathA.pathB[3].pathC".equals(pp.ancestor(1).toString());
		assert "pathA.pathB[3]".equals(pp.ancestor(2).toString());
		assert "pathA".equals(pp.ancestor(3).toString());

		// test indexedParent method
		assert "pathA.pathB".equals(pp.ancestor(2).indexedParent().toString());

		// test nested method
		assert "pathA.pathB[3].pathC.pathD".equals(pp.nested(0).toString());
		assert "pathB[3].pathC.pathD".equals(pp.nested(1).toString());
		assert "pathC.pathD".equals(pp.nested(2).toString());
		assert "pathD".equals(pp.nested(3).toString());

		// test replace at
		pp.replaceAt(0, "pathAU");
		assert "pathAU.pathB[3].pathC.pathD".equals(pp.toString());
		pp.replaceAt(3, "pathDU");
		assert "pathAU.pathB[3].pathC.pathDU".equals(pp.toString());
		pp.replaceAt(2, null);
		assert "pathAU.pathB[3].pathDU".equals(pp.toString());

		// test replace
		pp.replace("pathAU", "pathA");
		assert "pathA.pathB[3].pathDU".equals(pp.toString());
		pp.replace("pathB[3]", "pathB[3]");
		assert "pathA.pathB[3].pathDU".equals(pp.toString());
		pp.replace("pathDU", "pathD");
		assert "pathA.pathB[3].pathD".equals(pp.toString());

		// test nested method against single prop name
		path = "pathA";
		pp.parse(path);
		assert pp.nested(0) == null;

	}
}
