::[Bat To Exe Converter]
::
::YAwzoRdxOk+EWAjk
::fBw5plQjdCuDJHuR/U40Oyd3WReWM2bvOrwf5+a15umIwg==
::YAwzuBVtJxjWCl3EqQJgSA==
::ZR4luwNxJguZRRnk
::Yhs/ulQjdF+5
::cxAkpRVqdFKZSTk=
::cBs/ulQjdF+5
::ZR41oxFsdFKZSDk=
::eBoioBt6dFKZSDk=
::cRo6pxp7LAbNWATEpCI=
::egkzugNsPRvcWATEpCI=
::dAsiuh18IRvcCxnZtBJQ
::cRYluBh/LU+EWAnk
::YxY4rhs+aU+JeA==
::cxY6rQJ7JhzQF1fEqQJQ
::ZQ05rAF9IBncCkqN+0xwdVs0
::ZQ05rAF9IAHYFVzEqQJQ
::eg0/rx1wNQPfEVWB+kM9LVsJDGQ=
::fBEirQZwNQPfEVWB+kM9LVsJDGQ=
::cRolqwZ3JBvQF1fEqQJQ
::dhA7uBVwLU+EWDk=
::YQ03rBFzNR3SWATElA==
::dhAmsQZ3MwfNWATElA==
::ZQ0/vhVqMQ3MEVWAtB9wSA==
::Zg8zqx1/OA3MEVWAtB9wSA==
::dhA7pRFwIByZRRnk
::Zh4grVQjdCuDJHuR/U40Oyd3WReWM2bvOqcJ5qb+9+/n
::YB416Ek+ZW8=
::
::
::978f952a14a936cc963da21a135fa983
@echo off
setlocal enableextensions

cd /d "%~dp0"
set "APP_DIR=%CD%"
set "JAR_NAME=ENavbat-Printer-Service.jar"
set "PORT=8696"
set "JAVA_EXEC=%APP_DIR%\jre\bin\javaw.exe"
set "TASK_NAME=ENavbatPrinterStartup"
set "LAUNCH_EXE=%APP_DIR%\run.exe"

echo [*] ENavbatPrinter ishga tushmoqda...

:: 1. Task Scheduler orqali doimiy startup
schtasks /Query /TN "%TASK_NAME%" >nul 2>&1
IF ERRORLEVEL 1 (
    echo [+] Task Scheduler'ga qo‘shilmoqda...
    schtasks /Create /TN "%TASK_NAME%" /TR "\"%LAUNCH_EXE%\"" /SC ONSTART /RL HIGHEST /F >nul
)

:: 2. Port band bo‘lsa, uni o‘ldiramiz
FOR /F "tokens=5" %%a IN ('netstat -ano ^| findstr :%PORT% ^| findstr LISTENING') DO (
    echo [-] Port %PORT% band - PID: %%a. Yakunlanmoqda...
    taskkill /PID %%a /F >nul 2>&1
)

:: 3. Java orqali jar ni ishga tushirish
IF EXIST "%JAVA_EXEC%" (
    echo [+] Java topildi. Ilova ishga tushmoqda...
    start "" "%JAVA_EXEC%" -jar "%APP_DIR%\%JAR_NAME%"
) ELSE (
    echo [!] Xatolik: Java topilmadi: %JAVA_EXEC%
)

exit