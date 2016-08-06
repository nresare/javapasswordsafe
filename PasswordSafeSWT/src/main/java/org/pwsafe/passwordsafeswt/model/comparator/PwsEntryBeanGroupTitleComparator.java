/*
 * Copyright (c) 2010-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.model.comparator;

import java.util.Comparator;

import org.pwsafe.lib.datastore.PwsEntryBean;

/**
 * Compares two PwsEntryBeans by group and title.
 * Works well for tree view.
 * 
 * @author roxon
 */
public class PwsEntryBeanGroupTitleComparator implements Comparator<PwsEntryBean> {

	/**
	 * Compares its two arguments for order. Returns a negative integer, zero,
	 * or a positive integer as the first argument is less than, equal to, or
	 * greater than the second.
	 * <p>
	 * <p/>
	 * In the foregoing description, the notation <tt>sgn(</tt><i>expression</i>
	 * <tt>)</tt> designates the mathematical <i>signum</i> function, which is
	 * defined to return one of <tt>-1</tt>, <tt>0</tt>, or <tt>1</tt> according
	 * to whether the value of <i>expression</i> is negative, zero or positive.
	 * <p>
	 * <p/>
	 * The implementor must ensure that <tt>sgn(compare(x, y)) ==
	 * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This implies
	 * that <tt>compare(x, y)</tt> must throw an exception if and only if
	 * <tt>compare(y, x)</tt> throws an exception.)
	 * <p>
	 * <p/>
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
	 * <tt>compare(x, z)&gt;0</tt>.
	 * <p>
	 * <p/>
	 * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt>
	 * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
	 * <tt>z</tt>.
	 * <p>
	 * <p/>
	 * It is generally the case, but <i>not</i> strictly required that
	 * <tt>(compare(x, y)==0) == (x.equals(y))</tt>. Generally speaking, any
	 * comparator that violates this condition should clearly indicate this
	 * fact. The recommended language is "Note: this comparator imposes
	 * orderings that are inconsistent with equals."
	 * 
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 * @throws ClassCastException if the arguments' types prevent them from
	 *         being compared by this comparator.
	 */
	public int compare(final PwsEntryBean o1, final PwsEntryBean o2) {
		int returnValue = nullSafeCompare (o1.getGroup(), o2.getGroup());

		if (returnValue == 0) {
			// They're in the same directory
			returnValue = nullSafeCompare(o1.getTitle(), o2.getTitle());
		}

		return returnValue;
	}
	private int nullSafeCompare (final String one, final String two) {
		if (one != null) {
			return one.compareTo(two);
		} else {
			return two == null ? 0 : -1;
		}

	}

}
