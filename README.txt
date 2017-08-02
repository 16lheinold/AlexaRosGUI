########################Read Me: Alexa Helper GUI##############################

How to install and run: 
1. If you haven't already, install rosjava (http://wiki.ros.org/rosjava/Tutorials/indigo/Deb%20Installation)
2. Inside your catkin workspace, create a new rosjava project (you can do this with catkin build or catkin_make)
	catkin_create_rosjava_pkg alexa_helper_gui
3. Clone the git repo into the alexa_helper_gui folder(https://github.com/16lheinold/AlexaRosGUI.git)
4. Rename the folder AlexaRosGUI to gui
5. Go to the scripts folder and open run_test_project in a text editor
6. Fill in the following information
	WS: name of your workspace
	PKG: name of your package (alexa_helper_gui)
	PROJECT: gui
	FILE_NAME: Listener
	PATH_TO_PROJECT: The path to your project (ending with /$PKG)
	PROJECT_CLASS_PATH: com.github.rosjava.alexa_gui_updated.$PROJECT.$FILE_NAME
		**you shouldn't need to edit this
7. Move to your rosjava package directory and open settings.gradle
	Add the line: include 'gui' 
8. Run ./gradlew tasks and make sure the tasks installApp and run_project are on the list
	If they are not, run ./gradlew projects and make sure the subproject 'gui' is listed. If it isn't, check settings.gradle and make sure 'gui' is included as a subproject
9. Run ./gradlew installApp to compile
10. Run ./gradlew run_project to run the project