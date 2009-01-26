/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.pwsafe.lib.file.PwsRecord;
import org.pwsafe.lib.file.PwsRecordV1;
import org.pwsafe.lib.file.PwsRecordV2;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;

/**
 * Copyies the username to the clipboard.
 *
 * @author Glen Smith
 */
public class CopyUsernameAction extends Action {

    public CopyUsernameAction() {
        super("Copy Username");
        setAccelerator( SWT.MOD1 | 'U'  );
        setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader().getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_user.gif")));
        setToolTipText("Copy Username To Clipboard");

    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();

        PwsRecord recordToCopy = app.getSelectedRecord();

        Clipboard cb = new Clipboard(app.getShell().getDisplay());

        app.copyToClipboard(cb, recordToCopy, recordToCopy instanceof PwsRecordV1 ? PwsRecordV1.USERNAME : PwsRecordV2.USERNAME);

        cb.dispose();

    }

}