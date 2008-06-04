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

		// test single node non-indexed property path
		path = "path";
		pp.parse(path);
		assert path.equals(pp.toString());
		assert pp.depth() == 1;
		assert pp.nameAt(0) == path;
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

		// test single node unbound indexed property path
		path = "path{2}";
		pp.parse(path);
		assert path.equals(pp.toString());
		assert pp.depth() == 1;
		assert "path".equals(pp.nameAt(0));
		assert pp.indexAt(0) == 2;
		assert pp.nextUnboundNode(0) == 0;

		// test property path depth
		path = "pathA.pathB[3].pathC.pathD";
		pp.parse(path);
		assert pp.toString() == path;
		assert pp.depth() == 4;
		assert "pathA".equals(pp.nameAt(0));
		assert "pathB".equals(pp.nameAt(1));
		assert "pathC".equals(pp.nameAt(2));
		assert "pathD".equals(pp.nameAt(3));
		assert pp.indexAt(0) == -1;
		assert pp.indexAt(1) == 3;
		assert pp.indexAt(2) == -1;
		assert pp.indexAt(3) == -1;

		path = "pathA.pathB{3}.pathC.pathD";
		pp.parse(path);

		// test unbound node search
		assert pp.nextUnboundNode(0) == 1;
		assert pp.nextUnboundNode(1) == 1;
		assert pp.nextUnboundNode(2) == -1;
		assert pp.nextUnboundNode(3) == -1;

		// test parent method
		assert null == pp.parent(0);
		assert "pathA".equals(pp.parent(1).toString());
		assert "pathA.pathB{3}".equals(pp.parent(2).toString());
		assert "pathA.pathB{3}.pathC".equals(pp.parent(3).toString());

		// test indexedParent method
		assert "pathA.pathB".equals(pp.parent(2).indexedParent().toString());

		// test nested method
		assert "pathB{3}.pathC.pathD".equals(pp.nested(0).toString());
		assert "pathC.pathD".equals(pp.nested(1).toString());
		assert "pathD".equals(pp.nested(2).toString());
		assert pp.nested(3) == null;

		// test nested method against single prop name
		path = "pathA";
		pp.parse(path);
		assert pp.nested(0) == null;
	}
}
