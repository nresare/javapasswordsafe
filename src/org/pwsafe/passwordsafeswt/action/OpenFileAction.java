package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.PasswordDialog;

/**
 * Open command.
 *
 * @author Glen Smith
 */
public class OpenFileAction extends Action {

    public OpenFileAction() {
        super("&Open File@Ctrl+O");
        setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader().getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_open.gif")));
        setToolTipText("Open Existing Safe");
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();
        FileDialog fod = new FileDialog(app.getShell(), SWT.OPEN);
        String fileName = fod.open();
        if (fileName != null) {
            PasswordDialog pd = new PasswordDialog(app.getShell());
            pd.setFileName(fileName);
            String password = (String) pd.open();
            if (password != null) {
                try {
                    app.openFile(fileName, password);
                } catch (Exception e) {
                    app.displayErrorDialog("Error Opening Safe", "Invalid Passphrase", e);
                }
            }

        }
    }

}