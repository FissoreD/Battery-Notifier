# BATTERY NOTIFIER

### Aim of project 
This project is a battery notifier aiming to protect you battery life.   
A battery live longer if you keep the battery level between a range of about 15-90%   
For that the executable lets you know with a windows popup if you have to plug or unplug   
the charger since the chosen battery limits have been passed (if you want you can customize them).

Default battery notification levels : 
- min : 15 % [that is the battery level at which the plug-in notification will popup]
- max : 90 % [that is the battery level at which the **un**plug notification will popup]
- sec : 5    [the frequency of popup creation]

### How it works
When you launch the jar, an icon will appear in the windows bar, with
- left mouse click ends the program
- right mouse click opens a small Frame  

In this window you can : 
  - Set the min battery level 
  - Set the max battery level 
  - Set the sec time

### Technical infos
This is a maven project using java 17 version working on Windows OS   

You can compile the project with the command `mvn install`

Build `exe` for Windows with Launch4j : 
- In basic 
  - Output file : path\Battery.exe 
  - Jar : the jar file obtained from mvn and stored in `.\target`
  - Icon : it is in `.\target\classes\`
- In JRE
  - Bundled JRE path : the path to the java `jdk`folder in `C:\\`
- Single Instance
  - Select : *Allow only a single instance*
  - Mutex name : *Battery_Notifier*
