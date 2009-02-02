package org.pwsafe.lib.crypto;

import org.pwsafe.lib.Util;
import org.pwsafe.lib.exception.PasswordSafeException;

public class BlowfishPwsECB extends BlowfishPws {
	/**
	 * Constructor, sets the initial vector to zero.
	 * 
	 * @param bfkey the encryption/decryption key.
	 * @param cbc Use CBC mode (otherwise ECB is used).  Normally this should be true.
	 * @throws PasswordSafeException 
	 */
	public BlowfishPwsECB( byte[] bfkey ) throws PasswordSafeException
	{
		super(bfkey, zeroIV(), false);
	}

	/**
	 * Constructor, sets the initial vector to the value given.
	 * 
	 * @param bfkey      the encryption/decryption key.
	 * @param lInitCBCIV the initial vector.
	 * @param cbc Use CBC mode (otherwise ECB is used).  Normally this should be true.
	 * @throws PasswordSafeException 
	 */
	public BlowfishPwsECB( byte[] bfkey, long lInitCBCIV ) throws PasswordSafeException
	{
		super(bfkey, makeByteKey(lInitCBCIV), false);
	}

	/**
	 * Constructor, sets the initial vector to the value given.
	 * 
	 * @param bfkey      the encryption/decryption key.
	 * @param ivBytes the initial vector.
	 * @param cbc Use CBC mode (otherwise ECB is used).  Normally this should be true.
	 * @throws PasswordSafeException 
	 */
	public BlowfishPwsECB( byte[] bfkey, byte[] ivBytes ) {
		super(bfkey, ivBytes, false);
	}
	
	/**
	 * Decrypts <code>buffer</code> in place.
	 * 
	 * @param buffer the buffer to be decrypted.
	 * @throws PasswordSafeException 
	 */
	public void decrypt( byte[] buffer ) throws PasswordSafeException {
		/* The endian conversion is simply to make this compatible with
		 * use in previous versions of PasswordSafe (in ECB mode).  Why
		 * the inversion is necessary for CBC mode and why it has to
		 * "cancelled out" in this (ECB mode), I don't know but it
		 * is the only way to get the correct ordering for the
		 * CBC and ECB contexts within a standard password safe file.
		 */
		Util.bytesToLittleEndian(buffer);
		super.decrypt(buffer);
		Util.bytesToLittleEndian(buffer);
	}

	/**
	 * Encrypts <code>buffer</code> in place.
	 * 
	 * @param buffer the buffer to be encrypted.
	 * @throws PasswordSafeException 
	 */
	public void encrypt( byte[] buffer ) throws PasswordSafeException {
		/* The endian conversion is simply to make this compatible with
		 * use in previous versions of PasswordSafe (in ECB mode).  Why
		 * the inversion is necessary for CBC mode and why it has to
		 * "cancelled out" in this (ECB mode), I don't know but it
		 * is the only way to get the correct ordering for the
		 * CBC and ECB contexts within a standard password safe file.
		 */
		Util.bytesToLittleEndian(buffer);
		super.encrypt(buffer);
		Util.bytesToLittleEndian(buffer);
	}
}
