@echo off
echo 🎤 AI Voice Coach - System Test
echo ================================

REM Test if backend is running
echo Testing backend connection...
curl -s http://localhost:8080/api/test/health >nul 2>&1
if %errorlevel% == 0 (
    echo ✅ Backend is running on port 8080
) else (
    echo ❌ Backend is not running. Please start it first:
    echo    cd aipresentationmaker/aipresentationbackend/aipresentationbackend
    echo    mvnw spring-boot:run
    pause
    exit /b 1
)

REM Test health endpoint
echo.
echo Testing health endpoint...
curl -s http://localhost:8080/api/test/health | findstr "UP" >nul
if %errorlevel% == 0 (
    echo ✅ Health check passed
) else (
    echo ❌ Health check failed
)

REM Test voice models
echo.
echo Testing voice models...
curl -s http://localhost:8080/api/test/voices | findstr "success" >nul
if %errorlevel% == 0 (
    echo ✅ Voice models loaded successfully
) else (
    echo ❌ Voice models test failed
)

REM Test voice recommendation
echo.
echo Testing voice recommendation...
curl -s -X POST http://localhost:8080/api/test/voice-recommendation | findstr "success" >nul
if %errorlevel% == 0 (
    echo ✅ Voice recommendation working
) else (
    echo ❌ Voice recommendation test failed
)

REM Check if frontend file exists
echo.
echo Checking frontend...
if exist "frontend\voice-coach.html" (
    echo ✅ Frontend file exists
    echo    Open frontend/voice-coach.html in your browser
) else (
    echo ❌ Frontend file not found
)

REM Final summary
echo.
echo ================================
echo 🎉 System Test Complete!
echo.
echo Next Steps:
echo 1. Open frontend/voice-coach.html in your browser
echo 2. Try the sample speeches or enter your own
echo 3. Check the console logs for detailed information
echo.
echo API Endpoints Available:
echo • http://localhost:8080/api/voice/recommend
echo • http://localhost:8080/api/voice/analyze
echo • http://localhost:8080/api/voice/voices
echo • http://localhost:8080/api/test/health
echo.
echo Happy voice coaching! 🎤

pause