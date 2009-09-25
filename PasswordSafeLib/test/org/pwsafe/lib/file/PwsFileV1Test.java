/*
 * $Id$
 * 
 * Copyright (c) 2008 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.file;

import junit.framework.TestCase;

import org.pwsafe.util.FileConverter;

/**
 *
 */
public class PwsFileV1Test extends TestCase
{
	private static final String V1_FILE_NAME = "V1 New File.dat";
	private static final String PASSPHRASE = "passphrase";
	
	/**
	 * Tests that the library can create a version 1 database.
	 */
	public void testCreateV1File () throws Exception
	{
		PwsRecord	rec = null;
		PwsFile 	v1File	= new PwsFileV1();

		v1File.setStorage( new PwsFileStorage(V1_FILE_NAME) );
		v1File.setPassphrase(new StringBuilder(PASSPHRASE));

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
		
		v1File.dispose();
		assertEquals(null, v1File.getPassphrase());
		
	}
	
	/**
	 * Tests that the library can create a version 1 database.
	 */
	public void testReadV1File () throws Exception
	{
		PwsFile v1File	= new PwsFileV1(new PwsFileStorage(V1_FILE_NAME), PASSPHRASE);
		v1File.readAll();
		v1File.close();
		
		assertEquals(2, v1File.getRecordCount());
				
	}

	/**
	 * Tests that the library can create a version 1 database.
	 */
	public void testConvertV1File () throws Exception
	{
		PwsFileV1 v1File	= new PwsFileV1(new PwsFileStorage(V1_FILE_NAME), PASSPHRASE);
		v1File.readAll();
		v1File.close();
		
		
		PwsFileV2 newTempFile = (PwsFileV2) FileConverter.convertV1ToV2(v1File);
		assertEquals(2, newTempFile.getRecordCount());
		
		PwsFileV1 newFile = (PwsFileV1) FileConverter.convertV2ToV1(newTempFile);
		assertEquals(2, newFile.getRecordCount());
		
		assertEquals(v1File.getRecord(0), newFile.getRecord(0));
	}

	
	
//	public void testProviders() throws Exception {
//		Provider p[] = Security.getProviders();
//		for (Provider pro : p) {
//			System.out.println (pro);
//			for (Enumeration e = pro.keys(); e.hasMoreElements();) {
//				System.out.println ("\t" + e.nextElement());
//			}
//			
//		}
//		BouncyCastleProvider bcp = new BouncyCastleProvider ();
//		System.out.println(bcp.getName());		
//		System.out.println(bcp.getInfo());
//		System.out.println(bcp.getServices());
//	}
}
