@echo off
echo Compiling all Java files...

REM Create output directory if missing
if not exist out mkdir out

REM Build a list of all Java source files
setlocal enabledelayedexpansion
set sources=
for /R %%f in (*.java) do (
    set sources=!sources! "%%f"
)

REM Compile
javac -d out %sources%

if %errorlevel% neq 0 (
    echo.
    echo *** Compilation failed ***
    pause
    exit /b
)

echo.
echo Running program...
java -cp out Main

echo.
pause
