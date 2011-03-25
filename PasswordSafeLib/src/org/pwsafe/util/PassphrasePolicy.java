 package org.pwsafe.util;


/**
 * This class defines a policy that will be used to generate a random password.
 * The policy defines how long the generated password should be and which
 * character classes it should contain.  The character classes are:
 * <p>
 * <sl>
 *   <li>Upper case letters,
 *   <li>Lowercase letters,
 *   <li>Digits and certain symbol characters.
 * </sl>
 * </p><p>
 * In addition it also specifies whether certain confusable characters should
 * be removed from the password.  These are characters such as '1' and 'I'.
 */
public class PassphrasePolicy
{
	// bit masks to interpret the formats V3 String - usage see valueOf method
	static int LOWER_MASK = 0x8000;
	static int UPPER_MASK = 0x4000;
	static int DIGITS_MASK = 0x2000;
	static int SYMBOL_MASK = 0x1000;
	static int HEX_MASK = 0x0800;
	static int EASYREAD_MASK = 0x0400;
	static int PRONOUNCABLE_MASK = 0x0200;

	/**
	 * <code>true</code> if generated password should contain lowercase characters.
	 * The default is <code>true</code>.
	 */
	public boolean	lowercaseChars	= true;

	/**
	 * <code>true</code> if generated password should contain uppercase characters.
	 * The default is <code>true</code>.
	 */
	public boolean	uppercaseChars	= true;

	/**
	 * <code>true</code> if generated password should contain digit characters.
	 * The default is <code>true</code>.
	 */
	public boolean	digitChars		= true;

	/**
	 * <code>true</code> if the generated password should contain symbol characters.
	 * The default is <code>true</code>.
	 */
	public boolean	symbolChars		= true;

	/**
	 * <code>true</code> if the generated password should not contain confusable characters.
	 * The default is <code>false</code>.
	 */
	public boolean	easyview		= false;

	/**
	 * The length of the generated password.  The default is 8.
	 */
	public int		length			= 8;

	/**
	 * Creates a password policy with fairly strong deafults.  If unaltered
	 * this policy will cause a password to be generated that is 8 characters
	 * long with at least one of each character class, i.e. at least one
	 * uppercase, lowercase, digit, and symbol characters.
	 */
	public PassphrasePolicy()
	{
		super();
	}

	/**
	 * Checks that it is possible to generate a password using this policy.  Returns
	 * <code>true</code> if at least one character category is selected and the password
	 * length is equal to or greater than the number of classes selected.
	 * 
	 * @return
	 */
	public boolean isValid()
	{
		int		count	= 0;

		if ( lowercaseChars )	++count;
		if ( uppercaseChars )	++count;
		if ( digitChars )		++count;
		if ( symbolChars )		++count;

		if ( (count > 0) && (length >= count) )
		{
			return true;
		}
		return false;
	}

	/**
	 * Creates a Password Policy from a String as defined in formatsV3.txt.
	 * The format is:<pre>
     *	"ffffnnnllluuudddsss"
	 *	where:
     *	ffff = 4 hexadecimal digits representing the following flags
     *   	UseLowercase =      0x8000  - can have a minimum length
     *   	UseUppercase =      0x4000  - can have a minimum length
     *   	UseDigits =         0x2000  - can have a minimum length
     *   	UseSymbols =        0x1000  - can have a minimum length
     *   	UseHexDigits =      0x0800 (if set, then no other flags can be set)
     *   	UseEasyVision =     0x0400
     *   	MakePronounceable = 0x0200
     *   	Unused              0x01ff
     *	nnn  = 3 hexadecimal digits password total length
     *	lll  = 3 hexadecimal digits password minimum number of lowercase characters
     *	uuu  = 3 hexadecimal digits password minimum number of uppercase characters
     *	ddd  = 3 hexadecimal digits password minimum number of digit characters
     *	sss  = 3 hexadecimal digits password minimum number of symbol characters
     *	</pre>
	 *
	 *
	 * @param policyString
	 * @return
	 */
	public static PassphrasePolicy valueOf(String policyString) {
		if (policyString == null || policyString.length() != 19) {
			throw new IllegalArgumentException("wrong Password policy length for " + policyString);
		}
		PassphrasePolicy policy = new PassphrasePolicy();
		String types = policyString.substring(0,4);
		int blabla = Integer.parseInt(types, 16);
		policy.lowercaseChars = (blabla & LOWER_MASK) == LOWER_MASK;
		policy.uppercaseChars = (blabla & UPPER_MASK) == UPPER_MASK;
		policy.digitChars = (blabla & DIGITS_MASK) == DIGITS_MASK;
		policy.symbolChars = (blabla & SYMBOL_MASK) == SYMBOL_MASK;
		policy.easyview = (blabla & EASYREAD_MASK) == EASYREAD_MASK;
		
		policy.length = Integer.parseInt(policyString.substring(4, 7), 16);
		
		//sublengths how to:
//		for (int i = 0; i < 4 * 3; i+=3) {
//			int sublength = Integer.parseInt(policyString.substring(7 + i, 7 + i + 3), 16) ;
//		}
		
		return policy;
	}
	
	/**
	 * Returns a <code>String</code> representation of the object.
	 * 
	 * @return A <code>String</code> representation of the object.
	 */
	public String toString()
	{
		StringBuffer	sb;

		sb = new StringBuffer();

		sb.append( "PassphrasePolicy{ Length=" );
		sb.append( length );
		sb.append( ", Uppercase=" );
		sb.append( uppercaseChars );
		sb.append( ", Lowercase=" );
		sb.append( lowercaseChars );
		sb.append( ", Digits=" );
		sb.append( digitChars );
		sb.append( ", Symbols=" );
		sb.append( symbolChars );
		sb.append( ", Easyview=" );
		sb.append( easyview );
		sb.append( " }" );

		return sb.toString();
	}
}
