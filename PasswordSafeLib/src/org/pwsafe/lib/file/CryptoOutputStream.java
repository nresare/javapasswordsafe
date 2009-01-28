package org.pwsafe.lib.file;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.blowfishj.SHA1;

import org.pwsafe.lib.Util;
import org.pwsafe.lib.crypto.BlowfishPws;

/**
 * This class is used to encrypt an existing OutputStream
 * while providing an OutputStream interface itself.
 * 
 * Note that because of the block nature of encryption there
 * will normally be extra bytes at the end of such a stream to
 * represented the encrypted "zero padding" added at the end of
 * the original stream.
 * 
 * Also because of the block nature of this stream, there is
 * implicit (and unflushable) buffering involved so closing
 * the stream is extremely important.
 * 
 * @author mtiller
 *
 */
public class CryptoOutputStream extends OutputStream {
	private byte [] block = new byte[16];
	private int index = 0;
	/* Header info */
	private byte []	randStuff = null;
	private byte []	randHash = null;
	private byte [] salt = null;
	private byte [] ipThing = null;
	
	private String passphrase;
	private OutputStream rawStream;
	private BlowfishPws engine;
	/**
	 * The constructor for the encrytped output stream class.
	 * @param passphrase A passphrase used for encryption
	 * @param stream The stream to be encrypted.
	 */
	public CryptoOutputStream(String passphrase, OutputStream stream) {
		rawStream = stream;
		this.passphrase = passphrase;
	}
	/**
	 * Constructs and initialises the blowfish encryption routines ready to decrypt or
	 * encrypt data.
	 * 
	 * @param passphrase
	 * 
	 * @return A properly initialised {@link BlowfishPws} object.
	 */
	private BlowfishPws makeBlowfish( byte [] passphrase )
	{
		SHA1	sha1;
		
		sha1 = new SHA1();

		sha1.update( passphrase, 0, passphrase.length );
		sha1.update( salt, 0, salt.length );
		sha1.finalize();

		return new BlowfishPws( sha1.getDigest(), ipThing );
	}
	/**
	 * a routine to initialize the encryption data structures
	 * and generate some random header data.
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		randStuff = new byte[8];
		randHash = new byte[20];
		salt = new byte[20];
		ipThing = new byte[8];
		Util.newRandBytes(randStuff);
		byte [] temp = Util.cloneByteArray( randStuff, 10 );
		randHash = PwsFileFactory.genRandHash( passphrase, temp );
		Util.newRandBytes(salt);
		Util.newRandBytes(ipThing);
		engine = makeBlowfish(passphrase.getBytes());
		rawStream.write(randStuff);
		rawStream.write(randHash);
		rawStream.write(salt);
		rawStream.write(ipThing);
	}
	/**
	 * Closes the stream and writes out the final block (zero padded if
	 * necessary.
	 */
	public void close() throws IOException {
		if (salt==null) initialize();
		for(;index<16;index++) { block[index] = 0; }
		index = 0;
		engine.encrypt(block);
		rawStream.write(block);
		rawStream.close();
		super.close();
	}
	/**
	 * Writes an individual byte.
	 */
	public void write(int b) throws IOException {
		/** first time through, parse header and set up engine */
		if (salt==null) initialize();
		if (index==16) {
			engine.encrypt(block);
			rawStream.write(block);
			index = 0;
		}
		block[index] = (byte)b;
		index++;
	}
}
