@echo off
echo Compiling Source Code
javac -d out src\fleet\*.java src\vehicles\*.java src\interfaces\*.java src\exceptions\*.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Running Fleet Manager
java -cp out fleet.Main

pause
