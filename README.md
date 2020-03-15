Click is command line auto clicker with a built in safety mode to prevent user error causing complete lockups. It is very versatile, with many options to get the desired behavior.  

OPTIONS
*all time is measured in milliseconds.

-startDelay TIME  = time to wait before starting first click

-every TIME  = specify time between clicks , makes click run in a loop. The time waited at the end of each click is: ( (-every) -  (-hold) )  +  (-afterClickDelay)

-hold TIME = time to wait before releasing after pressing

-afterClickDelay TIME  =  time to wait after each click. Is not effected by -every or -hold

-duration TIME  = amount of time to wait after first click before termination 

-location X,Y  = location to move cursor to before each click ( this can make it impossible to move cursor to safety location(s) )

-count NUMBER  =  how many times to click before termination.

-button 1-3  = buttons 1,2,3 or l,m,r or left,middle,right

-press  TRUE | FALSE = press mouse button each time if true, not if false

-release TRUE | FALSE  =   release mouse button each time if true, not if false(script will always release before exiting, unless user kills task outside of click program)




-safety  =   defaults are 0,0,Meta …. defaults are used if -safety tag is not used
*Meta is the Windows Key, or Mac Command Key.

	       Examples | Explanations:

-safety 0,0,Meta 	| stop if cursor ever goes to 0,0 or if user presses Meta

-safety alt		| stop if user presses alt

-safety 1920,1080	| stop if cursor ever goes to cords… bottom right of a 1080p screen 
(might not be reachable depending on OS behavior with edges of screen)

-safety alt,ctrl,0,0	| stop if alt is pressed, stop if ctrl is pressed, stop if cursor goes to 0,0. Multiple keys are not shortcuts… they are extra safety keys. Its a safety, not a hotkey!

-safety is made to prevent a user from locking up there computer to the point they would have to power it off with the power button. They can meet the safety condition(s) to terminate the process.

-safety off   or   -safety false  will turn it off complete.  Only do this if you know exactly what you are doing!

Option Abbreviations
-startDelay		-sd
-every			-e
-hold			-h
-afterClickDelay	-ad
-duration		-d
-location		-l
-count			-c
-button			-b
-press			-p
-release		-r
-safety			-s



USAGE:
click -every 200 -duration 10000 -button 2
(click every 200 mills , terminate after 10 seconds, use mouse button 2/middle click)

click -every 200 -count 50 -startDelay 5000 -safety a,s,d,f,0,0
(click every 200 mills, terminate after 50 clicks, terminate if user presses any of the specified keys, terminate if cursor moves to 0,0)

click -release false -hold 5000
(press mouse button once, do not release, hold for 5 seconds,  exit, exit always releases mouse button.)


*Note
On Linux, avoid calling java “-jar path/to/click.jar -every 100”
and instead call “click -every 100” with the following command:
alias click='java -jar /path/to/click.jar'


Supported Keys:
Backspace
Insert
Home
Tab
Q
W
E
R
T
Y
U
I
O
P
Delete
End
Up
A
S
D
F
G
H
J
K
L
Semicolon
Quote
Enter
Clear
Shift
Z
X
C
V
B
N
M
Comma
Period
Slash
Ctrl
Meta
Alt
Space
Left
Down
Right
Space
