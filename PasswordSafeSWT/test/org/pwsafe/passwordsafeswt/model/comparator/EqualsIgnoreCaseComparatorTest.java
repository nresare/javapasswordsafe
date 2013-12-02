package org.pwsafe.passwordsafeswt.model.comparator;

import junit.framework.TestCase;

import org.pwsafe.lib.datastore.PwsEntryBean;

/**
 * Created by IntelliJ IDEA.
 * User: tim
 * Date: 23-Feb-2010
 * Time: 16:47:25
 * To change this template use File | Settings | File Templates.
 */
public class EqualsIgnoreCaseComparatorTest extends TestCase {

    private EqualsIgnoreCaseComparator comparator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        comparator = new EqualsIgnoreCaseComparator();
    }

    public void testCompareEqual() {
        PwsEntryBean o1 = new PwsEntryBean("abcd", "username", new StringBuilder("password"), "notes" );
        o1.setTitle( "aaaa" );
        PwsEntryBean o2 = new PwsEntryBean("abcd", "username", new StringBuilder("password"), "notes" );
        o2.setTitle( "aaaa" );

        assertEquals( 0, comparator.compare( o1, o2 ) );
        assertEquals( 0, comparator.compare( o2, o1 ) );
    }


    public void testCompareDifferentGroup() {
        PwsEntryBean o1 = new PwsEntryBean("abcd", "username", new StringBuilder("password"), "notes" );
        o1.setTitle( "aaaa" );
        PwsEntryBean o2 = new PwsEntryBean("abcd.def", "username", new StringBuilder("password"), "notes" );
        o2.setTitle( "aaaa" );
        // todo check this is the right way around
        assertTrue( comparator.compare( o1, o2 ) < 0 );
        assertTrue( comparator.compare( o2, o1 ) > 0 );
    }

    public void testCompareDifferentTitle() {
        PwsEntryBean o1 = new PwsEntryBean("abcd", "username", new StringBuilder("password"), "notes" );
        o1.setTitle( "aaaa" );
        PwsEntryBean o2 = new PwsEntryBean("abcd", "username", new StringBuilder("password"), "notes" );
        o2.setTitle( "bbbb" );
        assertTrue( comparator.compare( o1, o2 ) < 0 );
        assertTrue( comparator.compare( o2, o1 ) > 0 );
    }



}
