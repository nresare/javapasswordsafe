package org.pwsafe.lib.crypto;

import junit.framework.TestCase;

import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.pwsafe.lib.Util;
import org.pwsafe.lib.exception.PasswordSafeException;

public class BlowfishPwsTest extends TestCase {
	// This cipher text comes from baseline BlowfishPws data.
    int[] key = {0x9F, 0x58, 0x9F, 0x5C, 0xF6, 0x12, 0x2C, 0x32,
            0xB6, 0xBF, 0xEC, 0x2F, 0x2A, 0xE8, 0xC3, 0x5A};
    int[] plainText = { 0xD4, 0x91, 0xDB, 0x16, 0xE7, 0xB1, 0xC3, 0x9E,
              0x86, 0xCB, 0x08, 0x6B, 0x78, 0x9F, 0x54, 0x19 };
    int[] cipherText =  { 0xA4, 0x3D, 0x6E, 0x0B, 0x0F, 0xD6, 0xEF, 0xAA,
              0xBF, 0x74, 0xDE, 0x87, 0x38, 0x53, 0x91, 0x5E };
    
    int[] key32 = { 0xD4, 0x3B, 0xB7, 0x55, 0x6E, 0xA3, 0x2E, 0x46,
            0xF2, 0xA2, 0x82, 0xB7, 0xD4, 0x5B, 0x4E, 0x0D,
            0x57, 0xFF, 0x73, 0x9D, 0x4D, 0xC9, 0x2C, 0x1B,
            0xD7, 0xFC, 0x01, 0x70, 0x0C, 0xC8, 0x21, 0x6F };
    int[] plainText32 = { 0x90, 0xAF, 0xE9, 0x1B, 0xB2, 0x88, 0x54, 0x4F,
            0x2C, 0x32, 0xDC, 0x23, 0x9B, 0x26, 0x35, 0xE6 };
    int[] cipherText32 = { 0x4F, 0x16, 0xBC, 0x11, 0x57, 0x4A, 0x9D, 0x55,
            0xA1, 0xA2, 0x33, 0xAA, 0xA8, 0x05, 0xD2, 0x5C };
    
	byte[] k16 = Util.unsignedToSigned(key);
	byte[] k32 = Util.unsignedToSigned(key32);
	
	byte[] pt16 = Util.unsignedToSigned(plainText);
	byte[] pt32 = Util.unsignedToSigned(plainText32);

	byte[] ct16 = Util.unsignedToSigned(cipherText);
	byte[] ct32 = Util.unsignedToSigned(cipherText32);
    
    public void BCDecrypt() throws PasswordSafeException {
		System.out.println("--> Testing decryption");
		BlowfishPws bc16 = new BlowfishPws(k16);
		BlowfishPws bc32 = new BlowfishPws(k32);
		
		byte[] buf16 = Util.unsignedToSigned(cipherText);
		byte[] buf32 = Util.unsignedToSigned(cipherText32);

		System.out.println("cipherText16 = "+Util.bytesToHex(buf16));
		bc16.decrypt(buf16);
		System.out.println("plainText16 = "+Util.bytesToHex(buf16));
		assertEquals(Util.bytesToHex(pt16),Util.bytesToHex(buf16));
		
		System.out.println("cipherText32 = "+Util.bytesToHex(buf32));
		bc32.decrypt(buf32);
		System.out.println("plainText32 = "+Util.bytesToHex(buf32));
		assertEquals(Util.bytesToHex(pt32),Util.bytesToHex(buf32));
    }
    public void BCEncrypt() throws PasswordSafeException {
		byte[] buf16 = null;
		byte[] buf32 = null;
		
		BlowfishPws bc16;
		BlowfishPws bc32;
		
		System.out.println("--> Testing encryption");
		bc16 = new BlowfishPws(k16);
		bc32 = new BlowfishPws(k32);
		
		buf16 = Util.unsignedToSigned(plainText);
		buf32 = Util.unsignedToSigned(plainText32);

		System.out.println("plainText16 = "+Util.bytesToHex(buf16));
		bc16.encrypt(buf16);
		System.out.println("cipherText16 = "+Util.bytesToHex(buf16));
		assertEquals(Util.bytesToHex(ct16),Util.bytesToHex(buf16));
		
		System.out.println("plainText32 = "+Util.bytesToHex(buf32));
		bc32.encrypt(buf32);
		System.out.println("cipherText32 = "+Util.bytesToHex(buf32));
		assertEquals(Util.bytesToHex(ct32),Util.bytesToHex(buf32));
    }
	public void testBCBlowfish() throws PasswordSafeException {
		System.out.println("== Testing BCBlowfishPws ==");
	
		BCEncrypt();
		BCDecrypt();
	}
	
	public void runBCRoundTrip() throws PasswordSafeException {
		byte[] iv = new byte[8];
		Util.newRandBytes(iv);
		BlowfishPws ebc16 = new BlowfishPws(k16, iv, true);
		BlowfishPws ebc32 = new BlowfishPws(k32, iv, true);
		BlowfishPws ej16 = new BlowfishPws(k16, iv);
		BlowfishPws ej32 = new BlowfishPws(k32, iv);
		BlowfishPws dbc16 = new BlowfishPws(k16, iv, true);
		BlowfishPws dbc32 = new BlowfishPws(k32, iv, true);

		byte[] buf16 = new byte[64];
		Util.newRandBytes(buf16);
		byte[] orig = Util.cloneByteArray(buf16);
		byte[] j16 = Util.cloneByteArray(buf16);
		
		ebc16.encrypt(buf16);
		ej16.encrypt(j16);
		
		// Make sure the two blowfish implementations give the same
		// answer here.
		assertEquals(Util.bytesToHex(j16),Util.bytesToHex(buf16));
		
		dbc16.decrypt(buf16);
		
		assertEquals(Util.bytesToHex(orig),Util.bytesToHex(buf16));
		
		byte[] buf32 = new byte[64];
		Util.newRandBytes(buf32);
		orig = Util.cloneByteArray(buf32);
		byte[] j32 = Util.cloneByteArray(buf32);
		
		ebc32.encrypt(buf32);
		ej32.encrypt(j32);
		
		// Make sure the two blowfish implementations give the same
		// answer here.
		assertEquals(Util.bytesToHex(j32),Util.bytesToHex(buf32));
		
		dbc32.decrypt(buf32);
		
		assertEquals(Util.bytesToHex(orig),Util.bytesToHex(buf32));
	}
		
	public void testRunTrip() throws PasswordSafeException {
		for(int i=0;i<100;i++) {
			runBCRoundTrip();
		}
	}
	
	public void testFixedBareBC16() {
		runBareBC(k16, pt16, ct16);
	}	

	public void testFixedBareBC32() {
		runBareBC(k32, pt32, ct32);
	}	
	
	public void testRandomBareBC() {
		byte[] orig = new byte[64];
		Util.newRandBytes(orig);
		runBareBC(k16, orig, null);
	}
	
	public void runBareBC(byte[] key, byte[] orig, byte[] expct) {
		CBCBlockCipher cipher = new CBCBlockCipher(new BlowfishEngine());
		byte[] iv = new byte[8];
		for(int j=0;j<8;j++) iv[j] = 0;
    	KeyParameter ekp = new KeyParameter(Util.cloneByteArray(key));
		ParametersWithIV eiv = new ParametersWithIV(ekp, iv);

		cipher.init(true, eiv);
		
		Util.bytesToLittleEndian(orig);
		byte[] buf16 = Util.cloneByteArray(orig);
		byte[] enc = new byte[buf16.length];
		
		System.out.println("orig  = "+Util.bytesToHex(orig));
		System.out.println("buf16 = "+Util.bytesToHex(orig));
		for(int i=0;i<orig.length;i+=8) {
			System.out.println("enc"+i+" = "+Util.bytesToHex(enc));
			cipher.processBlock(buf16, i, enc, i);
		}
		System.out.println("enc = "+Util.bytesToHex(enc));
		Util.bytesToLittleEndian(enc);
		
		if (expct!=null) {
			System.out.println("exp = "+Util.bytesToHex(enc));
			assertEquals(Util.bytesToHex(expct), Util.bytesToHex(enc));
		}
		
		Util.bytesToLittleEndian(enc);
		KeyParameter dkp = new KeyParameter(Util.cloneByteArray(key));
		ParametersWithIV div = new ParametersWithIV(dkp, iv);
		
		buf16 = Util.cloneByteArray(enc);
		cipher = new CBCBlockCipher(new BlowfishEngine());
		cipher.init(false, div);
		
		byte[] dec = new byte[buf16.length];
		
		for(int i=0;i<orig.length;i+=8) {
			System.out.println("dec"+i+" = "+Util.bytesToHex(dec));
			cipher.processBlock(buf16, i, dec, i);
		}
		System.out.println("dec64 = "+Util.bytesToHex(dec));
		System.out.println("orig = "+Util.bytesToHex(orig));
		assertEquals(Util.bytesToHex(dec), Util.bytesToHex(orig));
	}
	
	public void testBaselineECB() throws Exception {
	    // test vector #1 (checking for the "signed bug")
	    byte[] testKey1 = { (byte) 0x1c, (byte) 0x58, (byte) 0x7f, (byte) 0x1c,
	                        (byte) 0x13, (byte) 0x92, (byte) 0x4f, (byte) 0xef };
	    byte[] invTestKey1 = new byte[testKey1.length];
	    Util.bytesToLittleEndian(invTestKey1);
	    int[] tv_p1 = { 0x30553228, 0x6d6f295a };
	    byte[] tv_p1b = convert(tv_p1);
	    int[] tv_c1 = { 0x55cb3774, 0xd13ef201 };
	    byte[] tv_c1b = convert(tv_c1);
	    byte[] tv_t1b = new byte[8];

	    // test vector #2 (offical vector by Bruce Schneier)
	    String sTestKey2 = "Who is John Galt?";
	    byte[] testKey2 = sTestKey2.getBytes();
	    //byte[] invTestKey2 = new byte[testKey2.length];
	    //Util.bytesToLittleEndian(invTestKey2);

	    int[] tv_p2 = { 0xfedcba98, 0x76543210 };
	    byte[] tv_p2b = convert(tv_p2);
	    int[] tv_c2 = { 0xcc91732b, 0x8022f684 };
	    byte[] tv_c2b = convert(tv_c2);
	    byte[] tv_t2b = new byte[8];

	    // start the tests, check for a proper decryption, too
	    BlowfishPwsECB testpws1 = new BlowfishPwsECB(testKey1);

	    tv_t1b = Util.cloneByteArray(tv_p1b);
	    
	    testpws1.encrypt(tv_t1b);
	    assertEquals(Util.bytesToHex(tv_c1b), Util.bytesToHex(tv_t1b));

	    testpws1.decrypt(tv_t1b);
	    assertEquals(Util.bytesToHex(tv_p1b), Util.bytesToHex(tv_t1b));

	    BlowfishPwsECB testpws2 = new BlowfishPwsECB(testKey2);
	    
	    tv_t2b = Util.cloneByteArray(tv_p2b);
	    testpws2.encrypt(tv_t2b);
	    assertEquals(Util.bytesToHex(tv_c2b), Util.bytesToHex(tv_t2b));

	    testpws2.decrypt(tv_t2b);
	    assertEquals(Util.bytesToHex(tv_p2b), Util.bytesToHex(tv_t2b));
	}
	
	public byte[] convert(int[] v) {
		byte[] b = new byte[v.length*4];

		for(int j=0;j<v.length;j++) {
				b[4*j] = (byte)(v[j] >> 24);
				b[4*j+1] = (byte)(v[j] >> 16);
				b[4*j+2] = (byte)(v[j] >> 8);
				b[4*j+3] = (byte)(v[j] >> 0);
		}

		return b;
	}
}
