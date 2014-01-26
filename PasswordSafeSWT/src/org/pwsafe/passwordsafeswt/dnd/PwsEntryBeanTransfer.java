/*
 * Copyright (c) 2013-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.dnd;

import java.nio.ByteBuffer;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * Handles data conversion in the tree view.
 * For now only the drop of a bean on groups is correctly handled.
 * 
 * @author roxon
 *
 */
public class PwsEntryBeanTransfer extends ByteArrayTransfer {
	private static PwsEntryBeanTransfer instance = new PwsEntryBeanTransfer();
	private static final String ENTRY_TYPE_NAME = "pwsEntry-transfer-format";
	private static final int ENTRY_TYPEID = registerType(ENTRY_TYPE_NAME);
	private static final String GROUP_TYPE_NAME = "pwsGroup-transfer-format";
	private static final int GROUP_TYPEID = registerType(GROUP_TYPE_NAME);

	/**
	 * Returns the singleton gadget transfer instance.
	 */
	public static PwsEntryBeanTransfer getInstance() {
		return instance;
	}
	/**
	 * Avoid explicit instantiation
	 */
	private PwsEntryBeanTransfer() {
	}

	/*
	 * Method declared on Transfer.
	 */
	@Override
	protected int[] getTypeIds() {
		return new int[] { ENTRY_TYPEID, GROUP_TYPEID };
	}

	/*
	 * Method declared on Transfer.
	 */
	@Override
	protected String[] getTypeNames() {
		return new String[] { ENTRY_TYPE_NAME, GROUP_TYPE_NAME };
	}

	/*
	 * Method declared on Transfer.
	 */
	@Override
	protected void javaToNative(final Object object, final TransferData transferData) {
		if (object instanceof Integer) {
			final byte[] bytes = ByteBuffer.allocate(4).putInt((Integer) object).array();

			if (bytes != null) {
				super.javaToNative(bytes, transferData);
			}
		} else if (object instanceof String) {
			if (! TextTransfer.getInstance().isSupportedType(transferData)) {
				// FIXME this is a hack - works on Linux x64...
				// no danger for now as treegroups are disabled on drag side
				long type =  transferData.type;
				for (final TransferData data : TextTransfer.getInstance().getSupportedTypes()) {
					type = data.type;
				}
				// on 32bit you need int instead of long, to avoid compile errors there use this here
				transferData.type = (int) type;
			}
			TextTransfer.getInstance().javaToNative(object, transferData);
		}
	}
	/*
	 * Method declared on Transfer.
	 */
	@Override
	protected Object nativeToJava(final TransferData transferData) {
		if (transferData.type == ENTRY_TYPEID) {
			Object result = super.nativeToJava(transferData);

			if (result != null) {
				final byte[] bytes = (byte[])result;
				return result =  ByteBuffer.wrap(bytes).getInt();
			}
		} else if (transferData.type == GROUP_TYPEID) {
			return TextTransfer.getInstance().nativeToJava(transferData);
		}

		return null;
	}
}
