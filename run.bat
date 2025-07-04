@echo off
set CLASSPATH=bin;lib/mysql-connector-j-8.0.33.jar
set PATH_TO_FX=C:\javafx-sdk-21.0.7\lib

java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp %CLASSPATH% Main
pause