package org.pwsafe.passwordsafeswt.model;

import junit.framework.TestCase;

import org.pwsafe.passwordsafeswt.model.PasswordTreeContentProvider.TreeGroup;

public class TreeGroupTest extends TestCase {

	TreeGroup treeGroup;

	public void testNullGroupPath () {
		treeGroup = new TreeGroup(null);
		assertEquals("", treeGroup.name);
		assertEquals("", treeGroup.getParent());
		assertEquals("", treeGroup.getGroupPath());
	}

	public void testEmptyGroupPath () {
		treeGroup = new TreeGroup("");
		assertEquals("", treeGroup.name);
		assertEquals("", treeGroup.getParent());
		assertEquals("", treeGroup.getGroupPath());
	}

	public void testTopGroup () {
		treeGroup = new TreeGroup("top");
		assertEquals("top", treeGroup.name);
		assertEquals("", treeGroup.getParent());
		assertEquals("top", treeGroup.getGroupPath());
	}

	public void testSubGroup () {
		treeGroup = new TreeGroup("top.sub");
		assertEquals("sub", treeGroup.name);
		assertEquals("top", treeGroup.getParent());
		assertEquals("top.sub", treeGroup.getGroupPath());
	}

	public void testSubSubGroup () {
		treeGroup = new TreeGroup("top.sub1.sub2");
		assertEquals("sub2", treeGroup.name);
		assertEquals("top.sub1", treeGroup.getParent());
		assertEquals("top.sub1.sub2", treeGroup.getGroupPath());
	}

	public void testGroupWithWhiteSpace () {
		treeGroup = new TreeGroup("top.sub  1.sub 2");
		assertEquals("sub 2", treeGroup.name);
		assertEquals("top.sub  1", treeGroup.getParent());
		assertEquals("top.sub  1.sub 2", treeGroup.getGroupPath());

		treeGroup = new TreeGroup("top.sub\t1.sub 2");
		assertEquals("sub 2", treeGroup.name);
		assertEquals("top.sub	1", treeGroup.getParent());
		assertEquals("top.sub	1.sub 2", treeGroup.getGroupPath());

		treeGroup = new TreeGroup("top.sub\r\n1.sub 2");
		assertEquals("sub 2", treeGroup.name);
		assertEquals("top.sub\r\n1", treeGroup.getParent());
		assertEquals("top.sub\r\n1.sub 2", treeGroup.getGroupPath());
	}
}
