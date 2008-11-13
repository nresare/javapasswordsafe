/*
 * $Id$
 * 
 * Copyright (c) 2008 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.test;

import org.pwsafe.lib.file.PwsFile;
import org.pwsafe.lib.file.PwsFileV1;
import org.pwsafe.lib.file.PwsRecord;
import org.pwsafe.lib.file.PwsRecordV1;
import org.pwsafe.lib.file.PwsStringField;

import junit.framework.TestCase;

/**
 *
 */
public class FileTest extends TestCase
{
	/**
	 * Tests that the library can create a version 1 database.
	 */
	public void testCreateV1File()
	{
		try
		{
			PwsFile		v1File;
			PwsRecord	rec;
	
			v1File	= new PwsFileV1();
	
			v1File.setFilename( "V1 New File.dat" );
			v1File.setPassphrase( "passphrase" );
	
			rec = v1File.newRecord();
			
			rec.setField( new PwsStringField(PwsRecordV1.TITLE, "Entry number 1") );
			rec.setField( new PwsStringField(PwsRecordV1.PASSWORD, "Password 1") );

			v1File.add( rec );
			
			rec = v1File.newRecord();
			
			rec.setField( new PwsStringField(PwsRecordV1.TITLE, "Entry number 2") );
			rec.setField( new PwsStringField(PwsRecordV1.PASSWORD, "Password 2") );
			rec.setField( new PwsStringField(PwsRecordV1.USERNAME, "Username 2") );
			rec.setField( new PwsStringField(PwsRecordV1.NOTES, "Notes line 1\r\nNotes line 2") );
			
			v1File.add( rec );

			assertTrue( "Modified flag is not TRUE", v1File.isModified() );
			assertEquals( "Record count is not = 2", 2, v1File.getRecordCount() );

			v1File.save();

			assertTrue( "Modified flag is not FALSE", !v1File.isModified() );
		}
		catch ( Exception e )
		{
			fail( "Unexpected exception caught - " + e.getMessage() );
		}
	}
}
