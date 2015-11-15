Lists the necessary local environment and IDE settings for building and deploying _tll_ projects.

# Required Software #
The following software is required:
| **Name** | **Version** | **Notes** |
|:---------|:------------|:----------|
| [jdk](http://java.sun.com/javase/downloads/index.jsp) | 1.6.0\_14+  |           |
| [eclipse](http://www.eclipse.org/) | 3.7+        | JEE version |
| [maven](http://maven.apache.org/) | 3.0.3+      |           |
| [ant](http://ant.apache.org/) | 1.7.1+      | Not strictly necessary as maven maintains its own instance. |
| [groovy](http://groovy.codehaus.org/) | 1.6+        | Use the windows installer download is possible. |
| [subversion client](http://subversion.tigris.org/) | 1.6.0+      | Preferred windows client: [sliksvn](http://www.sliksvn.com/en/download) |
| [GWT](http://code.google.com/p/google-web-toolkit/) | 2.4+        |           |

# environment #
  * Create the tll root directory in a desired location.  This will be the root dir of all tll source code.

  * Ensure the following environment variables exist:
| **Name** | **Description** |
|:---------|:----------------|
| JAVA\_HOME | Points to the root **JDK** install dir. |
| JDK\_HOME | Alias for `JAVA_HOME` used by, for example, the GWT build routine. |
| SVN\_HOME | Points to the root svn client install dir. |
| M2\_HOME | Points to the maven install dir. |
| M2\_REPO | Points to your local maven repository dir. |
| GROOVY\_HOME | Points to the root groovy install dir. |
| ANT\_HOME | Points to the root Ant install dir. |
| TLL\_ROOT | Points to the root tll dir. |
| GWT\_HOME | Points to the root GWT _install_ dir. (Used for building GWT from source.) |
| GWT\_TOOLS | Points to the root GWT Tools dir. (Used for building GWT from source.)|

  * Add the following paths to the system path:
```
{JAVA_HOME}/bin
{GROOVY_HOME}/bin
{ANT_HOME}/bin
{SVN_HOME}/bin
{M2_HOME}/bin
{TLL_ROOT}/build-tools/bin
```

```
%JAVA_HOME%\bin;%GROOVY_HOME%\bin;%ANT_HOME%\bin;%SVN_HOME%\bin;%M2_HOME%\bin;%TLL_ROOT%\build-tools\bin;
```

  * Append these global ignores to the installed SVN config file:
```
global-ignores = war debug.bat shell.bat .metadata build classes bin bin-groovy dist Desktop.ini *.log *.tmp target .metadata .gwt-tmp test-output tomcat Constants.properties config-local.properties
```

  * Create a user-scoped maven `settings.xml` file under the `.m2` dir corresponding to the local maven install.  Here is the prototype:
```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

	<activeProfiles>

		<!-- stage profile ('dev' or 'prod') -->
		<activeProfile>dev</activeProfile>

		<!-- dao impl profile ('jdo' or 'db4o') -->
		<activeProfile>jdo</activeProfile>

		<!-- security profile ('acegi' or 'nosecurity') -->
		<activeProfile>nosecurity</activeProfile>

	</activeProfiles>

</settings>
```

  * Checkout tll project:
```
svn checkout http://tll.googlecode.com/svn/trunk/ {TLL_ROOT}
```

  * Install the tll modules into the local maven repository by invoking the following maven command from the tll root directory:
```
mvn install
```


---

# eclipse #
The following steps cover the necessary [eclipse](http://www.eclipse.org/) IDE workspace settings.

## bootstrap ##
Specify the -vm option in the eclipse.ini or at the command line (This is necessary for the m2eclipse plugin):
```
-vm {JAVA_HOME}/bin/javaw.exe
```

## Add Ons ##
| **Name** | **URL** | **Notes** |
|:---------|:--------|:----------|
| subclipse | [http://subclipse.tigris.org/update\_1.6.x](http://subclipse.tigris.org/update_1.6.x) |           |
| m2e      | [http://m2eclipse.sonatype.org/update/](http://m2eclipse.sonatype.org/update/) | Install configurator: aspectj and groovy and more as needed. (Preferences -> Maven -> Discovery) |
| TestNG   | [http://beust.com/eclipse](http://beust.com/eclipse) |           |
| Groovy   | [http://dist.groovy.codehaus.org/distributions/updateDev/](http://dist.groovy.codehaus.org/distributions/updateDev/) |           |
| FindBugs | [http://findbugs.cs.umd.edu/eclipse/](http://findbugs.cs.umd.edu/eclipse/) |           |
| google eclipse plugin | http://dl.google.com/eclipse/plugin/3.7 |           |

## Preferences ##
Import the tll eclipse preferences files.  These files are located under:  [eclipse](http://tll.googlecode.com/svn/eclipse).