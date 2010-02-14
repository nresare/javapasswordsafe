/**
 * 
 */
package org.pwsafe.passwordsafeswt;

import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.SHOW_ICON_IN_SYSTEM_TRAY;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.carbon.HICommand;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.widgets.Display;
import org.pwsafe.passwordsafeswt.util.UserPreferences;

import com.swtdesigner.SWTResourceManager;

/**
 * @author corvi42
 * 
 *         This subclass of PasswordSafeJFace adds MacOSX specific menus. This
 *         is necessary because OS X has some peculiarities which are not
 *         generalized in SWT, and require some custom non-portable code.
 * 
 *         This is based upon the Eclipse project code
 *         org.eclipse.ui.internal.carbon.CarbonUIEnhancer
 */
public class PasswordSafeMacOSX extends PasswordSafeJFace
{
  private static final int kHICommandPreferences =
    ( 'p' << 24 ) + ( 'r' << 16 ) + ( 'e' << 8 ) + 'f';
  private static final int kHICommandAbout =
    ( 'a' << 24 ) + ( 'b' << 16 ) + ( 'o' << 8 ) + 'u';
  private static final int kHICommandServices =
    ( 's' << 24 ) + ( 'e' << 16 ) + ( 'r' << 8 ) + 'v';
  private static final int kHICommandQuit =
    ( 'q' << 24 ) + ( 'u' << 16 ) + ( 'i' << 8 ) + 't';
  private static final String aboutString = "About PasswordSafe";

  /**
   * Constructor
   */
  public PasswordSafeMacOSX()
  {
    // initialize the standard PasswordSafeJFace app
    super();
    
    // disable standard exit action & associated separator (last 2 items on file menu)
    MenuManager fileMenu = ((MenuManager) getMenuBarManager().getItems()[0]);
    IContributionItem[] fileItems = fileMenu.getItems();
    fileMenu.remove( fileItems[fileItems.length-1] );
    fileMenu.remove( fileItems[fileItems.length-2] );
    
    // enable Mac Application menus
    final Display display = Display.getDefault();
    display.syncExec( new Runnable() {
      public void run()
      {
        setupMacAppMenus( display );
      }
    } );
  }

  /**
   * This method initializes the Mac-specific Application menu.
   */
  protected void setupMacAppMenus( Display display )
  { 
    // register ourselves as a handler for Application Menu Commands
    // Install event handler for commands
    final Callback commandCallback = new Callback( this, "commandProc", 3 );
    int commandProc = commandCallback.getAddress();
    if( commandProc == 0 ) {
      commandCallback.dispose();
      return; // give up
    }

    int[] mask = new int[]
      { OS.kEventClassCommand, OS.kEventProcessCommand };
    int ret =
      OS.InstallEventHandler( OS.GetApplicationEventTarget(), commandProc,
                              mask.length / 2, mask, 0, null );

    // create About Eclipse menu command
    int[] outMenu = new int[1];
    short[] outIndex = new short[1];
    ret =
      OS.GetIndMenuItemWithCommandID( 0, kHICommandPreferences, 1, outMenu,
                                      outIndex );
    if( ret == OS.noErr && outMenu[0] != 0 ) {
      int menu = outMenu[0];
      
      // add the about action
      int l = aboutString.length();
      char buffer[] = new char[l];
      aboutString.getChars(0, l, buffer, 0);
      int str = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, buffer, l);
      OS.InsertMenuItemTextWithCFString(menu, str, (short) 0, 0, kHICommandAbout);
      OS.CFRelease(str);
      
      // add separator between About & Preferences
      OS.InsertMenuItemTextWithCFString( menu, 0, (short) 1,
                                         OS.kMenuItemAttrSeparator, 0 );

      // enable pref menu
      OS.EnableMenuCommand( menu, kHICommandPreferences );

      // disable services menu
      OS.DisableMenuCommand( menu, kHICommandServices );
    }

    // schedule disposal of callback object
    display.disposeExec( new Runnable() {
      public void run()
      {
        commandCallback.dispose();
      }
    } );
  }

  /**
   * Process OS menu event.
   * 
   * @param nextHandler
   *          unused
   * @param theEvent
   *          the OS event
   * @param userData
   *          unused
   * @return whether or not the event was handled by this processor
   */
  public int commandProc( int nextHandler, int theEvent, int userData )
  {
    if( OS.GetEventKind( theEvent ) == OS.kEventProcessCommand ) {
      HICommand command = new HICommand();
      OS.GetEventParameter( theEvent, OS.kEventParamDirectObject,
                            OS.typeHICommand, null, HICommand.sizeof, null,
                            command );
      switch( command.commandID ) {
        case kHICommandPreferences:
          return runAction( this.optionsAction );
        case kHICommandAbout:
          return runAction( this.aboutAction );
        case kHICommandQuit:
          return runAction( this.exitAppAction );
        default:
          break;
      }
    }
    return OS.eventNotHandledErr;
  }

  /**
   * Show the options menu
   */
  private int runAction( Action action )
  {
    if( action != null && action.isEnabled() ) {
      action.run();
      return OS.noErr;
    }
    return OS.eventNotHandledErr;
  }

  /**
   * Main program entry point.
   * 
   * @param args
   *          commandline args that are passed in
   */
  public static void main( String args[] )
  {
    log.info( "PasswordSafe starting..." );
    log.info( "java.library.path is: ["
              + System.getProperty( "java.library.path" ) + "]" );
    log.info( "log: " + log.getClass().getName() );
    
    try {
	  //IMPORTANT: Disable Systray on mac
	  IPreferenceStore prefStore = UserPreferences.getInstance().getPreferenceStore();
	  prefStore.setDefault(SHOW_ICON_IN_SYSTEM_TRAY, false);
	  prefStore.setValue(SHOW_ICON_IN_SYSTEM_TRAY, false);

      PasswordSafeMacOSX window = new PasswordSafeMacOSX();
      window.setBlockOnOpen( true );
      window.open();
    } catch( Exception e ) {
      log.error( "Error Starting PasswordSafe", e );
    } finally { //try to clean up in any case
		try {
			SWTResourceManager.dispose();
		} catch (Exception anEx) {}// ok
		try {
			Display.getCurrent().dispose();
		} catch (Exception anEx1) {}// ok
	}
    log.info( "PasswordSafe terminating..." );
  }
}
