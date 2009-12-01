package org.pwsafe.passwordsafeswt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class S3InformationComposite extends org.eclipse.swt.widgets.Composite {
	private Label lblBucketName;
	private Text txtSecret;
	private Label lblSecret;
	private Text txtAccess;
	private Label lblAccess;
	private Text txtBucketName;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void main(String[] args) {
		showGUI();
	}
		
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		S3InformationComposite inst = new S3InformationComposite(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public S3InformationComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			int vertical_spacing = 13;
			
			FormLayout thisLayout = new FormLayout();
			this.setLayout(new FormLayout());
			this.setSize(297, 158);
			{
				lblBucketName = new Label(this, SWT.NONE);
				FormData label1LData = new FormData();
				label1LData.left =  new FormAttachment(0, 10);
				label1LData.top =  new FormAttachment(0, 1000, 21);
				lblBucketName.setLayoutData(label1LData);
				lblBucketName.setText(Messages.getString("S3InformationComposite.BucketName")); //$NON-NLS-1$
			}
			{
				txtBucketName = new Text(this, SWT.BORDER);
				FormData txtBucketNameLData = new FormData();
				txtBucketNameLData.left =  new FormAttachment(lblBucketName, 137, SWT.LEFT);
				txtBucketNameLData.top =  new FormAttachment(lblBucketName, 0, SWT.CENTER);
				txtBucketNameLData.right = new FormAttachment(95, 0);
				txtBucketName.setLayoutData(txtBucketNameLData);
			}
			{
				lblAccess = new Label(this, SWT.NONE);
				FormData label1LData1 = new FormData();
				label1LData1.left =  new FormAttachment(lblBucketName, 0, SWT.LEFT);
				label1LData1.top =  new FormAttachment(lblBucketName, 13);
				lblAccess.setLayoutData(label1LData1);
				lblAccess.setText(Messages.getString("S3InformationComposite.AccessKey")); //$NON-NLS-1$
			}
			{
				txtAccess = new Text(this, SWT.BORDER);
				FormData text1LData = new FormData();
				text1LData.left =  new FormAttachment(lblAccess, 137, SWT.LEFT);
				text1LData.top =  new FormAttachment(lblAccess, 0, SWT.CENTER);
				text1LData.right =  new FormAttachment(txtBucketName, 0, SWT.RIGHT);
				txtAccess.setLayoutData(text1LData);
			}
			{
				lblSecret = new Label(this, SWT.NONE);
				FormData label2LData = new FormData();
				label2LData.left =  new FormAttachment(lblBucketName, 0, SWT.LEFT);
				label2LData.top =  new FormAttachment(lblAccess, 13);
				lblSecret.setLayoutData(label2LData);
				lblSecret.setText(Messages.getString("S3InformationComposite.AccessSecret")); //$NON-NLS-1$
			}
			{
				txtSecret = new Text(this, SWT.BORDER);
				FormData text2LData = new FormData();
				text2LData.left =  new FormAttachment(lblSecret, 137, SWT.LEFT);
				text2LData.top =  new FormAttachment(lblSecret, 0, SWT.CENTER);
				text2LData.right =  new FormAttachment(txtAccess, 0, SWT.RIGHT);
				txtSecret.setLayoutData(text2LData);
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isVerified(){
		boolean verifyOk = true;
		verifyOk = verifyOk && getBucketName() != null;
		verifyOk = verifyOk && getAccessKey() != null;
		verifyOk = verifyOk && getAccessSecret() != null;
		verifyOk = verifyOk && getBucketName().length() > 1;
		verifyOk = verifyOk && getAccessKey().length() > 1;
		verifyOk = verifyOk && getAccessSecret().length > 1;
		
		return verifyOk;
	}

	public String getBucketName () {
		return txtBucketName.getText();
	}
	
	public String getAccessKey () {
		return txtAccess.getText();
	}

	public char[] getAccessSecret () {
		return txtSecret.getText().toCharArray();
	}


}
