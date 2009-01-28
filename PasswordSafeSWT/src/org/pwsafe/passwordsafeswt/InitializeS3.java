package org.pwsafe.passwordsafeswt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class InitializeS3 {
	public static class Row extends Composite {
		public Row(Composite parent, String title) {
			super(parent, SWT.NONE);
			RowLayout layout = new RowLayout();
			// Optionally set layout fields.
			layout.wrap = true;
			// Set the layout into the composite.
			setLayout(layout);
			// Create the children of the composite.
			Label l = new Label(this, SWT.NONE);
			l.setText(title+":");
			l.pack();
			Text t = new Text(this, SWT.NONE);
			t.pack();
		}
	}
	public static class UI extends Composite {
		public UI(Composite parent) {
			super(parent, SWT.SHADOW_ETCHED_IN);
			//this.setSize(600,200);
			Group group = new Group(this, SWT.SHADOW_ETCHED_IN);
			
			RowLayout layout = new RowLayout(SWT.VERTICAL);
			group.setLayout(layout);
			group.setText("Amazon S3 Account Information");
			group.setLocation(0, 0);
			
			Row bucket = new Row(group, "Bucket Name");
			bucket.pack();
			Row access = new Row(group, "Access Key");
			access.pack();
			Row secret = new Row(group, "Secret Key");
			secret.pack();
			Row pass = new Row(group, "Passphrase");
			pass.pack();

			Button b = new Button(group, SWT.PUSH);
			b.setText("Create Reference File");
			
			group.pack();
		}
	}

	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell(display);
		shell.setText("SWT Group Example");
		
		RowLayout layout = new RowLayout();
		shell.setLayout(layout);
		//SWTGroup ui = new SWTGroup(shell);
		UI ui = new UI(shell);
		
		Label exp = new Label(shell, SWT.NONE);
		String msg = "In order to use an S3 account to store your Password Safe\n"+
					 "file you must first provide information about your S3 account.\n"+
					 "If you don't have an S3 account, you must fist sign up for one.\n"+
					 "Please provide the account information on the left.  This\n"+
					 "information will be encrypted using the passphrase you provide.\n"+
					 "If the bucket named does not already contain a Password Safe\n"+
					 "file, one will be created *using the same passphrase* that is\n"+
					 "is used to encrypt the S3 account information.";
		exp.setText(msg);
		exp.pack();
	 
		shell.pack();
		shell.open();
	 
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
