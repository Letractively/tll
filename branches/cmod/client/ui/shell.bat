set PDIR=%cd%
ant -find build-gwt.xml -Dgwt.shell.project.dir=%PDIR% -Dgwt.shell.startupUrl=UITests.html -Dgwt.shell.entryModule=com.tll.UITests shell