/*
 * $Id$
 * 
 * Copyright (c) 2008 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.crypto;

import blowfishj.BlowfishCBC;

import org.pwsafe.lib.Util;

/**
 * An extension to the BlowfishJ.BlowfishCBC to allow it to be used for PasswordSafe. Byte 
 * order differences prevent BlowfishCBC being used directly.
 * 
 * @author Kevin Preece
 */
public class BlowfishPws
{
	private BlowfishCBC blowfishCbc;
	
	/**
	 * Constructor, sets the initial vector to zero.
	 * 
	 * @param bfkey the encryption/decryption key.
	 */
	public BlowfishPws( byte[] bfkey )
	{
		blowfishCbc = new BlowfishCBC(bfkey, 0, bfkey.length);
	}

	/**
	 * Constructor, sets the initial vector to the value given.
	 * 
	 * @param bfkey      the encryption/decryption key.
	 * @param lInitCBCIV the initial vector.
	 */
	public BlowfishPws( byte[] bfkey, long lInitCBCIV )
	{
		blowfishCbc = new BlowfishCBC(bfkey, 0, bfkey.length, lInitCBCIV);
	}

	/**
	 * Constructor, sets the initial vector to the value given.
	 * 
	 * @param bfkey      the encryption/decryption key.
	 * @param initCBCIV the initial vector.
	 */
	public BlowfishPws( byte[] bfkey, byte[] initCBCIV )
	{
		blowfishCbc = new BlowfishCBC( bfkey, 0, bfkey.length );
		setCBCIV( initCBCIV );
	}

	/**
	 * Decrypts <code>buffer</code> in place.
	 * 
	 * @param buffer the buffer to be decrypted.
	 */
	public void decrypt( byte[] buffer )
	{
		Util.bytesToLittleEndian( buffer );
		blowfishCbc.decrypt( buffer, 0, buffer, 0, buffer.length );
		Util.bytesToLittleEndian( buffer );
	}

	/**
	 * Encrypts <code>buffer</code> in place.
	 * 
	 * @param buffer the buffer to be encrypted.
	 */
	public void encrypt( byte[] buffer )
	{
		Util.bytesToLittleEndian( buffer );
		blowfishCbc.encrypt(buffer, 0, buffer, 0, buffer.length);
		Util.bytesToLittleEndian( buffer );
	}

	/**
	 * Sets the initial vector.
	 * 
	 * @param newCBCIV the new value for the initial vector.
	 */
	public void setCBCIV( byte[] newCBCIV )
	{
		byte temp[] = new byte [ newCBCIV.length ];
		System.arraycopy( newCBCIV, 0, temp, 0, newCBCIV.length );
		Util.bytesToLittleEndian( temp );
		blowfishCbc.setCBCIV( temp, 0 );
	}

}
