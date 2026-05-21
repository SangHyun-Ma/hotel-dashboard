@echo off
cd /d "%~dp0backend"
call "%~dp0backend\gradlew.bat" bootRun
