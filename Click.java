
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseMotionListener;

public class Click implements NativeKeyListener,NativeMouseMotionListener{
	ArrayList<String> safetyKeys = null;
	int mouseButton = 1;
	Robot robot = new Robot();
	int targetClickCount = 0,
			valueOfTag_every = 0,
			startDelay = 0,
			holdTime = 0,
			afterClickDelay=0,
	//test comment
			terminationDelay = 0;
	Point clickLocation = null;//if null, don't move
	boolean safetyEnabled = true, press = true,release = true;
	boolean hasEveryTag = false;
	ArrayList<Point>safetyLocations = null;
	String[] defaultSafetyArgs = new String[] {"-safety","0,0,Meta"};
	static String supportedKeys = "Backspace\n" + 
			"Insert\n" + "Home\n" + "Tab\n" + "Q\n" + "W\n" + "E\n" + "R\n" + "T\n" + "Y\n" + "U\n" + "I\n" + 
			"O\n" + "P\n" + "Delete\n" + "End\n" +  "Up\n" + "A\n" + "S\n" + "D\n" + "F\n" + "G\n" + "H\n" + "J\n" + "K\n" + "L\n" + 
			"Semicolon\n" + "Quote\n" + "Enter\n" + "Clear\n" + "Shift\n" + "Z\n" + "X\n" + "C\n" + 
			"V\n" + "B\n" + "N\n" + "M\n" + "Comma\n" + "Period\n" + "Slash\n" + "Ctrl\n" + "Meta\n" + 
			"Alt\n" + "Space\n" + "Left\n" + "Down\n" + "Right\n" + "Space";


	public static void main(String[] args) throws InterruptedException, AWTException {
		try {
			// Get the logger for "org.jnativehook" and set the level to off.
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);
			// Don't forget to disable the parent handlers.
			logger.setUseParentHandlers(false);
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		if(args.length==1 && (args[0].equals("help")|args[0].equals("-help"))) {
			System.out.println("Click is command line auto clicker with a built in safety mode to prevent user error causing complete lockups. It is very versatile, with many options to get the desired behavior.  \n" + 
					"\n" + 
					"OPTIONS\n" + 
					"*all time is measured in milliseconds.\n" + 
					"\n" + 
					"-startDelay TIME  = time to wait before starting first click\n" + 
					"\n" + 
					"-every TIME  = specify time between clicks , makes click run in a loop. The time waited at the end of each click is: ( (-every) -  (-hold) )  +  (-afterClickDelay)\n" + 
					"\n" + 
					"-hold TIME = time to wait before releasing after pressing\n" + 
					"\n" + 
					"-afterClickDelay TIME  =  time to wait after each click. Is not effected by -every or -hold\n" + 
					"\n" + 
					"-duration TIME  = amount of time to wait after first click before termination \n" + 
					"\n" + 
					"-location X,Y  = location to move cursor to before each click ( this can make it impossible to move cursor to safety location(s) )\n" + 
					"\n" + 
					"-count NUMBER  =  how many times to click before termination.\n" + 
					"\n" + 
					"-button 1-3  = buttons 1,2,3 or l,m,r or left,middle,right\n" + 
					"\n" + 
					"-press  TRUE | FALSE = press mouse button each time if true, not if false\n" + 
					"\n" + 
					"-release TRUE | FALSE  =   release mouse button each time if true, not if false(script will always release before exiting, unless user kills task outside of click program)\n" + 
					"\n" + 
					"\n" + 
					"\n" + 
					"\n" + 
					"-safety  =   defaults are 0,0,Meta …. defaults are used if -safety tag is not used\n" + 
					"*Meta is the Windows Key, or Mac Command Key.\n" + 
					"\n" + 
					"	       Examples | Explanations:\n" + 
					"\n" + 
					"-safety 0,0,Meta 	| stop if cursor ever goes to 0,0 or if user presses Meta\n" + 
					"\n" + 
					"-safety alt		| stop if user presses alt\n" + 
					"\n" + 
					"-safety 1920,1080	| stop if cursor ever goes to cords… bottom right of a 1080p screen \n" + 
					"(might not be reachable depending on OS behavior with edges of screen)\n" + 
					"\n" + 
					"-safety alt,ctrl,0,0	| stop if alt is pressed, stop if ctrl is pressed, stop if cursor goes to 0,0. Multiple keys are not shortcuts… they are extra safety keys. Its a safety, not a hotkey!\n" + 
					"\n" + 
					"-safety is made to prevent a user from locking up there computer to the point they would have to power it off with the power button. They can meet the safety condition(s) to terminate the process.\n" + 
					"\n" + 
					"-safety off   or   -safety false  will turn it off complete.  Only do this if you know exactly what you are doing!\n" + 
					"\n" + 
					"Option Abbreviations\n" + 
					"-startDelay		-sd\n" + 
					"-every			-e\n" + 
					"-hold			-h\n" + 
					"-afterClickDelay	-ad\n" + 
					"-duration		-d\n" + 
					"-location		-l\n" + 
					"-count			-c\n" + 
					"-button			-b\n" + 
					"-press			-p\n" + 
					"-release		-r\n" + 
					"-safety			-s\n" + 
					"\n" + 
					"\n" + 
					"\n" + 
					"USAGE:\n" + 
					"click -every 200 -duration 10000 -button 2\n" + 
					"(click every 200 mills , terminate after 10 seconds, use mouse button 2/middle click)\n" + 
					"\n" + 
					"click -every 200 -count 50 -startDelay 5000 -safety a,s,d,f,0,0\n" + 
					"(click every 200 mills, terminate after 50 clicks, terminate if user presses any of the specified keys, terminate if cursor moves to 0,0)\n" + 
					"\n" + 
					"click -release false -hold 5000\n" + 
					"(press mouse button once, do not release, hold for 5 seconds,  exit, exit always releases mouse button.)\n" + 
					"\n" + 
					"\n" + 
					"*Note\n" + 
					"On Linux, avoid calling java “-jar path/to/click.jar -every 100”\n" + 
					"and instead call “click -every 100” with the following command:\n" + 
					"alias click='java -jar /path/to/click.jar'\n" + 
					"\n" + 
					"\n" + 
					""+ supportedKeys);




			System.exit(0);
		}else {
			Click click = new Click(args);
			GlobalScreen.addNativeKeyListener(click);
			GlobalScreen.addNativeMouseMotionListener(click);
		}

	}

	public Click(String[] args) throws InterruptedException, AWTException {

		//DEBUG ONLY
		if(args.length>0) {
			//set values from args
			setVariablesFromArgs(args);
			if(!safetyEnabled) {
				System.out.println("Safety is off!");
			}else if(safetyEnabled && safetyKeys==null && safetyLocations==null) {
				//set default safety
				setVariablesFromArgs(defaultSafetyArgs);
				System.out.println("Set Default safety to '-safety 0,0,Meta' run 'click -help' for more info.");
			}
			//if -every tag was not set, prevent from clicking indefinitely
			if(hasEveryTag==false && targetClickCount==0) targetClickCount=1;
			//CLICK LOOP
			new Thread(new Runnable() {
				public void run() {
					try {
						//sleep start delay
						if(startDelay>0)System.out.println("Waiting "+ startDelay+"ms ....");
						Thread.sleep(startDelay);
						//start a thread to terminate process when time runs out
						new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(terminationDelay);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if(terminationDelay!=0)exit(mouseButton,"Exit after -duration of "+terminationDelay+"ms!");
							}
						}).start();







						//start click loop
						int buttonMask = MouseEvent.getMaskForButton(mouseButton);
						int clicksDone = 0;
						long loopStartTime = System.currentTimeMillis();
						System.out.println("clicking..." +targetClickCount);

						while((clicksDone<targetClickCount || targetClickCount==0) && 
								(terminationDelay==0 || (System.currentTimeMillis()-loopStartTime)< terminationDelay )) {   
							long startTime = System.currentTimeMillis();
							if(clickLocation!=null)robot.mouseMove(clickLocation.x, clickLocation.y);
							if(press)robot.mousePress(buttonMask);
							if(holdTime>0)Thread.sleep(holdTime);
							if(release)robot.mouseRelease(buttonMask);
							long timeLapsed = System.currentTimeMillis()-startTime;
							long timeToSleep = (valueOfTag_every-timeLapsed)+afterClickDelay;
							if(timeToSleep>0)Thread.sleep(timeToSleep);
							clicksDone++;
						}
						exit(mouseButton,"Clicking Loop Finished! ClicksDone="+clicksDone);

					}catch(InterruptedException e) {

					}


				}
			}).start();

		}else {//no arguments, so left click once
			new Robot().mousePress(MouseEvent.BUTTON1_MASK);
			new Robot().mouseRelease(MouseEvent.BUTTON1_MASK);
			exit(mouseButton,"Left Clicked!\nDone!");
		}

	}


	private void setVariablesFromArgs(String[] args) {
		try {
			int index=0;
			for(String arg:args) {//todo handle parsing errors //print out error message to user? lazy way
				if(arg.startsWith("-") && index < args.length-1) {
					String value = args[index +1];
					switch(arg){
					//time based int values, all in Milliseconds
					case "-every":case "-e":
						valueOfTag_every = Integer.parseInt(value);
						hasEveryTag=true;
						break;
					case "-afterClickDelay": case "-ad":
						afterClickDelay = Integer.parseInt(value);
						break;
					case "-duration":case "-d": 
						terminationDelay = Integer.parseInt(value);
						break;
					case "-startDelay":case "-sd":
						startDelay = Integer.parseInt(value);
						break;
					case "-hold":case "-h":
						holdTime = Integer.parseInt(value);
						break;
					case "-button":case "-b":
						if(value.startsWith("l")) {
							mouseButton=1;
						}else if(value.startsWith("r")) {
							mouseButton=3;
						}else if(value.startsWith("m")) {
							mouseButton=2;
						}else {
							mouseButton = Integer.parseInt(value);
						}
						break;
					case "-count": case "-c":
						targetClickCount = Integer.parseInt(value);
						if(targetClickCount<1 )exit(mouseButton,"Exit: -count value must be above zero!");
						break;
					case "-location": case "-l":
						String[] x_y = value.split(",");
						clickLocation = new Point (Integer.parseInt(x_y[0]),Integer.parseInt(x_y[1]));
						break;
					case "-press": case "-p":
						press = Boolean.parseBoolean(value);
						break;
					case "-release": case"-r":
						release = Boolean.parseBoolean(value);
						break;
					case "-safety":
						//examples
						// 0,0,escape,alt
						// escape,0,0,alt
						// escape,alt,0,0
						safetyEnabled = !(value.equals("off") ||  value.equals("false"));
						if(safetyEnabled) {
							ArrayList<String> supportedKeys = null;
							//if safety enabled
							String[] safetyStrings = value.split(",");//split by comma
							int safetyIndex = 0;
							boolean skipIndex = false;
							for(String safetyString:safetyStrings) {// escape,0,7

								try{
									if(!skipIndex) {
										int x = Integer.parseInt(safetyString);//fails if not a number
										int y = Integer.parseInt(safetyStrings[safetyIndex+1]);
										if(safetyLocations==null)safetyLocations=new ArrayList<>();
										safetyLocations.add(new Point(x,y));
										skipIndex=true;
									}else {
										skipIndex=false; 
										//this is definitely a y value, ignore it!
									}



								}catch(NumberFormatException  e) {
									//a key
									if(safetyKeys==null)safetyKeys=new ArrayList<>();
									if(supportedKeys==null)supportedKeys=createSupportedKeysList();
									if(supportedKeys.contains(safetyString)) {
										safetyKeys.add(safetyString);
									}else {exit(mouseButton,"Safety key:"+safetyString +" Not supported, use 'click help' for a list of supported keys.");}



								}catch(ArrayIndexOutOfBoundsException e){
									System.err.println("Check Your Arguments!a");
									System.exit(0);
								}
								safetyIndex++;
							}
						}


						break;
					}

				}
				index++;
			}
		}catch(NumberFormatException exception) {
			System.err.println("Check Your Arguments!b");
			System.exit(0);
		}
	}


	public void exit(int button,String message){
		try {
			new Robot().mouseRelease(MouseEvent.getMaskForButton(button));
		} catch (AWTException e) {
			e.printStackTrace();
		}
		System.out.println(message);
		System.exit(0);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		int code = arg0.getKeyCode();
		String text = NativeKeyEvent.getKeyText(code);
		if(safetyKeys!=null) {
			if(safetyKeys.contains(text) || safetyKeys.contains(text.toLowerCase()))exit(mouseButton,"Exit Button Pressed!");
		}

	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {

	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {

	}


	public ArrayList<String> createSupportedKeysList() {

		String[] keys = supportedKeys.split("\\n");
		ArrayList<String> keysList = new ArrayList<>(); 
		for(String key:keys) {
			keysList.add(key);
			keysList.add(key.toLowerCase());
		}
		return keysList;

	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent arg0) {

	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent arg0) {
		if(safetyEnabled & safetyLocations!=null) {
			Point p = arg0.getPoint();
			if(safetyLocations.contains(p)) {
				exit(mouseButton,"Exit due to cursor being at a safety location: "+p.x+","+p.y+"!");
			}
		}

	}



}








