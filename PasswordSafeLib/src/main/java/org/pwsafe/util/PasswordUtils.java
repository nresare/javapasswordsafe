/*
 * Copyright (c) 2008-2011 David Muller <roxon@users.sourceforge.net>.
 * Copyright (c) 2016 Noa Resare (noa@resare.com)
 *
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.pwsafe.lib.Log;
import org.pwsafe.lib.exception.InvalidPassphrasePolicy;

/**
 * Password generation utilities.
 */
public class PasswordUtils {

  private static final Log LOG = Log.getInstance(PassphraseUtils.class.getPackage().getName());
  private static final SecureRandom secureRandom = new SecureRandom();

  /**
   * Standard lowercase characters.
   */
  private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";

  /**
   * Standard uppercase characters.
   */
  private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  /**
   * Standard digit characters.
   */
  private static final String DIGIT_CHARS = "1234567890";

  /**
   * Standard symbol characters.
   */
  private static final String SYMBOL_CHARS = "+-=_@#$%^&;:,.<>/~\\[](){}?!|";

  /**
   * Lowercase characters with confusable characters removed.
   */
  private static final String EASYVISION_LC_CHARS = "abcdefghijkmnopqrstuvwxyz";

  /**
   * Uppercase characters with confusable characters removed.
   */
  private static final String EASYVISION_UC_CHARS = "ABCDEFGHJKLMNPQRTUVWXY";

  /**
   * Digit characters with confusable characters removed.
   */
  private static final String EASYVISION_DIGIT_CHARS = "346789";

  /**
   * Symbol characters with confusable characters removed.
   */
  private static final String EASYVISION_SYMBOL_CHARS = "+-=_@#$%^&<>/~\\?";


  /**
   * Create a password conforming to the provided PassphrasePolicy
   *
   * @param policy the policy to conform to
   * @return A generated password
   * @throws InvalidPassphrasePolicy if the policy is invalid
   */
  public static String makePassword(PassphrasePolicy policy) throws InvalidPassphrasePolicy {
    LOG.enterMethod("makePassword");

    LOG.debug2(policy.toString());

    List<String> alphabets = new ArrayList<>();
    if (policy.easyview) {
      if (policy.digitChars) {
        alphabets.add(EASYVISION_DIGIT_CHARS);
      }
      if (policy.lowercaseChars) {
        alphabets.add(EASYVISION_LC_CHARS);
      }
      if (policy.uppercaseChars) {
        alphabets.add(EASYVISION_UC_CHARS);
      }
      if (policy.symbolChars) {
        alphabets.add(EASYVISION_SYMBOL_CHARS);
      }
    } else {
      if (policy.digitChars) {
        alphabets.add(DIGIT_CHARS);
      }
      if (policy.lowercaseChars) {
        alphabets.add(LOWERCASE_CHARS);
      }
      if (policy.uppercaseChars) {
        alphabets.add(UPPERCASE_CHARS);
      }
      if (policy.symbolChars) {
        alphabets.add(SYMBOL_CHARS);
      }
    }

    return makePassword(policy.length, alphabets);
  }

    /**
     * Create a password with at least one character each from each of the alphabets.
     *
     * @param length the length of the specified password
     * @param alphabets strings containing letters to choose characters from
     * @return A password of the specified length generated from the provided alphabets
     */
  static String makePassword(int length, List<String> alphabets) {
    if (alphabets.size() > length) {
      throw new IllegalArgumentException(
          "You can't create a password shorter than the number of alphabets");
    }
    List<Character> output = new ArrayList<>(Collections.nCopies(length, null));
    int charsLeftToAdd = length;

    // first add one random character from each alphabet, at random locations of the output
    for (String alphabet : alphabets) {
      char c = alphabet.charAt(secureRandom.nextInt(alphabet.length()));
      addAtNthFree(output, c, secureRandom.nextInt(charsLeftToAdd--));
    }

    // fill out the rest with values randomly picked from the different alphabets.
    // concatenate them together as to not skew the probability towards the alphabets
    // with fewer letters.
    String allChars = String.join("", alphabets);
    for (int i = 0; i < output.size(); i++) {
      if (output.get(i) == null) {
        output.set(i, allChars.charAt(secureRandom.nextInt(allChars.length())));
      }
    }
    return output.stream().map(Object::toString).collect(Collectors.joining());
  }

  /**
   * Count the number of non-null elements from the beginning of list, setting
   * the where the count is equal to fromStart to toAdd.
   *
   * @param list a list to add things to
   * @param toAdd the value to put in the list
   * @param fromStart the number of non-null values to count before you set a value
   */
  private static <T> void addAtNthFree(List<T> list, T toAdd, int fromStart) {
    int actualPosition = 0;
    for (T c : list) {
      if (c == null) {
        if (fromStart == 0) {
          list.set(actualPosition, toAdd);
          break;
        }
        fromStart--;
      }
      actualPosition++;
    }
  }
}

