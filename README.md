# LazyMan

A simple program that lets you stream every NHL and MLB game from your favorite media player for free.

## Requirements

* Windows 7 SP1, onward
* Mac/Linux: Must support Python 2.7+ or 3.3+ and Java 8+

* Java JRE 8+
* VLC 2.2+ x64
* Python (Mac/Linux)
* Python-setuptools (Linux)
* Python-dev (Linux)
* .NET 4.6+ (Windows 7 SP1 and 8 must install it)

## Contribute 

LazyMan is written in Java. We use NetBeans to edit the code and GUI.

To make it an all in one jar:

Under the Files tab, right click `build.xml` then Run Target > Other Targets > package-for-store. The jar should appear in the folder called `store`.


### Running on IntelliJ

Add the project source as new from existing sources to IntelliJ.

Install the Ant extension, then add `build.xml` to the ant build steps.

Edit the bottom of `project.properties` file to have `platforms.JDK_1.8.home={your-JDK1.8-path}`

Create a new run configuration, select JAR application. From this, add a before launch step of `Run Ant target` with a 
target of `package-for-store`. Select the jar path as `store/LazyMan.jar`.

In order to load the proxy properly, you will need to copy the `mlbamproxy` folder that the program is distributed with
to the root directory.

It should now run and build correctly, as well as support debugging.